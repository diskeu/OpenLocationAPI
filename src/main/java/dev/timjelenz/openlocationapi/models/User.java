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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(
        length = 64,
        unique = true,
        nullable = false
    )
    private String userName;

    @Column(
        length = 255,
        unique = true,
        nullable = false
    )
    private String userEmail;

    @Column(nullable = false)
    private byte[] userPasswordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserStarredLocation> userStarredLocations;
}
