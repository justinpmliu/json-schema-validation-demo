package com.example.justifydemo;

import com.example.justifydemo.model.Record;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ValidationConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JustifyDemoApplicationTests {

    // The only instance of validation service.
    private static final JsonValidationService service = JsonValidationService.newInstance();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testJustifyWithDefaultValue() throws IOException {
        String schemaPath = "/schema/records-schema.json";
        List<Record> records = Arrays.asList(
                new Record("Holding"),
                new Record("Pay/Rec")
        );

        records.get(0).setServiceCode("BO");

        // Reads the JSON schema from the given path.
        JsonSchema schema = null;
        try(InputStream in = getClass().getResourceAsStream(schemaPath)) {
            schema = service.readSchema(in);
        }

        // Creates a configuration.
        // Filling with default values is enabled.
        ValidationConfig config = service.createValidationConfig();
        config.withSchema(schema).withDefaultValues(true);

        // Creates a configured reader factory.
        JsonReaderFactory readerFactory = service.createReaderFactory(config.getAsMap());

        StringReader json = new StringReader(mapper.writeValueAsString(records));

        JsonValue value = null;

        // Creates JSON reader from the factory.
        try (JsonReader reader = readerFactory.createReader(json)) {
            // Reads the whole JSON object.
            value = reader.readValue();
        }

        // Prints the JSON object filled with default values.
        System.out.println(value);

        // Our own problem handler
        ValidationProblemHandler handler = new ValidationProblemHandler();

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(new StringReader(value.toString()), schema, handler)) {
            // Reads the root JSON value from the instance.
            reader.readValue();
        }

        handler.flush();
    }

}
