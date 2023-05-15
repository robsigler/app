package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;

@Serdeable // <1>
public class BookUpdateCommand {

    @NotBlank
    private String name;

    @NotBlank
    private String isbn;

    @NotBlank
    private Genre genre;

    public BookUpdateCommand(String name, String isbn, Genre genre) {
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
