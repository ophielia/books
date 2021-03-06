// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.catalog.db.FoundWordsDaoDataOnDemand;
import meg.biblio.catalog.db.FoundWordsDaoIntegrationTest;
import meg.biblio.catalog.db.FoundWordsRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FoundWordsDaoIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FoundWordsDaoIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FoundWordsDaoIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: FoundWordsDaoIntegrationTest: @Transactional;
    
    @Autowired
    FoundWordsDaoDataOnDemand FoundWordsDaoIntegrationTest.dod;
    
    @Autowired
    FoundWordsRepository FoundWordsDaoIntegrationTest.foundWordsRepository;
    
    @Test
    public void FoundWordsDaoIntegrationTest.testCount() {
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to initialize correctly", dod.getRandomFoundWordsDao());
        long count = foundWordsRepository.count();
        Assert.assertTrue("Counter for 'FoundWordsDao' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FoundWordsDaoIntegrationTest.testFind() {
        FoundWordsDao obj = dod.getRandomFoundWordsDao();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to provide an identifier", id);
        obj = foundWordsRepository.findOne(id);
        Assert.assertNotNull("Find method for 'FoundWordsDao' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'FoundWordsDao' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FoundWordsDaoIntegrationTest.testFindEntries() {
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to initialize correctly", dod.getRandomFoundWordsDao());
        long count = foundWordsRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<FoundWordsDao> result = foundWordsRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'FoundWordsDao' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'FoundWordsDao' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FoundWordsDaoIntegrationTest.testFlush() {
        FoundWordsDao obj = dod.getRandomFoundWordsDao();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to provide an identifier", id);
        obj = foundWordsRepository.findOne(id);
        Assert.assertNotNull("Find method for 'FoundWordsDao' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFoundWordsDao(obj);
        Integer currentVersion = obj.getVersion();
        foundWordsRepository.flush();
        Assert.assertTrue("Version for 'FoundWordsDao' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FoundWordsDaoIntegrationTest.testSaveUpdate() {
        FoundWordsDao obj = dod.getRandomFoundWordsDao();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to provide an identifier", id);
        obj = foundWordsRepository.findOne(id);
        boolean modified =  dod.modifyFoundWordsDao(obj);
        Integer currentVersion = obj.getVersion();
        FoundWordsDao merged = foundWordsRepository.save(obj);
        foundWordsRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'FoundWordsDao' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FoundWordsDaoIntegrationTest.testSave() {
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to initialize correctly", dod.getRandomFoundWordsDao());
        FoundWordsDao obj = dod.getNewTransientFoundWordsDao(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'FoundWordsDao' identifier to be null", obj.getId());
        try {
            foundWordsRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        foundWordsRepository.flush();
        Assert.assertNotNull("Expected 'FoundWordsDao' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FoundWordsDaoIntegrationTest.testDelete() {
        FoundWordsDao obj = dod.getRandomFoundWordsDao();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FoundWordsDao' failed to provide an identifier", id);
        obj = foundWordsRepository.findOne(id);
        foundWordsRepository.delete(obj);
        foundWordsRepository.flush();
        Assert.assertNull("Failed to remove 'FoundWordsDao' with identifier '" + id + "'", foundWordsRepository.findOne(id));
    }
    
}
