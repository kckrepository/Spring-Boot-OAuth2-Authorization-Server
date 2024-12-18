package org.example.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ProtectedController {
    @GetMapping("/secure")
    public Mono<String> getInfo() {
        return Mono.just("protected data!");
    }
}