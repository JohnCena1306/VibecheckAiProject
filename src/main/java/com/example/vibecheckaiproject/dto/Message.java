package com.example.vibecheckaiproject.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Generated;

import java.util.LinkedHashMap;
import java.util.Map;

// Felter der er null kommer ikke med
@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "role",
        "content"
})
@Generated("jsonschema2pojo")
public class Message {

    @JsonProperty("role")
        private String role;
    @JsonProperty("content")
        private String content;

    // Ignorerer hvis ai sender felt som vi ikke genkender
    @JsonIgnore
        private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Message() {
    }

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
