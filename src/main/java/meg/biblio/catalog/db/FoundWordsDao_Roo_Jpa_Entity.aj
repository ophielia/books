// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import meg.biblio.catalog.db.FoundWordsDao;

privileged aspect FoundWordsDao_Roo_Jpa_Entity {
    
    declare @type: FoundWordsDao: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long FoundWordsDao.id;
    
    @Version
    @Column(name = "version")
    private Integer FoundWordsDao.version;
    
    public Long FoundWordsDao.getId() {
        return this.id;
    }
    
    public void FoundWordsDao.setId(Long id) {
        this.id = id;
    }
    
    public Integer FoundWordsDao.getVersion() {
        return this.version;
    }
    
    public void FoundWordsDao.setVersion(Integer version) {
        this.version = version;
    }
    
}
