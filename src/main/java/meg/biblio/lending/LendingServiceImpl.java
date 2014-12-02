package meg.biblio.lending;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import meg.biblio.common.ClientService;
import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.lending.db.SchoolGroupRepository;
import meg.biblio.lending.db.TeacherRepository;
import meg.biblio.lending.db.dao.SchoolGroupDao;
import meg.biblio.lending.db.dao.TeacherDao;
import meg.biblio.lending.web.model.ClassModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LendingServiceImpl implements LendingService {

	@Autowired
	ClientService clientService;
	
	@Autowired
	SchoolGroupRepository sgroupRepo;
	
	@Autowired
	TeacherRepository teacherRepo;	
	
	/**
	 * This method creates a class from a (rather empty) ClassModel. It takes
	 * the teacher entry, creates a TeacherDao object, and places this in a new
	 * SchoolGroupDao object. Then, the schoolyear information is added, and the
	 * SchoolGroupDao object is persisted, reloaded, and returned.
	 */
	@Override
	public ClassModel createClassFromClassModel(ClassModel model, Long clientkey) {
		// get client
		ClientDao client = clientService.getClientForKey(clientkey);
		// get class and teacher
		SchoolGroupDao sgroup = model.getSchoolGroup();
		TeacherDao teacher = sgroup.getTeacher();
		
		// set client in teacher and class
		sgroup.setClient(client);
		teacher.setClient(client);
		
		// save teacher, and reset in class
		teacher = teacherRepo.save(teacher);
		sgroup.setTeacher(teacher);
		
		// configure school year dates
		Integer schoolbegin = getSchoolYearBeginForDate(new Date());
		sgroup.setSchoolyearbegin(schoolbegin);
		sgroup.setSchoolyearend(schoolbegin+1);
		
		// persist
		sgroup = sgroupRepo.save(sgroup);
		
		// reload model
		ClassModel newclass = loadClassModelById(sgroup.getId());
		// return reloaded model
		return newclass;
	}

	@Override
	public ClassModel loadClassModelById(Long id) {
		// get schoolgroup
		SchoolGroupDao schoolgroup = sgroupRepo.findOne(id);
		ClassModel loadclass = new ClassModel(schoolgroup);
		// set in model
		return loadclass;
	}

	/**
	 * Returns the beginning of the schoolyear according to date - for example,
	 * if called in september 1989, the school year will be 1989/1990. If called
	 * in march 2000, it will also be be 1999/2000. But in august 2000, it will
	 * be 2000/2001. Any date before July will be classified as the current
	 * school year. Anything after July will be classified as the next school
	 * year.
	 */
	@Override
	public Integer getSchoolYearBeginForDate(Date currentdate) {
		// make calendar
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentdate);
		// get month, year
		int currentmonth = cal.get(Calendar.MONTH);
		int currentyear = cal.get(Calendar.YEAR);
		// if month between January and June, the begin year is the previous
		// year
		if (currentmonth < Calendar.JULY) {
			return new Integer(currentyear - 1);
		} else {
			// if month after June, the begin year is the current
			return new Integer(currentyear);
		}

	}

	@Override
	public List<SchoolGroupDao> getClassesForClient(Long clientkey) {
		ClientDao client = clientService.getClientForKey(clientkey);
		
		return sgroupRepo.findSchoolGroupByClient(client);
	}

}
