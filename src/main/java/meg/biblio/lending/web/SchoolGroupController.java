package meg.biblio.lending.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import meg.biblio.catalog.CatalogService;
import meg.biblio.catalog.db.dao.ClassificationDao;
import meg.biblio.common.ClientService;
import meg.biblio.common.SelectKeyService;
import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.lending.LendingService;
import meg.biblio.lending.db.dao.SchoolGroupDao;
import meg.biblio.lending.web.model.ClassModel;
import meg.biblio.lending.web.validator.ClassModelValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import flexjson.JSONSerializer;


@RequestMapping("/classes")
@Controller
public class SchoolGroupController {

	
	@Autowired
	LendingService lendingService;
	
	
	@Autowired
	SelectKeyService keyService;
	
	@Autowired
	ClientService clientService;	
	
	@Autowired
	ClassModelValidator classValidator;
	
	
	
    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
	public String showClassList(Model uiModel, HttpServletRequest request) {
    	Long clientkey = clientService.getCurrentClientKey(request);
		
		List<SchoolGroupDao> classes = lendingService.getClassesForClient(clientkey);
    	uiModel.addAttribute("listofclasses",classes);
		return "schoolgroups/list";
	}

	@RequestMapping(value="/create",params = "form",method = RequestMethod.GET, produces = "text/html")
    public String createClassForm(Model uiModel, HttpServletRequest httpServletRequest) {
    	// create empty book model
    	ClassModel model = new ClassModel();
    	// place in uiModel
    	uiModel.addAttribute("classModel",model);
    	// return book/create
    	return "schoolgroups/create";
    }
    
    @RequestMapping(value="/create",method = RequestMethod.POST, produces = "text/html")
    public String createClass(ClassModel model,  Model uiModel,BindingResult bindingResult, HttpServletRequest httpServletRequest) {
    	Long clientkey = clientService.getCurrentClientKey(httpServletRequest);
    	Locale locale = httpServletRequest.getLocale();
    	String lang = locale.getLanguage();
    	
    	model.fillInTeacherFromEntry();
    	
    	classValidator.validateTeacherEntry(model,bindingResult);
    	
    	if (bindingResult.hasErrors()) {
			uiModel.addAttribute("classModel", model);
			return "schoolgroups/create";
		}
    	
    	uiModel.asMap().clear();
    	
    	// create class for teacher - and load class model
    	ClassModel newclass = lendingService.createClassFromClassModel(model, clientkey);
    	uiModel.addAttribute("classModel", newclass);
    	uiModel.addAttribute("newlycreated",true);
    	
    	// return edit/display page
    	return "schoolgroups/edit";
    	
    }    
    
    @RequestMapping(value="/display/{id}", method = RequestMethod.GET, produces = "text/html")
    public String showEditClassForm(@PathVariable("id") Long id, Model uiModel, HttpServletRequest httpServletRequest) {
    	Locale locale = httpServletRequest.getLocale();
    	String lang = locale.getLanguage();

    	// load ClassModel
    	ClassModel sclass = lendingService.loadClassModelById(id);
    	
    	// put classmodel in model
    	uiModel.addAttribute("classModel",sclass);
    	
    	// return edit view
    	return "schoolgroups/edit";
    	}    
    
    @RequestMapping(value="/display/{id}", method = RequestMethod.POST, produces = "text/html")
	public String saveEditClass(@ModelAttribute("classModel") ClassModel bookModel,@PathVariable("id") Long id, Model uiModel, HttpServletRequest httpServletRequest) {
		Locale locale = httpServletRequest.getLocale();
		Long clientkey = clientService.getCurrentClientKey(httpServletRequest);
		String lang = locale.getLanguage();
	
		/*
		 * // only making a few changes. load the model from the database, and copy changes into database model (from passed model)
		if (id!=null) {
			BookModel model = catalogService.loadBookModel(id);	
			model.setType(bookModel.getType());
			model.setShelfclass(bookModel.getShelfclass());
			model.setStatus(bookModel.getStatus());
			model.setLanguage(bookModel.getLanguage());
			BookModel book = catalogService.updateCatalogEntryFromBookModel(clientkey,model);
			uiModel.addAttribute("bookModel", book);
		} else {
			uiModel.addAttribute("bookModel", bookModel);	
		}
	
	
	    return "book/show";
		 */
		return null;
	}

    @ModelAttribute("classJson")
    public String getClassificationInfoAsJson(HttpServletRequest httpServletRequest) {
    	/**
    	 * 
    	 * Locale locale = httpServletRequest.getLocale();
    	String lang = locale.getLanguage();
    	Long clientkey = clientService.getCurrentClientKey(httpServletRequest);
    	
    	List<ClassificationDao> shelfclasses =catalogService.getShelfClassList(clientkey,lang);
    	
    	JSONSerializer serializer = new JSONSerializer();
		String json = serializer.exclude("*.class").serialize(shelfclasses);
		return json;
    	 */
    	return null;
    }    
    
    @ModelAttribute("detailstatusLkup")
    public HashMap<Long,String> getDetailStatusLkup(HttpServletRequest httpServletRequest) {
    	Locale locale = httpServletRequest.getLocale();
    	String lang = locale.getLanguage();
    	
    	HashMap<Long, String> booktypedisps = keyService
    			.getDisplayHashForKey(CatalogService.detailstatuslkup, lang);
    	return booktypedisps; 
    }  
    
	@ModelAttribute("clientname") 
	public String getClientName(HttpServletRequest httpServletRequest) {
		ClientDao clientkey = clientService.getCurrentClient(httpServletRequest);
		return clientkey.getName();
	}
}
