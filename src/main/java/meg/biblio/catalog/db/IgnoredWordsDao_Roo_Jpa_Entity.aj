// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import meg.biblio.catalog.db.IgnoredWordsDao;

privileged aspect IgnoredWordsDao_Roo_Jpa_Entity {
    
    declare @type: IgnoredWordsDao: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long IgnoredWordsDao.id;
    
    @Version
    @Column(name = "version")
    private Integer IgnoredWordsDao.version;
    
    public Long IgnoredWordsDao.getId() {
        return this.id;
    }
    
    public void IgnoredWordsDao.setId(Long id) {
        this.id = id;
    }
    
    public Integer IgnoredWordsDao.getVersion() {
        return this.version;
    }
    
    public void IgnoredWordsDao.setVersion(Integer version) {
        this.version = version;
    }
    
}