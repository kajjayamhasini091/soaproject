package soa.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soa.access.model.Access;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findByUserId(Long userId);
    List<Access> findByContentId(Long contentId);
    Optional<Access> findByUserIdAndContentId(Long userId, Long contentId);
    Optional<Access> findByUserIdAndContentIdAndAccessStatus(Long userId, Long contentId, String accessStatus);
}
