package com.project.project.service;

import com.project.project.dto.UserDto;
import com.project.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {


      List<User> findAll() ;

     UserDto createUser(UserDto userDto);

     List<User> getUsers();
     User getUserById(int userid);

     User getUserByEmail(String email);

     User updateUser(User user,int userid);

     boolean deleteById(int userid);

     List<User> getByName(String name);

     Page<User> getPagenation(int pagenumber, int pagesize);

     List<User> findByProjectNameAndJobType(String projectName, String jobType);

     List<User> findByProjectName(String projectName);
      List<User> findByJobType(String jobType);
}
