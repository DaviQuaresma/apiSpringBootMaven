package com.movies.moviesApiRest.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @CreationTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;

    //Getters e Setters

    public String getFullname(){ return fullname; }
    public void setFullname(String fullname){ this.fullname = fullname; }

    public Integer getId(){ return id; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }

}
