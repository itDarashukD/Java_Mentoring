package com.example.socialNetwork.service;

import java.util.List;
import java.util.Map;

public interface RowCreator {

    List<Map<String, Object>> createRows(int desireRowCount,
	   int desireNumberOfTableToInserts,
	   List<String> tables);

}
