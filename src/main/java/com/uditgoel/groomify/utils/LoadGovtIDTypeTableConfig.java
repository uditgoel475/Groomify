package com.uditgoel.groomify.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:govtidtype.properties")
@ConfigurationProperties("idtype")
public class LoadGovtIDTypeTableConfig {

	private List<GovtIdType> govtIdTypes = new ArrayList<>();

	public static class GovtIdType {

		private String idType;
		private String regex;

		public GovtIdType(String idType, String regex) {
			super();
			this.idType = idType;
			this.regex = regex;
		}

		public GovtIdType() {
			super();
		}

		/**
		 * @param idType the idType to set
		 */
		public void setIdType(String idType) {
			this.idType = idType;
		}

		/**
		 * @param regex the regex to set
		 */
		public void setRegex(String regex) {
			this.regex = regex;
		}

		/**
		 * @return the idType
		 */
		public String getIdType() {
			return idType;
		}

		/**
		 * @return the regex
		 */
		public String getRegex() {
			return regex;
		}
	}

	/**
	 * @return the govtIdTypes
	 */
	public List<GovtIdType> getGovtIdTypes() {
		return govtIdTypes;
	}

	/**
	 * @param govtIdTypes the govtIdTypes to set
	 */
	public void setGovtIdTypes(List<GovtIdType> govtIdTypes) {
		this.govtIdTypes = govtIdTypes;
	}

}
