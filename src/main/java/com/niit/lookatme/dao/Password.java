package com.niit.lookatme.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

/**
 * 
 * @author Konika
 *
 */

@Entity
@Table(name = "PASSWORD")
public class Password {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_ID")
	private Long id;
	
	@Column(name = "PASSWORD_1", nullable = false)
	private String password1;
	
	@Column(name = "PASSWORD_2")
	private String password2;
	@Column(name = "PASSWORD_3")
	private String password3;
	@Column(name = "PASSWORD_4")
	private String password4;
	@Column(name = "PASSWORD_5")
	private String password5;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "CREATION_DATE")
	private Date expirationDate;
	
	@Transient
	private String lastPassword;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the password1
	 */
	public String getPassword1() {
		return password1;
	}

	/**
	 * @return the password2
	 */
	public String getPassword2() {
		return password2;
	}

	/**
	 * @return the password3
	 */
	public String getPassword3() {
		return password3;
	}

	/**
	 * @return the password4
	 */
	public String getPassword4() {
		return password4;
	}

	/**
	 * @return the password5
	 */
	public String getPassword5() {
		return password5;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param password1 the password1 to set
	 */
	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	/**
	 * @param password2 the password2 to set
	 */
	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	/**
	 * @param password3 the password3 to set
	 */
	public void setPassword3(String password3) {
		this.password3 = password3;
	}

	/**
	 * @param password4 the password4 to set
	 */
	public void setPassword4(String password4) {
		this.password4 = password4;
	}

	/**
	 * @param password5 the password5 to set
	 */
	public void setPassword5(String password5) {
		this.password5 = password5;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	@Transient
	public String getLastPassword() {
		if(!StringUtils.isEmpty(password5))
			lastPassword = password5;
		else if(!StringUtils.isEmpty(password4))
			lastPassword = password4;
		else if(!StringUtils.isEmpty(password3))
			lastPassword = password3;
		else if(!StringUtils.isEmpty(password2))
			lastPassword = password2;
		else if(!StringUtils.isEmpty(password1))
			lastPassword = password1;
		return lastPassword;
	}
	
	@Transient
	public long passwordAge() {
		return TimeUnit.DAYS.convert(Calendar.getInstance().getTimeInMillis() - creationDate.getTime(), TimeUnit.MILLISECONDS);
	}
	
	public boolean isPasswordExpired() {
		return Calendar.getInstance().getTimeInMillis() - expirationDate.getTime() > 0;
	}

}
