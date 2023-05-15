package example.micronaut;

import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton // <1>
public class BookRepositoryImpl implements BookRepository {

    private static final List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");

    private final EntityManager entityManager;  // <2>
    private final ApplicationConfiguration applicationConfiguration;

    public BookRepositoryImpl(EntityManager entityManager, // <2>
                               ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @ReadOnly // <3>
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    @Transactional // <4>
    public Book save(@NotBlank String name, @NotBlank String isbn, @NotBlank Long genreId) {
        Book book = new Book(name, isbn, entityManager.find(Genre.class, genreId));
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional // <4>
    public void deleteById(long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @ReadOnly // <3>
    public List<Book> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT b FROM Book as b";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY b." + args.getSort().get() + ' ' + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Book> query = entityManager.createQuery(qlString, Book.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);

        return query.getResultList();
    }

    @Override
    @Transactional // <4>
    public int update(long id, @NotBlank String name) {
        return entityManager.createQuery("UPDATE Book b SET name = :name where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional // <4>
    public Book saveWithException(@NotBlank String name, @NotBlank String isbn, @NotBlank Long genreId) {
        save(name, isbn, genreId);
        throw new PersistenceException();
    }
}
