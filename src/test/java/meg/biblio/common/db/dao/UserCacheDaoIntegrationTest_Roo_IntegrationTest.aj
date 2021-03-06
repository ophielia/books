// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db.dao;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.common.db.UserCacheRepository;
import meg.biblio.common.db.dao.UserCacheDaoDataOnDemand;
import meg.biblio.common.db.dao.UserCacheDaoIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect UserCacheDaoIntegrationTest_Roo_IntegrationTest {
    
    declare @type: UserCacheDaoIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: UserCacheDaoIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: UserCacheDaoIntegrationTest: @Transactional;
    
    @Autowired
    UserCacheDaoDataOnDemand UserCacheDaoIntegrationTest.dod;
    
    @Autowired
    UserCacheRepository UserCacheDaoIntegrationTest.userCacheRepository;
    
    @Test
    public void UserCacheDaoIntegrationTest.testCount() {
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", dod.getRandomUserCacheDao());
        long count = userCacheRepository.count();
        Assert.assertTrue("Counter for 'UserCacheDao' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void UserCacheDaoIntegrationTest.testFind() {
        UserCacheDao obj = dod.getRandomUserCacheDao();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to provide an identifier", id);
        obj = userCacheRepository.findOne(id);
        Assert.assertNotNull("Find method for 'UserCacheDao' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UserCacheDao' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void UserCacheDaoIntegrationTest.testFindAll() {
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", dod.getRandomUserCacheDao());
        long count = userCacheRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'UserCacheDao', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UserCacheDao> result = userCacheRepository.findAll();
        Assert.assertNotNull("Find all method for 'UserCacheDao' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UserCacheDao' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void UserCacheDaoIntegrationTest.testFindEntries() {
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", dod.getRandomUserCacheDao());
        long count = userCacheRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UserCacheDao> result = userCacheRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'UserCacheDao' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UserCacheDao' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void UserCacheDaoIntegrationTest.testFlush() {
        UserCacheDao obj = dod.getRandomUserCacheDao();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to provide an identifier", id);
        obj = userCacheRepository.findOne(id);
        Assert.assertNotNull("Find method for 'UserCacheDao' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUserCacheDao(obj);
        Integer currentVersion = obj.getVersion();
        userCacheRepository.flush();
        Assert.assertTrue("Version for 'UserCacheDao' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void UserCacheDaoIntegrationTest.testSaveUpdate() {
        UserCacheDao obj = dod.getRandomUserCacheDao();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to provide an identifier", id);
        obj = userCacheRepository.findOne(id);
        boolean modified =  dod.modifyUserCacheDao(obj);
        Integer currentVersion = obj.getVersion();
        UserCacheDao merged = userCacheRepository.save(obj);
        userCacheRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'UserCacheDao' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void UserCacheDaoIntegrationTest.testSave() {
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", dod.getRandomUserCacheDao());
        UserCacheDao obj = dod.getNewTransientUserCacheDao(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UserCacheDao' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'UserCacheDao' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void UserCacheDaoIntegrationTest.testDelete() {
        UserCacheDao obj = dod.getRandomUserCacheDao();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserCacheDao' failed to provide an identifier", id);
        obj = userCacheRepository.findOne(id);
        userCacheRepository.delete(obj);
        userCacheRepository.flush();
        Assert.assertNull("Failed to remove 'UserCacheDao' with identifier '" + id + "'", userCacheRepository.findOne(id));
    }
    
}
