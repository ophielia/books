package meg.biblio.lending.db;
import java.util.List;

import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.lending.db.dao.PersonDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = PersonDao.class)
public interface PersonRepository {
	

	@Query("select r from PersonDao as r where r.barcodeid = :code")
	PersonDao findPersonByBarcode(@Param("code") String code);

	@Query("select r from PersonDao as r where r.client = :client")
	List<PersonDao> findPeopleBelongingToClient(@Param("client") ClientDao client);
}
