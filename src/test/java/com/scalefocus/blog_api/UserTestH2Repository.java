package com.scalefocus.blog_api;

import com.scalefocus.blog_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTestH2Repository extends JpaRepository<User, Long> {
}