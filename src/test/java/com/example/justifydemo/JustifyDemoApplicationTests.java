package com.example.justifydemo;

import com.example.justifydemo.model.Person;
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
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JustifyDemoApplicationTests {

    // The only instance of validation service.
    private static final JsonValidationService service = JsonValidationService.newInstance();
    private static final ObjectMapper mapper = new ObjectMapper();

    private String schemaPath;

    @Before
    public void setup() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testJustinfyWithPersonWhenMissingAge() throws IOException {
        schemaPath = "/schema/person-schema.json";
        Person person = new Person("John", "Liu");

        StringReader json = new StringReader(mapper.writeValueAsString(person));

        // Reads the JSON schema from the given path.
        JsonSchema schema = null;
        try(InputStream in = getClass().getResourceAsStream(schemaPath)) {
            schema = service.readSchema(in);
        }

        // Our own problem handler
        ValidationProblemHandler handler = new ValidationProblemHandler();


        // JSON value to be read.
        JsonValue value = null;

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            value = reader.readValue();
        }

        handler.flush();

    }

    @Test
    public void testJustinfyWithPersonWhenMissingHobbies() throws IOException {
        schemaPath = "/schema/person-schema.json";
        Person person = new Person("John", "Wang");

        StringReader json = new StringReader(mapper.writeValueAsString(person));

        // Reads the JSON schema from the given path.
        JsonSchema schema = null;
        try(InputStream in = getClass().getResourceAsStream(schemaPath)) {
            schema = service.readSchema(in);
        }
        // Our own problem handler
        ValidationProblemHandler handler = new ValidationProblemHandler();

        // JSON value to be read.
        JsonValue value = null;

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            value = reader.readValue();
        }

        handler.flush();

    }

    @Test
    public void testJustifyWithPerson() throws IOException {
        schemaPath = "/schema/person-schema.json";
        Person person = new Person("Justin", "Liu");

        StringReader json = new StringReader(mapper.writeValueAsString(person));

        // Reads the JSON schema from the given path.
        JsonSchema schema = null;
        try(InputStream in = getClass().getResourceAsStream(schemaPath)) {
            schema = service.readSchema(in);
        }
        // Our own problem handler
        ValidationProblemHandler handler = new ValidationProblemHandler();

        // JSON value to be read.
        JsonValue value = null;

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            value = reader.readValue();
        }

        handler.flush();
    }

    @Test
    public void testJustifyWithPersons() throws IOException {
        schemaPath = "/schema/persons-schema.json";
        List<Person> persons = Arrays.asList(
                new Person("John", "Liu"),
                new Person("John", "Wang"),
                new Person("Justin", "Liu")
        );
        persons.get(1).setAge(-1);

        List<String> hobbies = new ArrayList<>();
        hobbies.add("");
        persons.get(2).setHobbies(hobbies);

        StringReader json = new StringReader(mapper.writeValueAsString(persons));

        // Reads the JSON schema from the given path.
        JsonSchema schema = null;
        try(InputStream in = getClass().getResourceAsStream(schemaPath)) {
            schema = service.readSchema(in);
        }

        // JSON value to be read.
        JsonValue value = null;

        // Our own problem handler
        ValidationProblemHandler handler = new ValidationProblemHandler();

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            value = reader.readValue();
        }

        handler.flush();
    }

    @Test
    public void testJustifyWithDefaultValue() throws IOException {
        schemaPath = "/schema/persons-schema.json";
        List<Person> persons = Arrays.asList(
                new Person("John", "Liu"),
                new Person("John", "Wang"),
                new Person("Justin", "Liu")
        );
        persons.get(1).setAge(-1);

        List<String> hobbies = new ArrayList<>();
        hobbies.add("");
        persons.get(2).setHobbies(hobbies);

        StringReader json = new StringReader(mapper.writeValueAsString(persons));

        // Reads the JSON schema from the given path.
        JsonSchema schema = null;
        try(InputStream in = getClass().getResourceAsStream(schemaPath)) {
            schema = service.readSchema(in);
        }

        // Our own problem handler
        ValidationProblemHandler handler = new ValidationProblemHandler();

        // Creates a configuration.
        // Filling with default values is enabled.
        ValidationConfig config = service.createValidationConfig();
        config.withSchema(schema)
                .withProblemHandler(handler)
                .withDefaultValues(true);

        // Creates a configured reader factory.
        JsonReaderFactory readerFactory = service.createReaderFactory(config.getAsMap());

        JsonValue value = null;

        // Creates JSON reader from the factory.
        try (JsonReader reader = readerFactory.createReader(json)) {
            // Reads the whole JSON object.
            value = reader.readValue();
        }

        handler.flush();

        // Prints the JSON object filled with default values.
        System.out.println(value);

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(new StringReader(value.toString()), schema, handler)) {
            // Reads the root JSON value from the instance.
            reader.readValue();
        }

        handler.flush();

    }

}
