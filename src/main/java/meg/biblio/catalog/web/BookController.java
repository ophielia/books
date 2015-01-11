package meg.biblio.catalog.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import meg.biblio.catalog.CatalogService;
import meg.biblio.catalog.db.dao.ArtistDao;
import meg.biblio.catalog.db.dao.BookDao;
import meg.biblio.catalog.db.dao.ClassificationDao;
import meg.biblio.catalog.db.dao.FoundDetailsDao;
import meg.biblio.catalog.web.NewBookController.EditMode;
import meg.biblio.catalog.web.model.BookModel;
import meg.biblio.catalog.web.validator.BookModelValidator;
import meg.biblio.common.AppSettingService;
import meg.biblio.common.ClientService;
import meg.biblio.common.SelectKeyService;
import meg.biblio.common.db.dao.ClientDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import flexjson.JSONSerializer;

@RequestMapping("/books")
@Controller
public class BookController {

	@Autowired
	CatalogService catalogService;

	@Autowired
	SelectKeyService keyService;

	@Autowired
	ClientService clientService;

	@Autowired
	BookModelValidator bookValidator;
	
    @Autowired
    AppSettingService settingService;	

	@RequestMapping(params = "form", method = RequestMethod.GET, produces = "text/html")
	public String showNewBookPage(BookModel bookModel,
			Model uiModel, HttpServletRequest httpServletRequest,
			Principal principal) {
		ClientDao client = clientService.getCurrentClient(principal);
		Locale locale = httpServletRequest.getLocale();

		// clear model, place in uiModel
		Long classification = bookModel.getShelfclass();
		bookModel = new BookModel();
		bookModel.setShelfclass(classification);
		bookModel.setCreatenewid(new Boolean(true));
		bookModel.setStatus(CatalogService.Status.SHELVED);

		uiModel.addAttribute("bookModel", bookModel);

		String shortname = client.getShortname();
		uiModel.addAttribute("clientname",shortname);
		


		// return choosebook page
		return "book/create";
	}	



	
	// assign code for new (to catalog) book
	@RequestMapping(value = "/editbook", params = "newbook", method = RequestMethod.POST, produces = "text/html")
	public String createNewBook(BookModel bookModel,
			Model uiModel, BindingResult bindingResult,HttpServletRequest httpServletRequest,
			Principal principal) {
		ClientDao client = clientService.getCurrentClient(principal);
		Long clientid = client.getId();

		// validation - check isbn - OR - title 
		// if not generate new - no existing book for book code
		bookValidator.validateNewBookEntry(bookModel, bindingResult,client);
		if (bindingResult.hasErrors()) {
			String shortname = client.getShortname();
			uiModel.addAttribute("clientname",shortname);
			if (bindingResult.hasFieldErrors("newbooknr")) {
				// this book is already found, so add clue that link should be 
				// shown to assign code to existingbook
				uiModel.addAttribute("showlinkexist",true);
			}
			// return to choosenewbook page
			return "book/create";
		}
		
		// gatherinfo -cBookid (or generateok), isbn, title, author
		String cbooknr = bookModel.getClientbookid();
		String isbn = bookModel.getIsbnentry();
		String title = bookModel.getTitle();
		String author = bookModel.getAuthorname();
		Boolean createclientbookid = bookModel.getCreatenewid();

		// create book in catalog
		BookModel model = new BookModel();
		if (cbooknr!=null) model.setClientbookid(cbooknr);
		if (isbn!=null) model.setIsbn10(isbn);
		if (title!=null) model.setTitle(title);
		ArtistDao artist = catalogService.textToArtistName(author);
		if (artist!=null) {
			model.setAuthorInBook(artist);
		}
		model = catalogService.createCatalogEntryFromBookModel(clientid, model,
				createclientbookid);
		// book-> BookModel -> uiModel
		BookDao book = model.getBook();
		bookModel.setBook(book);
		uiModel.addAttribute("bookModel", bookModel);

		// choose return view (isbnedit if no details, otherwise editbook)
		bookModel.setEditMode(EditMode.editbook);
		String returnview = "book/editbook";
		if (book.getDetailstatus().longValue() != CatalogService.DetailStatus.DETAILFOUND) {
			if (book.getTitle()!=null && book.getTitle().equals(CatalogService.titledefault)) {
				bookModel.setTitle("");
				bookModel.setEditMode(EditMode.title);
				uiModel.addAttribute("bookModel",bookModel);
				returnview = "book/titleedit";
			} else {
				bookModel.setEditMode(EditMode.isbn);
				returnview = "book/isbnedit";	
			}
		}

		// add lookups / displays for view
		String shortname = client.getShortname();
		uiModel.addAttribute("clientname",shortname);
		
		// check uses barcodes
		Boolean showbarcode = client.getUsesBarcodes()!=null && client.getUsesBarcodes();
		uiModel.addAttribute("showbarcodes",showbarcode);	
		// return view
		return returnview;

	}

