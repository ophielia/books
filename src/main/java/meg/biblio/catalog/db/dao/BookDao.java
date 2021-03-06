package meg.biblio.catalog.db.dao;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@Table(name="book")
public class BookDao {

private Long clientid;
private Long status;

private Long clientshelfcode;
private String clientshelfclass;
private Date createdon;
private String clientbookid;
private Long clientbookidsort;
private String barcodeid;
@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)/*@JoinColumn(name="ID")*/
private BookDetailDao bookdetail;
private Long clientbooktype;
private String note;
private Boolean tocount;
private Date counteddate;
private Long countstatus;

private Long userid;

	public void setClientbookid(String clientbookid) {
        if (clientbookid!=null) {
		this.clientbookid = clientbookid.trim();
        setClientbookidsort(clientbookid);
        }
    }
	
	public void setClientbookidsort(String clientbid) {
		if (clientbid!=null) {
			if (clientbid.matches("^[0-9]+$")) {
				// only numbers - save in sort field
				Long longclientid = new Long(clientbid);
				this.clientbookidsort=longclientid;
			}
		}
	}


	public BookDetailDao getBookdetail() {
        if (this.bookdetail==null) {
        	this.bookdetail = new BookDetailDao();
        }
		return this.bookdetail;
    }
}
