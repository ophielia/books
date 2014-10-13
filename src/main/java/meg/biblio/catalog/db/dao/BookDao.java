package meg.biblio.catalog.db.dao;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.Date;
import java.util.List;

import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaEntity
@Table(name="book")
public class BookDao {

private Long clientid;
@NotNull
private String title;

@ManyToMany
@JoinTable(
    name="BOOK_AUTHOR",
    joinColumns=@JoinColumn(name="book_id", referencedColumnName="id"),
    inverseJoinColumns={@JoinColumn(name="artist_id", referencedColumnName="id")})
@OrderColumn(name="authororder")
private List<ArtistDao> authors;

@ManyToMany
@JoinTable(
	    name="BOOK_ILLUSTRATOR",
	    joinColumns=@JoinColumn(name="book_id", referencedColumnName="id"),
	    inverseJoinColumns={@JoinColumn(name="artist_id", referencedColumnName="id")})
	@OrderColumn(name="illustorder")
	private List<ArtistDao> illustrators;

@ManyToMany
@JoinTable(
	    name="BOOK_SUBJECT",
	    joinColumns=@JoinColumn(name="book_id", referencedColumnName="id"),
	    inverseJoinColumns={@JoinColumn(name="subject_id", referencedColumnName="id")})
	@OrderColumn(name="subjectorder")
	private List<SubjectDao> subjects;

private Long publisherkey;
private Long publishyear;
private String isbn10;
private String isbn13;
private Long language;
private Long type;
private String description;
private Long status;
private Long detailstatus;
private Long shelfclass;
private Boolean shelfclassverified;
private Date createdon;
private String clientbookid;
}
