package com.borzdykooa.service;

import com.borzdykooa.dto.DocumentDto;
import com.borzdykooa.dto.DocumentSearchDto;
import com.borzdykooa.dto.DocumentType;
import com.borzdykooa.dto.SearchRequestDto;
import com.borzdykooa.entity.Document;
import com.borzdykooa.repository.DocumentRepository;
import com.borzdykooa.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private static final String DIRECTORY = "documents";
    private static final String EXTENSION = ".txt";
    private static final String TITLE = "title";
    private static final String TYPE = "type";

    private final DocumentRepository documentRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Transactional
    public DocumentDto save(MultipartFile multipartFile, String title, DocumentType type) {
        Document document = Document.builder()
                .title(title)
                .type(type)
                .dateTime(LocalDateTime.now(Clock.systemUTC()))
                .url(buildUrl(title))
                .build();
        Optional<Document> documentFromDatabase = documentRepository.findByTitle(title);
        documentFromDatabase.ifPresent(docFromDatabase -> document.setId(docFromDatabase.getId()));
        Document savedDocument = documentRepository.save(document);
        try {
            File file = new File(buildUrl(title));
            multipartFile.transferTo(file.getAbsoluteFile());
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred in DocumentService:save");
        }

        return DtoConverter.convert(savedDocument);
    }

    public String getTitleById(Long id) {
        StringBuilder title = new StringBuilder();
        documentRepository.findById(id).ifPresent(document -> title.append(document.getTitle()));

        return title.toString();
    }

    public Resource downloadByTitle(String title) {
        return new FileSystemResource(buildUrl(title));
    }

    public List<DocumentSearchDto> search(SearchRequestDto searchRequestDto) {
        String partTitle = searchRequestDto.getTitle();
        DocumentType type = searchRequestDto.getType();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteria = cb.createQuery(Document.class);
        Root<Document> root = criteria.from(Document.class);
        Predicate titlePredicate = cb.like(root.get(TITLE), "%" + partTitle + "%");
        Predicate typePredicate = cb.equal(root.get(TYPE), type);
        Predicate finalPredicate;
        if (partTitle == null) {
            finalPredicate = typePredicate;
        } else if (type == null) {
            finalPredicate = titlePredicate;
        } else {
            finalPredicate = cb.and(typePredicate, titlePredicate);
        }
        criteria.select(root).where(finalPredicate);
        List<Document> resultList = entityManager.createQuery(criteria).getResultList();

        return resultList.stream().map(DtoConverter::convertSearch).collect(Collectors.toList());
    }

    private String buildUrl(String title) {
        return String.join(File.separator, DIRECTORY, title + EXTENSION);
    }
}
