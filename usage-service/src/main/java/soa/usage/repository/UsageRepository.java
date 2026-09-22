package soa.usage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soa.usage.model.Usage;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsageRepository extends JpaRepository<Usage, Long> {
    List<Usage> findByUserIdOrderBySessionStartDesc(Long userId);
    List<Usage> findByContentIdOrderBySessionStartDesc(Long contentId);
    Optional<Usage> findTopByUserIdAndContentIdAndReadingStatusOrderBySessionStartDesc(Long userId, Long contentId, String status);
    List<Usage> findByUserIdAndContentId(Long userId, Long contentId);
}
