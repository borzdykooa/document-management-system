package com.borzdykooa.controller;

import com.borzdykooa.dto.DocumentDto;
import com.borzdykooa.dto.DocumentSearchDto;
import com.borzdykooa.dto.DocumentType;
import com.borzdykooa.dto.SearchRequestDto;
import com.borzdykooa.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public DocumentDto upload(@RequestPart MultipartFile document,
                              @RequestParam("title") String title,
                              @RequestParam("type") DocumentType type) {
        return documentService.save(document, title, type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        String title = documentService.getTitleById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + title + ".txt\"")
                .body(documentService.downloadByTitle(title));
    }

    @GetMapping("/search")
    public List<DocumentSearchDto> search(@Valid SearchRequestDto searchRequestDto) {
        return documentService.search(searchRequestDto);
    }
}
