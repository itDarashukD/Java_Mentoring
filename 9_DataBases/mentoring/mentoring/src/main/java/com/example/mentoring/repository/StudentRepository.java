package com.example.mentoring.repository;

import com.example.mentoring.model.Address;
import com.example.mentoring.model.ExamResult;
import com.example.mentoring.model.Student;
import com.example.mentoring.model.Subject;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

    @Insert("INSERT INTO student(name, surname, phone_number, primary_skill, date_of_birth ) VALUES (#{name},#{surName},#{phoneNumber},#{primarySkill},#{dateOfBirth} )")
    void addStudent(Student student);

    @Insert("INSERT INTO subjects (subject_name, tutor_name) VALUES (#{subjectName},#{tutorName})")
    void addSubject(Subject subject);

    @Insert("INSERT INTO exam_result(student_surname, subject, mark) VALUES (#{studentSurname},#{subject},#{mark})")
    void addExamResult(ExamResult results);

    @Result(column = "phone_number", property = "phoneNumber")
    @Result(column = "date_of_birth", property = "dateOfBirth")
    @Result(column = "primary_skill", property = "primarySkill")
    @Select("SELECT * FROM student WHERE name = #{name}")
    List<Student> findByName(@Param("name") String name);

    @Select("SELECT * FROM student WHERE surname = #{surname}")
    List<Student> findBySurname(@Param("surname") String surName);

    @Select("SELECT * FROM student WHERE phone_number = #{phoneNumber}")
    Student findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Result(column = "surname", property = "studentSurname")
    @Select("SELECT * FROM exam_result WHERE student.surname=#{surname}")
    List<ExamResult> findExamResultBySurname(String surname);

    @Insert("INSERT INTO student_address (student_surname, address) VALUES (#{studentSurname},#{address})")
    void addAddress(Address address);

    @Result(column = "student_surname", property = "studentSurname")
    @Select("SELECT * FROM student_address WHERE student_surname = #{studentSurname}")
    Address findAddressBySurname(String studentSurname);

    @Update("UPDATE student_address SET address=#{address} VALUES student_surname=#{studentSurname}")
    void updateAddress(Address address);

}

