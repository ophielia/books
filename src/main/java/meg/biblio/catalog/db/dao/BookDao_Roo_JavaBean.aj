// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db.dao;

import java.util.Date;
import meg.biblio.catalog.db.dao.BookDao;
import meg.biblio.catalog.db.dao.BookDetailDao;

privileged aspect BookDao_Roo_JavaBean {
    
    public Long BookDao.getClientid() {
        return this.clientid;
    }
    
    public void BookDao.setClientid(Long clientid) {
        this.clientid = clientid;
    }
    
    public Long BookDao.getStatus() {
        return this.status;
    }
    
    public void BookDao.setStatus(Long status) {
        this.status = status;
    }
    
    public Long BookDao.getClientshelfcode() {
        return this.clientshelfcode;
    }
    
    public void BookDao.setClientshelfcode(Long clientshelfcode) {
        this.clientshelfcode = clientshelfcode;
    }
    
    public String BookDao.getClientshelfclass() {
        return this.clientshelfclass;
    }
    
    public void BookDao.setClientshelfclass(String clientshelfclass) {
        this.clientshelfclass = clientshelfclass;
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
    
    public Long BookDao.getClientbookidsort() {
        return this.clientbookidsort;
    }
    
    public void BookDao.setClientbookidsort(Long clientbookidsort) {
        this.clientbookidsort = clientbookidsort;
    }
    
    public String BookDao.getBarcodeid() {
        return this.barcodeid;
    }
    
    public void BookDao.setBarcodeid(String barcodeid) {
        this.barcodeid = barcodeid;
    }
    
    public void BookDao.setBookdetail(BookDetailDao bookdetail) {
        this.bookdetail = bookdetail;
    }
    
    public Long BookDao.getClientbooktype() {
        return this.clientbooktype;
    }
    
    public void BookDao.setClientbooktype(Long clientbooktype) {
        this.clientbooktype = clientbooktype;
    }
    
    public String BookDao.getNote() {
        return this.note;
    }
    
    public void BookDao.setNote(String note) {
        this.note = note;
    }
    
    public Boolean BookDao.getTocount() {
        return this.tocount;
    }
    
    public void BookDao.setTocount(Boolean tocount) {
        this.tocount = tocount;
    }
    
    public Long BookDao.getCountstatus() {
        return this.countstatus;
    }
    
    public void BookDao.setCountstatus(Long countstatus) {
        this.countstatus = countstatus;
    }
    
    public Date BookDao.getCounteddate() {
        return this.counteddate;
    }
    
    public void BookDao.setCounteddate(Date counteddate) {
        this.counteddate = counteddate;
    }
    
    public Long BookDao.getUserid() {
        return this.userid;
    }
    
    public void BookDao.setUserid(Long userid) {
        this.userid = userid;
    }
    
}