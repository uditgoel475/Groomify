
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
 * Generated from http://www.jsonschema2pojo.org/
 * @author Konika
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "postalcodes"
})
public class StateToZip implements Serializable
{

    @JsonProperty("postalcodes")
    private List<Postalcode> postalcodes = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -7423466363046723131L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public StateToZip() {
    }

    /**
     * 
     * @param postalcodes
     */
    public StateToZip(List<Postalcode> postalcodes) {
        super();
        this.postalcodes = postalcodes;
    }

    @JsonProperty("postalcodes")
    public List<Postalcode> getPostalcodes() {
        return postalcodes;
    }

    @JsonProperty("postalcodes")
    public void setPostalcodes(List<Postalcode> postalcodes) {
        this.postalcodes = postalcodes;
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
        return new ToStringBuilder(this).append("postalcodes", postalcodes).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(postalcodes).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(postalcodes, rhs.postalcodes).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
