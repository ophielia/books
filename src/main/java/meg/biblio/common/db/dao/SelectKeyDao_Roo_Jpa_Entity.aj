// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import meg.biblio.common.db.dao.SelectKeyDao;

privileged aspect SelectKeyDao_Roo_Jpa_Entity {
    
    declare @type: SelectKeyDao: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long SelectKeyDao.id;
    
    @Version
    @Column(name = "version")
    private Integer SelectKeyDao.version;
    
    public Long SelectKeyDao.getId() {
        return this.id;
    }
    
    public void SelectKeyDao.setId(Long id) {
        this.id = id;
    }
    
    public Integer SelectKeyDao.getVersion() {
        return this.version;
    }
    
    public void SelectKeyDao.setVersion(Integer version) {
        this.version = version;
    }
    
}
