package com.niit.lookatme.dto.employee;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.niit.lookatme.dao.employee.Qualifications;
import com.niit.lookatme.dao.role.RoleName;

public class EmployeeInput extends EmployeeDTO {

	private String fName;
	private String mName;
	private String lName;
	private MultipartFile pictureFile;
	private Roster roster;
	private Qualifications qualificationType;
	private boolean availableOvertime;
	private long secondaryContact;
	private long whatsappContact;
	private boolean isSamePermanent;
	private String password;
	private String govtIdType;
	private String govtId;
	private MultipartFile govtIdPic;
	private double salary;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date joiningDate;
	private RoleName roleName;

	public EmployeeInput() {
		super();
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
	 * @return the pictureFile
	 */
	public MultipartFile getPictureFile() {
		return pictureFile;
	}

	/**
	 * @return the qualificationType
	 */
	public Qualifications getQualificationType() {
		return qualificationType;
	}

	/**
	 * @return the availableOvertime
	 */
	public boolean isAvailableOvertime() {
		return availableOvertime;
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
	 * @return the isSamePermanent
	 */
	public boolean isSamePermanent() {
		return isSamePermanent;
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
	 * @return the roleName
	 */
	public RoleName getRoleName() {
		return roleName;
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
	 * @param pictureFile
	 *            the pictureFile to set
	 */
	public void setPictureFile(MultipartFile pictureFile) {
		this.pictureFile = pictureFile;
	}

	/**
	 * @param qualificationType
	 *            the qualificationType to set
	 */
	public void setQualificationType(Qualifications qualificationType) {
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
	 * @param isSamePermanent
	 *            the isSamePermanent to set
	 */
	public void setSamePermanent(boolean isSamePermanent) {
		this.isSamePermanent = isSamePermanent;
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
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the roster
	 */
	public Roster getRoster() {
		return roster;
	}

	/**
	 * @param roster
	 *            the roster to set
	 */
	public void setRoster(Roster roster) {
		this.roster = roster;
	}

}
