package com.scalefocus.blog_api;

import com.scalefocus.blog_api.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogTestH2Repository extends JpaRepository<Blog,Long> {
}
