// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db.dao;

import java.util.List;
import meg.biblio.common.db.dao.SelectKeyDao;
import meg.biblio.common.db.dao.SelectValueDao;

privileged aspect SelectKeyDao_Roo_JavaBean {
    
    public String SelectKeyDao.getLookup() {
        return this.lookup;
    }
    
    public void SelectKeyDao.setLookup(String lookup) {
        this.lookup = lookup;
    }
    
    public List<SelectValueDao> SelectKeyDao.getSelectvalues() {
        return this.selectvalues;
    }
    
    public void SelectKeyDao.setSelectvalues(List<SelectValueDao> selectvalues) {
        this.selectvalues = selectvalues;
    }
    
}
