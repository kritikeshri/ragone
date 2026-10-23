package dev.kriti.ragone.Service;

import org.springframework.ai.converter.StructuredOutputConverter;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

public class DynamicJsonSchemaConverter implements StructuredOutputConverter<JsonNode> {

    private final String schemaJson;
    private final JsonMapper mapper = JsonMapper.builder().build();

    public DynamicJsonSchemaConverter(String schemaJson) {
        this.schemaJson = schemaJson;
    }

    @Override
    public String getJsonSchema() {
        return schemaJson;
    }

    @Override
    public String getFormat() {
        return "Return only a JSON value conforming to the provided schema. "
                + "Do not include Markdown or explanatory text." + schemaJson;
    }

    @Override
    public JsonNode convert(String source) {
        return mapper.readTree(source);
    }

}
