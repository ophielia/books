// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db.dao;

import java.util.Date;
import java.util.List;

import meg.biblio.catalog.db.dao.ArtistDao;
import meg.biblio.catalog.db.dao.BookDao;
import meg.biblio.catalog.db.dao.SubjectDao;

privileged aspect BookDao_Roo_JavaBean {
    
    public Long BookDao.getClientid() {
        return this.clientid;
    }
    
    public void BookDao.setClientid(Long clientid) {
        this.clientid = clientid;
    }
    
    public String BookDao.getTitle() {
        return this.title;
    }
    
    public void BookDao.setTitle(String title) {
        this.title = title;
    }
    
    public List<ArtistDao> BookDao.getAuthors() {
        return this.authors;
    }
    
    public void BookDao.setAuthors(List<ArtistDao> authors) {
        this.authors = authors;
    }
    
    public List<ArtistDao> BookDao.getIllustrators() {
        return this.illustrators;
    }
    
    public void BookDao.setIllustrators(List<ArtistDao> illustrators) {
        this.illustrators = illustrators;
    }
    
    public List<SubjectDao> BookDao.getSubjects() {
        return this.subjects;
    }
    
    public void BookDao.setSubjects(List<SubjectDao> subjects) {
        this.subjects = subjects;
    }
    
    public Long BookDao.getPublisherkey() {
        return this.publisherkey;
    }
    
    public void BookDao.setPublisherkey(Long publisherkey) {
        this.publisherkey = publisherkey;
    }
    
    public Long BookDao.getPublishyear() {
        return this.publishyear;
    }
    
    public void BookDao.setPublishyear(Long publishyear) {
        this.publishyear = publishyear;
    }
    
    public String BookDao.getIsbn10() {
        return this.isbn10;
    }
    
    public void BookDao.setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }
    
    public String BookDao.getIsbn13() {
        return this.isbn13;
    }
    
    public void BookDao.setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }
    
    public Long BookDao.getLanguage() {
        return this.language;
    }
    
    public void BookDao.setLanguage(Long language) {
        this.language = language;
    }
    
    public Long BookDao.getType() {
        return this.type;
    }
    
    public void BookDao.setType(Long type) {
        this.type = type;
    }
    
    public String BookDao.getDescription() {
        return this.description;
    }
    
    public void BookDao.setDescription(String description) {
        this.description = description;
    }
    
    public Long BookDao.getStatus() {
        return this.status;
    }
    
    public void BookDao.setStatus(Long status) {
        this.status = status;
    }
    
    public Long BookDao.getDetailstatus() {
        return this.detailstatus;
    }
    
    public void BookDao.setDetailstatus(Long detailstatus) {
        this.detailstatus = detailstatus;
    }
    
    public Long BookDao.getShelfclass() {
        return this.shelfclass;
    }
    
    public void BookDao.setShelfclass(Long shelfclass) {
        this.shelfclass = shelfclass;
    }
    
    public Boolean BookDao.getShelfclassverified() {
        return this.shelfclassverified;
    }
    
    public void BookDao.setShelfclassverified(Boolean shelfclassverified) {
        this.shelfclassverified = shelfclassverified;
    }
    
    public Date BookDao.getCreatedon() {
        return this.createdon;
    }
    
    public void BookDao.setCreatedon(Date createdon) {
        this.createdon = createdon;
    }
    
    public String BookDao.getClientbookid() {
        return this.clientbookid;
    }
    
    public void BookDao.setClientbookid(String clientbookid) {
        this.clientbookid = clientbookid;
    }
    
}
