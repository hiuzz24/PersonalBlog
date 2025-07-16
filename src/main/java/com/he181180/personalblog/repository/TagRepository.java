package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tags,Integer> {
}
