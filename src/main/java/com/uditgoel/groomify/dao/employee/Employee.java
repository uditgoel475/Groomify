package com.uditgoel.groomify.dao.employee;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import com.uditgoel.groomify.dao.Address;
import com.uditgoel.groomify.dao.AuditInfo;
import com.uditgoel.groomify.dao.Gender;
import com.uditgoel.groomify.dao.GovtIdType;
import com.uditgoel.groomify.dao.Password;
import com.uditgoel.groomify.dao.Rating;
import com.uditgoel.groomify.dao.role.Role;

/**
 * This class maps to table EMPLOYEE which holds the details of all the
 * employees working in the salon
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "EMPLOYEE", uniqueConstraints = { @UniqueConstraint(columnNames = { "USERNAME" }) })
public class Employee extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -597647737681006869L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "FIRST_NAME", nullable = false)
	private String fname;

	@Column(name = "MIDDLE_NAME")
	private String mname;

	@Column(name = "LAST_NAME")
	private String lname;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "DOB")
	private Date dob;

	@Column(name = "USERNAME", nullable = false, unique = true)
	private String username;

	@Column(name = "PICTURE_URL", unique = true)
	private String pictureUrl;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "PASSWORD", updatable = false, referencedColumnName = "PASSWORD_ID")
	private Password password;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SCHEDULE", referencedColumnName = "SCHEDULE_ID")
	private EmployeeRoster schedule;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "QUALIFICATION", referencedColumnName = "QUALIFICATION_ID")
	private EmployeeQualification qualification;

	@Column(name = "IS_OVERTIME_WORKER")
	@ColumnDefault("false")
	private boolean isOvertimeWorker;

	@Column(name = "PRIMARY_CONTACT", nullable = false)
	@ColumnDefault("0000000000")
	private long primaryContact;

	@Column(name = "SECONDARY_CONTACT")
	private long secondaryContact;

	@Column(name = "WHATSAPP_CONTACT")
	private long whatsappContact;

	@Column(name = "EMAIL")
	@Email
	@NaturalId
	@NotBlank
	@Size(max = 40)
	private String email;

	@Column(name = "REGISTRATION_ID", nullable = false, unique = true)
	private String regId;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "GENDER")
	@ColumnDefault("'MALE'")
	private Gender gender;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CURRENT_ADDRESS", referencedColumnName = "ADDRESS_ID")
	private Address currentAddress;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "PERMANENT_ADDRESS", referencedColumnName = "ADDRESS_ID")
	private Address permanentAddress;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOVT_ID_TYPE", referencedColumnName = "TYPE_ID")
	private GovtIdType govtIdType;

	@Column(name = "GOVT_ID", nullable = false, unique = true)
	private String govtId;

	@Column(name = "GOVT_ID_SNAP_URL", unique = true)
	private String govtIdSnapUrl;

	@Column(name = "SALARY")
	private double salary;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "JOINING_DATE", nullable = false)
	private Date joiningDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LEAVING_DATE")
	private Date leavingDate;

	@Enumerated(EnumType.STRING)
	@Column(length = 2, name = "RATING")
	private Rating rating;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "EMPLOYEE_ROLES", joinColumns = { @JoinColumn(name = "EMPLOYEE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private Set<Role> employeeRoles;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public Password getPassword() {
		return password;
	}

	/**
	 * @return the schedule
	 */
	public EmployeeRoster getSchedule() {
		return schedule;
	}

	/**
	 * @return the qualification
	 */
	public EmployeeQualification getQualification() {
		return qualification;
	}

	/**
	 * @return the primaryContact
	 */
	public long getPrimaryContact() {
		return primaryContact;
	}

	/**
	 * @return the secondaryContact
	 */
	public long getSecondaryContact() {
		return secondaryContact;
	}

	/**
	 * @return the whatsappContact
	 */
	public long getWhatsappContact() {
		return whatsappContact;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the isOvertimeWorker
	 */
	public boolean isOvertimeWorker() {
		return isOvertimeWorker;
	}

	/**
	 * @param isOvertimeWorker
	 *            the isOvertimeWorker to set
	 */
	public void setOvertimeWorker(boolean isOvertimeWorker) {
		this.isOvertimeWorker = isOvertimeWorker;
	}

	/**
	 * @return the regId
	 */
	public String getRegId() {
		return regId;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return the currentAddress
	 */
	public Address getCurrentAddress() {
		return currentAddress;
	}

	/**
	 * @return the govtIdType
	 */
	public GovtIdType getGovtIdType() {
		return govtIdType;
	}

	/**
	 * @return the govtId
	 */
	public String getGovtId() {
		return govtId;
	}

	/**
	 * @return the salary
	 */
	public double getSalary() {
		return salary;
	}

	/**
	 * Get salary locale based.
	 * 
	 * @param locale
	 * @return
	 */
	@Transient
	public String getSalary(Locale locale) {
		return NumberFormat.getCurrencyInstance(locale).format(salary);
	}

	/**
	 * @return the joiningDate
	 */
	public Date getJoiningDate() {
		return joiningDate;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(Password password) {
		this.password = password;
	}

	/**
	 * @param schedule
	 *            the schedule to set
	 */
	public void setSchedule(EmployeeRoster schedule) {
		this.schedule = schedule;
	}

	/**
	 * @param qualification
	 *            the qualification to set
	 */
	public void setQualification(EmployeeQualification qualification) {
		this.qualification = qualification;
	}

	/**
	 * @param primaryContact
	 *            the primaryContact to set
	 */
	public void setPrimaryContact(long primaryContact) {
		this.primaryContact = primaryContact;
	}

	/**
	 * @param secondaryContact
	 *            the secondaryContact to set
	 */
	public void setSecondaryContact(long secondaryContact) {
		this.secondaryContact = secondaryContact;
	}

	/**
	 * @param whatsappContact
	 *            the whatsappContact to set
	 */
	public void setWhatsappContact(long whatsappContact) {
		this.whatsappContact = whatsappContact;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param regId
	 *            the regId to set
	 */
	public void setRegId(String regId) {
		this.regId = regId;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @param currentAddress
	 *            the currentAddress to set
	 */
	public void setCurrentAddress(Address currentAddress) {
		this.currentAddress = currentAddress;
	}

	/**
	 * @param govtIdType
	 *            the govtIdType to set
	 */
	public void setGovtIdType(GovtIdType govtIdType) {
		this.govtIdType = govtIdType;
	}

	/**
	 * @param govtId
	 *            the govtId to set
	 */
	public void setGovtId(String govtId) {
		this.govtId = govtId;
	}

	/**
	 * @param salary
	 *            the salary to set
	 */
	public void setSalary(double salary) {
		this.salary = salary;
	}

	/**
	 * @param joiningDate
	 *            the joiningDate to set
	 */
	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	/**
	 * @return the pictureUrl
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}

	/**
	 * @return the govtIdSnapUrl
	 */
	public String getGovtIdSnapUrl() {
		return govtIdSnapUrl;
	}

	/**
	 * @return the leavingDate
	 */
	public Date getLeavingDate() {
		return leavingDate;
	}

	/**
	 * @param pictureUrl
	 *            the pictureUrl to set
	 */
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/**
	 * @param govtIdSnapUrl
	 *            the govtIdSnapUrl to set
	 */
	public void setGovtIdSnapUrl(String govtIdSnapUrl) {
		this.govtIdSnapUrl = govtIdSnapUrl;
	}

	/**
	 * @param leavingDate
	 *            the leavingDate to set
	 */
	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}

	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname
	 *            the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * @return the mname
	 */
	public String getMname() {
		return mname;
	}

	/**
	 * @param mname
	 *            the mname to set
	 */
	public void setMname(String mname) {
		this.mname = mname;
	}

	/**
	 * @return the lname
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * @param lname
	 *            the lname to set
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}

	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @return the permanentAddress
	 */
	public Address getPermanentAddress() {
		return permanentAddress;
	}

	/**
	 * @param permanentAddress
	 *            the permanentAddress to set
	 */
	public void setPermanentAddress(Address permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	@Transient
	public String getName() {
		return new StringBuilder(fname).append(" ").append(StringUtils.isEmpty(mname) ? StringUtils.EMPTY : mname)
				.append(StringUtils.isEmpty(mname) ? StringUtils.EMPTY : " ").append(lname).toString().trim();
	}

	/**
	 * @return the employeeRoles
	 */
	public Set<Role> getEmployeeRoles() {
		return employeeRoles;
	}

	/**
	 * @param employeeRoles
	 *            the employeeRoles to set
	 */
	public void setEmployeeRoles(Set<Role> employeeRoles) {
		this.employeeRoles = employeeRoles;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(currentAddress).append(permanentAddress).append(dob).append(email)
				.append(fname).append(gender).append(govtId).append(govtIdSnapUrl).append(govtIdType)
				.append(employeeRoles).append(isOvertimeWorker).append(joiningDate).append(leavingDate).append(lname)
				.append(mname).append(password).append(pictureUrl).append(primaryContact).append(qualification)
				.append(rating).append(regId).append(schedule).append(secondaryContact).append(username)
				.append(whatsappContact).append(salary).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Employee rhs = (Employee) obj;
		return new EqualsBuilder().append(currentAddress, rhs.currentAddress)
				.append(permanentAddress, rhs.permanentAddress).append(dob, rhs.dob).append(email, rhs.email)
				.append(fname, rhs.fname).append(gender, rhs.gender).append(govtId, rhs.govtId)
				.append(govtIdSnapUrl, rhs.govtIdSnapUrl).append(govtIdType, rhs.govtIdType)
				.append(employeeRoles, rhs.employeeRoles).append(isOvertimeWorker, rhs.isOvertimeWorker)
				.append(joiningDate, rhs.joiningDate).append(leavingDate, rhs.leavingDate).append(lname, rhs.lname)
				.append(mname, rhs.mname).append(password, rhs.password).append(pictureUrl, rhs.pictureUrl)
				.append(primaryContact, rhs.primaryContact).append(qualification, rhs.qualification)
				.append(rating, rhs.rating).append(regId, rhs.regId).append(schedule, rhs.schedule)
				.append(secondaryContact, rhs.secondaryContact).append(username, rhs.username)
				.append(whatsappContact, rhs.whatsappContact).append(salary, rhs.salary).isEquals();
	}

}