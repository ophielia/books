package meg.biblio.lending;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import meg.biblio.catalog.CatalogService;
import meg.biblio.catalog.db.dao.BookDao;
import meg.biblio.common.db.dao.ClientDao;
import meg.biblio.lending.db.dao.LoanRecordDisplay;
import meg.biblio.lending.db.dao.PersonDao;
import meg.biblio.search.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LendingSearchServiceImpl implements LendingSearchService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CatalogService catalogService;

	@Override
	public List<LoanRecordDisplay> findLoanRecordsByCriteria(
			LendingSearchCriteria criteria, Long clientid) {
		// set clientid in criteria
		criteria.setClientid(clientid);

		// put together joins
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery c = cb.createQuery(LoanRecordDisplay.class);
		// CriteriaQuery<LoanRecordDisplay> c =
		// cb.createQuery(LoanRecordDisplay.class);
		Root<LoanRecordDisplay> loanrec = c.from(LoanRecordDisplay.class);

		// build select clause
		c.select(loanrec);

		// add predicate
		List<Predicate> whereclause = getPredicatesForCriteria(criteria, cb,
				loanrec);

		// putting where clause together
		c.where(cb.and(whereclause.toArray(new Predicate[whereclause.size()])));

		// order clause
		long sortkey = criteria.getSortKey();
		long sortdir = criteria.getSortDir();
		if (sortkey > 0) {
			String key = null;
			if (sortkey == LendingSearchCriteria.SortKey.BOOKID) {
				key = "bookclientidsort";
			} else if (sortkey == LendingSearchCriteria.SortKey.CHECKEDOUT) {
				key = "checkedout";
			} else if (sortkey == LendingSearchCriteria.SortKey.CLASS) {
				key = "classid";
			} else if (sortkey == LendingSearchCriteria.SortKey.RETURNED) {
				key = "returned";
			} else if (sortkey == LendingSearchCriteria.SortKey.STUDENTFIRSTNAME) {
				key = "borrowerfn";
			} else if (sortkey == LendingSearchCriteria.SortKey.TITLE) {
				key = "booktitle";
			}
			Expression sortexp = loanrec.get(key);
			if (sortdir == LendingSearchCriteria.SortByDir.ASC) {
				c.orderBy(cb.asc(sortexp), cb.desc(loanrec.get("checkedout")));
			} else {
				c.orderBy(cb.desc(sortexp), cb.desc(loanrec.get("checkedout")));
			}
		}

		// creating query
		TypedQuery<LoanRecordDisplay> q = em.createQuery(c);

		// adding parameters
		setParametersInQuery(criteria, q, clientid);

		// running query
		List<LoanRecordDisplay> toreturn = q.getResultList();

		return toreturn;

	}

	@Override
	public HashMap<Long, Long> checkoutBreakout(long breakoutkey,
			Long clientid, Boolean currentYearOnly) {

		// determine field string
		String fieldstring = null;
		if (breakoutkey == LendingSearchService.Breakoutfield.CLIENTCATEGORY) {
			fieldstring = "shelfclass";
		}

		if (fieldstring != null) {
			LendingSearchCriteria criteria = new LendingSearchCriteria();

			// put together query
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> c = cb.createQuery(Tuple.class);
			Root<LoanRecordDisplay> loanrec = c.from(LoanRecordDisplay.class);

			Expression countExpression = cb.count(loanrec
					.<Number> get("loanrecordid"));
			c.multiselect(loanrec.get(fieldstring), countExpression).groupBy(
					loanrec.get(fieldstring));

			List<Predicate> whereclause = new ArrayList<Predicate>();

			// making space for parameters
			// always add client id
			ParameterExpression<Long> clientparam = cb.parameter(Long.class,
					"clientid");
			whereclause.add(cb.equal(loanrec.<Long> get("clientid"),
					clientparam));
			if (currentYearOnly) {
				criteria.setCheckoutTimeselect(LendingSearchCriteria.TimePeriodType.CURRENTSCHOOLYEAR);
				ParameterExpression<Date> param = cb.parameter(Date.class,
						"checkoutdate");
				whereclause.add(cb.greaterThanOrEqualTo(
						loanrec.<Date> get("checkedout"), param));
			}

			// adding the whereclause
			c.where(cb.and(whereclause.toArray(new Predicate[whereclause.size()])));

			c.orderBy(cb.desc(cb.count(loanrec.<Number> get("loanrecordid"))));
			// creating the query
			TypedQuery<Tuple> q = em.createQuery(c);

			// setting the parameters
			// always add clientid
			q.setParameter("clientid", clientid);
			if (currentYearOnly) {
				// do checkedouton
				q.setParameter("checkoutdate", criteria.getCheckedoutafter());
			}

			List<Tuple> results = q.getResultList();
			LinkedHashMap<Long, Long> toreturn = new LinkedHashMap<Long, Long>();

			for (Tuple t : results) {
				Long key = (Long) t.get(0);
				Long value = (Long) t.get(1);
				toreturn.put(key, value);
			}

			return toreturn;
		}
		return null;
	}	
	
	
