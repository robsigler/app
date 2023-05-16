package example.micronaut;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.util.List;

import static io.micronaut.http.HttpHeaders.LOCATION;

@ExecuteOn(TaskExecutors.IO)  // <1>
@Controller("/books")  // <2>
class BookController {

    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) { // <3>
        this.bookRepository = bookRepository;
    }

    @Get("/{id}") // <4>
    Book show(Long id) {
        return bookRepository
                .findById(id)
                .orElse(null); // <5>
    }

    @View("bookscreate")
    @Get("/create")
    public Map<String, Object> create() {
        return createModelWithBlankValues();
    }

    private Map<String, Object> createModelWithBlankValues() {
        final Map<String, Object> model = new HashMap<>();
        model.put("title", "");
        model.put("isbn", "");
        model.put("genreId", "");
        return model;
    }

    @Put // <6>
    HttpResponse<?> update(@Body @Valid BookUpdateCommand command) { // <7>
        int numberOfEntitiesUpdated = bookRepository.update(command.getId(), command.getName(), command.getIsbn(), command.getGenreId());

        return HttpResponse
                .noContent()
                .header(LOCATION, location(command.getId()).getPath()); // <8>
    }

    @Get(value = "/list{?args*}") // <9>
    List<Book> list(@Valid SortingAndOrderArguments args) {
        return bookRepository.findAll(args);
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post()
    HttpResponse<Book> save(@Body @Valid BookSaveCommand cmd) {
        Book book = bookRepository.save(cmd.getName(), cmd.getIsbn(), cmd.getGenreId());

        return HttpResponse
                .created(book)
                .headers(headers -> headers.location(location(book.getId())));
    }

    @Post("/ex")
    HttpResponse<Book> saveExceptions(@Body @Valid BookSaveCommand cmd) {
        try {
            Book book = bookRepository.saveWithException(cmd.getName(), cmd.getIsbn(), cmd.getGenreId());
            return HttpResponse
                    .created(book)
                    .headers(headers -> headers.location(location(book.getId())));
        } catch(PersistenceException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}")
    HttpResponse<?> delete(Long id) {
        bookRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/books/" + id);
    }
}
