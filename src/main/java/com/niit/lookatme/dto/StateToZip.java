
package com.niit.lookatme.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
    "postalCodes"
})
public class StateToZip implements Serializable
{

    @JsonProperty("postalCodes")
    private List<PostalCode> postalCodes = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 1279617222251387866L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public StateToZip() {
    }

    /**
     * 
     * @param postalCodes
     */
    public StateToZip(List<PostalCode> postalCodes) {
        super();
        this.postalCodes = postalCodes;
    }

    @JsonProperty("postalCodes")
    public List<PostalCode> getPostalCodes() {
        return postalCodes;
    }

    @JsonProperty("postalCodes")
    public void setPostalCodes(List<PostalCode> postalCodes) {
        this.postalCodes = postalCodes;
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
        return new ToStringBuilder(this).append("postalCodes", postalCodes).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(postalCodes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StateToZip) == false) {
            return false;
        }
        StateToZip rhs = ((StateToZip) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(postalCodes, rhs.postalCodes).isEquals();
    }

}
