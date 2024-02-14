package com.coursework1.appliedcloudcw1.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
public class CoreRestController {

    private String value = "";

    @GetMapping("/uuid")
    public ResponseEntity<String> getStudentId() {
        // Return student ID embedded in <h1></h1> tag
        String htmlResponse = "<h1>s2015345</h1>" + "\n";
        return ResponseEntity.ok().body(htmlResponse);
    }

    @PostMapping("/writevalue")
    public ResponseEntity<?> writeValue(@RequestParam("value") String newValue) {
        // Overwrite the previous value and respond with HTTP 200 OK
        value = newValue + "\n";
        return ResponseEntity.ok().build();
    }

    @GetMapping("/readvalue")
    public ResponseEntity<String> readValue() {
        // Directly return the value. If it's not set, it will return an empty string.
        return ResponseEntity.ok().contentType(org.springframework.http.MediaType.TEXT_PLAIN).body(value+ "\n");
    }

    @PostMapping("/callservice")
    public ResponseEntity<String> callService() {

        return ResponseEntity.ok().body("");
    }

}
