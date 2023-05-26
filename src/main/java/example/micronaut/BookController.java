package example.micronaut;

import static io.micronaut.http.HttpHeaders.LOCATION;

import example.micronaut.domain.Book;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;
import javax.validation.Valid;

@ExecuteOn(TaskExecutors.IO)
@Controller("/books")
class BookController {

  private final BookRepository bookRepository;

  BookController(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Get("/{id}")
  Book show(Long id) {
    return bookRepository.findById(id).orElse(null);
  }

  @View("bookscreate")
  @Get("/create")
  public Map<String, Object> create() {
    return createModelWithBlankValues();
  }

  private Map<String, Object> createModelWithBlankValues() {
    final Map<String, Object> model = new HashMap<>();
    model.put("name", "");
    model.put("isbn", "");
    model.put("genreId", "");
    return model;
  }

  @Put("/save")
  HttpResponse<?> update(@Body @Valid BookUpdateCommand command) {
    int numberOfEntitiesUpdated =
        bookRepository.update(
            command.getId(), command.getName(), command.getIsbn(), command.getGenreId());

    return HttpResponse.noContent().header(LOCATION, location(command.getId()).getPath());
  }

  @Get(value = "/list{?args*}")
  List<Book> list(@Valid SortingAndOrderArguments args) {
    return bookRepository.findAll(args);
  }

  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Post("/save")
  HttpResponse<Book> save(@Body @Valid BookSaveCommand cmd) {
    Book book = bookRepository.save(cmd.getName(), cmd.getIsbn(), cmd.getGenreId());

    return HttpResponse.created(book).headers(headers -> headers.location(location(book.getId())));
  }

  @Post("/ex")
  HttpResponse<Book> saveExceptions(@Body @Valid BookSaveCommand cmd) {
    try {
      Book book = bookRepository.saveWithException(cmd.getName(), cmd.getIsbn(), cmd.getGenreId());
      return HttpResponse.created(book)
          .headers(headers -> headers.location(location(book.getId())));
    } catch (PersistenceException e) {
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
