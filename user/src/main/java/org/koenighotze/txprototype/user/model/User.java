package org.koenighotze.txprototype.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents a user.
 *
 * @author David Schmitz
 */
public class User {
    @Id
    @JsonIgnore
    private String userId;

    @Size(min = 1, max = 40)
    @NotNull
    private String publicId;

    @Size(min = 1, max = 20)
    @NotNull
    private String firstname;

    @Size(min = 1, max = 20)
    @NotNull
    private String lastname;

    @Size(min = 1, max = 20)
    @NotNull
    private String username;

    @Size(min = 1, max = 50)
    @NotNull
    private String email;

    public User() {
    }

    public User(String publicId, String firstname, String lastname, String username, String email) {
        this.publicId = publicId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", userId)
                .append("publicId", publicId)
                .append("username", username)
                .append("email", email)
                .append("firstname", firstname)
                .append("lastname", lastname)
                .toString();
    }
}
