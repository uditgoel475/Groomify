package com.uditgoel.groomify.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import com.uditgoel.groomify.dto.UserType;
import com.uditgoel.groomify.utils.AppUtils;

/**
 * Stores the user's current password and references the rotating history.
 *
 * <p>The previous design kept the last five passwords in five columns and rotated them in-place.
 * This version stores the current password in one column and keeps prior passwords in a
 * {@link PasswordHistory} table. The public API ({@link #getCurrentPassword},
 * {@link #getAllPasswordList}, {@link #setPassword}) is preserved so callers don't change.
 */
@Entity
@Table(name = "PASSWORD")
public class Password implements Serializable {

	private static final long serialVersionUID = -1353846980808372019L;

	/** How many previous passwords to retain (in addition to the current one). */
	private static final int HISTORY_LIMIT = 4;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PASSWORD_ID")
	private Long id;

	@Column(name = "CURRENT_PASSWORD", nullable = false)
	private String currentPassword;

	@Column(name = "PWD_CREATION_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date pwdCreationDate;

	@Column(name = "PWD_EXPIRATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date pwdExpirationDate;

	@OneToMany(mappedBy = "password", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@OrderBy("createdAt DESC")
	private List<PasswordHistory> history = new ArrayList<>();

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

	public List<PasswordHistory> getHistory() {
		return history;
	}

	@Transient
	public String getCurrentPassword() {
		return currentPassword;
	}

	/**
	 * Returns the current password plus the historical hashes, newest first. Used by
	 * reuse-check logic to ensure a new password doesn't match any recent one.
	 */
	@Transient
	public List<String> getAllPasswordList() {
		List<String> all = new ArrayList<>();
		if (currentPassword != null) {
			all.add(currentPassword);
		}
		for (PasswordHistory entry : history) {
			all.add(entry.getHashedPassword());
		}
		return all;
	}

	/**
	 * Rotates the current password into history and sets a new current password.
	 * Keeps the most recent {@link #HISTORY_LIMIT} historical entries.
	 */
	@Transient
	public void setPassword(String password, UserType userType) {
		if (currentPassword != null) {
			PasswordHistory entry = new PasswordHistory(this, currentPassword, new Date());
			history.add(0, entry);
			while (history.size() > HISTORY_LIMIT) {
				history.remove(history.size() - 1);
			}
		}
		currentPassword = password;

		Calendar cal = Calendar.getInstance();
		pwdCreationDate = cal.getTime();
		if (userType == UserType.CUSTOMER) {
			pwdExpirationDate = AppUtils.getCustomerExpirationDateFromCurrent(cal);
		} else if (userType == UserType.EMPLOYEE) {
			pwdExpirationDate = AppUtils.getEmployeeExpirationDateFromCurrent(cal);
		}
	}

	@Transient
	public long passwordAge() {
		return TimeUnit.DAYS.convert(Calendar.getInstance().getTimeInMillis() - pwdCreationDate.getTime(),
				TimeUnit.MILLISECONDS);
	}

	public boolean isPasswordExpired() {
		return pwdExpirationDate != null
				&& Calendar.getInstance().getTimeInMillis() - pwdExpirationDate.getTime() > 0;
	}

}
