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
public class GoogleDetailFinderTest {

	@Autowired
	CatalogService catalogService;

	
	@Autowired
	ClientService clientService;	
	
	@Autowired
	SearchService searchService;

	@Autowired
	BookMemberService bMemberService;
	
	@Autowired
	GoogleDetailFinder googleSearch;

	@Before
	public void setup() {
	}


	@Test
	public void testSearchLogic() throws Exception {
		Long clientkey = clientService.getTestClientId();
		ClientDao client = clientService.getClientForKey(clientkey);
		BookDao book = new BookDao();
		book.getBookdetail().setTitle("coco tout nu");
		ArtistDao author = bMemberService.textToArtistName("Monfreid");
		List<ArtistDao> authors = new ArrayList<ArtistDao>();
		authors.add(author);
		book.getBookdetail().setAuthors(authors);

		FinderObject findobj = new FinderObject(book.getBookdetail(),client);
		

		// service call
		findobj = googleSearch.findDetails(findobj,210);
		BookDetailDao bookdetail = findobj.getBookdetail();
		
		// check call
		Assert.assertNotNull(findobj);
		Assert.assertFalse(findobj.getSearchStatus() == CatalogService.DetailStatus.NODETAIL);
		Assert.assertEquals(0L, findobj.getCurrentFinderLog()%2L);
	}
	
	@Test
	public void testAssignDetails() throws Exception {
		Long clientkey = clientService.getTestClientId();
		ClientDao client = clientService.getClientForKey(clientkey);
		// first get some multi details
		BookDao book = new BookDao();
		book.getBookdetail().setTitle("coco");
		ArtistDao author = bMemberService.textToArtistName("Monfreid");
		List<ArtistDao> authors = new ArrayList<ArtistDao>();
		authors.add(author);
		book.getBookdetail().setAuthors(authors);
		FinderObject findobj = new FinderObject(book.getBookdetail(),client);
		// service call - to get multidetails
		findobj = googleSearch.findDetails(findobj, 210);
		// get the first of the multidetails
		List<FoundDetailsDao> detailslist = findobj.getMultiresults();
		if (detailslist != null && detailslist.size() > 0) {
			FoundDetailsDao fd = detailslist.get(0);
			String titlecompare = fd.getTitle();
			// service call
			findobj = googleSearch.assignDetailToBook(findobj, fd);
			BookDetailDao bdetail = findobj.getBookdetail();
			// ensure - finder is logged in findobj, detailstatus in
			// findobj is found, titles match
			Long finderlog = findobj.getCurrentFinderLog();
			Assert.assertTrue(finderlog % 2 == 0);
			Assert.assertEquals(titlecompare, bdetail.getTitle());
			Assert.assertEquals(findobj.getSearchStatus().longValue(),
					CatalogService.DetailStatus.DETAILFOUND);
		} else {
			// test fail - no multidetails found
			Assert.assertEquals(1L, 2L);
		}

	}

	@Test
	public void testISBNNotFoundWrite() throws Exception {
		Long clientkey = clientService.getTestClientId();
		ClientDao client = clientService.getClientForKey(clientkey);
		BookDao book = new BookDao();
		book.getBookdetail().setIsbn13("1111111111111");
		book.getBookdetail().setTitle("coco tout nu");
		ArtistDao author = bMemberService.textToArtistName("Monfreid");
		List<ArtistDao> authors = new ArrayList<ArtistDao>();
		authors.add(author);
		book.getBookdetail().setAuthors(authors);

		FinderObject findobj = new FinderObject(book.getBookdetail(),client);

		// service call
		findobj = googleSearch.searchLogic(findobj);
		Assert.assertTrue(findobj.getSearchStatus() == CatalogService.DetailStatus.DETAILNOTFOUNDWISBN);
	}

	@Test
	public void testISBNNotFoundRead() throws Exception {
		Long clientkey = clientService.getTestClientId();
		ClientDao client = clientService.getClientForKey(clientkey);
		BookDao book = new BookDao();
		book.getBookdetail().setIsbn13("1111111111111");
		book.getBookdetail().setTitle("Superlapin");
		ArtistDao author = bMemberService.textToArtistName("Stephanie Blake");
		List<ArtistDao> authors = new ArrayList<ArtistDao>();
		authors.add(author);
		book.getBookdetail().setAuthors(authors);
		book.getBookdetail().setDetailstatus(CatalogService.DetailStatus.DETAILNOTFOUNDWISBN);

		FinderObject findobj = new FinderObject(book.getBookdetail(),client);

		// service call
		findobj = googleSearch.searchLogic(findobj);
		Assert.assertFalse(findobj.getSearchStatus() == CatalogService.DetailStatus.DETAILNOTFOUNDWISBN);
	}	
}