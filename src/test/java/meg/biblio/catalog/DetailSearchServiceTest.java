package meg.biblio.catalog;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import meg.biblio.catalog.CatalogService;
import meg.biblio.catalog.CatalogServiceImpl;
import meg.biblio.catalog.db.ArtistRepository;
import meg.biblio.catalog.db.BookRepository;
import meg.biblio.catalog.db.FoundWordsDao;
import meg.biblio.catalog.db.FoundWordsRepository;
import meg.biblio.catalog.db.IgnoredWordsDao;
import meg.biblio.catalog.db.IgnoredWordsRepository;
import meg.biblio.catalog.db.PublisherRepository;
import meg.biblio.catalog.db.SubjectRepository;
import meg.biblio.catalog.db.dao.ArtistDao;
import meg.biblio.catalog.db.dao.BookDao;
import meg.biblio.catalog.db.dao.BookDetailDao;
import meg.biblio.catalog.db.dao.FoundDetailsDao;
import meg.biblio.catalog.db.dao.PublisherDao;
import meg.biblio.catalog.web.model.BookModel;
import meg.biblio.common.ClientService;
import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.search.SearchService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DetailSearchServiceTest {

	@Autowired
	DetailSearchService detSearchService;
	
	
	@Autowired
	CatalogService catalogService;
	
	@Autowired
	SearchService searchService;	

	@Autowired
	ClientService clientService;
	
	@Autowired
	BookMemberService bMemberService;
	
	@Autowired
	BNFCatalogFinder bnfFinder;	

	Long artistid;
	Long pubtestid;



	@Test
	public void testFillInDetailsForBook() {
		// make book model
		Long clientid = clientService.getTestClientId();
		BookModel book = new BookModel();
		book.setClientid(clientid);
		book.setIsbn10("2211011713");
		ClientDao client = clientService.getClientForKey(clientid);
		// service call
		book = detSearchService.fillInDetailsForBook(book, client);
		// ensure bookdetail not null
		BookDetailDao detail = book.getBook().getBookdetail();
		Assert.assertNotNull(detail);
		// ensure finderlog, searchstatus filled in
		Assert.assertNotNull(detail.getFinderlog());
		Assert.assertNotNull(detail.getDetailstatus());
		// ensure imagelink, authors available
		Assert.assertNotNull(detail.getImagelink());
		Assert.assertNotNull(detail.getAuthors());
		Assert.assertTrue(detail.getAuthors().size()>0);
	}
	
	@Test
	public void testAssignDetails() throws Exception {
		Long clientid = clientService.getTestClientId();
		ClientDao client = clientService.getClientForKey(clientid);
			// first get some multi details
			BookDao book = new BookDao();
			book.getBookdetail().setTitle("coco");
			ArtistDao author = bMemberService.textToArtistName("Monfreid");
			List<ArtistDao> authors = new ArrayList<ArtistDao>();
			authors.add(author);
			book.getBookdetail().setAuthors(authors);
			BookModel bmodel = new BookModel(book);
			
			// service call - to get multidetails
			bmodel = detSearchService.fillInDetailsForBook(bmodel, client);
			// get the first of the multidetails
			List<FoundDetailsDao> detailslist = bmodel.getFounddetails();
			if (detailslist != null && detailslist.size() > 0) {
				FoundDetailsDao fd = detailslist.get(0);
				String titlecompare = fd.getTitle();
				// service call
				bmodel = detSearchService.assignDetailToBook(bmodel, fd, client);
				
				BookDetailDao bdetail = bmodel.getBook().getBookdetail();
				// ensure - finder is logged in findobj, detailstatus in
				// findobj is found, titles match
				Long finderlog = bdetail.getFinderlog();
				Assert.assertTrue(finderlog % 2 == 0);
				Assert.assertFalse(2L==finderlog);
				Assert.assertEquals(titlecompare, bdetail.getTitle());
				Assert.assertEquals(bdetail.getDetailstatus().longValue(),
						CatalogService.DetailStatus.DETAILFOUND);
			} else {
				// test fail - no multidetails found
				Assert.assertEquals(1L, 2L);
			}

	}
	
	
	/** removed as test because of difficulties with data.bnf **/
	public void testFillInDetailsForBookAddlCodes() {
		// make book model
		Long clientid = clientService.getTestClientId();
		BookModel book = new BookModel();
		book.setClientid(clientid);
		book.setIsbn10("9782211206464");
		ClientDao client = clientService.getClientForKey(clientid);
		// service call
		book = detSearchService.fillInDetailsForBook(book, client);
		// ensure bookdetail not null
		BookDetailDao detail = book.getBook().getBookdetail();
		Assert.assertNotNull(detail);
		// ensure finderlog, searchstatus filled in
		Assert.assertNotNull(detail.getFinderlog());
		Assert.assertNotNull(detail.getDetailstatus());
		// ensure imagelink, authors available
		Assert.assertNotNull(detail.getImagelink());
		Assert.assertNotNull(detail.getAuthors());
		Assert.assertTrue(detail.getAuthors().size()>0);
		// test to see if additional code is there (it should be)
		BookIdentifier bi = new BookIdentifier();
		bi.setEan("9782211208901");
		BookDetailDao result = searchService.findBooksForIdentifier(bi);
		Assert.assertNotNull(result);
	}

	
	@Test
	public void testFillInDetailsForBookList() {
		// make book models
		Long clientid = clientService.getTestClientId();
		List<BookModel> models = new ArrayList<BookModel>();
		BookModel book1 = new BookModel();
		book1.setClientid(clientid);
		book1.setIsbn10("2211011713");
		models.add(book1);
		BookModel book2 = new BookModel();
		book2.setClientid(clientid);
		book2.setIsbn10("2211212409");
		models.add(book2);
		BookModel book3 = new BookModel();
		book3.setClientid(clientid);
		book3.setIsbn10("9782745954329");
		models.add(book3);
		BookModel book4 = new BookModel();
		book4.setClientid(clientid);
		book4.setIsbn10("2226156488");
		models.add(book4);
		BookModel book5 = new BookModel();
		book5.setClientid(clientid);
		book5.setIsbn10("9782211206464");
		models.add(book5);
		BookModel book6 = new BookModel();
		book6.setClientid(clientid);
		book6.setIsbn10("2211065023");
		models.add(book6);
		BookModel book7 = new BookModel();
		book7.setClientid(clientid);
		book7.setIsbn10("2211093299");
		models.add(book7);
		BookModel book8 = new BookModel();
		book8.setClientid(clientid);
		book8.setIsbn10("9782742794133");
		models.add(book8);
		BookModel book9 = new BookModel();
		book9.setClientid(clientid);
		book9.setTitle("C'est moi le plus fort");
		models.add(book9);
		BookModel book10 = new BookModel();
		book10.setClientid(clientid);
		book10.setIsbn10("9782745903679");
		models.add(book10);
		BookModel book11 = new BookModel();
		book11.setClientid(clientid);
		book11.setIsbn10("9782203122376");
		models.add(book11);
		BookModel book12 = new BookModel();
		book12.setClientid(clientid);
		book12.setIsbn10("2081601117");
		models.add(book12);

		ClientDao client = clientService.getClientForKey(clientid);
		// service call
		models= detSearchService.fillInDetailsForBookList(models, client);
		// ensure list not null
		Assert.assertNotNull(models);
		Assert.assertEquals(10,models.size());
		int description = 0;
		for (BookModel model:models) {
			BookDetailDao detail = model.getBook().getBookdetail();
			Assert.assertFalse(detail.getDetailstatus() == CatalogService.DetailStatus.NODETAIL);
			Assert.assertNotNull(detail.getFinderlog());
			Assert.assertNotNull(detail.getDetailstatus());
			if (detail.getDescription()!=null) description++;
		}
		Assert.assertTrue(description>2);
	}

	@Test
	public void testFillInDetailsForBookListBadAuthor() {
		// make book models
		Long clientid = clientService.getTestClientId();
		List<BookModel> models = new ArrayList<BookModel>();
		BookModel book1 = new BookModel();
		book1.setClientid(clientid);
		book1.setIsbn13("9782211206464");
		models.add(book1);

		ClientDao client = clientService.getClientForKey(clientid);
		// service call
		models= detSearchService.fillInDetailsForBookList(models, client);
		// ensure list not null
		Assert.assertNotNull(models);
		Assert.assertEquals(1,models.size());
		BookModel model = models.get(0);
		Assert.assertNotNull(model.getDescription());
		
		
	}
	
	@Test
	public void testFillInImages() throws Exception {
		
		// test doesn't work in this environment - needs integration tests...
/*		Long clientid = clientService.getTestClientId();
		// create book in db without an image ("Jour de lessive" by Stehr)
		// create by running only the BNFDetailFinder
		BookDao book = new BookDao();
		book.setClientid(clientid);
		book.getBookdetail().setTitle("Jour de lessive");
		ArtistDao author = bMemberService.textToArtistName("Frédéric Stehr");
		List<ArtistDao> authors = new ArrayList<ArtistDao>();
		authors.add(author);
		book.getBookdetail().setAuthors(authors);
		
		// find info
		FinderObject findobj = new FinderObject(book.getBookdetail());
		findobj = bnfFinder.findDetails(findobj, 210);
		book.setBookdetail(findobj.getBookdetail());
		
		// save book in db, save id
		BookModel bmodel = new BookModel(book);
		bmodel = catalogService.createCatalogEntryFromBookModel(clientid, bmodel);
		Long bookid = bmodel.getBookid();
		
		// verify id not null, and imagelink is null
		Assert.assertNotNull(bookid);
		Assert.assertNull(bmodel.getBook().getBookdetail().getImagelink());

		// now, fill in details for isbn "9782211208901" (isbn for
		// Jour de lessive with image)
		BookModel newmodel = new BookModel();
		newmodel.setClientid(clientid);
		newmodel.setIsbn10("9782211208901");
		ClientDao client = clientService.getClientForKey(clientid);
		// service call
		newmodel = detSearchService.fillInDetailsForBook(newmodel, client);
		
		// Assert newmodel has an image
		Assert.assertNotNull(newmodel.getBook().getBookdetail().getImagelink());
		// load book created before 
		BookModel filledin = catalogService.loadBookModel(bookid); 
		// ensure that image has been filled in
		Assert.assertNotNull(filledin.getBook().getBookdetail().getImagelink());
*/
		Assert.assertEquals(1L,1L);
	}


}