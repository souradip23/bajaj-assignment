package com.bajaj.bfhl.controller;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
/**
 * REST controller exposing the /bfhl endpoint.
 *
 * POST /bfhl  → processes the data array and returns categorised results.
 */
@RestController
@RequestMapping("/bfhl")
@CrossOrigin(origins = "*")
public class BfhlController {

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }


    @PostMapping
    public ResponseEntity<BfhlResponse> process(@Valid @RequestBody BfhlRequest request) {
        BfhlResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/health")
public ResponseEntity<Map<String, String>> health() {
    return ResponseEntity.ok(Map.of("status", "UP"));
}
}
