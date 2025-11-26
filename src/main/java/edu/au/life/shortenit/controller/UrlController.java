package edu.au.life.shortenit.controller;

import edu.au.life.shortenit.dto.UrlResponse;
import edu.au.life.shortenit.dto.UrlShortenRequest;
import edu.au.life.shortenit.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
        UrlResponse response = urlService.shortenUrl(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/urls")
    public ResponseEntity<List<UrlResponse>> getAllUrls() {
        List<UrlResponse> urls = urlService.getAllUrls();
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/urls/{shortCode}")
    public ResponseEntity<UrlResponse> getUrlInfo(@PathVariable String shortCode) {
        UrlResponse response = urlService.getUrlInfo(shortCode);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/urls/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}