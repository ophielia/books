// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import meg.biblio.catalog.db.dao.FoundDetailsDao;

privileged aspect FoundDetailsDao_Roo_Jpa_Entity {
    
    declare @type: FoundDetailsDao: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long FoundDetailsDao.id;
    
    @Version
    @Column(name = "version")
    private Integer FoundDetailsDao.version;
    
    public Long FoundDetailsDao.getId() {
        return this.id;
    }
    
    public void FoundDetailsDao.setId(Long id) {
        this.id = id;
    }
    
    public Integer FoundDetailsDao.getVersion() {
        return this.version;
    }
    
    public void FoundDetailsDao.setVersion(Integer version) {
        this.version = version;
    }
    
}
