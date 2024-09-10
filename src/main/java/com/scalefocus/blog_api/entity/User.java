package com.scalefocus.blog_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String displayName;

    @OneToMany(mappedBy = "user")
    private List<Token> tokenList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Blog> blogList = new HashSet<>();
}