package com.uditgoel.groomify.dao.customer;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import com.uditgoel.groomify.dao.AuditInfo;
import com.uditgoel.groomify.dao.Membership;

@Entity
@Table(name = "CUSTOMER_MEMBERSHIP", uniqueConstraints = @UniqueConstraint(columnNames = "MEMBERSHIP_NO"))
public class CustomerMembership extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8660197942316395969L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMBERSHIP_ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "MEMBERSHIP_NO", nullable = false, updatable = false, unique = true)
	private String membershipNo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CUSTOMER", referencedColumnName = "CUSTOMER_ID")
	private Customer customer;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MEMBERSHIP", referencedColumnName = "TYPE_ID")
	private Membership membership;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "ISSUE_DATE", nullable = false)
	private Date issueDate;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;

	@Column(name = "PRICE_PAID", nullable = false)
	private Double pricePaid;

	public CustomerMembership(String membershipNo, Customer customer, Membership membership, Date issueDate,
			Date expiryDate, Double pricePaid) {
		super();
		this.membershipNo = membershipNo;
		this.customer = customer;
		this.membership = membership;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.pricePaid = pricePaid;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the membershipNo
	 */
	public String getMembershipNo() {
		return membershipNo;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the membership
	 */
	public Membership getMembership() {
		return membership;
	}

	/**
	 * @return the issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @return the pricePaid
	 */
	public Double getPricePaid() {
		return pricePaid;
	}

	/**
	 * @param membershipNo
	 *            the membershipNo to set
	 */
	public void setMembershipNo(String membershipNo) {
		this.membershipNo = membershipNo;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @param membership
	 *            the membership to set
	 */
	public void setMembership(Membership membership) {
		this.membership = membership;
	}

	/**
	 * @param issueDate
	 *            the issueDate to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @param pricePaid
	 *            the pricePaid to set
	 */
	public void setPricePaid(Double pricePaid) {
		this.pricePaid = pricePaid;
	}
}
