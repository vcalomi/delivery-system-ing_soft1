package com.ing_software.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String name;
    private String lastname;
    private String email;
    private int age;
    private String address;
    private String gender;
}
