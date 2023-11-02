package com.project.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_Details")
@ToString
@Entity
//@SQLDelete(sql = "UPDATE User SET deleted = true WHERE id = ?")
//@Where(clause = "deleted = false")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;
    @Column(name = "name")
    private String name;
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+com)$", message = "Invalid email format")
    @NonNull
    @Column(unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "mobile no")
    private long  mobile_no;
    @Column(name = "date of brith")
    private String date_of_brith;
    @Column(name = "gender")
    private String gender;
    @Column(name = "address")
    private String address;
    @Column(name = "jobType")
    private String jobType;
    @Column(name = "projectName")
    private String projectName;


    @Column(name = "Active")
    private boolean deleted = true;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLoginLogout> userLoginLogouts;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
