package edu.udel.cisc.cisc437.controllers;

import edu.udel.cisc.cisc437.services.DatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/crud")
public class CrudController {
    private static final Logger log = Logger.getLogger("CrudController");

    private final DatabaseService databaseService;

    public CrudController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody Map<String, Object> user) {
        String userJson = databaseService.create(user);
        return new ResponseEntity<>(userJson, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<String> read(@RequestParam("objectId") String id) {
        String userJson = databaseService.read(id);
        if(userJson.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userJson, HttpStatus.OK);
    }

    // uses replace, MongoDB supports an update too
    @PutMapping("/")
    public ResponseEntity<String> update(@RequestParam("objectId") String id, @RequestBody Map<String, Object> user) {
        boolean success = databaseService.update(id, user);
        if(success) new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> delete(@RequestParam("objectId") String id) {
        boolean success = databaseService.delete(id);
        if(success) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
