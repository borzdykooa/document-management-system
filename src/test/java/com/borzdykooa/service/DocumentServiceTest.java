package com.borzdykooa.service;

import com.borzdykooa.dto.DocumentSearchDto;
import com.borzdykooa.dto.DocumentType;
import com.borzdykooa.dto.SearchRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Test
    public void searchByType() {
        List<DocumentSearchDto> result = documentService.search(new SearchRequestDto(DocumentType.POETRY));

        assertEquals(2, result.size());
        assertEquals(new DocumentSearchDto(2L, "1984", "Poetry"), result.get(0));
        assertEquals(new DocumentSearchDto(4L, "title", "Poetry"), result.get(1));
    }

    @Test
    public void searchByPartTitle() {
        List<DocumentSearchDto> result = documentService.search(new SearchRequestDto("t"));

        assertEquals(2, result.size());
        assertEquals(new DocumentSearchDto(3L, "Mysterious Affair at Styles", "Detective"), result.get(0));
        assertEquals(new DocumentSearchDto(4L, "title", "Poetry"), result.get(1));
    }

    @Test
    public void searchByPartTitleAndType() {
        List<DocumentSearchDto> result = documentService.search(new SearchRequestDto("t", DocumentType.POETRY));

        assertEquals(1, result.size());
        assertEquals(new DocumentSearchDto(4L, "title", "Poetry"), result.get(0));
    }
}
