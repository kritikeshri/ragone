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
import dev.kriti.ragone.DTO.InferenceWithSchemaRequestDto;
import dev.kriti.ragone.Response.CommonApiResponse;

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
    public ResponseEntity<CommonApiResponse> askGemini(@RequestBody AskRequestDto request) {
        return ragService.askGemini(request.getQuestion());
    }

    @PostMapping("/inference")
    public ResponseEntity<CommonApiResponse> ask(@RequestBody AskRequestDto request) {
        return ragService.processQuestionWithContext(request.getQuestion());
    }

    @PostMapping("/inferenceWithStructuredOutput")
    public ResponseEntity<CommonApiResponse> inferenceWithStructuredOutput(@RequestBody AskRequestDto request) {
        return ragService.processQuestionWithStructuredOutput(request.getQuestion());
    }

    @PostMapping("/inferenceWithCustomStructuredOutput")
    public ResponseEntity<CommonApiResponse> inferenceWithCustomStructuredOutput(
            @RequestBody InferenceWithSchemaRequestDto request) {
        return ragService.processQuestionWithCustomStructuredOutput(request.getQuestion(),
                request.getOutputJsonSchema());
    }

}