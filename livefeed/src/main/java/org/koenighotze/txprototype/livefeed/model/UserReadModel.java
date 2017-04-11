package org.koenighotze.txprototype.livefeed.model;

import java.time.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserReadModel {
    @Size(min = 1, max = 40)
    @NotNull
    private String publicId;

    @Size(min = 1, max = 20)
    @NotNull
    private String username;

    @NotNull
    private LocalDateTime createdTimestamp;

    public UserReadModel(@JsonProperty("publicId") String publicId, @JsonProperty("username") String username) {
        this.publicId = publicId;
        this.username = username;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public String toString() {
        return "UserReadModel{" + "publicId='" + publicId + '\'' + ", username='" + username + '\'' + '}';
    }
}
