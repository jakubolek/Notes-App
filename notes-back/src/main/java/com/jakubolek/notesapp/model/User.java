package com.jakubolek.notesapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    public void addRole(Role role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }
}
