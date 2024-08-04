package org.example.pccwtest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link UserDetails} for Spring Security.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginUser implements UserDetails {

    /**
     * The user associated with this {@link LoginUser}.
     */
    User user;

    /**
     * The time when the user logged in.
     * Formatted as "yyyy-MM-dd HH:mm:ss".
     */
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    LocalDateTime loginTime;

    /**
     * Constructor to initialize the {@link LoginUser} with a {@link User}.
     *
     * @param user the {@link User} object to associate with this {@link LoginUser}
     */
    public LoginUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
