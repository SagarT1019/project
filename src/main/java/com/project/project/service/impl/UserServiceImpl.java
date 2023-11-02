package com.project.project.service.impl;

import com.project.project.dto.UserDto;
import com.project.project.exception.BadCredentialException;
import com.project.project.exception.EmptyListException;
import com.project.project.exception.ValidationException;
import com.project.project.model.User;
import com.project.project.repository.UserRepository;
import com.project.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto createUser(UserDto userDto){
        if(userDto.getName().isEmpty()||userDto.getName().length()==0){
            throw new BadCredentialException("501","please enter the name");
        }
        if(userDto.getGender().isEmpty()||userDto.getGender().length()== 0){
            throw new BadCredentialException("502","please enter the proper gender");
        }
        if(userDto.getEmail().isEmpty()){
            throw new BadCredentialException("503","please enter the proper email");
        }
        if(userDto.getDate_of_brith().isEmpty()||userDto.getDate_of_brith().length()==0){
            throw new BadCredentialException("503","please enter the proper Date of Birth");
        }
        if(userDto.getAddress().isEmpty()||userDto.getAddress().length()==0){
            throw new BadCredentialException("504","please enter the proper Address");
        }

        List<User> existingUser = userRepository.findUserByEmail(userDto.getEmail());
        if (!existingUser.isEmpty()) {
            throw new ValidationException("User already exists with the provided email");
        }
        userDto.setActive(true);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }
    public List<User> getUsers(){
        List<User> list = userRepository.findAll();
        if(list.isEmpty()){
            throw new EmptyListException("506","there is nothing to return");
        }
        return list;
    }

    @Override
    public User getUserById(int userid){
        return userRepository.findById(userid).get();
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User Not Found By This Email"));
    }

    @Override
    public User updateUser(User user,int userid){
        User existingUser = userRepository.findById(userid).orElseThrow(()->new RuntimeException("User Not Found"));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setMobile_no(user.getMobile_no());
        existingUser.setGender(user.getGender());
        existingUser.setDate_of_brith(user.getDate_of_brith());
        existingUser.setJobType(user.getJobType());
        existingUser.setProjectName(user.getProjectName());
        existingUser.setRole(user.getRole());

        return  userRepository.save(existingUser);
    }

    @Override
    public boolean deleteById(int userid) {
        Optional<User> user = userRepository.findById(userid);

        if (user.isPresent()) {

            User user1 = user.get();
            userRepository.save(user1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> getByName(String name){
        return userRepository.findByName(name);
    }

    @Override
    public Page<User> getPagenation(int pagenumber, int pagesize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findByProjectNameAndJobType(String projectName, String jobType) {
        return userRepository.findByProjectNameAndJobType(projectName, jobType);
    }

    @Override
    public List<User> findByProjectName(String projectName) {
        return userRepository.findByProjectName(projectName);
    }

    @Override
    public List<User> findByJobType(String jobType) {
        return userRepository.findByJobType(jobType);
    }

}
