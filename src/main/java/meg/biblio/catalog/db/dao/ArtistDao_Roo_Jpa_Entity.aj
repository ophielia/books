// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import meg.biblio.catalog.db.dao.ArtistDao;

privileged aspect ArtistDao_Roo_Jpa_Entity {
    
    declare @type: ArtistDao: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ArtistDao.id;
    
    @Version
    @Column(name = "version")
    private Integer ArtistDao.version;
    
    public Long ArtistDao.getId() {
        return this.id;
    }
    
    public void ArtistDao.setId(Long id) {
        this.id = id;
    }
    
    public Integer ArtistDao.getVersion() {
        return this.version;
    }
    
    public void ArtistDao.setVersion(Integer version) {
        this.version = version;
    }
    
}
