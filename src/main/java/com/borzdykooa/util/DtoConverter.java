package com.borzdykooa.util;

import com.borzdykooa.dto.DocumentDto;
import com.borzdykooa.dto.DocumentSearchDto;
import com.borzdykooa.entity.Document;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoConverter {

    public static DocumentDto convert(Document document) {
        return DocumentDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .type(document.getType().getValue())
                .dateTime(document.getDateTime())
                .url(document.getUrl())
                .build();
    }

    public static DocumentSearchDto convertSearch(Document document) {
        return DocumentSearchDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .type(document.getType().getValue())
                .build();
    }
}
