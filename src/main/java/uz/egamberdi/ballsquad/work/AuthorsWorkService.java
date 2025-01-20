package uz.egamberdi.ballsquad.work;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.egamberdi.ballsquad.author.Author;
import uz.egamberdi.ballsquad.author.AuthorService;
import uz.egamberdi.ballsquad.openLibrary.OpenLibraryService;

import java.util.List;

@Service
public class AuthorsWorkService {

    private final AuthorService authorService;
    private final AuthorsWorkRepository authorsWorkRepository;
    private final OpenLibraryService openLibraryService;

    public AuthorsWorkService(AuthorService authorService,
                              AuthorsWorkRepository authorsWorkRepository,
                              OpenLibraryService openLibraryService) {
        this.authorService = authorService;
        this.authorsWorkRepository = authorsWorkRepository;
        this.openLibraryService = openLibraryService;
    }

    public Page<AuthorsWork> getAuthorsWork(String akey, int page, int size) {
        Author author = authorService.findByAKey(akey);
        if (author == null) {
            return null;
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AuthorsWork> dbresponse = authorsWorkRepository.findAuthorsWork(akey, pageRequest);
        if (dbresponse.hasContent()) {
            return dbresponse;
        }
        openLibraryService.fetchWorksByAuthorId(author);
        return authorsWorkRepository.findAll(pageRequest);
    }

}