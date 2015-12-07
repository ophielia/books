package meg.biblio.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import meg.biblio.common.db.ClientRepository;
import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.common.report.Barcode;
import meg.biblio.common.report.BarcodeSheet;
import meg.biblio.lending.ClassManagementService;
import meg.biblio.lending.db.PersonRepository;
import meg.biblio.lending.db.StudentRepository;
import meg.biblio.lending.db.TeacherRepository;
import meg.biblio.lending.db.dao.PersonDao;
import meg.biblio.lending.db.dao.SchoolGroupDao;
import meg.biblio.lending.db.dao.StudentDao;
import meg.biblio.lending.db.dao.TeacherDao;
import meg.biblio.lending.web.model.ClassModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class BarcodeServiceImpl implements BarcodeService {

	// A10000001000
	private static final int barcodelength = 12;
	private static final String filler = "000000000000";

	@Autowired
	ClientService clientService;

	@Autowired
	PersonRepository personRepo;
	
	@Autowired
	MessageSource appMessageSource;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	TeacherRepository teacherRepo;

	@Autowired
	ClientRepository clientRepo;

	@Autowired
	ClassManagementService classService;

	@Override
	public BarcodeSheet assembleBarcodeSheetForBooks(int barcodecnt,int startcode,
			Long clientid, Locale locale) {
		
		// get client
		ClientDao client = clientService.getClientForKey(clientid);
		
		// get barcode type (random or linked)
		Boolean linked =client.getIdForBarcode();

		// message
		String message = client.getName();

		// get codelist (length barcodecnt)
		List<Barcode> codes = generateCodes(barcodecnt,startcode,
				BarcodeService.CodeType.BOOK, client, message);
		// place codes in BookBarcodeSheet, with client
		String title="";
		try {
			String base = appMessageSource.getMessage("reports_barcode_booktitle",
					null, locale);
			title = base + client.getName();
		} catch (Exception e) {
			title = "barcodes";
		}
		BarcodeSheet sheet = new BarcodeSheet(codes, title);
		// return BookBarcodeSheet
		return sheet;
	}

	




	@Override
	public BarcodeSheet assembleBarcodeSheetForClass(Long classId,
			Long clientid, Locale locale) {
		// get client
		ClientDao client = clientService.getClientForKey(clientid);

		// assign codes to any students without codes
		SchoolGroupDao schoolgroup = classService.getClassForClient(classId,
				clientid);
		List<StudentDao> nocodestudents = studentRepo
				.findActiveStudentsForClassWithoutBarcode(schoolgroup, client,
						null);
		// ensure that teacher has a code
		List<TeacherDao> nocodeteachers = teacherRepo
				.findActiveTeachersForClientAndClassWithoutBarcode(client,
						schoolgroup);
		
		List<PersonDao> toassign = new ArrayList<PersonDao>();
		toassign.addAll(nocodeteachers);
		toassign.addAll(nocodestudents);
		if (toassign!=null && toassign.size()>0) {
			List<PersonDao> toupdate = new ArrayList<PersonDao>();
			List<Barcode> newcodes = generateCodes(toassign.size(),0,
					BarcodeService.CodeType.PERSON, client, "");
			int i = 0;
			for (PersonDao person : toassign) {
				PersonDao toupd = personRepo.findOne(person.getId());
				Barcode bc = newcodes.get(i);
				toupd.setBarcodeid(bc.getCode());
				toupdate.add(toupd);
				i++;
			}
			personRepo.save(toupdate);
		}

		// load classmodel
		ClassModel model = classService.loadClassModelById(schoolgroup.getId());
		TeacherDao teacher = model.getTeacher();
		List<StudentDao> students = model.getStudents();

		// get message for title
		String teachername = model.getTeacher().getFulldisplayname();
		String base = appMessageSource.getMessage("reports_barcode_classtitle",
				null, locale);

		String title = base + teachername;

		// make list for codes
		List<Barcode> classcodes = new ArrayList<Barcode>();

		// add teacher to list - filling in message
		Barcode bc = new Barcode(teacher.getBarcodeid(),
				teacher.getFulldisplayname());
		classcodes.add(bc);

		// add all students to list - filling in message
		for (StudentDao student : students) {
			bc = new Barcode(student.getBarcodeid(),
					student.getFulldisplayname());
			classcodes.add(bc);
		}
		// construct BarcodeSheet
		BarcodeSheet bcs = new BarcodeSheet(classcodes, title);
		// return BarcodeSheet
		return bcs;

	}

	private List<Barcode> generateCodes(int length, int startcode,String codetype,
			ClientDao client, String message) {
		// make resultlist
		List<Barcode> results = new ArrayList<Barcode>();

		// make prefix
		Long clientid = client.getClientnr();
		String prefix = codetype + clientid;
		Boolean linkedtoclientid = client.getIdForBarcode()!=null?client.getIdForBarcode():false;
		
		// set start code (begin) and endcode (after)
		int begin=0;
		int after = 0;
		// determine if codes should be linked to clientbookid, or generated randomly
		if (codetype.equals(BarcodeService.CodeType.BOOK)&& linkedtoclientid) {
			begin = startcode;
			after = startcode + length + 1;
		} else {
			// generate new codegendao (as many as length)
			Long lastused = client.getLastBcBase();
			begin = lastused.intValue() + 1;
			after = lastused.intValue() + length + 1;
			client = clientRepo.findOne(client.getId());
			client.setLastBcBase(new Long(after));
			clientRepo.save(client);
		}

		for (int i = begin; i < after; i++) {
			String base = String.valueOf(i);
			int fillerlength = barcodelength - base.length();
			String codefiller = filler.substring(0, fillerlength);
			String barcode = prefix + codefiller + base;
			Barcode bc = new Barcode(barcode, message);
			results.add(bc);
		}

		// return resultlist
		return results;
	}

	@Override
	public String getBookBarcodeForClientid(ClientDao client,
			String clientbookid) {
		String codetype = BarcodeService.CodeType.BOOK;
		// make prefix
		Long clientid = client.getClientnr();
		String prefix = codetype + clientid;

		// generate new barcode
		int fillerlength = barcodelength - clientbookid.length();
		String codefiller = filler.substring(0, fillerlength);
		String barcode = prefix + codefiller + clientbookid;

		// return new barcode
		return barcode;
	}

}