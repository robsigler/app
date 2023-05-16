package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;
import javax.validation.constraints.NotBlank;

@Serdeable // <1>
public class BookUpdateCommand {
  @NotBlank private Long id;

  @NotBlank private String name;

  @NotBlank private String isbn;

  @NotBlank private Long genreId;

  public BookUpdateCommand(Long id, String name, String isbn, Long genreId) {
    this.id = id;
    this.name = name;
    this.isbn = isbn;
    this.genreId = genreId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Long getGenreId() {
    return genreId;
  }

  public void setGenreId(Long genreId) {
    this.genreId = genreId;
  }
}
