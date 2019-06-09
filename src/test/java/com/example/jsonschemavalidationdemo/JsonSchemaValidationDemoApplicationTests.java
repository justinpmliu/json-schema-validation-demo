package com.example.jsonschemavalidationdemo;

import com.example.jsonschemavalidationdemo.model.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonSchemaValidationDemoApplicationTests {

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
        MyProblemHandler handler = new MyProblemHandler();

        // JSON value to be read.
        JsonValue value = null;

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            value = reader.readValue();
        }

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
        MyProblemHandler handler = new MyProblemHandler();

        // JSON value to be read.
        JsonValue value = null;

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            value = reader.readValue();
        }

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
        MyProblemHandler handler = new MyProblemHandler();

        // JSON value to be read.
        JsonValue value = null;

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            value = reader.readValue();
        }

    }

    @Test
    public void testJustifyWithPersons() throws IOException {
        schemaPath = "/schema/persons-schema.json";
        List<Person> persons = Arrays.asList(
                new Person("John", "Liu"),
                new Person("John", "Wang"),
                new Person("Justin", "Liu")
        );

        StringReader json = new StringReader(mapper.writeValueAsString(persons));

        // Reads the JSON schema from the given path.
        JsonSchema schema = null;
        try(InputStream in = getClass().getResourceAsStream(schemaPath)) {
            schema = service.readSchema(in);
        }

        // Our own problem handler
        MyProblemHandler handler = new MyProblemHandler();

        // Creaates a JSON reader which will validate the instance while reading.
        try (JsonReader reader = service.createReader(json, schema, handler)) {
            // Reads the root JSON value from the instance.
            JsonValue value = reader.readValue();
        }
    }

}
