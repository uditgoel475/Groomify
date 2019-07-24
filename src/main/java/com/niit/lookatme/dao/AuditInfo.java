package com.niit.lookatme.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 
 * @author Konika
 *
 */
@MappedSuperclass
public class AuditInfo implements Serializable {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 4981730898573636165L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_DATE", nullable = false, updatable = false)
	@CreatedDate
	private Date creationDate;

	@Column(name = "CREATED_BY", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "LAST_MODIFIED_BY")
	@LastModifiedBy
	private String lastModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFICATION_DATE", nullable = false)
	@LastModifiedDate
	private Date lastModifiedDate;

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
