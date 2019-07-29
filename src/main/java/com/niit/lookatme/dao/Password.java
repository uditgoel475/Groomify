package com.niit.lookatme.dao;

import java.io.Serializable;
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

import com.niit.lookatme.utils.AppUtils;

/**
 * 
 * @author Konika
 *
 */

@Entity
@Table(name = "PASSWORD")
public class Password implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1353846980808372018L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PASSWORD_ID")
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

	@Column(name = "PWD_CREATION_DATE")
	private Date pwdCreationDate;

	@Column(name = "PWD_EXPIRATION_DATE")
	private Date pwdExpirationDate;

	@Transient
	private String lastPassword;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public Date getPwdCreationDate() {
		return pwdCreationDate;
	}

	public Date getPwdExpirationDate() {
		return pwdExpirationDate;
	}

	public void setPwdCreationDate(Date pwdCreationDate) {
		this.pwdCreationDate = pwdCreationDate;
	}

	public void setPwdExpirationDate(Date pwdExpirationDate) {
		this.pwdExpirationDate = pwdExpirationDate;
	}

	@Transient
	public String getCurrentPassword() {
		if (!StringUtils.isEmpty(password5))
			lastPassword = password5;
		else if (!StringUtils.isEmpty(password4))
			lastPassword = password4;
		else if (!StringUtils.isEmpty(password3))
			lastPassword = password3;
		else if (!StringUtils.isEmpty(password2))
			lastPassword = password2;
		else if (!StringUtils.isEmpty(password1))
			lastPassword = password1;
		return lastPassword;
	}

	@Transient
	public boolean isMatchesPreviousPasswords(String password) {
		return password.equals(password1) || password.equals(password2) || password.equals(password3)
				|| password.equals(password4) || password.equals(password5);
	}

	@Transient
	public void setPassword(String password) {
		if (StringUtils.isEmpty(password1))
			password1 = password;
		else if (StringUtils.isEmpty(password2))
			password2 = password;
		else if (StringUtils.isEmpty(password3))
			password3 = password;
		else if (StringUtils.isEmpty(password4))
			password4 = password;
		else if (StringUtils.isEmpty(password5))
			password5 = password;
		else {
			password1 = password2;
			password2 = password3;
			password3 = password4;
			password4 = password5;
			password5 = password;
		}
		Calendar cal = Calendar.getInstance();
		pwdCreationDate = cal.getTime();
		pwdExpirationDate = AppUtils.getCustomerExpirationDateFromCurrent(cal);
	}

	@Transient
	public long passwordAge() {
		return TimeUnit.DAYS.convert(Calendar.getInstance().getTimeInMillis() - pwdCreationDate.getTime(),
				TimeUnit.MILLISECONDS);
	}

	public boolean isPasswordExpired() {
		return Calendar.getInstance().getTimeInMillis() - pwdExpirationDate.getTime() > 0;
	}

}
