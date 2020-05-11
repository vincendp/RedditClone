package com.vincendp.RedditClone.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "UserAuthentication")
public class UserAuthentication {

    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    private User user;

    public UserAuthentication() {
    }

    public UserAuthentication(UUID user_id, String password, User user) {
        this.user_id = user_id;
        this.password = password;
        this.user = user;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
