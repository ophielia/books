// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.common.db.dao;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.common.db.LoginRepository;
import meg.biblio.common.db.dao.LoginDaoDataOnDemand;
import meg.biblio.common.db.dao.LoginDaoIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LoginDaoIntegrationTest_Roo_IntegrationTest {
    
    declare @type: LoginDaoIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: LoginDaoIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: LoginDaoIntegrationTest: @Transactional;
    
    @Autowired
    LoginDaoDataOnDemand LoginDaoIntegrationTest.dod;
    
    @Autowired
    LoginRepository LoginDaoIntegrationTest.loginRepository;
    
    @Test
    public void LoginDaoIntegrationTest.testCount() {
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", dod.getRandomUserLoginDao());
        long count = loginRepository.count();
        Assert.assertTrue("Counter for 'UserLoginDao' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void LoginDaoIntegrationTest.testFind() {
        UserLoginDao obj = dod.getRandomUserLoginDao();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to provide an identifier", id);
        obj = loginRepository.findOne(id);
        Assert.assertNotNull("Find method for 'UserLoginDao' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UserLoginDao' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void LoginDaoIntegrationTest.testFindAll() {
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", dod.getRandomUserLoginDao());
        long count = loginRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'UserLoginDao', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UserLoginDao> result = loginRepository.findAll();
        Assert.assertNotNull("Find all method for 'UserLoginDao' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UserLoginDao' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void LoginDaoIntegrationTest.testFindEntries() {
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", dod.getRandomUserLoginDao());
        long count = loginRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UserLoginDao> result = loginRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'UserLoginDao' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UserLoginDao' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void LoginDaoIntegrationTest.testFlush() {
        UserLoginDao obj = dod.getRandomUserLoginDao();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to provide an identifier", id);
        obj = loginRepository.findOne(id);
        Assert.assertNotNull("Find method for 'UserLoginDao' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUserLoginDao(obj);
        Integer currentVersion = obj.getVersion();
        loginRepository.flush();
        Assert.assertTrue("Version for 'UserLoginDao' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void LoginDaoIntegrationTest.testSaveUpdate() {
        UserLoginDao obj = dod.getRandomUserLoginDao();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to provide an identifier", id);
        obj = loginRepository.findOne(id);
        boolean modified =  dod.modifyUserLoginDao(obj);
        Integer currentVersion = obj.getVersion();
        UserLoginDao merged = loginRepository.save(obj);
        loginRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'UserLoginDao' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void LoginDaoIntegrationTest.testSave() {
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", dod.getRandomUserLoginDao());
        UserLoginDao obj = dod.getNewTransientUserLoginDao(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UserLoginDao' identifier to be null", obj.getId());
        try {
            loginRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        loginRepository.flush();
        Assert.assertNotNull("Expected 'UserLoginDao' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void LoginDaoIntegrationTest.testDelete() {
        UserLoginDao obj = dod.getRandomUserLoginDao();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserLoginDao' failed to provide an identifier", id);
        obj = loginRepository.findOne(id);
        loginRepository.delete(obj);
        loginRepository.flush();
        Assert.assertNull("Failed to remove 'UserLoginDao' with identifier '" + id + "'", loginRepository.findOne(id));
    }
    
}
