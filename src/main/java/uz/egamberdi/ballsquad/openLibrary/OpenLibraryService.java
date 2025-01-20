package uz.egamberdi.ballsquad.openLibrary;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import uz.egamberdi.ballsquad.author.Author;
import uz.egamberdi.ballsquad.author.AuthorRepository;
import uz.egamberdi.ballsquad.work.AuthorsWork;
import uz.egamberdi.ballsquad.work.AuthorsWorkRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OpenLibraryService {

    private static final String OPEN_LIBRARY_AUTHORS_API_URL = "https://openlibrary.org/search/authors.json";
    private static final String OPEN_LIBRARY_AUTHORS_WORK_API_URL = "https://openlibrary.org/authors/";
    private static final String OPEN_LIBRARY_API_URL = "https://openlibrary.org";

    private final OkHttpClient okHttpClient;
    private final AuthorRepository authorRepository;
    private final AuthorsWorkRepository authorsWorkRepository;

    public OpenLibraryService(OkHttpClient okHttpClient, AuthorRepository authorRepository, AuthorsWorkRepository authorsWorkRepository) {
        this.okHttpClient = okHttpClient;
        this.authorRepository = authorRepository;
        this.authorsWorkRepository = authorsWorkRepository;
    }

    public List<Author> fetchAuthorByName(String name, int offset, int limit) {
        String url = String.format("%s?q=%s&offset=%d&limit=%d", OPEN_LIBRARY_AUTHORS_API_URL, name, offset, limit);
        List<Author> authors = new ArrayList<>();
        try (Response response = okHttpClient.newCall(request(url)).execute()) {
            assert response.body() != null;
            JSONObject jsonObject = new JSONObject(response.body().string());
            int numFound = jsonObject.getInt("numFound");
            JSONArray authorArr = jsonObject.getJSONArray("docs");
            for (int i = 0; i < authorArr.length(); i++) {
                JSONObject authorObj = authorArr.getJSONObject(i);
                String authorName = authorObj.getString("name");
                String authorKey = authorObj.getString("key");
                int workCount = authorObj.getInt("work_count");
                Author author = new Author(authorName, authorKey, workCount);
                authors.add(author);
            }
            while (offset > numFound) {
                authorRepository.saveAll(authors);
                authors = new ArrayList<>();
                offset += limit;
                fetchAuthorByName(name, offset, limit);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authors;
    }

    public List<AuthorsWork> fetchWorksByAuthorId(Author author) {
        final String suffix = "/works.json";
        String url = String.format("%s%s%s", OPEN_LIBRARY_AUTHORS_WORK_API_URL, author.getAkey(), suffix);
        log.info("Initial OpenLibrary URL: {}", url);
        List<AuthorsWork> allWorks = new ArrayList<>();
        while (url != null) {
            try (Response response = okHttpClient.newCall(request(url)).execute()) {
                assert response.body() != null;
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray authorWorks = jsonObject.getJSONArray("entries");
                for (int i = 0; i < authorWorks.length(); i++) {
                    JSONObject authorWorkObj = authorWorks.getJSONObject(i);
                    String title = authorWorkObj.optString("title", null);
                    if (title != null) {
                        allWorks.add(new AuthorsWork(title, author));
                    }
                }
                authorsWorkRepository.saveAll(allWorks);
                JSONObject links = jsonObject.optJSONObject("links");
                url = links != null ? links.optString("next", null) : null;
                url = url != null ? OPEN_LIBRARY_API_URL + url : null;
                log.info("Next page URL: {}", url);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return allWorks;
    }

    private Request request(String url) {
        return new Request.Builder().url(url).build();
    }

}