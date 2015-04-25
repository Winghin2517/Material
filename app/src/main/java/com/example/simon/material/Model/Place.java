package com.example.simon.material.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Simon on 2015/04/12.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Place {
    @JsonProperty("place")
    private String name_of_place;
    @JsonProperty("desc")
    private String description_place;
    @JsonProperty("pic_url")
    private String pic_url;
    @JsonIgnore
    private int id;

    public Place() {}

    public Place(String name_of_place, String description_place, String pic_url) {
        super();
        this.name_of_place = name_of_place;
        this.description_place = description_place;
        this.pic_url = pic_url;
    }

    public String getName_of_place() {
        return name_of_place;
    }

    public void setName_of_place(String name_of_place) {
        this.name_of_place = name_of_place;
    }

    public String getDescription_place() {
        return description_place;
    }

    public void setDescription_place(String description_place) {
        this.description_place = description_place;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
