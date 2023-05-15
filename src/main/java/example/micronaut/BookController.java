package example.micronaut;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
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

    @Put // <6>
    HttpResponse<?> update(@Body @Valid GenreUpdateCommand command) { // <7>
        int numberOfEntitiesUpdated = bookRepository.update(command.getId(), command.getName());

        return HttpResponse
                .noContent()
                .header(LOCATION, location(command.getId()).getPath()); // <8>
    }

    @Get(value = "/list{?args*}") // <9>
    List<Book> list(@Valid SortingAndOrderArguments args) {
        return bookRepository.findAll(args);
    }

    @Post // <10>
    HttpResponse<Book> save(@Body @Valid BookSaveCommand cmd) {
        Book book = bookRepository.save(cmd.getName(), cmd.getIsbn(), cmd.getGenreId());

        return HttpResponse
                .created(book)
                .headers(headers -> headers.location(location(book.getId())));
    }

    @Post("/ex") // <11>
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

    @Delete("/{id}") // <12>
    HttpResponse<?> delete(Long id) {
        bookRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/books/" + id);
    }
}
