package meg.biblio.lending.db.dao;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class StudentDao extends PersonDao {
	
	
	private Long sectionkey;
	

	
}
