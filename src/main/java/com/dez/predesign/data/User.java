package com.dez.predesign.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private String firstName;
    private String lastName;
    private String Email;

    private String password;
    private String password2;

    private String postCode;
    private String address;
    private String phoneNumber;
    //
    private String roles;

}
