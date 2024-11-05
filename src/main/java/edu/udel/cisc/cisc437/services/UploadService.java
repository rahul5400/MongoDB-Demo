package edu.udel.cisc.cisc437.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UploadService {
    private final CSVFormat format = CSVFormat.DEFAULT.builder()
        .setHeader()
        .setSkipHeaderRecord(true)
        .build();
    private static final Logger log = Logger.getLogger("UploadService");

    public List<Document> convertFileToDocuments(MultipartFile file) throws IOException {
        try(
            Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        ) {
            List<CSVRecord> records = format.parse(reader).getRecords();
            log.info("Parsed records, converting to BSON documents...");
            List<Document> documents = convertCSVRecordsToDocuments(records);
            log.info("Converted documents");
            return documents;
        }
    }

    private List<Document> convertCSVRecordsToDocuments(List<CSVRecord> records) {
        return records
            .stream()
            .map(r -> {
                Document document = new Document();
                for(var entry : r.toMap().entrySet()) {
                    document.append(entry.getKey(), parseStringValue(entry.getValue()));
                }
                return document;
            })
            .toList();
    }

    private Object parseStringValue(String value) {
        try {
            return Integer.parseInt(value); // try integer parse
        } catch(NumberFormatException ignored) {}
        try {
            return Double.parseDouble(value); // try double parse
        } catch(NumberFormatException ignored) {}
        try {
            return Date.valueOf(value); // try date parse
        } catch(IllegalArgumentException ignored) {}
        return value; // if no parsing works, just send it as a string
    }
}
