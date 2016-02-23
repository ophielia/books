// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db.dao;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.common.db.UserCacheRepository;
import meg.biblio.common.db.dao.UserCacheDao;
import meg.biblio.common.db.dao.UserCacheDaoDataOnDemand;
import meg.biblio.common.db.dao.LoginDaoDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect UserCacheDaoDataOnDemand_Roo_DataOnDemand {
    
    declare @type: UserCacheDaoDataOnDemand: @Component;
    
    private Random UserCacheDaoDataOnDemand.rnd = new SecureRandom();
    
    private List<UserCacheDao> UserCacheDaoDataOnDemand.data;
    
    @Autowired
    LoginDaoDataOnDemand UserCacheDaoDataOnDemand.userLoginDaoDataOnDemand;
    
    @Autowired
    UserCacheRepository UserCacheDaoDataOnDemand.userCacheRepository;
    
    public UserCacheDao UserCacheDaoDataOnDemand.getNewTransientUserCacheDao(int index) {
        UserCacheDao obj = new UserCacheDao();
        setCachetag(obj, index);
        setExpiration(obj, index);
        setName(obj, index);
        setValue(obj, index);
        return obj;
    }
    
    public void UserCacheDaoDataOnDemand.setCachetag(UserCacheDao obj, int index) {
        String cachetag = "cachetag_" + index;
        obj.setCachetag(cachetag);
    }
    
    public void UserCacheDaoDataOnDemand.setExpiration(UserCacheDao obj, int index) {
        Date expiration = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setExpiration(expiration);
    }
    
    public void UserCacheDaoDataOnDemand.setName(UserCacheDao obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void UserCacheDaoDataOnDemand.setValue(UserCacheDao obj, int index) {
        String value = "value_" + index;
        obj.setValue(value);
    }
    
    public UserCacheDao UserCacheDaoDataOnDemand.getSpecificUserCacheDao(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UserCacheDao obj = data.get(index);
        Long id = obj.getId();
        return userCacheRepository.findOne(id);
    }
    
    public UserCacheDao UserCacheDaoDataOnDemand.getRandomUserCacheDao() {
        init();
        UserCacheDao obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return userCacheRepository.findOne(id);
    }
    
    public boolean UserCacheDaoDataOnDemand.modifyUserCacheDao(UserCacheDao obj) {
        return false;
    }
    
    public void UserCacheDaoDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = userCacheRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UserCacheDao' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UserCacheDao>();
        for (int i = 0; i < 10; i++) {
            UserCacheDao obj = getNewTransientUserCacheDao(i);
            try {
                userCacheRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            userCacheRepository.flush();
            data.add(obj);
        }
    }
    
}