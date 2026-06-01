package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a User entity in the application with fields for id, username,
 * password, full name, and role. This class is mapped to the "users" table
 * in the database.
 * Each field is associated with specific constraints and mapped to corresponding
 * table columns
 * An all-args constructor is provided to initialize a User object with all fields,
 * and a no-args constructor is available for use in frameworks or tools that
 * require a default constructor.
 */
@Entity
@Table(name = "users")
public class User {

    public interface OnCreate {}
    public interface OnUpdate {}

    public User(int id, String username, String password, String fullname, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    public User() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Column(name = "username", length = 125, nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is mandatory", groups = OnCreate.class)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
            groups = OnCreate.class,
            message = "Password must contain at least one uppercase letter, one number and one special character"
    )
    @Pattern(
            regexp = "^$|(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
            groups = OnUpdate.class,
            message = "Password must contain at least one uppercase letter, one number and one special character"
    )
    @Column(name = "password", length = 125)
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @Column(name = "fullname", length = 125)
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Column(name = "role", length = 125)
    private String role;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
