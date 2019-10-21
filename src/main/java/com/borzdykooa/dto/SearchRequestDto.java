package com.borzdykooa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDto {

    private String title;
    private DocumentType type;

    public SearchRequestDto(String title) {
        this.title = title;
    }

    public SearchRequestDto(DocumentType type) {
        this.type = type;
    }
}
