package com.scalefocus.blog_api.repository;


import com.scalefocus.blog_api.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {

}
