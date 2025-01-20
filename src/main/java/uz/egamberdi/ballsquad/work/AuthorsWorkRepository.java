package uz.egamberdi.ballsquad.work;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorsWorkRepository extends JpaRepository<AuthorsWork, Integer> {

    @Query("select aw from authors_work aw where aw.author.akey = :akey")
    Page<AuthorsWork> findAuthorsWork(@Param("akey") String akey, Pageable pageable);

}