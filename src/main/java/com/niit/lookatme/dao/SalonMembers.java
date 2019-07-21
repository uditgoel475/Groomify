package com.niit.lookatme.dao;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * This class maps to table EMPLOYEE which holds the details of all the
 * employees working in the salon
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "EMPLOYEE")
public class SalonMembers extends AuditInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -597647737681006869L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "USERNAME", nullable = false)
	private String username;

	@Column(name = "PICTURE_URL")
	private String pictureUrl;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PASSWORD", updatable = false, referencedColumnName = "PASSWORD_ID")
	private Password password;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEDULE", referencedColumnName = "SCHEDULE_ID")
	private EmployeeRoster schedule;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUALIFICATION", referencedColumnName = "QUALIFICATION_ID")
	private EmployeeQualification qualification;

	@Column(name = "IS_OVERTIME_WORKER", columnDefinition = "boolean default false")
	private boolean isOvertimeWorker;

	// To be corrected
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(
			  name = "SKILLSET", 
			  joinColumns = @JoinColumn(name = "MEMBER_ID"), 
			  inverseJoinColumns = @JoinColumn(name = "SKILL_ID"))
	private SkillSet skills;

	@Column(name = "PRIMARY_CONTACT", nullable = false, columnDefinition = "integer default 0000000000")
	private long primaryContact;

	@Column(name = "SECONDARY_CONTACT")
	private long secondaryContact;

	@Column(name = "WHATSAPP_CONTACT")
	private long whatsappContact;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "REGISTRATION_ID", nullable = false, unique = true)
	private String regId;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "GENDER", columnDefinition = "varchar(20) default MALE")
	private Gender gender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDRESS_FK")
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOVT_ID_TYPE_FK")
	private GovtIdType govtIdType;

	@Column(name = "GOVT_ID", nullable = false)
	private String govtId;

	@Column(name = "GOVT_ID_SNAP_URL")
	private String govtIdSnapUrl;

	@Column(name = "SALARY", nullable = false)
	private double salary;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "JOINING_DATE", nullable = false)
	private Date joiningDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LEAVING_DATE")
	private Date leavingDate;

	@Column(name = "IS_ADMIN_USER", nullable = false, columnDefinition = "boolean default false")
	private boolean isAdminUser;



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * @return the skills
	 */
	public SkillSet getSkills() {
		return skills;
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
	 * @param isOvertimeWorker the isOvertimeWorker to set
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
	 * @return the address
	 */
	public Address getAddress() {
		return address;
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(Password password) {
		this.password = password;
	}

	/**
	 * @param schedule the schedule to set
	 */
	public void setSchedule(EmployeeRoster schedule) {
		this.schedule = schedule;
	}

	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(EmployeeQualification qualification) {
		this.qualification = qualification;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(SkillSet skills) {
		this.skills = skills;
	}

	/**
	 * @param primaryContact the primaryContact to set
	 */
	public void setPrimaryContact(long primaryContact) {
		this.primaryContact = primaryContact;
	}

	/**
	 * @param secondaryContact the secondaryContact to set
	 */
	public void setSecondaryContact(long secondaryContact) {
		this.secondaryContact = secondaryContact;
	}

	/**
	 * @param whatsappContact the whatsappContact to set
	 */
	public void setWhatsappContact(long whatsappContact) {
		this.whatsappContact = whatsappContact;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param regId the regId to set
	 */
	public void setRegId(String regId) {
		this.regId = regId;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @param govtIdType the govtIdType to set
	 */
	public void setGovtIdType(GovtIdType govtIdType) {
		this.govtIdType = govtIdType;
	}

	/**
	 * @param govtId the govtId to set
	 */
	public void setGovtId(String govtId) {
		this.govtId = govtId;
	}

	/**
	 * @param salary the salary to set
	 */
	public void setSalary(double salary) {
		this.salary = salary;
	}

	/**
	 * @param joiningDate the joiningDate to set
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
	 * @param pictureUrl the pictureUrl to set
	 */
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/**
	 * @param govtIdSnapUrl the govtIdSnapUrl to set
	 */
	public void setGovtIdSnapUrl(String govtIdSnapUrl) {
		this.govtIdSnapUrl = govtIdSnapUrl;
	}

	/**
	 * @param leavingDate the leavingDate to set
	 */
	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}

}