// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db;

import meg.biblio.catalog.db.FoundWordsDao;
import meg.biblio.catalog.db.dao.BookDetailDao;

privileged aspect FoundWordsDao_Roo_JavaBean {
    
    public BookDetailDao FoundWordsDao.getBookdetail() {
        return this.bookdetail;
    }
    
    public void FoundWordsDao.setBookdetail(BookDetailDao bookdetail) {
        this.bookdetail = bookdetail;
    }
    
    public String FoundWordsDao.getWord() {
        return this.word;
    }
    
    public void FoundWordsDao.setWord(String word) {
        this.word = word;
    }
    
    public Integer FoundWordsDao.getCountintext() {
        return this.countintext;
    }
    
    public void FoundWordsDao.setCountintext(Integer countintext) {
        this.countintext = countintext;
    }
    
}
