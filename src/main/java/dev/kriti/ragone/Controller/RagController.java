package dev.kriti.ragone.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import dev.kriti.ragone.Service.RagService;
import dev.kriti.ragone.DTO.AskRequestDto;

@RestController
@RequestMapping("/api")
public class RagController {

    @Autowired
    private RagService ragService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/completions")
    public ResponseEntity askGemini(@RequestBody AskRequestDto request) {
        return ragService.askGemini(request.getQuestion());
    }

    @PostMapping("/inference")
    public ResponseEntity ask(@RequestBody AskRequestDto request) {
        return ragService.processQuestionWithContext(request.getQuestion());
    }

    @PostMapping("/inferenceWithStructuredOutput")
    public ResponseEntity inferenceWithStructuredOutput(@RequestBody AskRequestDto request) {
        return ragService.processQuestionWithStructuredOutput(request.getQuestion());
    }

}