package uz.egamberdi.ballsquad.author;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/")
    public HttpEntity<?> getAll() {
        try {
            return ResponseEntity.ok(authorService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public HttpEntity<?> getAuthor(@RequestParam(name = "q", defaultValue = "", required = false) String name,
                                   @RequestParam(defaultValue = "0", required = false) int page,
                                   @RequestParam(defaultValue = "10", required = false) int size) {
        try {
            return ResponseEntity.ok(authorService.findOrFetchAuthor(name, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}