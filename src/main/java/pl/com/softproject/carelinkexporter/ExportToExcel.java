/*
 * Copyright 2014-05-11 the original author or authors.
 */

package pl.com.softproject.carelinkexporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import pl.com.softproject.carelinkexporter.model.Mensuration;

/**
 *
 * @author Adrian Lapierre <adrian@softproject.com.pl>
 */
public class ExportToExcel {
    
    protected HSSFSheet sheet;
    protected HSSFWorkbook wb;
    protected DataFormat format;
    
    protected short currentRowNumber;
    protected int currentColumnNumber;

    public ExportToExcel() {
    
        createExcelSheet();
        format = wb.createDataFormat();
        
    }

    private void createExcelSheet() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("dane");
    }
    
    /**
     * Zapisuje utworzony arkusz do pliku
     *
     * @param outputFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void save(File outputFile) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(outputFile);
        wb.write(os);
        os.close();
    }
    
    public void craeteRow(Mensuration mensuration) {
        
        currentColumnNumber = 0;
        
        HSSFRow row = sheet.createRow(currentRowNumber);
        
        Cell cell = row.createCell(currentColumnNumber);
        
        
        currentRowNumber++;
        
    }
    
}
