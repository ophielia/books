// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.catalog.db.dao;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.catalog.db.BookRepository;
import meg.biblio.catalog.db.dao.BookDaoDataOnDemand;
import meg.biblio.catalog.db.dao.BookDaoIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BookDaoIntegrationTest_Roo_IntegrationTest {
    
    declare @type: BookDaoIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: BookDaoIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: BookDaoIntegrationTest: @Transactional;
    
    @Autowired
    BookDaoDataOnDemand BookDaoIntegrationTest.dod;
    
    @Autowired
    BookRepository BookDaoIntegrationTest.bookRepository;
    
    @Test
    public void BookDaoIntegrationTest.testCount() {
        Assert.assertNotNull("Data on demand for 'BookDao' failed to initialize correctly", dod.getRandomBookDao());
        long count = bookRepository.count();
        Assert.assertTrue("Counter for 'BookDao' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void BookDaoIntegrationTest.testFind() {
        BookDao obj = dod.getRandomBookDao();
        Assert.assertNotNull("Data on demand for 'BookDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BookDao' failed to provide an identifier", id);
        obj = bookRepository.findOne(id);
        Assert.assertNotNull("Find method for 'BookDao' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'BookDao' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void BookDaoIntegrationTest.testFindEntries() {
        Assert.assertNotNull("Data on demand for 'BookDao' failed to initialize correctly", dod.getRandomBookDao());
        long count = bookRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<BookDao> result = bookRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'BookDao' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'BookDao' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void BookDaoIntegrationTest.testFlush() {
        BookDao obj = dod.getRandomBookDao();
        Assert.assertNotNull("Data on demand for 'BookDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BookDao' failed to provide an identifier", id);
        obj = bookRepository.findOne(id);
        Assert.assertNotNull("Find method for 'BookDao' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyBookDao(obj);
        Integer currentVersion = obj.getVersion();
        bookRepository.flush();
        Assert.assertTrue("Version for 'BookDao' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void BookDaoIntegrationTest.testSaveUpdate() {
        BookDao obj = dod.getRandomBookDao();
        Assert.assertNotNull("Data on demand for 'BookDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BookDao' failed to provide an identifier", id);
        obj = bookRepository.findOne(id);
        boolean modified =  dod.modifyBookDao(obj);
        Integer currentVersion = obj.getVersion();
        BookDao merged = bookRepository.save(obj);
        bookRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'BookDao' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void BookDaoIntegrationTest.testSave() {
        Assert.assertNotNull("Data on demand for 'BookDao' failed to initialize correctly", dod.getRandomBookDao());
        BookDao obj = dod.getNewTransientBookDao(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'BookDao' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'BookDao' identifier to be null", obj.getId());
        try {
            bookRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        bookRepository.flush();
        Assert.assertNotNull("Expected 'BookDao' identifier to no longer be null", obj.getId());
    }
    
}
