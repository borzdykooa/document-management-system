package com.borzdykooa.controller;

import com.borzdykooa.dto.DocumentDto;
import com.borzdykooa.dto.DocumentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void upload() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("document", "some text".getBytes());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/document/upload")
                .file(multipartFile)
                .param("title", "title example")
                .param("type", DocumentType.POETRY.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        DocumentDto documentDto = objectMapper.readValue(response, DocumentDto.class);

        assertNotNull(documentDto.getId());
        assertEquals("title example", documentDto.getTitle());
        assertEquals("Poetry", documentDto.getType());
        assertNotNull(documentDto.getDateTime());
        assertEquals("documents\\title example.txt", documentDto.getUrl());

        File file = new File(String.join(File.separator, "documents", "title example.txt"));
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        bufferedReader.close();

        assertEquals("some text", line);
        assertTrue(file.delete());
    }

    @Test
    public void download() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/document/" + 4)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("some text" + System.lineSeparator(), mvcResult.getResponse().getContentAsString());
        assertEquals("application/octet-stream", mvcResult.getResponse().getContentType());
    }
}
