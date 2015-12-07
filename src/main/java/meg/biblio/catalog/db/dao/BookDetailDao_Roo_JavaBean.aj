// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db.dao;

import java.util.List;
import meg.biblio.catalog.db.FoundWordsDao;
import meg.biblio.catalog.db.dao.BookDetailDao;
import meg.biblio.catalog.db.dao.PublisherDao;
import meg.biblio.catalog.db.dao.SubjectDao;

privileged aspect BookDetailDao_Roo_JavaBean {
    
    public List<SubjectDao> BookDetailDao.getSubjects() {
        return this.subjects;
    }
    
    public PublisherDao BookDetailDao.getPublisher() {
        return this.publisher;
    }
    
    public Long BookDetailDao.getPublishyear() {
        return this.publishyear;
    }
    
    public String BookDetailDao.getIsbn10() {
        return this.isbn10;
    }
    
    public String BookDetailDao.getIsbn13() {
        return this.isbn13;
    }
    
    public String BookDetailDao.getLanguage() {
        return this.language;
    }
    
    public String BookDetailDao.getImagelink() {
        return this.imagelink;
    }
    
    public void BookDetailDao.setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
    
    public String BookDetailDao.getDescription() {
        return this.description;
    }
    
    public List<FoundWordsDao> BookDetailDao.getFoundwords() {
        return this.foundwords;
    }
    
    public void BookDetailDao.setFoundwords(List<FoundWordsDao> foundwords) {
        this.foundwords = foundwords;
    }
    
    public Long BookDetailDao.getDetailstatus() {
        return this.detailstatus;
    }
    
    public void BookDetailDao.setDetailstatus(Long detailstatus) {
        this.detailstatus = detailstatus;
    }
    
    public void BookDetailDao.setFinderlog(Long finderlog) {
        this.finderlog = finderlog;
    }
    
    public Long BookDetailDao.getListedtype() {
        return this.listedtype;
    }
    
    public void BookDetailDao.setListedtype(Long listedtype) {
        this.listedtype = listedtype;
    }
    
    public String BookDetailDao.getShelfclass() {
        return this.shelfclass;
    }
    
    public void BookDetailDao.setShelfclass(String shelfclass) {
        this.shelfclass = shelfclass;
    }
    
    public String BookDetailDao.getArk() {
        return this.ark;
    }
    
    public void BookDetailDao.setArk(String ark) {
        this.ark = ark;
    }
    
    public Boolean BookDetailDao.getTextchange() {
        return this.textchange;
    }
    
    public void BookDetailDao.setTextchange(Boolean textchange) {
        this.textchange = textchange;
    }
    
    public Boolean BookDetailDao.getTrackchange() {
        return this.trackchange;
    }
    
    public void BookDetailDao.setTrackchange(Boolean trackchange) {
        this.trackchange = trackchange;
    }
    
}
