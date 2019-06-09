package com.example.jsonschemavalidationdemo.model;

import lombok.Data;

import java.util.List;

@Data
public class Person {
    private String firstName;
    private String lastName;
    private Integer age;
    private List<String> hobbies;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
