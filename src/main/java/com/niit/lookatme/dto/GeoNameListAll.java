
package com.niit.lookatme.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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

/**
 * 
 * @author Konika
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totalResultsCount",
    "geonames"
})
public class GeoNameListAll implements Serializable
{

    @JsonProperty("totalResultsCount")
    private Integer totalResultsCount;
    @JsonProperty("geonames")
    private List<Geoname> geonames = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    private static final long serialVersionUID = -1988924369127589513L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GeoNameListAll() {
    }

    /**
     * 
     * @param totalResultsCount
     * @param geonames
     */
    public GeoNameListAll(Integer totalResultsCount, List<Geoname> geonames) {
        super();
        this.totalResultsCount = totalResultsCount;
        this.geonames = geonames;
    }

    @JsonProperty("totalResultsCount")
    public Integer getTotalResultsCount() {
        return totalResultsCount;
    }

    @JsonProperty("totalResultsCount")
    public void setTotalResultsCount(Integer totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    @JsonProperty("geonames")
    public List<Geoname> getGeonames() {
        return geonames;
    }

    @JsonProperty("geonames")
    public void setGeonames(List<Geoname> geonames) {
        this.geonames = geonames;
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
        return new ToStringBuilder(this).append("totalResultsCount", totalResultsCount).append("geonames", geonames).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalResultsCount).append(additionalProperties).append(geonames).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GeoNameListAll) == false) {
            return false;
        }
        GeoNameListAll rhs = ((GeoNameListAll) other);
        return new EqualsBuilder().append(totalResultsCount, rhs.totalResultsCount).append(additionalProperties, rhs.additionalProperties).append(geonames, rhs.geonames).isEquals();
    }

}
