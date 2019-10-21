package com.borzdykooa.entity;

import com.borzdykooa.dto.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "document", schema = "document_schema")
public class Document extends IdEntity {

    private String title;

    @Enumerated(value = EnumType.STRING)
    private DocumentType type;

    @Column(name = "upload_date")
    private LocalDateTime dateTime;

    private String url;
}
