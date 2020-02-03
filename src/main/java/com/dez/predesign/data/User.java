package com.dez.predesign.data;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Entity
@Table(name="usr")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message="Username is required")
    private String username;
    private String firstName;
    private String lastName;

    @Email(message = "Incorrect email")
    @NotBlank(message="Email is required")
    private String email;

    @NotBlank(message="Password is required")
    private String password;

    private String postCode;
    private String address;

    @NotBlank(message="Phone number is required")
    private String phoneNumber;

    private String activationCode;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    private boolean active;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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
        return isActive();
    }
}
