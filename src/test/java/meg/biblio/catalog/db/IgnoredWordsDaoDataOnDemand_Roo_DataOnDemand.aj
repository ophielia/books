// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.catalog.db.IgnoredWordsDao;
import meg.biblio.catalog.db.IgnoredWordsDaoDataOnDemand;
import meg.biblio.catalog.db.IgnoredWordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect IgnoredWordsDaoDataOnDemand_Roo_DataOnDemand {
    
    declare @type: IgnoredWordsDaoDataOnDemand: @Component;
    
    private Random IgnoredWordsDaoDataOnDemand.rnd = new SecureRandom();
    
    private List<IgnoredWordsDao> IgnoredWordsDaoDataOnDemand.data;
    
    @Autowired
    IgnoredWordsRepository IgnoredWordsDaoDataOnDemand.ignoredWordsRepository;
    
    public IgnoredWordsDao IgnoredWordsDaoDataOnDemand.getNewTransientIgnoredWordsDao(int index) {
        IgnoredWordsDao obj = new IgnoredWordsDao();
        setWord(obj, index);
        return obj;
    }
    
    public void IgnoredWordsDaoDataOnDemand.setWord(IgnoredWordsDao obj, int index) {
        String word = "word_" + index;
        obj.setWord(word);
    }
    
    public IgnoredWordsDao IgnoredWordsDaoDataOnDemand.getSpecificIgnoredWordsDao(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        IgnoredWordsDao obj = data.get(index);
        Long id = obj.getId();
        return ignoredWordsRepository.findOne(id);
    }
    
    public IgnoredWordsDao IgnoredWordsDaoDataOnDemand.getRandomIgnoredWordsDao() {
        init();
        IgnoredWordsDao obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ignoredWordsRepository.findOne(id);
    }
    
    public boolean IgnoredWordsDaoDataOnDemand.modifyIgnoredWordsDao(IgnoredWordsDao obj) {
        return false;
    }
    
    public void IgnoredWordsDaoDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ignoredWordsRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'IgnoredWordsDao' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<IgnoredWordsDao>();
        for (int i = 0; i < 10; i++) {
            IgnoredWordsDao obj = getNewTransientIgnoredWordsDao(i);
            try {
                ignoredWordsRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            ignoredWordsRepository.flush();
            data.add(obj);
        }
    }
    
}
