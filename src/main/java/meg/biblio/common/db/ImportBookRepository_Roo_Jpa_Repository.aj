// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db;

import meg.biblio.common.db.ImportBookRepository;
import meg.biblio.common.db.dao.ImportBookDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect ImportBookRepository_Roo_Jpa_Repository {
    
    declare parents: ImportBookRepository extends JpaRepository<ImportBookDao, Long>;
    
    declare parents: ImportBookRepository extends JpaSpecificationExecutor<ImportBookDao>;
    
    declare @type: ImportBookRepository: @Repository;
    
}
