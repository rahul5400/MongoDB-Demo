package edu.udel.cisc.cisc437.controllers;

import edu.udel.cisc.cisc437.services.DatabaseService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class MainController {
    private static final Logger log = Logger.getLogger("MainController");

    private final DatabaseService databaseService;

    public MainController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully uploaded and persisted file"),
        @ApiResponse(responseCode = "400", description = "Badly formatted CSV file"),
        @ApiResponse(responseCode = "500", description = "Error in persistence")
    })
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        log.info("Uploading CSV...");

        try {
            databaseService.persistCSVFileIntoDatabase(file);
        } catch(IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved data"),
        @ApiResponse(responseCode = "500", description = "Error in retrieval")
    })
    @GetMapping(path = "/findAll")
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        try {
            List<Map<String, Object>> users = databaseService.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved data"),
        @ApiResponse(responseCode = "500", description = "Error in retrieval")
    })
    @GetMapping(path = "/findAllByClub")
    public ResponseEntity<List<Map<String, Object>>> findAllByClubName(@RequestParam("clubName") String clubName) {
        try {
            List<Map<String, Object>> users = databaseService.findAllByClub(clubName);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/findAllOver21")
    public ResponseEntity<List<Map<String, Object>>> findAllOver21() {
        try {
            List<Map<String, Object>> users = databaseService.findAllOver21();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted all data"),
        @ApiResponse(responseCode = "500", description = "Error in deletion")
    })
    @DeleteMapping(path = "/deleteAll")
    public ResponseEntity<String> deleteAll() {
        databaseService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
