package com.project.project.repository;


import com.project.project.model.User;
import com.project.project.model.UserLoginLogout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLoginLogoutRepository extends JpaRepository<UserLoginLogout, Long> {

    List<UserLoginLogout> findByUserOrderByIdDesc(User user);
}
