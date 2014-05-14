/*
 * Copyright 2014-05-11 the original author or authors.
 */
package pl.com.softproject.carelinkexporter;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.Duration;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import pl.com.softproject.carelinkexporter.model.Mensuration;
import pl.com.softproject.carelinkexporter.util.DurationFormatter;
import pl.com.softproject.utils.excelexporter.ColumnDescriptor;
import pl.com.softproject.utils.excelexporter.ExcelExporter;

/**
 *
 * @author Adrian Lapierre <adrian@softproject.com.pl>
 */
public class ExportToExcel {

    private static final Logger logger = Logger.getLogger(ExportToExcel.class);
    
    public File exportToCVS(File inCareLinkCSVfile, int skipLines) throws IOException {
        
        ImportCareLinkCSV importCareLinkCSV = new ImportCareLinkCSV();
        
        Iterable<Mensuration> res = importCareLinkCSV.read(inCareLinkCSVfile, skipLines);
        
        File outFile = new File(inCareLinkCSVfile.getParentFile(), "converted.csv");
        
        try (FileOutputStream os = new FileOutputStream(outFile)) {
            
            CsvWriter writer = new CsvWriter(os, ';', Charset.forName("UTF-8"));
            
            for(Mensuration mensuration : res) {
                writer.writeRecord(new String[]{
                    convert(mensuration.getLp()), 
                    convert(mensuration.getRecordTime()),
                    convert(mensuration.getRecordDate()),
                    convert(mensuration.getRecordType()),
                    convert(mensuration.getGlucose()),
                    convert(mensuration.getBolusType()),
                    convert(mensuration.getBolusValue()),
                    convert(mensuration.getDeleyedBolusValue()),
                    DurationFormatter.format(mensuration.getBolusTime())
                });
            }
            writer.close();
            
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error(fileNotFoundException.getMessage());
            throw fileNotFoundException;
        }
        
        return outFile;
    }
    
    private String convert(Object o) {
        return o == null ? "" : String.valueOf(o); 
    }
    
    public File exportToExcel(File inCareLinkCSVfile, int skipLines) throws IOException {
        
        ImportCareLinkCSV importCareLinkCSV = new ImportCareLinkCSV();
        
        Iterable<Mensuration> res = importCareLinkCSV.read(inCareLinkCSVfile, skipLines);

        ExcelExporter excel = new ExcelExporter("dane");

        excel.addColumn(new ColumnDescriptor("lp", "lp"));
        //excel.addColumn(new ColumnDescriptor("date", "date"));

        excel.addColumn(new ColumnDescriptor("recordTime", "recordTime"));
        excel.addColumn(new ColumnDescriptor("recordDate", "recordDate"));

        excel.addColumn(new ColumnDescriptor("recordType", "recordType"));
        excel.addColumn(new ColumnDescriptor("glucose", "glucose"));
        excel.addColumn(new ColumnDescriptor("bolusType", "bolusType"));
        excel.addColumn(new ColumnDescriptor("bolusValue", "bolusValue"));
        excel.addColumn(new ColumnDescriptor("deleyedBolusValue", "deleyedBolusValue"));
        excel.addColumn(new ColumnDescriptor("bolusTime", "bolusTime", (Object value) -> {
            Duration duration = (Duration) value;
            return DurationFormatter.format(duration);
        }));

        for (Mensuration row : res) {

            excel.createRow(row);
        }

        File outFile = new File(inCareLinkCSVfile.getParentFile(), "out.xls");

        excel.save(outFile);
        
        return outFile;
    }

}
