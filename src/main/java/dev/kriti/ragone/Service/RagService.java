package dev.kriti.ragone.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import dev.kriti.ragone.DTO.RagAnswer;
import dev.kriti.ragone.Response.CommonApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;

@Component
public class RagService {

    private final ChatClient chatClient;
    private static final Logger logger = LoggerFactory.getLogger(RagService.class);

    public RagService(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();
    }

    public ResponseEntity<CommonApiResponse> processQuestionWithContext(String question) {
        try {
            String answer = chatClient.prompt()
                    .user(question)
                    .call()
                    .content();

            // Process the question and generate a response
            CommonApiResponse response = new CommonApiResponse();
            response.setResponseMessage(answer);
            response.setSuccess(true);
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error processing question: {}", question, e);
            CommonApiResponse response = new CommonApiResponse();
            response.setResponseMessage("Error processing question: " + e.getMessage());
            response.setSuccess(false);
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<CommonApiResponse> processQuestionWithStructuredOutput(String question) {
        logger.info("Processing question with structured output: {}", question);
        try {
            RagAnswer answer = chatClient.prompt()
                    .system("Return a structured answer. Use false for foundInContext.")
                    .user(question)
                    .call()
                    .entity(RagAnswer.class);

            CommonApiResponse response = new CommonApiResponse();
            logger.info("Answer: {}", answer.getAnswer());
            response.setResponseMessage(answer);
            response.setSuccess(true);
            logger.info("Answer11: {}", answer.getAnswer());
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error processing question: {}", question, e);
            CommonApiResponse response = new CommonApiResponse();
            response.setResponseMessage("Error processing question: " + e.getMessage());
            response.setSuccess(false);
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<CommonApiResponse> processQuestionWithCustomStructuredOutput(String question,
            JsonNode outputSchema) {
        logger.info("Processing question with custom structured output: {}", question);
        try {
            String schemaJson = outputSchema.toString();

            DynamicJsonSchemaConverter converter = new DynamicJsonSchemaConverter(schemaJson);

            // adding validation advisor to ensure the output conforms to the provided
            // schema
            var validationAdvisor = StructuredOutputValidationAdvisor.builder()
                    .outputJsonSchema(schemaJson)
                    .maxRepeatAttempts(1)
                    .build();

            JsonNode answer = chatClient.prompt()
                    .advisors(validationAdvisor)
                    .system("Answer the question using only the retrieved context.\n" + //
                            "Do not invent unsupported facts.\n" + //
                            "Return only the requested JSON structure.")
                    .user(question)
                    .call()
                    .entity(converter, spec -> spec
                            .useProviderStructuredOutput()
                            .validateSchema());

            CommonApiResponse response = new CommonApiResponse();
            logger.info("Answer: {}", answer.toString());
            response.setResponseMessage(answer);
            response.setSuccess(true);
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error processing question: {}", question, e);
            CommonApiResponse response = new CommonApiResponse();
            response.setResponseMessage("Error processing question: " + e.getMessage());
            response.setSuccess(false);
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<CommonApiResponse> askGemini(String question) {
        try {
            Client client = new Client();
            GenerateContentResponse LLMResponse = client.models.generateContent("gemini-3.1-flash-lite",
                    question, null);
            client.close();

            // Process the question and generate a response
            CommonApiResponse response = new CommonApiResponse();
            response.setResponseMessage(LLMResponse.text());
            response.setSuccess(true);
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            CommonApiResponse response = new CommonApiResponse();
            logger.error("Error processing question: {}", question, e);
            response.setResponseMessage("Error processing question: " + e.getMessage());
            response.setSuccess(false);
            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
