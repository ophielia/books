// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import meg.biblio.catalog.db.dao.ClassificationDao;

privileged aspect ClassificationDao_Roo_Jpa_Entity {
    
    declare @type: ClassificationDao: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ClassificationDao.id;
    
    @Version
    @Column(name = "version")
    private Integer ClassificationDao.version;
    
    public Long ClassificationDao.getId() {
        return this.id;
    }
    
    public void ClassificationDao.setId(Long id) {
        this.id = id;
    }
    
    public Integer ClassificationDao.getVersion() {
        return this.version;
    }
    
    public void ClassificationDao.setVersion(Integer version) {
        this.version = version;
    }
    
}
