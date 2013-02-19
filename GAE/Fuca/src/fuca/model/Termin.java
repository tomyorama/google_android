package fuca.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.api.datastore.Key;

@XmlRootElement(name = "termin")
@PersistenceCapable
public class Termin {

	public Termin() {

	}

	@PersistenceCapable
	public static class Player {

		public Player() {

		}

		public Player(Long userId) {
			this.userId = userId;
		}

		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private Key id;

		@Persistent
		private Long userId;

		@Persistent
		private String name;

		@Persistent
		private String nickname;

		@Persistent
		private Boolean Confirmed;

		public Key getId() {
			return id;
		}

		public void setId(Key id) {
			this.id = id;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Boolean getConfirmed() {
			return Confirmed;
		}

		public void setConfirmed(Boolean confirmed) {
			Confirmed = confirmed;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((userId == null) ? 0 : userId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Player other = (Player) obj;
			if (userId == null) {
				if (other.userId != null)
					return false;
			} else if (!userId.equals(other.userId))
				return false;
			return true;
		}

	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	@NotEmpty
	private String name;
	@Persistent
	@NotEmpty
	private String team1;
	@Persistent
	@NotEmpty
	private String team2;
	@Persistent
	@NotEmpty
	private String result;
	@Persistent
	private Date date;

	@JsonIgnore
	@Persistent(mappedBy = "terminX")
	@Element(dependent = "true")
	private List<Comment> comments;

	@JsonIgnore
	@Persistent
	private Set<Player> Team1Players;

	@JsonIgnore
	@Persistent
	private Set<Player> Team2Players;

	public Termin(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@XmlTransient
	public Date getDate() {
		return date;
	}

	public Long getDateNum() {
		return date.getTime();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@XmlTransient
	public Set<Player> getTeam1Players() {
		return Team1Players;
	}

	public void setTeam1Players(Set<Player> team1Players) {
		Team1Players = team1Players;
	}

	@XmlTransient
	public Set<Player> getTeam2Players() {
		return Team2Players;
	}

	public void setTeam2Players(Set<Player> team2Players) {
		Team2Players = team2Players;
	}

}
