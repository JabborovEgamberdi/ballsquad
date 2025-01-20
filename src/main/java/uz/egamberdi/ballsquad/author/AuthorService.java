package uz.egamberdi.ballsquad.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.egamberdi.ballsquad.openLibrary.OpenLibraryService;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final OpenLibraryService openLibraryService;

    public AuthorService(
            AuthorRepository authorRepository,
            OpenLibraryService openLibraryService
    ) {
        this.authorRepository = authorRepository;
        this.openLibraryService = openLibraryService;
    }

    public Author findByAKey(String aKey) {
        return authorRepository.findByAkey(aKey);
    }

    public Page<Author> findOrFetchAuthor(String authorName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (authorName.isEmpty()) {
            return authorRepository.findAll(pageable);
        }
        Page<Author> dbResult = authorRepository.findByFullTextSearch(authorName, pageable);
        if (dbResult.hasContent()) {
            return dbResult;
        }
        openLibraryService.fetchAuthorByName(authorName, 0, 10);
        return authorRepository.findByFullTextSearch(authorName, pageable);
    }

}