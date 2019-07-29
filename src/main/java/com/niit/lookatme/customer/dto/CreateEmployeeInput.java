package com.niit.lookatme.customer.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CreateEmployeeInput {

	private String username;
	private String fName;
	private String mName;
	private String lName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dob;
	private MultipartFile pictureFile;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date genericInTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date genericOutTime;
	private String shiftStartDay;
	private String shiftEndDay;
	private String qualificationType;
	private boolean availableOvertime;
	private long primaryContact;
	private long secondaryContact;
	private long whatsappContact;
	private String gender;
	private AddressInput currentAddress;
	private AddressInput permanentAddress;
	private boolean isSamePermanent;

	private String regId;
	private String email;
	private String govtIdType;
	private String govtId;
	private MultipartFile govtIdPic;
	private double salary;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date joiningDate;
	private boolean isAdminUser;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}

	/**
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}

	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @return the pictureFile
	 */
	public MultipartFile getPictureFile() {
		return pictureFile;
	}

	/**
	 * @return the genericInTime
	 */
	public Date getGenericInTime() {
		return genericInTime;
	}

	/**
	 * @return the genericOutTime
	 */
	public Date getGenericOutTime() {
		return genericOutTime;
	}

	/**
	 * @return the shiftStartDay
	 */
	public String getShiftStartDay() {
		return shiftStartDay;
	}

	/**
	 * @return the shiftEndDay
	 */
	public String getShiftEndDay() {
		return shiftEndDay;
	}

	/**
	 * @return the qualificationType
	 */
	public String getQualificationType() {
		return qualificationType;
	}

	/**
	 * @return the availableOvertime
	 */
	public boolean isAvailableOvertime() {
		return availableOvertime;
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
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the currentAddress
	 */
	public AddressInput getCurrentAddress() {
		return currentAddress;
	}

	/**
	 * @return the permanentAddress
	 */
	public AddressInput getPermanentAddress() {
		return permanentAddress;
	}

	/**
	 * @return the isSamePermanent
	 */
	public boolean isSamePermanent() {
		return isSamePermanent;
	}

	/**
	 * @return the regId
	 */
	public String getRegId() {
		return regId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the govtIdType
	 */
	public String getGovtIdType() {
		return govtIdType;
	}

	/**
	 * @return the govtId
	 */
	public String getGovtId() {
		return govtId;
	}

	/**
	 * @return the govtIdPic
	 */
	public MultipartFile getGovtIdPic() {
		return govtIdPic;
	}

	/**
	 * @return the salary
	 */
	public double getSalary() {
		return salary;
	}

	/**
	 * @return the joiningDate
	 */
	public Date getJoiningDate() {
		return joiningDate;
	}

	/**
	 * @return the isAdminUser
	 */
	public boolean isAdminUser() {
		return isAdminUser;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param fName
	 *            the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}

	/**
	 * @param mName
	 *            the mName to set
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}

	/**
	 * @param lName
	 *            the lName to set
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @param pictureFile
	 *            the pictureFile to set
	 */
	public void setPictureFile(MultipartFile pictureFile) {
		this.pictureFile = pictureFile;
	}

	/**
	 * @param genericInTime
	 *            the genericInTime to set
	 */
	public void setGenericInTime(Date genericInTime) {
		this.genericInTime = genericInTime;
	}

	/**
	 * @param genericOutTime
	 *            the genericOutTime to set
	 */
	public void setGenericOutTime(Date genericOutTime) {
		this.genericOutTime = genericOutTime;
	}

	/**
	 * @param shiftStartDay
	 *            the shiftStartDay to set
	 */
	public void setShiftStartDay(String shiftStartDay) {
		this.shiftStartDay = shiftStartDay;
	}

	/**
	 * @param shiftEndDay
	 *            the shiftEndDay to set
	 */
	public void setShiftEndDay(String shiftEndDay) {
		this.shiftEndDay = shiftEndDay;
	}

	/**
	 * @param qualificationType
	 *            the qualificationType to set
	 */
	public void setQualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
	}

	/**
	 * @param availableOvertime
	 *            the availableOvertime to set
	 */
	public void setAvailableOvertime(boolean availableOvertime) {
		this.availableOvertime = availableOvertime;
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
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @param currentAddress
	 *            the currentAddress to set
	 */
	public void setCurrentAddress(AddressInput currentAddress) {
		this.currentAddress = currentAddress;
	}

	/**
	 * @param permanentAddress
	 *            the permanentAddress to set
	 */
	public void setPermanentAddress(AddressInput permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	/**
	 * @param isSamePermanent
	 *            the isSamePermanent to set
	 */
	public void setSamePermanent(boolean isSamePermanent) {
		this.isSamePermanent = isSamePermanent;
	}

	/**
	 * @param regId
	 *            the regId to set
	 */
	public void setRegId(String regId) {
		this.regId = regId;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param govtIdType
	 *            the govtIdType to set
	 */
	public void setGovtIdType(String govtIdType) {
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
	 * @param govtIdPic
	 *            the govtIdPic to set
	 */
	public void setGovtIdPic(MultipartFile govtIdPic) {
		this.govtIdPic = govtIdPic;
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
	 * @param isAdminUser
	 *            the isAdminUser to set
	 */
	public void setAdminUser(boolean isAdminUser) {
		this.isAdminUser = isAdminUser;
	}
}
