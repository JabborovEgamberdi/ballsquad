package uz.egamberdi.ballsquad.author;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String akey; // key is a reserved keyword in MySQL, so akey -> author key

    @Column(nullable = false)
    private String name;

    private Integer workCount;

    public Author(String authorName, String authorKey, int workCount) {
        this.akey = authorKey;
        this.name = authorName;
        this.workCount = workCount;
    }

    public Author() {

    }

}