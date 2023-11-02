package com.project.project.entity;

import com.project.project.model.Role;
import com.project.project.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponce {

    private String jwtToken;
    private String userName;
    private Role role;

}
