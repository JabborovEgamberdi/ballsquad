package uz.egamberdi.ballsquad.work;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorWorkController {

    private final AuthorsWorkService authorsWorkService;

    public AuthorWorkController(AuthorsWorkService authorsWorkService) {
        this.authorsWorkService = authorsWorkService;
    }

    @GetMapping("/{akey}")
    public HttpEntity<?> authorWork(@PathVariable String akey,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(authorsWorkService.getAuthorsWork(akey, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}