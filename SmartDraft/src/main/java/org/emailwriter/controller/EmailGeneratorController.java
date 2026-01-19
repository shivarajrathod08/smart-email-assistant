package org.emailwriter.controller;

import lombok.AllArgsConstructor;
import org.emailwriter.request.EmailRequest;
import org.emailwriter.service.EmailGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins="*")
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;
    @PostMapping("/generate")
    public Mono<ResponseEntity<String>> generateEmail(@RequestBody EmailRequest emailRequest){
      //  String response = emailGeneratorService.generateEmailReply(emailRequest);
      //  return ResponseEntity.ok(response);

        return emailGeneratorService.generateEmailReply(emailRequest)
                .map(ResponseEntity::ok);
    }
}
