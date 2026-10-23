package dev.kriti.ragone.DTO;

import tools.jackson.databind.JsonNode;

public class InferenceWithSchemaRequestDto {
    private String question;
    private JsonNode outputJsonSchema;

    public InferenceWithSchemaRequestDto() {
    }

    public InferenceWithSchemaRequestDto(String question, JsonNode outputJsonSchema) {
        this.question = question;
        this.outputJsonSchema = outputJsonSchema;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public JsonNode getOutputJsonSchema() {
        return outputJsonSchema;
    }

    public void setOutputJsonSchema(JsonNode outputJsonSchema) {
        this.outputJsonSchema = outputJsonSchema;
    }
}
