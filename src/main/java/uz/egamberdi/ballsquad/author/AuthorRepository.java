package uz.egamberdi.ballsquad.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByName(String name);

    @Query(value = "SELECT * FROM authors WHERE MATCH(name) AGAINST (:searchTerm IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    Page<Author> findByFullTextSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

    Author findByAkey(String akey);

}