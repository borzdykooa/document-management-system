package com.borzdykooa.repository;

import com.borzdykooa.entity.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {

    Optional<Document> findByTitle(String title);
}
