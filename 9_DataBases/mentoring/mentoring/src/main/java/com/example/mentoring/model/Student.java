package com.example.mentoring.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Student {

    private String name;
    private String surName;
    private String primarySkill;
    private String phoneNumber;
    private LocalDate dateOfBirth;

}
