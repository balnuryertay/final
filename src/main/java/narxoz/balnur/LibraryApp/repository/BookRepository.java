package narxoz.balnur.LibraryApp.repository;

import narxoz.balnur.LibraryApp.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {
}
