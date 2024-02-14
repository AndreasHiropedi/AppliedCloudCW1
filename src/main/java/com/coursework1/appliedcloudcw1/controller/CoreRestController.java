package com.coursework1.appliedcloudcw1.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

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
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(value+ "\n");
    }

    @PostMapping(value="/callservice", consumes={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> callService(@RequestBody Map<String, String> requestBody) {
        String externalBaseUrl, parameters;

        // Check request format
        try {
            externalBaseUrl = requestBody.get("externalBaseUrl");
            parameters = requestBody.get("parameters");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Wrong request format" + "\n");
        }

        // Check for not null parameters
        if (externalBaseUrl == null || parameters == null) {
            return ResponseEntity.badRequest().body("Wrong request parameters" + "\n");
        }

        // Correctly format URL
        if (!externalBaseUrl.endsWith("/")) {
            externalBaseUrl += "/";
        }

        // Correctly format parameters
        if (!parameters.startsWith("/")) {
            parameters = "/" + parameters;
        }

        try {
            // Create the external service, URL, and request to be sent
            HttpClient externalService = HttpClient.newHttpClient();
            URI externalUrl = new URI(externalBaseUrl + parameters);
            HttpRequest request = HttpRequest.newBuilder().uri(externalUrl).GET().build();

            // Get the response and parse it accordingly
            HttpResponse<String> response = externalService.send(request, HttpResponse.BodyHandlers.ofString());
            Optional<String> contentType = response.headers().firstValue("Content-Type");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(contentType.map(MediaType::parseMediaType).orElse(MediaType.TEXT_PLAIN));
            return new ResponseEntity<>((response.body() + '\n'), headers, HttpStatus.valueOf(response.statusCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error calling external service." + "\n");
        }

    }

}
