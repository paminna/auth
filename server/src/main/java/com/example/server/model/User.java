package com.example.server.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

//    private String encodedPass;

    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }

//    private Float rating;
//
//    private String role;
//
//    @OneToMany(mappedBy = "user")
//    private Set<Book> book;
//
//    @ManyToMany
//    @JoinTable(name = "userroles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles;
}
