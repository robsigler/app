package example.micronaut;

import example.micronaut.domain.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<Book> findById(long id);

    Book save(@NotBlank String name, @NotBlank String isbn, @NotBlank Long genreId);

    Book saveWithException(@NotBlank String name, @NotBlank String isbn, @NotBlank Long genreId);

    void deleteById(long id);

    List<Book> findAll(@NotNull SortingAndOrderArguments args);

    int update(long id, @NotBlank String name);
}
