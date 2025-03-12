package com.expense.integration_test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties("pageable")
public class HelperPage<T> extends PageImpl<T> {
    private HelperPage(@JsonProperty("content") List<T> content,
                       @JsonProperty("number") int number,
                       @JsonProperty("size") int size,
                       @JsonProperty("totalElements") long totalElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }
}