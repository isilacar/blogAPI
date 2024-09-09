package com.scalefocus.blog_api;

import com.scalefocus.blog_api.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenTestH2Repository extends JpaRepository<Token, Long> {
}
