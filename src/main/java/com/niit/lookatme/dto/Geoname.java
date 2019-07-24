
package com.niit.lookatme.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lng",
    "geonameId",
    "countryCode",
    "name",
    "toponymName",
    "lat",
    "fcl",
    "fcode"
})
public class Geoname implements Serializable
{

    @JsonProperty("lng")
    private String lng;
    @JsonProperty("geonameId")
    private Integer geonameId;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("toponymName")
    private String toponymName;
    @JsonProperty("lat")
    private String lat;
    @JsonProperty("fcl")
    private String fcl;
    @JsonProperty("fcode")
    private String fcode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5859730565572515438L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Geoname() {
    }

    /**
     * 
     * @param toponymName
     * @param fcl
     * @param name
     * @param countryCode
     * @param lng
     * @param fcode
     * @param geonameId
     * @param lat
     */
    public Geoname(String lng, Integer geonameId, String countryCode, String name, String toponymName, String lat, String fcl, String fcode) {
        super();
        this.lng = lng;
        this.geonameId = geonameId;
        this.countryCode = countryCode;
        this.name = name;
        this.toponymName = toponymName;
        this.lat = lat;
        this.fcl = fcl;
        this.fcode = fcode;
    }

    @JsonProperty("lng")
    public String getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(String lng) {
        this.lng = lng;
    }

    @JsonProperty("geonameId")
    public Integer getGeonameId() {
        return geonameId;
    }

    @JsonProperty("geonameId")
    public void setGeonameId(Integer geonameId) {
        this.geonameId = geonameId;
    }

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("toponymName")
    public String getToponymName() {
        return toponymName;
    }

    @JsonProperty("toponymName")
    public void setToponymName(String toponymName) {
        this.toponymName = toponymName;
    }

    @JsonProperty("lat")
    public String getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(String lat) {
        this.lat = lat;
    }

    @JsonProperty("fcl")
    public String getFcl() {
        return fcl;
    }

    @JsonProperty("fcl")
    public void setFcl(String fcl) {
        this.fcl = fcl;
    }

    @JsonProperty("fcode")
    public String getFcode() {
        return fcode;
    }

    @JsonProperty("fcode")
    public void setFcode(String fcode) {
        this.fcode = fcode;
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
        return new ToStringBuilder(this).append("lng", lng).append("geonameId", geonameId).append("countryCode", countryCode).append("name", name).append("toponymName", toponymName).append("lat", lat).append("fcl", fcl).append("fcode", fcode).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(toponymName).append(fcl).append(additionalProperties).append(name).append(countryCode).append(lng).append(fcode).append(geonameId).append(lat).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Geoname) == false) {
            return false;
        }
        Geoname rhs = ((Geoname) other);
        return new EqualsBuilder().append(toponymName, rhs.toponymName).append(fcl, rhs.fcl).append(additionalProperties, rhs.additionalProperties).append(name, rhs.name).append(countryCode, rhs.countryCode).append(lng, rhs.lng).append(fcode, rhs.fcode).append(geonameId, rhs.geonameId).append(lat, rhs.lat).isEquals();
    }

}
