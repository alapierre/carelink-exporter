/*
 * Copyright 2014-05-11 the original author or authors.
 */
package pl.com.softproject.carelinkexporter;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.com.softproject.carelinkexporter.model.BolusType;
import pl.com.softproject.carelinkexporter.model.Mensuration;
import pl.com.softproject.carelinkexporter.model.RecordType;
import pl.com.softproject.carelinkexporter.util.DurationFormatter;

/**
 *
 * @author Adrian Lapierre <adrian@softproject.com.pl>
 */
public class ImportCareLinkCSV {

    private static final Logger logger = Logger.getLogger(ImportCareLinkCSV.class.getName());
    
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

    private NumberFormat nf;

    {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator(' ');
        nf = new DecimalFormat("###.##", otherSymbols);
        
    }

    private Duration durationToMerge = Duration.ofMinutes(15);

    
    
    public Iterable<Mensuration> read(File file, int skipLins) throws FileNotFoundException, IOException {

        List<Mensuration> res = new LinkedList<>();
        
        FileInputStream is = new FileInputStream(file);

        CsvReader reader = new CsvReader(is, ';', Charset.forName("UTF-8"));

        for (int i = 0; i < skipLins && reader.readRecord(); i++) {
            // skip lines
        }

        int lp = 0;

        Mensuration nextRecord = null;
        Mensuration currentRecord = null;
        
        while (reader.readRecord()) {
            try {
                String idx = reader.get(0);
                //System.out.println(idx);
                                
                currentRecord = processRecord(lp, reader);
                
                if(currentRecord.getRecordType() == null) {
                    continue;
                }
                
                if(reader.readRecord()) {
                    nextRecord = processRecord(++lp, reader);
                    if(nextRecord.getRecordType() != null) {
                        final Duration durationBetweenRecords = Duration.between(currentRecord.getDate(), nextRecord.getDate());
                        System.out.println(DurationFormatter.format(durationBetweenRecords) + " " + currentRecord.getLp() + " do " + nextRecord.getLp());
                        if(durationBetweenRecords.compareTo(durationToMerge) < 0) {
                            mergeRecords(currentRecord, nextRecord);
                        }
                    }
                }
                
                
                System.out.println(currentRecord);

                if(currentRecord.getRecordType() != null)
                    res.add(currentRecord);

                System.out.println(nextRecord);
                if(nextRecord.getRecordType() != null)
                    res.add(nextRecord);

                lp++;
                
                
            } catch (ParseException | RuntimeException ex) {
                logger.log(Level.SEVERE, "błąd parsowania wartości z pliku " + ex.getMessage(), ex);
            } 
            
            
            
        }
        return res;
    }
    

    private Mensuration processRecord(int lp, CsvReader reader) throws IOException, ParseException {

        Mensuration currentRecord = new Mensuration();
        currentRecord.setLp(lp);
        currentRecord.setDate(LocalDateTime.parse(reader.get(3), df));

        String glucose = reader.get(5);
        String bolus = reader.get(12);
        if (!glucose.isEmpty()) {
            currentRecord.setGlucose(Integer.parseInt(glucose));
            currentRecord.setRecordType(RecordType.GLUCOSE);
        } else if (!bolus.isEmpty()) {
            currentRecord.setBolusValue(parseBolus(bolus));
            currentRecord.setRecordType(RecordType.BOLUS);
            
            switch (reader.get(10)) {
                case "Normalny":
                    currentRecord.setBolusType(BolusType.NORMAL);
                    break;
                case "Złożony/normalny" :
                        currentRecord.setBolusType(BolusType.COMPLEX);
                        
                        if(reader.readRecord()) {
                            if("Złożony/przedłużony".equals(reader.get(10))) {
                                currentRecord.setDeleyedBolusValue(parseBolus(reader.get(12)));
                                Duration bolusTime = DurationFormatter.parse(reader.get(13));
                                currentRecord.setBolusTime(bolusTime);
                            } else processRecord(++lp, reader);
                        }
                    break; 
                case "Przedłużony" :
                        currentRecord.setBolusType(BolusType.COMPLEX);
                        currentRecord.setBolusValue(null);
                        currentRecord.setDeleyedBolusValue(parseBolus(reader.get(12)));
                        Duration bolusTime = DurationFormatter.parse(reader.get(13));
                        currentRecord.setBolusTime(bolusTime);
                    break;
                default:
                    throw new RuntimeException("nieznany typ bolusa " + reader.get(10));
            }
            
        }

        return currentRecord;
    }

    private double parseBolus(String bolus) throws ParseException {
        return nf.parse(bolus).doubleValue();
    }

    private void mergeRecords(Mensuration currentRecord, Mensuration nextRecord) {
        
        if(currentRecord.getBolusType() != null && nextRecord.getRecordType() == RecordType.GLUCOSE) {
            currentRecord.setGlucose(nextRecord.getGlucose());
            currentRecord.setRecordType(RecordType.MERGED);
            nextRecord.setRecordType(null);
        } else if(nextRecord.getBolusType() != null && currentRecord.getRecordType() == RecordType.GLUCOSE) {
            nextRecord.setGlucose(currentRecord.getGlucose());
            nextRecord.setRecordType(RecordType.MERGED);
            currentRecord.setRecordType(null);
        }
        
    }

}
