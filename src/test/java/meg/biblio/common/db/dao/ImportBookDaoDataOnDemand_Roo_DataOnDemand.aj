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
import meg.biblio.common.db.ImportBookRepository;
import meg.biblio.common.db.dao.ImportBookDao;
import meg.biblio.common.db.dao.ImportBookDaoDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect ImportBookDaoDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ImportBookDaoDataOnDemand: @Component;
    
    private Random ImportBookDaoDataOnDemand.rnd = new SecureRandom();
    
    private List<ImportBookDao> ImportBookDaoDataOnDemand.data;
    
    @Autowired
    ImportBookRepository ImportBookDaoDataOnDemand.importBookRepository;
    
    public ImportBookDao ImportBookDaoDataOnDemand.getNewTransientImportBookDao(int index) {
        ImportBookDao obj = new ImportBookDao();
        setAuthor(obj, index);
        setBarcode(obj, index);
        setClientbookid(obj, index);
        setError(obj, index);
        setIllustrator(obj, index);
        setIsbn10(obj, index);
        setIsbn13(obj, index);
        setPublisher(obj, index);
        setTitle(obj, index);
        return obj;
    }
    
    public void ImportBookDaoDataOnDemand.setAuthor(ImportBookDao obj, int index) {
        String author = "author_" + index;
        obj.setAuthor(author);
    }
    
    public void ImportBookDaoDataOnDemand.setBarcode(ImportBookDao obj, int index) {
        String barcode = "barcode_" + index;
        obj.setBarcode(barcode);
    }
    
    public void ImportBookDaoDataOnDemand.setClientbookid(ImportBookDao obj, int index) {
        String clientbookid = "clientbookid_" + index;
        obj.setClientbookid(clientbookid);
    }
    
    public void ImportBookDaoDataOnDemand.setError(ImportBookDao obj, int index) {
        String error = "error_" + index;
        obj.setError(error);
    }
    
    public void ImportBookDaoDataOnDemand.setIllustrator(ImportBookDao obj, int index) {
        String illustrator = "illustrator_" + index;
        obj.setIllustrator(illustrator);
    }
    
    public void ImportBookDaoDataOnDemand.setIsbn10(ImportBookDao obj, int index) {
        String isbn10 = "isbn10_" + index;
        obj.setIsbn10(isbn10);
    }
    
    public void ImportBookDaoDataOnDemand.setIsbn13(ImportBookDao obj, int index) {
        String isbn13 = "isbn13_" + index;
        obj.setIsbn13(isbn13);
    }
    
    public void ImportBookDaoDataOnDemand.setPublisher(ImportBookDao obj, int index) {
        String publisher = "publisher_" + index;
        obj.setPublisher(publisher);
    }
    
    public void ImportBookDaoDataOnDemand.setTitle(ImportBookDao obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }
    
    public ImportBookDao ImportBookDaoDataOnDemand.getSpecificImportBookDao(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ImportBookDao obj = data.get(index);
        Long id = obj.getId();
        return importBookRepository.findOne(id);
    }
    
    public ImportBookDao ImportBookDaoDataOnDemand.getRandomImportBookDao() {
        init();
        ImportBookDao obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return importBookRepository.findOne(id);
    }
    
    public boolean ImportBookDaoDataOnDemand.modifyImportBookDao(ImportBookDao obj) {
        return false;
    }
    
    public void ImportBookDaoDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = importBookRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ImportBookDao' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ImportBookDao>();
        for (int i = 0; i < 10; i++) {
            ImportBookDao obj = getNewTransientImportBookDao(i);
            try {
                importBookRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            importBookRepository.flush();
            data.add(obj);
        }
    }
    
}
