
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
    "lng",
    "countryCode",
    "postalCode",
    "placeName",
    "lat"
})
public class PostalCode implements Serializable
{

    @JsonProperty("lng")
    private Double lng;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("placeName")
    private String placeName;
    @JsonProperty("lat")
    private Double lat;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6913498005549443345L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PostalCode() {
    }

    /**
     * 
     * @param postalCode
     * @param countryCode
     * @param placeName
     * @param lng
     * @param lat
     */
    public PostalCode(Double lng, String countryCode, String postalCode, String placeName, Double lat) {
        super();
        this.lng = lng;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.placeName = placeName;
        this.lat = lat;
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

    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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
        return new ToStringBuilder(this).append("lng", lng).append("countryCode", countryCode).append("postalCode", postalCode).append("placeName", placeName).append("lat", lat).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(postalCode).append(additionalProperties).append(countryCode).append(placeName).append(lng).append(lat).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PostalCode) == false) {
            return false;
        }
        PostalCode rhs = ((PostalCode) other);
        return new EqualsBuilder().append(postalCode, rhs.postalCode).append(additionalProperties, rhs.additionalProperties).append(countryCode, rhs.countryCode).append(placeName, rhs.placeName).append(lng, rhs.lng).append(lat, rhs.lat).isEquals();
    }

}
