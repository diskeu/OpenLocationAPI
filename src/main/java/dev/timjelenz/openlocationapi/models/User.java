package dev.timjelenz.openlocationapi.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity representing the Users table.
 */
@Getter
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    /**
     * Protected constructor for Hibernate.
     */
    protected User() { }

    /**
     * Constructor to initalize the columns directly.
     *
     * @param userName name of the user
     * @param userEmail email of the user
     * @param userPasswordHash hash of the user's password
     */
    public User(
        final String userName,
        final String userEmail,
        final String userPasswordHash
    ) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPasswordHash = userPasswordHash;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @Column(
        length = 64,
        unique = true,
        nullable = false
    )
    @Size(min = 4, max = 64)
    private String userName;

    @Setter
    @Column(
        length = 255,
        unique = true,
        nullable = false
    )
    @Email
    private String userEmail;

    @Setter
    @Column(nullable = false, length = 255)
    private String userPasswordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserStarredLocation> userStarredLocations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return userPasswordHash;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
