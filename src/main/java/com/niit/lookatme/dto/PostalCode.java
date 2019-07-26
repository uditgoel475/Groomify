
package com.niit.lookatme.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Generated from http://www.jsonschema2pojo.org/
 * @author Konika
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "adminCode2",
    "adminName3",
    "adminCode1",
    "adminName2",
    "lng",
    "countryCode",
    "postalcode",
    "adminName1",
    "placeName",
    "lat"
})
public class Postalcode implements Serializable
{

    @JsonProperty("adminCode2")
    private String adminCode2;
    @JsonProperty("adminName3")
    private String adminName3;
    @JsonProperty("adminCode1")
    private String adminCode1;
    @JsonProperty("adminName2")
    private String adminName2;
    @JsonProperty("lng")
    private Double lng;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("postalcode")
    private String postalcode;
    @JsonProperty("adminName1")
    private String adminName1;
    @JsonProperty("placeName")
    private String placeName;
    @JsonProperty("lat")
    private Double lat;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -8453802235383550889L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Postalcode() {
    }

    /**
     * 
     * @param adminName2
     * @param postalcode
     * @param adminName3
     * @param adminCode2
     * @param adminCode1
     * @param countryCode
     * @param placeName
     * @param lng
     * @param lat
     * @param adminName1
     */
    public Postalcode(String adminCode2, String adminName3, String adminCode1, String adminName2, Double lng, String countryCode, String postalcode, String adminName1, String placeName, Double lat) {
        super();
        this.adminCode2 = adminCode2;
        this.adminName3 = adminName3;
        this.adminCode1 = adminCode1;
        this.adminName2 = adminName2;
        this.lng = lng;
        this.countryCode = countryCode;
        this.postalcode = postalcode;
        this.adminName1 = adminName1;
        this.placeName = placeName;
        this.lat = lat;
    }

    @JsonProperty("adminCode2")
    public String getAdminCode2() {
        return adminCode2;
    }

    @JsonProperty("adminCode2")
    public void setAdminCode2(String adminCode2) {
        this.adminCode2 = adminCode2;
    }

    @JsonProperty("adminName3")
    public String getAdminName3() {
        return adminName3;
    }

    @JsonProperty("adminName3")
    public void setAdminName3(String adminName3) {
        this.adminName3 = adminName3;
    }

    @JsonProperty("adminCode1")
    public String getAdminCode1() {
        return adminCode1;
    }

    @JsonProperty("adminCode1")
    public void setAdminCode1(String adminCode1) {
        this.adminCode1 = adminCode1;
    }

    @JsonProperty("adminName2")
    public String getAdminName2() {
        return adminName2;
    }

    @JsonProperty("adminName2")
    public void setAdminName2(String adminName2) {
        this.adminName2 = adminName2;
    }

    @JsonProperty("lng")
    public Double getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(Double lng) {
        this.lng = lng;
    }

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("postalcode")
    public String getPostalcode() {
        return postalcode;
    }

    @JsonProperty("postalcode")
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    @JsonProperty("adminName1")
    public String getAdminName1() {
        return adminName1;
    }

    @JsonProperty("adminName1")
    public void setAdminName1(String adminName1) {
        this.adminName1 = adminName1;
    }

    @JsonProperty("placeName")
    public String getPlaceName() {
        return placeName;
    }

    @JsonProperty("placeName")
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("adminCode2", adminCode2).append("adminName3", adminName3).append("adminCode1", adminCode1).append("adminName2", adminName2).append("lng", lng).append("countryCode", countryCode).append("postalcode", postalcode).append("adminName1", adminName1).append("placeName", placeName).append("lat", lat).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(adminName2).append(postalcode).append(adminName3).append(adminCode2).append(adminCode1).append(additionalProperties).append(countryCode).append(placeName).append(lng).append(lat).append(adminName1).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Postalcode) == false) {
            return false;
        }
        Postalcode rhs = ((Postalcode) other);
        return new EqualsBuilder().append(adminName2, rhs.adminName2).append(postalcode, rhs.postalcode).append(adminName3, rhs.adminName3).append(adminCode2, rhs.adminCode2).append(adminCode1, rhs.adminCode1).append(additionalProperties, rhs.additionalProperties).append(countryCode, rhs.countryCode).append(placeName, rhs.placeName).append(lng, rhs.lng).append(lat, rhs.lat).append(adminName1, rhs.adminName1).isEquals();
    }

}
