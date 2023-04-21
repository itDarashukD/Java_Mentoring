package com.example.socialNetwork.service;

import java.util.List;

public interface TablesCreator {

    List<String> createTable(int tableCount, int columnsCount, int typeCount);

}
