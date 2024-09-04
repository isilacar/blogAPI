package com.scalefocus.blog_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TOKEN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}