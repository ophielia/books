package meg.biblio.catalog.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import meg.biblio.catalog.BasicStat;
import meg.biblio.catalog.CatalogService;
import meg.biblio.catalog.StatBreakout;
import meg.biblio.catalog.db.dao.ArtistDao;
import meg.biblio.catalog.db.dao.BookDao;
import meg.biblio.catalog.db.dao.BookDetailDao;
import meg.biblio.catalog.db.dao.FoundDetailsDao;
import meg.biblio.catalog.db.dao.PublisherDao;
import meg.biblio.catalog.db.dao.SubjectDao;
import meg.biblio.lending.db.dao.LoanRecordDisplay;

public class StatsModel implements Serializable {

	private static final long serialVersionUID = 1L;

	List<BasicStat> zone1;
	List<BasicStat> zone2;
	List<StatBreakout> zone3;

	public void setZone1Stats(List<BasicStat> zone1) {
		this.zone1 = zone1;

	}

	public List<BasicStat> getZone1Stats() {
		return zone1;
	}

	public void setZone2Stats(List<BasicStat> zone2) {
		this.zone2 = zone2;
	}

	public List<BasicStat> getZone2Stats() {
		return zone2;
	}

	public void setZone3Stats(List<StatBreakout> zone3) {
		this.zone3 = zone3;
	}

	public List<StatBreakout> getZone3Stats() {
		return zone3;
	}

}
