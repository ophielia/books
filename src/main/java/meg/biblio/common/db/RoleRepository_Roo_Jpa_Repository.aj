// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db;

import meg.biblio.common.db.RoleRepository;
import meg.biblio.common.db.dao.RoleDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect RoleRepository_Roo_Jpa_Repository {
    
    declare parents: RoleRepository extends JpaRepository<RoleDao, Long>;
    
    declare parents: RoleRepository extends JpaSpecificationExecutor<RoleDao>;
    
    declare @type: RoleRepository: @Repository;
    
}
