package com.example.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageDTO implements Serializable {
    @JsonProperty("pageNo")
    private int currentPage;

    @JsonProperty("pageSize")
    private int pageSize;


}
