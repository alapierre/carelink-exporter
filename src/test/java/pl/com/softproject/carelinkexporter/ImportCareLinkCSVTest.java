/*
 * Copyright 2014-05-11 the original author or authors.
 */

package pl.com.softproject.carelinkexporter;

import java.io.File;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import org.apache.poi.util.TempFile;
import static org.junit.Assert.*;
import org.junit.Test;
import pl.com.softproject.carelinkexporter.model.Mensuration;
import pl.com.softproject.carelinkexporter.util.DurationFormatter;
import pl.com.softproject.utils.excelexporter.ColumnDescriptor;
import pl.com.softproject.utils.excelexporter.ColumnValueFormatter;
import pl.com.softproject.utils.excelexporter.ExcelExporter;

/**
 *
 * @author Adrian Lapierre <adrian@softproject.com.pl>
 */
public class ImportCareLinkCSVTest {
    
    ImportCareLinkCSV importCareLinkCSV = new ImportCareLinkCSV();
    
    
    /**
     * Test of read method, of class ImportCareLinkCSV.
     */
    @Test
    public void testRead() throws Exception {
        
        importCareLinkCSV.read(new File("D:\\realizacje\\SoftProject\\diabetyk\\exporter\\CareLink-Export-1399797066778.csv"), 11);
        
    }
    
    @Test
    public void testExport() throws Exception {
        
        File file = new File("D:\\realizacje\\SoftProject\\diabetyk\\exporter\\CareLink-Export-1399918014410.csv");
        
        Iterable<Mensuration> res = importCareLinkCSV.read(file, 11);
        
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
        
        for(Mensuration row : res) {
            
            excel.createRow(row);
        }
        
        File outFile = new File(file.getParentFile(), "out.xls");
        
        excel.save(outFile);
        
    }
    
    //@Test
    public void test2() {
        
        final Duration d = Duration.parse("PT90M");
        System.out.println(d);
        
        System.out.println(d.toHours() + " " + d.toMinutes());
    }
    
}
