import org.example.serviceApi.Service;
import org.example.service.impl.ServiceImpl;
import org.example.service.repository.BankCardDao;
import org.example.service.repository.BankCardDaoMapImpl;

module jmp_cloud_service_impl {
    requires hsqldb;
    requires jmp_dto;
    requires java.sql;
    requires jmp_service_api;

    exports org.example.service.impl;
    exports org.example.service.repository;

    provides Service with ServiceImpl;
    provides BankCardDao with BankCardDaoMapImpl;

}