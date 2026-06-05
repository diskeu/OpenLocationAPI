package dev.timjelenz.openlocationapi.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing the Users table.
 */
@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    /**
     * Protected constructor for Hibernate.
     */
    protected User() { }
    
    /**
     * Constructor to initalize the columns directly.
     */
    public User(String userName, String userEmail, byte[] userPasswordHash) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPasswordHash = userPasswordHash;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(
        length = 64,
        unique = true,
        nullable = false
    )
    @Size(min = 4, max = 64)
    private String userName;

    @Column(
        length = 255,
        unique = true,
        nullable = false
    )
    @Email
    private String userEmail;

    @Column(nullable = false, length = 255)
    private byte[] userPasswordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserStarredLocation> userStarredLocations;
}
