package com.example.simon.material.Model;

import java.util.List;

/**
 * Created by Simon on 2015/04/26.
 */
public class Subject {
    private String subjectname;
    private List<String> year;

    public Subject(String subjectname, List<String> year) {
        this.subjectname = subjectname;
        this.year = year;
    }

    public String getData() {
        return subjectname;
    }

    public List<String> getChildren() {
        return year;
    }
}
