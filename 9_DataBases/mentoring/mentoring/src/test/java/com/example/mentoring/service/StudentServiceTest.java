package com.example.mentoring.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mentoring.TestSupporter;
import com.example.mentoring.repository.StudentRepository;
import com.example.mentoring.model.Address;
import com.example.mentoring.model.ExamResult;
import com.example.mentoring.model.Student;
import com.example.mentoring.model.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("tests for StudentService class")
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest extends TestSupporter {

    @InjectMocks
    private StudentService service;
    @Mock
    private StudentRepository repository;


    @DisplayName("addStudent(), check if method addStudent() in StudentRepository invoked")
    @Test
    public void addStudent_dummyStudent_addStudentForStudentRepositoryInvoked() {
        doNothing().when(repository).addStudent(any(Student.class));

        service.addStudent(studentWithDummyName);

        verify(repository, times(1)).addStudent(studentWithDummyName);
    }

    @DisplayName("addSubject(), check if method addSubject() in StudentRepository invoked")
    @Test
    public void addSubject_dummySubject_addSubjectForStudentRepositoryInvoked() {
        doNothing().when(repository).addSubject(any(Subject.class));

        service.addSubject(subjectWithDummyName);

        verify(repository, times(1)).addSubject(subjectWithDummyName);
    }

    @DisplayName("addExamResult(), check if method addExamResult() in StudentRepository invoked")
    @Test
    public void addExamResult_dummyExamResult_addExamResultForStudentRepositoryInvoked() {
        doNothing().when(repository).addExamResult(any(ExamResult.class));

        service.addExamResult(resultWithDummySubject);

        verify(repository, times(1)).addExamResult(resultWithDummySubject);
    }

    @DisplayName("findByName(), check if method findByName() in StudentRepository invoked")
    @Test
    public void findByName_fakeName_FindByNameForStudentRepositoryInvoked() {
        String fakeName = "fakeName";
        when(repository.findByName(anyString())).thenReturn(studentListWithDummyStudent);

        service.findByName(fakeName);

        verify(repository, times(1)).findByName(fakeName);
    }

    @DisplayName("findBySurname(), check if method findBySurname() in StudentRepository invoked")
    @Test
    public void findBySurname_fakeSurName_findBySurnameForStudentRepositoryInvoked() {
        when(repository.findBySurname(anyString())).thenReturn(studentListWithDummyStudent);

        service.findBySurname(FAKE_SURNAME);

        verify(repository, times(1)).findBySurname(FAKE_SURNAME);
    }

    @DisplayName("findByPhoneNumber(), check if method findByPhoneNumber() in StudentRepository invoked")
    @Test
    public void findByPhoneNumber_fakePhoneNumber_findByPhoneNumberForStudentRepositoryInvoked() {
        String fakePhoneNumber = "1111111";
        when(repository.findByPhoneNumber(anyString())).thenReturn(studentWithDummyName);

        service.findByPhoneNumber(fakePhoneNumber);

        verify(repository, times(1)).findByPhoneNumber(fakePhoneNumber);
    }

    @DisplayName("findStudentWithMarksBySureName(), check if method findStudentWithMarksBySureName() in StudentRepository invoked")
    @Test
    public void findStudentWithMarksBySureName_fakeSurName_findStudentWithMarksBySureNameForStudentRepositoryInvoked() {
        when(repository.findExamResultBySurname(anyString())).thenReturn(examResultListWithDummyExam);

        service.findExamResultBySurname(FAKE_SURNAME);

        verify(repository, times(1)).findExamResultBySurname(FAKE_SURNAME);
    }

    @DisplayName("addAddress(), check if method addAddress() in StudentRepository invoked")
    @Test
    public void addAddress_dummyAddress_addAddressForStudentRepositoryInvoked() {
        doNothing().when(repository).addAddress(any(Address.class));

        service.addAddress(addressWithDummySurName);

        verify(repository, times(1)).addAddress(addressWithDummySurName);
    }

    @DisplayName("getAddress(), check if method getAddress() in StudentRepository invoked")
    @Test
    public void getAddress_fakeSurName_getAddressForStudentRepositoryInvoked() {
        when(repository.findAddressBySurname(anyString())).thenReturn(addressWithDummySurName);

        service.getAddress(FAKE_SURNAME);

        verify(repository, times(1)).findAddressBySurname(FAKE_SURNAME);
    }

    @DisplayName("updateAddress(), check if method updateAddress() in StudentRepository invoked")
    @Test
    public void updateAddress_dummyAddress_updateAddressForStudentRepositoryInvoked() {
        doNothing().when(repository).updateAddress(any(Address.class));

        service.updateAddress(addressWithDummySurName);

        verify(repository, times(1)).updateAddress(addressWithDummySurName);
    }


}