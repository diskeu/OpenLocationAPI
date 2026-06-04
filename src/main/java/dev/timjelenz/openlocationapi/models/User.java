package dev.timjelenz.openlocationapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing the Users table
 */
@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(
        name = "UserName",
        length=64,
        unique=true,
        nullable=false
    )
    private String userName;

    @Column(
        name= "UserEmail",
        length=255,
        unique=true,
        nullable=false
    )
    private String userEmail;

    @Column(
        name = "UserPasswordHash",
        nullable=false
    )
    private byte[] userPasswordHash;
}