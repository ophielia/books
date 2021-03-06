// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.lending.db.dao;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.lending.db.LoanRecordRepository;
import meg.biblio.lending.db.dao.LoanRecordDaoDataOnDemand;
import meg.biblio.lending.db.dao.LoanRecordDaoIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LoanRecordDaoIntegrationTest_Roo_IntegrationTest {
    
    declare @type: LoanRecordDaoIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: LoanRecordDaoIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: LoanRecordDaoIntegrationTest: @Transactional;
    
    @Autowired
    LoanRecordDaoDataOnDemand LoanRecordDaoIntegrationTest.dod;
    
    @Autowired
    LoanRecordRepository LoanRecordDaoIntegrationTest.loanRecordRepository;
    
    @Test
    public void LoanRecordDaoIntegrationTest.testCount() {
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", dod.getRandomLoanRecordDao());
        long count = loanRecordRepository.count();
        Assert.assertTrue("Counter for 'LoanRecordDao' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void LoanRecordDaoIntegrationTest.testFind() {
        LoanRecordDao obj = dod.getRandomLoanRecordDao();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to provide an identifier", id);
        obj = loanRecordRepository.findOne(id);
        Assert.assertNotNull("Find method for 'LoanRecordDao' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LoanRecordDao' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void LoanRecordDaoIntegrationTest.testFindAll() {
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", dod.getRandomLoanRecordDao());
        long count = loanRecordRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'LoanRecordDao', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<LoanRecordDao> result = loanRecordRepository.findAll();
        Assert.assertNotNull("Find all method for 'LoanRecordDao' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LoanRecordDao' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void LoanRecordDaoIntegrationTest.testFindEntries() {
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", dod.getRandomLoanRecordDao());
        long count = loanRecordRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LoanRecordDao> result = loanRecordRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'LoanRecordDao' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LoanRecordDao' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void LoanRecordDaoIntegrationTest.testFlush() {
        LoanRecordDao obj = dod.getRandomLoanRecordDao();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to provide an identifier", id);
        obj = loanRecordRepository.findOne(id);
        Assert.assertNotNull("Find method for 'LoanRecordDao' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLoanRecordDao(obj);
        Integer currentVersion = obj.getVersion();
        loanRecordRepository.flush();
        Assert.assertTrue("Version for 'LoanRecordDao' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void LoanRecordDaoIntegrationTest.testSaveUpdate() {
        LoanRecordDao obj = dod.getRandomLoanRecordDao();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to provide an identifier", id);
        obj = loanRecordRepository.findOne(id);
        boolean modified =  dod.modifyLoanRecordDao(obj);
        Integer currentVersion = obj.getVersion();
        LoanRecordDao merged = loanRecordRepository.save(obj);
        loanRecordRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'LoanRecordDao' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void LoanRecordDaoIntegrationTest.testSave() {
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", dod.getRandomLoanRecordDao());
        LoanRecordDao obj = dod.getNewTransientLoanRecordDao(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LoanRecordDao' identifier to be null", obj.getId());
        try {
            loanRecordRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        loanRecordRepository.flush();
        Assert.assertNotNull("Expected 'LoanRecordDao' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void LoanRecordDaoIntegrationTest.testDelete() {
        LoanRecordDao obj = dod.getRandomLoanRecordDao();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LoanRecordDao' failed to provide an identifier", id);
        obj = loanRecordRepository.findOne(id);
        loanRecordRepository.delete(obj);
        loanRecordRepository.flush();
        Assert.assertNull("Failed to remove 'LoanRecordDao' with identifier '" + id + "'", loanRecordRepository.findOne(id));
    }
    
}
