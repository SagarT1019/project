package com.project.project.repository;


import com.project.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    public Optional<User> findByEmail(String email);

    List<User> findUserByEmail(String email);

    List<User> findByName(String name);

    List<User> findByProjectName(String projectName);

    List<User> findByJobType(String jobType );

    @Query("SELECT user FROM User user " +
            "WHERE user.projectName = :projectName AND user.jobType = :jobType ")
    List<User> findByProjectNameAndJobType(
            @Param("projectName") String projectName,
            @Param("jobType") String jobType);

}


