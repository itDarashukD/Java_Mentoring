package com.example.mentoring.service;

import com.example.mentoring.repository.StudentRepository;
import com.example.mentoring.model.Address;
import com.example.mentoring.model.ExamResult;
import com.example.mentoring.model.Student;
import com.example.mentoring.model.Subject;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public void addStudent(Student student) {
        repository.addStudent(student);
    }

    public void addSubject(Subject subject) {
        repository.addSubject(subject);
    }

    public void addExamResult(ExamResult results) {
        repository.addExamResult(results);
    }

    public List<Student> findByName(String name) {
        return repository.findByName(name);
    }

    public List<Student> findBySurname(String sureName) {
        return repository.findBySurname(sureName);
    }

    public Student findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    public List<ExamResult> findExamResultBySurname(String surname) {
        return repository.findExamResultBySurname(surname);
    }

    public void addAddress(Address address) {
        repository.addAddress(address);
    }

    public Address getAddress(String studentSurname) {
        return repository.findAddressBySurname(studentSurname);
    }

    public void updateAddress(Address address) {
        repository.updateAddress(address);
    }

}
