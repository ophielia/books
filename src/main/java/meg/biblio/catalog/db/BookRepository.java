package meg.biblio.catalog.db;
import meg.biblio.catalog.db.dao.BookDao;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = BookDao.class)
public interface BookRepository {
}
