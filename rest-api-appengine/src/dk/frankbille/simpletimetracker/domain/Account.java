package dk.frankbille.simpletimetracker.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Account {
	
	@PrimaryKey
	private String accountKey;
	
	@Persistent
	private Date created;
	
	public String getAccountKey() {
		return accountKey;
	}
	
	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
}
