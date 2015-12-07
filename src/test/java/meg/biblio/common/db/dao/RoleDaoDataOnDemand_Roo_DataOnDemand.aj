// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db.dao;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.common.db.RoleRepository;
import meg.biblio.common.db.dao.RoleDao;
import meg.biblio.common.db.dao.RoleDaoDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect RoleDaoDataOnDemand_Roo_DataOnDemand {
    
    declare @type: RoleDaoDataOnDemand: @Component;
    
    private Random RoleDaoDataOnDemand.rnd = new SecureRandom();
    
    private List<RoleDao> RoleDaoDataOnDemand.data;
    
    @Autowired
    RoleRepository RoleDaoDataOnDemand.roleRepository;
    
    public RoleDao RoleDaoDataOnDemand.getNewTransientRoleDao(int index) {
        RoleDao obj = new RoleDao();
        setRolename(obj, index);
        return obj;
    }
    
    public RoleDao RoleDaoDataOnDemand.getSpecificRoleDao(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        RoleDao obj = data.get(index);
        Long id = obj.getId();
        return roleRepository.findOne(id);
    }
    
    public RoleDao RoleDaoDataOnDemand.getRandomRoleDao() {
        init();
        RoleDao obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return roleRepository.findOne(id);
    }
    
    public boolean RoleDaoDataOnDemand.modifyRoleDao(RoleDao obj) {
        return false;
    }
    
    public void RoleDaoDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = roleRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'RoleDao' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<RoleDao>();
        for (int i = 0; i < 10; i++) {
            RoleDao obj = getNewTransientRoleDao(i);
            try {
                roleRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            roleRepository.flush();
            data.add(obj);
        }
    }
    
}
