package com.uditgoel.groomify.dao;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * A previous password for a given {@link Password} owner. Rows are added when a password
 * changes and trimmed to a fixed window (see {@code Password.HISTORY_LIMIT}).
 */
@Entity
@Table(name = "PASSWORD_HISTORY", indexes = {
		@Index(name = "idx_password_history_password_id", columnList = "PASSWORD_ID")
})
public class PasswordHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PASSWORD_HISTORY_ID")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "PASSWORD_ID", nullable = false)
	private Password password;

	@Column(name = "HASHED_PASSWORD", nullable = false)
	private String hashedPassword;

	@Column(name = "CREATED_AT", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public PasswordHistory() {
	}

	public PasswordHistory(Password password, String hashedPassword, Date createdAt) {
		this.password = password;
		this.hashedPassword = hashedPassword;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