	// persist any changes to book (new classification, addition of isbn)
	@RequestMapping(value = "/updatebook", method = RequestMethod.POST, produces = "text/html")
	public String updateBook(BookModel bookModel, Model uiModel,
			HttpServletRequest httpServletRequest, BindingResult bindingResult,Principal principal) {
		ClientDao client = clientService.getCurrentClient(principal);
		Long clientid = client.getId();

		// update book - gather classification, isbn
		Long classification=bookModel.getShelfclass();
		String isbn = bookModel.getIsbnentry();
		Long status = bookModel.getStatus();
		String title =bookModel.getTitle();
		String author = bookModel.getAuthorname();
		
		// put book into model
		BookModel model = catalogService.loadBookModel(bookModel.getBook().getId());
		if (classification!=null) model.setShelfclass(classification);
		if (status!=null) model.setStatus(status);
		if (isbn!=null) model.setIsbn10(isbn);
		if (title!=null) model.setTitle(title);
		ArtistDao artist = catalogService.textToArtistName(author);
		if (artist!=null) {
			model.setAuthorInBook(artist);
		}
		// now, put this model updated book into the (session) book model
		bookModel.setBook(model.getBook());
		
		bookValidator.validateUpdateBook(bookModel,bindingResult);
		if (bindingResult.hasErrors()) {
			String returnview = "book/editbook";
			if (bookModel.getEditMode().equals(EditMode.title)) {
					returnview = "book/titleedit";
				} else if (bookModel.getEditMode().equals(EditMode.title)) {
					returnview = "book/isbnedit";	
			}

			return returnview;
		}
		
		// update book - if changed
		// fill in details if not detailfound, and isbn exists
		boolean fillindetails = model.getBook().hasIsbn() && !(model.getDetailstatus().longValue()==CatalogService.DetailStatus.DETAILFOUND);
		try {
			model = catalogService.updateCatalogEntryFromBookModel(clientid, model, fillindetails);
			bookModel.setBook(model.getBook());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// return view - returns either to display book, or to assign code
		uiModel.addAttribute("bookModel",bookModel);
		Boolean showbarcode = client.getUsesBarcodes()!=null && client.getUsesBarcodes();
		uiModel.addAttribute("showbarcodes",showbarcode);
		if (showbarcode) {
			// return assign code page
			return "book/assigncode";
		} else {
			// redirect to display book page
			String redirect = "/books/display/" + bookModel.getBookid();
			return "redirect:" + redirect;
			
		}
	}

	// persist any changes to book (new classification, addition of isbn)
	@RequestMapping(value = "/assign", method = RequestMethod.POST, produces = "text/html")
	public String assignCodeToBook(BookModel bookModel,
			Model uiModel, BindingResult bindingResult,HttpServletRequest httpServletRequest,
			Principal principal) {
		ClientDao client = clientService.getCurrentClient(principal);
		Long clientid = client.getId();

		// gather info - code, bookid
		Long bookid = bookModel.getBook().getId();
		String code = bookModel.getAssignedcode();

		// validation - has this barcode already been assigned??
		bookValidator.validateAssignCodeToBook(code,bindingResult);
		if (bindingResult.hasErrors()) {
			bookModel.setAssignedcode(null);
			uiModel.addAttribute("bookModel",bookModel);
			return "book/isbnedit";
		}
		
		// service call - assignCodeToBook
		catalogService.assignCodeToBook(code, bookid);
		// load book model
		BookModel model = catalogService.loadBookModel(bookid);
		uiModel.addAttribute("bookModel",bookModel);
		// redirect to display book page
		String redirect = "/books/display/" + bookModel.getBookid();
		return "redirect:" + redirect;
	}
  
	@RequestMapping(value = "/display/{id}", method = RequestMethod.GET, produces = "text/html")
	public String showBook(@PathVariable("id") Long id, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {

		BookModel model = new BookModel();
		if (id != null) {
			model = catalogService.loadBookModel(id);
		}

		uiModel.addAttribute("bookModel", model);

		return "book/show";
	}

	@RequestMapping(value = "/editall/{id}", method = RequestMethod.POST, produces = "text/html")
	public String saveEditAll(
			@ModelAttribute("bookModel") BookModel bookModel,
			@PathVariable("id") Long id, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		ClientDao client = clientService.getCurrentClient(principal);
		Long clientkey = client.getId();

		// only making a few changes. load the model from the database, and copy
		// changes into database model (from passed model)
		if (id != null) {

			ArtistDao author = catalogService.textToArtistName(bookModel
					.getAuthorname());
			ArtistDao illustrator = catalogService.textToArtistName(bookModel
					.getIllustratorname());
			String publisher = bookModel.getPublishername();
			String isbn = bookModel.getIsbn10();

			BookModel model = catalogService.loadBookModel(id);
			if (isbn != null)
				model.setIsbn10(isbn);
			if (publisher != null)
				model.setPublisher(publisher);
			if (author != null)
				model.setAuthorInBook(author);
			if (illustrator != null)
				model.setIllustratorInBook(author);
			model.setType(bookModel.getType());
			model.setShelfclass(bookModel.getShelfclass());
			model.setStatus(bookModel.getStatus());
			model.setLanguage(bookModel.getLanguage());

			BookModel book;
			try {
				book = catalogService.updateCatalogEntryFromBookModel(
						clientkey, model, true);
				uiModel.addAttribute("bookModel", book);
			} catch (GeneralSecurityException | IOException e) {
				e.printStackTrace();
			}
		}
		return "book/show";
	}

	@RequestMapping(value = "/choosedetails/{id}", method = RequestMethod.GET, produces = "text/html")
	public String showBookDetails(@PathVariable("id") Long id, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {

		BookModel model = new BookModel();
		if (id != null) {
			// add found objects to model
			List<FoundDetailsDao> multidetails = catalogService
					.getFoundDetailsForBook(id);
			uiModel.addAttribute("foundDetails", multidetails);

			model = catalogService.loadBookModel(id);
		}

		uiModel.addAttribute("bookModel", model);

		return "book/choosedetails";
	}

	@RequestMapping(value = "/choosedetails", params = { "detailid", "bookid" }, method = RequestMethod.POST, produces = "text/html")
	public String assignBookDetails(@RequestParam("detailid") Long detailid,
			@RequestParam("bookid") Long bookid, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		// call catalog service
		try {
			catalogService.assignDetailToBook(detailid, bookid);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// show book
		BookModel model = catalogService.loadBookModel(bookid);
		uiModel.addAttribute("bookModel", model);

		return "book/show";
	}

	@RequestMapping(value = "/choosedetails", params = "isbn", method = RequestMethod.POST, produces = "text/html")
	public String assignBookISBN(BookModel bookModel, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		ClientDao client = clientService.getCurrentClient(principal);
		Long clientkey = client.getId();
		Long id = bookModel.getBookid();
		// add isbn to book model
		String isbn = bookModel.getIsbn10();

		if (isbn != null && isbn.length() > 0) {
			BookModel dbmodel = catalogService.loadBookModel(id);
			dbmodel.setIsbn10(isbn);
			// update book , search for details
			dbmodel = catalogService.addToFoundDetails(clientkey, dbmodel);
			uiModel.addAttribute("bookModel", dbmodel);

			List<FoundDetailsDao> multidetails = catalogService
					.getFoundDetailsForBook(id);
			uiModel.addAttribute("foundDetails", multidetails);

			uiModel.addAttribute("bookModel", dbmodel);
			return "book/choosedetails";
		}
		bookModel = catalogService.loadBookModel(id);
		List<FoundDetailsDao> multidetails = catalogService
				.getFoundDetailsForBook(id);
		uiModel.addAttribute("foundDetails", multidetails);

		uiModel.addAttribute("bookModel", bookModel);
		return "book/choosedetails";
	}

	@ModelAttribute("classHash")
	public HashMap<Long, ClassificationDao> getClassificationInfo(
			HttpServletRequest httpServletRequest, Principal principal) {
		Locale locale = httpServletRequest.getLocale();
		String lang = locale.getLanguage();
		ClientDao client = clientService.getCurrentClient(principal);
		Long clientkey = client.getId();

		HashMap<Long, ClassificationDao> shelfclasses = catalogService
				.getShelfClassHash(clientkey, lang);

		return shelfclasses;
	}
	
	

	@ModelAttribute("classJson")
	public String getClassificationInfoAsJson(
			HttpServletRequest httpServletRequest, Principal principal) {
		Locale locale = httpServletRequest.getLocale();
		String lang = locale.getLanguage();
		ClientDao client = clientService.getCurrentClient(principal);
		Long clientkey = client.getId();

		List<ClassificationDao> shelfclasses = catalogService
				.getShelfClassList(clientkey, lang);

		JSONSerializer serializer = new JSONSerializer();
		String json = serializer.exclude("*.class").serialize(shelfclasses);
		return json;
	}

	@ModelAttribute("typeLkup")
	public HashMap<Long, String> getBookTypeLkup(
			HttpServletRequest httpServletRequest) {
		Locale locale = httpServletRequest.getLocale();
		String lang = locale.getLanguage();

		HashMap<Long, String> booktypedisps = keyService.getDisplayHashForKey(
				CatalogService.booktypelkup, lang);
		return booktypedisps;
	}

	@ModelAttribute("statusLkup")
	public HashMap<Long, String> getStatusLkup(
			HttpServletRequest httpServletRequest) {
		Locale locale = httpServletRequest.getLocale();
		String lang = locale.getLanguage();

		HashMap<Long, String> booktypedisps = keyService.getDisplayHashForKey(
				CatalogService.bookstatuslkup, lang);
		return booktypedisps;
	}

	@ModelAttribute("langLkup")
	public HashMap<String, String> getLanguageLkup(
			HttpServletRequest httpServletRequest) {
		Locale locale = httpServletRequest.getLocale();
		String lang = locale.getLanguage();

		HashMap<String, String> langdisps = keyService
				.getStringDisplayHashForKey(CatalogService.languagelkup, lang);
		return langdisps;
	}

	@ModelAttribute("detailstatusLkup")
	public HashMap<Long, String> getDetailStatusLkup(
			HttpServletRequest httpServletRequest) {
		Locale locale = httpServletRequest.getLocale();
		String lang = locale.getLanguage();

		HashMap<Long, String> booktypedisps = keyService.getDisplayHashForKey(
				CatalogService.detailstatuslkup, lang);
		return booktypedisps;
	}
	
    
    @ModelAttribute("imagebasedir")
    public String getImageBaseSetting(HttpServletRequest httpServletRequest) {
    	String imagebase = settingService.getSettingAsString("biblio.imagebase");
    	return imagebase; 
    }  

}
