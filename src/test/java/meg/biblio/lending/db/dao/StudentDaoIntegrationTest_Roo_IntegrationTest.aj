// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package meg.biblio.lending.db.dao;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import meg.biblio.lending.db.StudentRepository;
import meg.biblio.lending.db.dao.StudentDaoDataOnDemand;
import meg.biblio.lending.db.dao.StudentDaoIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect StudentDaoIntegrationTest_Roo_IntegrationTest {
    
    declare @type: StudentDaoIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: StudentDaoIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: StudentDaoIntegrationTest: @Transactional;
    
    @Autowired
    StudentDaoDataOnDemand StudentDaoIntegrationTest.dod;
    
    @Autowired
    StudentRepository StudentDaoIntegrationTest.studentRepository;
    
    @Test
    public void StudentDaoIntegrationTest.testCount() {
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", dod.getRandomStudentDao());
        long count = studentRepository.count();
        Assert.assertTrue("Counter for 'StudentDao' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void StudentDaoIntegrationTest.testFind() {
        StudentDao obj = dod.getRandomStudentDao();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to provide an identifier", id);
        obj = studentRepository.findOne(id);
        Assert.assertNotNull("Find method for 'StudentDao' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'StudentDao' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void StudentDaoIntegrationTest.testFindAll() {
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", dod.getRandomStudentDao());
        long count = studentRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'StudentDao', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<StudentDao> result = studentRepository.findAll();
        Assert.assertNotNull("Find all method for 'StudentDao' illegally returned null", result);
        Assert.assertTrue("Find all method for 'StudentDao' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void StudentDaoIntegrationTest.testFindEntries() {
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", dod.getRandomStudentDao());
        long count = studentRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<StudentDao> result = studentRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'StudentDao' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'StudentDao' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void StudentDaoIntegrationTest.testFlush() {
        StudentDao obj = dod.getRandomStudentDao();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to provide an identifier", id);
        obj = studentRepository.findOne(id);
        Assert.assertNotNull("Find method for 'StudentDao' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyStudentDao(obj);
        Integer currentVersion = obj.getVersion();
        studentRepository.flush();
        Assert.assertTrue("Version for 'StudentDao' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void StudentDaoIntegrationTest.testSaveUpdate() {
        StudentDao obj = dod.getRandomStudentDao();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to provide an identifier", id);
        obj = studentRepository.findOne(id);
        boolean modified =  dod.modifyStudentDao(obj);
        Integer currentVersion = obj.getVersion();
        StudentDao merged = (StudentDao)studentRepository.save(obj);
        studentRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'StudentDao' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void StudentDaoIntegrationTest.testSave() {
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", dod.getRandomStudentDao());
        StudentDao obj = dod.getNewTransientStudentDao(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'StudentDao' identifier to be null", obj.getId());
        try {
            studentRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        studentRepository.flush();
        Assert.assertNotNull("Expected 'StudentDao' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void StudentDaoIntegrationTest.testDelete() {
        StudentDao obj = dod.getRandomStudentDao();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StudentDao' failed to provide an identifier", id);
        obj = studentRepository.findOne(id);
        studentRepository.delete(obj);
        studentRepository.flush();
        Assert.assertNull("Failed to remove 'StudentDao' with identifier '" + id + "'", studentRepository.findOne(id));
    }
    
}
