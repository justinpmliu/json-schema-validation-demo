package com.example.justifydemo.model;

import lombok.Data;

import java.util.List;

@Data
public class Record {
    private String recordNature;
    private String serviceCode;
    private String name;
    private Integer age;
    private List<String> hobbies;

    public Record(String recordNature) {
        this.recordNature = recordNature;
    }

}
