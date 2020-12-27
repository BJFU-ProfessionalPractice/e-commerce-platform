package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "`shopuser`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Integer userId;

    private String username;

    private String password;

}
