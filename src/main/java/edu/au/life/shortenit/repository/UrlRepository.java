package edu.au.life.shortenit.repository;

import edu.au.life.shortenit.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    Optional<Url> findByCustomAlias(String customAlias);

    boolean existsByShortCode(String shortCode);

    boolean existsByCustomAlias(String customAlias);

    Page<Url> findAll(Pageable pageable); // to take pagination support

    @Query("SELECT u FROM Url u ORDER BY u.createdAt DESC")
    Page<Url> findAllOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT u FROM Url u WHERE u.isActive = true ORDER BY u.createdAt DESC") // Active URLs only
    Page<Url> findAllActiveUrls(Pageable pageable);

    @Query("SELECT COUNT(u) FROM Url u WHERE u.isActive = true")
    long countActiveUrls();

}