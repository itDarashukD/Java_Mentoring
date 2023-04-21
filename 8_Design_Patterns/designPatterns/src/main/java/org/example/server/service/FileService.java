package org.example.server.service;


public interface FileService<T> {

    T readSettings(String url, Class<T> targetClass);

}
