package soa.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soa.content.model.Content;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByCategoryIgnoreCase(String category);
    List<Content> findByTypeIgnoreCase(String type);
    List<Content> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}
