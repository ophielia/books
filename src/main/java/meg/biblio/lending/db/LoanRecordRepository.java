package meg.biblio.lending.db;
import java.util.List;

import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.lending.db.dao.LoanRecordDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = LoanRecordDao.class)
public interface LoanRecordRepository {

	
	@Query("select r from LoanRecordDao as r where r.client = :client")
	List<LoanRecordDao> findForClient(@Param("client") ClientDao client);
}
