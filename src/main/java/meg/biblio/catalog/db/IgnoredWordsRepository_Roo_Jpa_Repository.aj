// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db;

import meg.biblio.catalog.db.IgnoredWordsDao;
import meg.biblio.catalog.db.IgnoredWordsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect IgnoredWordsRepository_Roo_Jpa_Repository {
    
    declare parents: IgnoredWordsRepository extends JpaRepository<IgnoredWordsDao, Long>;
    
    declare parents: IgnoredWordsRepository extends JpaSpecificationExecutor<IgnoredWordsDao>;
    
    declare @type: IgnoredWordsRepository: @Repository;
    
}
