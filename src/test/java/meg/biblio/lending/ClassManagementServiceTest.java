package meg.biblio.lending;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import meg.biblio.common.ClientService;
import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.lending.db.SchoolGroupRepository;
import meg.biblio.lending.db.StudentRepository;
import meg.biblio.lending.db.TeacherRepository;
import meg.biblio.lending.db.dao.SchoolGroupDao;
import meg.biblio.lending.db.dao.StudentDao;
import meg.biblio.lending.db.dao.TeacherDao;
import meg.biblio.lending.web.model.ClassModel;
import meg.biblio.lending.web.model.TeacherInfo;
import meg.tools.DateUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ClassManagementServiceTest {

	@Autowired
	ClientService clientService;

	@Autowired
	ClassManagementService classService;

	@Autowired
	SchoolGroupRepository sgroupRepo;

	@Autowired
	TeacherRepository teacherRepo;

	@Autowired
	StudentRepository studentRepo;

	Long artistid;
	Long pubtestid;
	Long testclassid;

	@Before
	public void inittest() {
		// make class
		// make new class model
		ClassModel classmod = new ClassModel();
		// set "Alba Rodrieguez" as teacherentry
		classmod.setTeachername("Mrs. Watson");
		// set "alba@free.fr" as email entry
		classmod.setTeacheremail("watson@free.fr");
		classmod.fillInTeacherFromEntry();

		// service call
		ClassModel result = classService
				.createClassFromClassModel(classmod, 1L);
		testclassid = result.getClassid();
	}

	@Test
	public void testCreateClassFromClassModel() {
		// make new class model
		ClassModel classmod = new ClassModel();
		// set "Alba Rodrieguez" as teacherentry
		classmod.setTeachername("Alba Rodrieguez");
		// set "alba@free.fr" as email entry
		classmod.setTeacheremail("alba@free.fr");
		classmod.fillInTeacherFromEntry();

		// service call
		ClassModel result = classService
				.createClassFromClassModel(classmod, 1L);
		// assert - teacher created with id and client, class created with id
		// and client
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getSchoolGroup());
		Assert.assertNotNull(result.getTeacher());
		SchoolGroupDao schoolgroup = result.getSchoolGroup();

		// also assert that school year is correct
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		if (month < Calendar.JULY) {
			year--;
		}
		Assert.assertEquals(new Integer(year), schoolgroup.getSchoolyearbegin());
		Assert.assertEquals(new Integer(year + 1),
				schoolgroup.getSchoolyearend());

	}

	@Test
	public void testNewAddStudentToClass() {
		// make dummy class
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("willy wonka");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);

		// service call
		classService.addNewStudentToClass("keli skalicky", 1L,
				model.getSchoolGroup(), 1L);

		// load classmodel
		ClassModel test = classService.loadClassModelById(model.getClassid());

		// ensure that: model not null, at least one student in class
		Assert.assertNotNull(test);
		Assert.assertNotNull(test.getStudents());
		Assert.assertTrue(test.getStudents().size() > 0);

		// find student
		for (StudentDao student : test.getStudents()) {
			if (student.getFirstname().equals("keli")
					&& student.getLastname().equals("keli")) {
				// ensure that student first name and last name are correct by
				// being here! :-)
				// ensure that client and section are set
				Assert.assertNotNull(student.getClient());
				Assert.assertNotNull(student.getSchoolgroup()); // may need to
																// change this
																// to db
																// grab....
			}
		}

	}

	@Test
	public void removeStudentsFromClass() {
		// make dummy class
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("willy wonka");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);
		Long classid = model.getClassid();
		classService.addNewStudentToClass("keli skalicky", 1L,
				model.getSchoolGroup(), 1L);
		classService.addNewStudentToClass("björn straube", 1L,
				model.getSchoolGroup(), 1L);
		classService.addNewStudentToClass("allison noinvite", 1L,
				model.getSchoolGroup(), 1L);

		// load test, and make list of ids to be removed(2)
		// first two will be removed
		ClassModel test = classService.loadClassModelById(classid);
		List<Long> toremove = new ArrayList<Long>();
		List<StudentDao> students = test.getStudents();
		toremove.add(students.get(0).getId());
		toremove.add(students.get(1).getId());

		// service call
		test = classService.removeStudentsFromClass(toremove,
				test.getSchoolGroup(), 1L);
		// reload class
		ClassModel results = classService.loadClassModelById(classid);
		// assertions - classmodel not null, students not null, students size 1
		Assert.assertNotNull(results);
		Assert.assertNotNull(results.getStudents());
		Assert.assertTrue(results.getStudents().size() == 1);
		// hashmap - ids to StudentDao
		HashMap<Long, StudentDao> map = new HashMap<Long, StudentDao>();
		for (StudentDao student : results.getStudents()) {
			map.put(student.getId(), student);
		}
		// assert toremove ids aren't in hashmap
		for (Long removeid : toremove) {
			Assert.assertTrue(!map.containsKey(removeid));
		}

	}



	@Test
	public void testEditStudent() {
		SchoolGroupDao sgroup = sgroupRepo.findOne(testclassid);
		// create student for class
		StudentDao student = classService.addNewStudentToClass("george booth",
				1L, sgroup, sgroup.getClient().getId());
		long studentid = student.getId();
		// service call
		classService.editStudent(sgroup.getClient().getId(), student.getId(),
				"charles", "schulz", 2L);

		// reload student
		StudentDao result = studentRepo.findOne(studentid);

		// Assert - not null, name charles, last name schulz, section 2
		Assert.assertNotNull(result);
		Assert.assertEquals("charles", result.getFirstname());
		Assert.assertEquals("schulz", result.getLastname());
		Assert.assertEquals(new Long(2), result.getSectionkey());
		Assert.assertNotNull(result.getClient());
		Assert.assertNotNull(result.getSchoolgroup());

	}

	@Test
	public void testAddExistingStudent() {
		// dummy class
		SchoolGroupDao dummyone = sgroupRepo.findOne(testclassid);
		Long dummyoneclient = dummyone.getClient().getId();
		// add students to class
		classService.addNewStudentToClass("keli skalicky", 1L, dummyone,
				dummyoneclient);
		classService.addNewStudentToClass("björn straube", dummyoneclient,
				dummyone, 1L);
		classService.addNewStudentToClass("allison noinvite", dummyoneclient,
				dummyone, 1L);
		ClassModel test = classService.loadClassModelById(dummyone.getId());

		// get ids
		List<StudentDao> students = test.getStudents();
		List<Long> studentids = new ArrayList<Long>();
		for (StudentDao student : students) {
			studentids.add(student.getId());
		}
		// remove students from class
		test = classService.removeStudentsFromClass(studentids, dummyone,
				dummyoneclient);

		// now, add these students to another dummy class
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("pete repeat");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);

		// service call
		ClassModel resultclass = classService.assignStudentsToClass(studentids,
				model.getSchoolGroup(), model.getSchoolGroup().getClient()
						.getId());

		// repo call to get students for another dummy class
		List<StudentDao> results = studentRepo.findActiveStudentsForClass(
				model.getSchoolGroup(), model.getSchoolGroup().getClient(),
				new Sort(Sort.Direction.ASC, "sectionkey"));

		// should be three students
		Assert.assertNotNull(results);
		Assert.assertTrue(results.size() == 3);

	}

	@Test
	public void testDeleteClass() {
		// make class - teacher "darth vader"
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("darth vader");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);

		// add students to class - "luke skywalker, obi wan, princess leia"
		classService.addNewStudentToClass("luke skywalker", 1L,
				model.getSchoolGroup(), 1L);
		classService.addNewStudentToClass("obi wan", 1L,
				model.getSchoolGroup(), 1L);
		classService.addNewStudentToClass("princess leia", 1L,
				model.getSchoolGroup(), 1L);
		classService.loadClassModelById(model.getClassid());
		ClassModel test = classService.loadClassModelById(model.getClassid());

		// save class
		Long classid = test.getClassid();
		List<StudentDao> studentids = test.getStudents();
		Long teacherid = test.getTeacher().getId();

		// service call
		classService.deleteClass(classid, 1L);

		// Assert - class not found, teacher not found, students in unassigned
		// list
		SchoolGroupDao resultgroup = sgroupRepo.findOne(classid);
		TeacherDao resultteacher = teacherRepo.findOne(teacherid);
		Assert.assertNull(resultgroup);
		Assert.assertFalse(resultteacher.getActive());

		for (StudentDao student : studentids) {
			StudentDao check = studentRepo.findOne(student.getId());
			Assert.assertNull(check.getSchoolgroup());
		}

	}

	@Test
	public void testGraduation() {
		// make dummy class, and add three students
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("darth vader");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);
		// testing - set sgroup beginyear to 1977
		SchoolGroupDao updateyear = sgroupRepo.findOne(model.getClassid());
		updateyear.setSchoolyearbegin(1977);
		updateyear.setSchoolyearend(1978);
		sgroupRepo.save(updateyear);
		model = classService.loadClassModelById(model.getClassid());

		// add students to class - "luke skywalker, obi wan, princess leia"
		StudentDao luke = classService.addNewStudentToClass("luke skywalker",
				1L, model.getSchoolGroup(), 1L);
		StudentDao obiwan = classService.addNewStudentToClass("obi wan", 1L,
				model.getSchoolGroup(), 1L);
		StudentDao leia = classService.addNewStudentToClass("princess leia",
				2L, model.getSchoolGroup(), 1L);
		StudentDao hansolo = classService.addNewStudentToClass("han solo", 3L,
				model.getSchoolGroup(), 1L);

		// just for testing, remove hansolo from class
		List<Long> removeids = new ArrayList<Long>();
		removeids.add(hansolo.getId());
		classService.removeStudentsFromClass(removeids, model.getSchoolGroup(),
				1L);

		ClassModel test = classService.loadClassModelById(model.getClassid());

		// service call
		classService.moveAllStudentsToNextSection(1L);

		// Assert students are in next section
		StudentDao result = studentRepo.findOne(luke.getId());
		Assert.assertEquals(new Long(2), result.getSectionkey());
		result = studentRepo.findOne(obiwan.getId());
		Assert.assertEquals(new Long(2), result.getSectionkey());
		result = studentRepo.findOne(leia.getId());
		Assert.assertEquals(new Long(3), result.getSectionkey());
		result = studentRepo.findOne(hansolo.getId());
		Assert.assertEquals(new Long(4), result.getSectionkey());

		// now - assert that class is updated
		model = classService.loadClassModelById(model.getClassid());
		Assert.assertNotEquals(new Integer(1977), model.getSchoolGroup()
				.getSchoolyearbegin());
	}

	@Test
	public void testClearClassLists() {
		// make dummy class, and add three students
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("big bird");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);

		// add students to class -
		StudentDao luke = classService.addNewStudentToClass("mr ernie", 1L,
				model.getSchoolGroup(), 1L);
		StudentDao obiwan = classService.addNewStudentToClass("mr bert", 1L,
				model.getSchoolGroup(), 1L);
		StudentDao leia = classService.addNewStudentToClass("the grouch", 2L,
				model.getSchoolGroup(), 1L);
		StudentDao hansolo = classService.addNewStudentToClass("the count", 3L,
				model.getSchoolGroup(), 1L);

		// gather info
		Long classid = model.getClassid();

		// service call
		classService.clearStudentListsForClient(1L);

		// Assert - all classes for client have no students
		List<SchoolGroupDao> classes = classService.getClassesForClient(1L);
		for (SchoolGroupDao sclass : classes) {
			int size = sclass.getStudents()==null?0:sclass.getStudents().size();
			Assert.assertEquals(0,size);
		}

		// Assert students all have no classes
		StudentDao result = studentRepo.findOne(luke.getId());
		Assert.assertNull(result.getSchoolgroup());
		result = studentRepo.findOne(obiwan.getId());
		Assert.assertNull(result.getSchoolgroup());
		result = studentRepo.findOne(leia.getId());
		Assert.assertNull(result.getSchoolgroup());
		result = studentRepo.findOne(hansolo.getId());
		Assert.assertNull(result.getSchoolgroup());
	}

	@Test
	public void testSetStudentsInactive() {
		// create dummy class, and three students
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("harry potter");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);

		// add students to class -
		StudentDao luke = classService.addNewStudentToClass("hermione granger",
				1L, model.getSchoolGroup(), 1L);
		StudentDao obiwan = classService.addNewStudentToClass("ron weasley",
				1L, model.getSchoolGroup(), 1L);
		StudentDao leia = classService.addNewStudentToClass("draco malfoy", 2L,
				model.getSchoolGroup(), 1L);
		StudentDao hansolo = classService.addNewStudentToClass(
				"neville longbottom", 3L, model.getSchoolGroup(), 1L);
		model = classService.loadClassModelById(model.getClassid());

		// gather info
		Long classid = model.getClassid();
		List<Long> studentids = new ArrayList<Long>();
		studentids.add(luke.getId());
		studentids.add(obiwan.getId());
		studentids.add(leia.getId());
		studentids.add(hansolo.getId());

		// remove students from class
		classService.removeStudentsFromClass(studentids,
				model.getSchoolGroup(), 1L);

		// service call
		classService.setStudentsAsInactive(studentids, 1L);
		// Assert - all students are inactive
		List<StudentDao> students = studentRepo.findAll(studentids);
		for (StudentDao st : students) {
			Assert.assertTrue(!st.getActive());
		}
	}

	@Test
	public void testStudentsForClass() {
		// create dummy class, and three students
		SchoolGroupDao sgroup = new SchoolGroupDao();
		ClassModel model = new ClassModel(sgroup);
		model.setTeachername("george booth");
		model.fillInTeacherFromEntry();
		model = classService.createClassFromClassModel(model, 1L);

		// add students to class -
		StudentDao luke = classService.addNewStudentToClass("hermione granger",
				1L, model.getSchoolGroup(), 1L);
		StudentDao obiwan = classService.addNewStudentToClass("ron weasley",
				1L, model.getSchoolGroup(), 1L);
		StudentDao leia = classService.addNewStudentToClass("draco malfoy", 2L,
				model.getSchoolGroup(), 1L);
		StudentDao hansolo = classService.addNewStudentToClass(
				"neville longbottom", 3L, model.getSchoolGroup(), 1L);
		model = classService.loadClassModelById(model.getClassid());

		// service call
		List<StudentDao> students = classService.getStudentsForClass(
				model.getClassid(), 1L);

		// should be not null, and size should be 3
		Assert.assertNotNull(students);
		Assert.assertEquals(4, students.size());
	}

	@Test
	public void testGetClassInfo() {
		ClientDao client = clientService.getClientForKey(1L);
		// get all schoolgroups for client
		List<SchoolGroupDao> schoolgroups = sgroupRepo
				.findSchoolGroupsByClient(client, new Sort("id"));
		// count them
		int comparison = schoolgroups.size();
		// service call
		HashMap<Long, TeacherInfo> results = classService
				.getTeacherByClassForClient(1L);
		// ensure that equals number of schoolgroups
		Assert.assertNotNull(results);
		Assert.assertEquals(comparison, results.size());

		// ensure that every key in map has non-null object
		for (Long key : results.keySet()) {
			Assert.assertNotNull(results.get(key));
		}
	}
}