@Override
	public HashMap<String, Long> mostPopularBreakout(Long clientid, Boolean currentYearOnly) {
			LendingSearchCriteria criteria = new LendingSearchCriteria();

			// put together query
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> c = cb.createQuery(Tuple.class);
			Root<LoanRecordDisplay> loanrec = c.from(LoanRecordDisplay.class);

			Expression countExpression = cb.count(loanrec
					.<Number> get("loanrecordid"));
			Expression havingExpression = cb.greaterThanOrEqualTo(
					countExpression, 2);
			c.multiselect(loanrec.get("booktitle"), countExpression).having(havingExpression).groupBy(
					loanrec.get("booktitle"));

			List<Predicate> whereclause = new ArrayList<Predicate>();

			// making space for parameters
			// always add client id
			ParameterExpression<Long> clientparam = cb.parameter(Long.class,
					"clientid");
			whereclause.add(cb.equal(loanrec.<Long> get("clientid"),
					clientparam));
			if (currentYearOnly) {
				criteria.setCheckoutTimeselect(LendingSearchCriteria.TimePeriodType.CURRENTSCHOOLYEAR);
				ParameterExpression<Date> param = cb.parameter(Date.class,
						"checkoutdate");
				whereclause.add(cb.greaterThanOrEqualTo(
						loanrec.<Date> get("checkedout"), param));
			}

			// adding the whereclause
			c.where(cb.and(whereclause.toArray(new Predicate[whereclause.size()])));

			c.orderBy(cb.desc(cb.count(loanrec.<Number> get("loanrecordid"))));
			// creating the query
			TypedQuery<Tuple> q = em.createQuery(c);

			// setting the parameters
			// always add clientid
			q.setParameter("clientid", clientid);
			if (currentYearOnly) {
				// do checkedouton
				q.setParameter("checkoutdate", criteria.getCheckedoutafter());
			}

			List<Tuple> results = q.getResultList();
			LinkedHashMap<String, Long> toreturn = new LinkedHashMap<String, Long>();

			for (Tuple t : results) {
				String key = (String) t.get(0);
				Long value = (Long) t.get(1);
				toreturn.put(key, value);
			}

			return toreturn;

	}	
	
	
	private List<Predicate> getPredicatesForCriteria(
			LendingSearchCriteria criteria, CriteriaBuilder cb,
			Root<LoanRecordDisplay> loanrec) {
		// put together where clause
		List<Predicate> whereclause = new ArrayList<Predicate>();

		// making space for parameters
		// always add client id
		ParameterExpression<Long> clientparam = cb.parameter(Long.class,
				"clientid");
		whereclause.add(cb.equal(loanrec.<Long> get("clientid"), clientparam));

		// do checkedoutonafter
		if (criteria.getCheckedoutafter() != null) {
			ParameterExpression<Date> param = cb.parameter(Date.class,
					"checkoutdate");
			whereclause.add(cb.greaterThanOrEqualTo(
					loanrec.<Date> get("checkedout"), param));

		}

		// do checkedoutonafter
		if (criteria.getCheckedoutbefore() != null) {
			ParameterExpression<Date> param = cb.parameter(Date.class,
					"checkoutdatebefore");
			whereclause.add(cb.lessThanOrEqualTo(
					loanrec.<Date> get("checkedout"), param));

		}

		// do returnedafter
		if (criteria.getReturnedafter() != null) {
			ParameterExpression<Date> param = cb.parameter(Date.class,
					"returnedafter");
			whereclause.add(cb.greaterThanOrEqualTo(
					loanrec.<Date> get("returned"), param));
		}	
		
		if (criteria.getReturnedbefore() != null) {
			ParameterExpression<Date> param = cb.parameter(Date.class,
					"returnedbefore");
			whereclause.add(cb.lessThanOrEqualTo(
					loanrec.<Date> get("returned"), param));
		}		

		// do forschoolgroup
		if (criteria.getSchoolgroup() != null) {
			ParameterExpression<Long> param = cb.parameter(Long.class,
					"schoolgroupid");
			whereclause.add(cb.equal(loanrec.<Long> get("classid"), param));
		}

		// do borrowerid
		if (criteria.getBorrowerid() != null) {
			ParameterExpression<Long> param = cb.parameter(Long.class,
					"borrowerid");
			whereclause.add(cb.equal(loanrec.<Long> get("borrowerid"), param));
		}

		// do bookid
		if (criteria.getBookid() != null) {
			ParameterExpression<Long> param = cb
					.parameter(Long.class, "bookid");
			whereclause.add(cb.equal(loanrec.<Long> get("bookid"), param));
		}

		// to lentto
		if (criteria.getLentToType() != null) {
			Expression<java.lang.Boolean> isteacher = cb.literal(new Boolean(
					true));
			if (criteria.getLentToType() == LendingSearchCriteria.LentToType.STUDENT) {
				isteacher = cb.literal(new Boolean(false));
			}
			whereclause.add(cb.equal(loanrec.<Boolean> get("isteacher"),
					isteacher));
		}

		// to overdue only
		if (criteria.getOverdueOnly() != null && criteria.getOverdueOnly()) {
			Expression<java.lang.Boolean> truelit = cb
					.literal(new Boolean(true));
			whereclause.add(cb.equal(loanrec.<Boolean> get("currentlyoverdue"),
					truelit));

		}

		// to checkedout only
		if (criteria.getCheckedoutOnly() != null
				&& criteria.getCheckedoutOnly()) {
			Expression<java.lang.Boolean> truelit = cb
					.literal(new Boolean(true));
			whereclause.add(cb.equal(
					loanrec.<Boolean> get("currentlycheckedout"), truelit));
		}
		return whereclause;

	}

	private void setParametersInQuery(LendingSearchCriteria criteria,
			TypedQuery q, Long clientid) {
		// always add clientid
		q.setParameter("clientid", clientid);

		// do checkedouton
		if (criteria.getCheckedoutafter() != null) {
			q.setParameter("checkoutdate", criteria.getCheckedoutafter());
		}

		// do checkedouton
		if (criteria.getCheckedoutbefore() != null) {
			q.setParameter("checkoutdatebefore", criteria.getCheckedoutbefore());
		}

		// do returned after
		if (criteria.getReturnedafter() != null) {
			q.setParameter("returnedafter", criteria.getReturnedafter());
		}

		if (criteria.getReturnedbefore() != null) {
			q.setParameter("returnedbefore", criteria.getReturnedbefore());
		}
		
		// do forschoolgroup
		if (criteria.getSchoolgroup() != null) {
			q.setParameter("schoolgroupid", criteria.getSchoolgroup());
		}

		// do forschoolgroup
		if (criteria.getBorrowerid() != null) {
			q.setParameter("borrowerid", criteria.getBorrowerid());
		}

		// do bookid
		if (criteria.getBookid() != null) {
			q.setParameter("bookid", criteria.getBookid());
		}

		// to lentto - no parameter

		// to overdue only - no parameter

	}

	@Override
	public Long getActiveBorrowerCount(ClientDao client) {
		// put together query
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<PersonDao> exp = c.from(PersonDao.class);
		Expression countExpression = cb.count(exp.<Number> get("id"));
		c.select(countExpression);

		List<Predicate> whereclause = new ArrayList<Predicate>();

		// making space for parameters
		// always add client id
		ParameterExpression<ClientDao> clientparam = cb.parameter(ClientDao.class,
				"client");
		whereclause.add(cb.equal(exp.<Long> get("client"), clientparam));
		whereclause.add(cb.isTrue(exp.<Boolean> get("active")));
		
		// adding the whereclause
		c.where(cb.and(whereclause.toArray(new Predicate[whereclause.size()])));

		// creating the query
		TypedQuery<Long> q = em.createQuery(c);

		// setting the parameters
		// always add clientid
		q.setParameter("client", client);

		List<Long> results = q.getResultList();
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return 0L;
	}

	@Override
	public Long findCountByCriteria(LendingSearchCriteria criteria,Long clientid) {
		// set clientid in criteria
		criteria.setClientid(clientid);

		// put together joins
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		// CriteriaQuery<LoanRecordDisplay> c =
		// cb.createQuery(LoanRecordDisplay.class);
		Root<LoanRecordDisplay> loanrec = c.from(LoanRecordDisplay.class);

		// build select clause
		Expression countExpression = cb.count(loanrec
				.<Number> get("loanrecordid"));
		c.select(countExpression);

		// add predicate
		List<Predicate> whereclause = getPredicatesForCriteria(criteria, cb,
				loanrec);

		// putting where clause together
		c.where(cb.and(whereclause.toArray(new Predicate[whereclause.size()])));

		// creating query
		TypedQuery<Long> q = em.createQuery(c);

		// adding parameters
		setParametersInQuery(criteria, q, clientid);

		List<Long> results = q.getResultList();
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return 0L;
	}



	/*
	 * 
	 * @Override public List<LoanHistoryDisplay> findLoanHistoryByCriteria(
	 * LendingSearchCriteria criteria, Long clientid) { // set clientid in
	 * criteria criteria.setClientid(clientid);
	 * 
	 * // put together joins CriteriaBuilder cb = em.getCriteriaBuilder();
	 * CriteriaQuery<Tuple> c = cb.createTupleQuery(); Root<LoanHistoryDao>
	 * loanrec = c.from(LoanHistoryDao.class); Join<LoanHistoryDao, BookDao>
	 * book = loanrec.join("book"); Join<LoanHistoryDao, PersonDao> person =
	 * loanrec.join("borrower"); Join<PersonDao, SchoolGroupDao> sgroup =
	 * person.join("schoolgroup");
	 * 
	 * // build select clause c.select(cb.tuple(book, person, loanrec,
	 * sgroup.<Long> get("id")));
	 * 
	 * // add predicate List<Predicate> whereclause =
	 * getPredicatesForCriteria(criteria, cb, book, person, sgroup, null,
	 * loanrec, LendingSearchCriteria.SearchType.RETURNED);
	 * 
	 * // putting where clause together c.where(cb.and(whereclause.toArray(new
	 * Predicate[whereclause.size()])));
	 * 
	 * // creating query TypedQuery<Tuple> q = em.createQuery(c);
	 * 
	 * // adding parameters setParametersInQuery(criteria, q,
	 * clientid,LendingSearchCriteria.SearchType.RETURNED);
	 * 
	 * // running query List<Tuple> results = q.getResultList();
	 * List<LoanHistoryDisplay> toreturn = new ArrayList<LoanHistoryDisplay>();
	 * 
	 * for (Tuple t : results) { // LoanRecordDisplay result =
	 * (LoanRecordDisplay) t.get(0); BookDao bookres = (BookDao) t.get(0);
	 * PersonDao personres = (PersonDao) t.get(1); LoanHistoryDao lrec =
	 * (LoanHistoryDao) t.get(2); Long classid = (Long) t.get(3);
	 * LoanHistoryDisplay display = new LoanHistoryDisplay(lrec, personres,
	 * bookres, classid); toreturn.add(display); }
	 * 
	 * return toreturn;
	 * 
	 * }
	 */
}
