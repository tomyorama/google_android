package fuca.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;

@XmlRootElement(name = "comment")
@PersistenceCapable
public class Comment {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	@NotEmpty
	private String text;

	@Persistent
	@NotEmpty
	private String user;

	@Persistent
	private Termin terminX;

	@Persistent
	private Date dateCreated;

	@Persistent
	BlobKey picture;

	@Persistent
	BlobKey video;

	@XmlElement(name = "Id")
	public Long getLongId() {
		return id.getId();
	}

	@XmlTransient
	protected Key getId() {
		return id;
	}

	protected void setId(Key id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@XmlTransient
	public Termin getTerminX() {
		return terminX;
	}

	public void setTerminX(Termin terminX) {
		this.terminX = terminX;
	}
	@XmlElement(name = "DateCreated")
	public Long getDate() {
		return dateCreated.getTime();
	}

	@XmlTransient
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlTransient
	public BlobKey getPicture() {
		return this.picture;
	}

	public void setPicture(BlobKey picture) {
		this.picture = picture;
	}

	@XmlTransient
	public BlobKey getVideo() {
		return this.video;
	}

	public void setVideo(BlobKey video) {
		this.video = video;
	}

}
