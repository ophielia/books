// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db;

import meg.biblio.catalog.db.BookRepository;
import meg.biblio.catalog.db.dao.BookDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect BookRepository_Roo_Jpa_Repository {
    
    declare parents: BookRepository extends JpaRepository<BookDao, Long>;
    
    declare parents: BookRepository extends JpaSpecificationExecutor<BookDao>;
    
    declare @type: BookRepository: @Repository;
    
}
