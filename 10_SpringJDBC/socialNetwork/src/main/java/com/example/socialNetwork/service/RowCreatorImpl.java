package com.example.socialNetwork.service;

import com.example.socialNetwork.repository.SocialRepositoryImpl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RowCreatorImpl extends CreationSupporter implements RowCreator {

    private final static String COLUMN_NAMES_AND_TYPES = "([a-zA-Z_\\s]+)";
    private final static Integer[] rowsCount = {0, 1, 2, 3, 4, 5, 6, 7};
    private final static String NOT_IN_BRACKETS = "(.*\\()";


    @Autowired
    private SocialRepositoryImpl repository;

    @Override
    public List<Map<String, Object>> createRows(int desireRowCount,
	   int desireNumberOfTableToInserts,
	   List<String> tablesList) {
        if (desireNumberOfTableToInserts >= tablesList.size()) {
	   throw new IllegalArgumentException(
		  "desireNumberOfTableToInserts has to be fewer than tablesList.size()");
        }

        String script = tablesList.get(desireNumberOfTableToInserts);

        Integer rowCountToFilling = getRowCount(desireRowCount);

        String tableName = getTableName(script);

        List<String> columAndTypeNames = getColumAndTypesName(script);

        Map<String, String> columnTypeMap = getColumnTypeMap(columAndTypeNames);

        List<Map<String, Object>> listMapsToInsert = prepareMapForInsertingToDB(columnTypeMap,
	       rowCountToFilling);

        insertIntoRandomTable(tableName, listMapsToInsert);

        return listMapsToInsert;
    }

    private Integer getRowCount(int desireRowCount) {
        return rowsCount[desireRowCount];
    }

    private String getTableName(String script) {
        String withoutPunctuation = script.replaceAll("\\p{Punct}", " ");

        return tableNames.values()
	       .stream()
	       .filter(name -> withoutPunctuation.contains(name))
	       .findAny()
	       .orElseThrow(IllegalArgumentException::new);
    }

    private List<String> getColumAndTypesName(String script) {
        List<String> matchers = new ArrayList<>();
        String onlyInBrackets = script.replaceAll(NOT_IN_BRACKETS, " ");

        Pattern pattern = Pattern.compile(COLUMN_NAMES_AND_TYPES);
        Matcher matcher = pattern.matcher(onlyInBrackets);
        while (matcher.find()) {
	   String group = matcher.group();
	   matchers.add(group.trim());
        }
        return matchers;
    }

    private Map<String, String> getColumnTypeMap(List<String> columns) {
        return columns.stream()
	       .map(string -> string.split(" ", 2))
	       .collect(Collectors.toMap(k -> k[0], v -> v[1]));
    }

    private List<Map<String, Object>> prepareMapForInsertingToDB(Map<String, String> columnTypeMap,
	   Integer rowCountToFilling) {
        List<Map<String, Object>> listMapsToInsert = new ArrayList<>();

        for (int i = 0; i < rowCountToFilling; i++) {
	   Map<String, Object> mapToInsert = new HashMap<>();

	   columnTypeMap.entrySet().stream().forEach(entry -> {
	       switch (entry.getValue()) {
		  case "text":
		      mapToInsert.put(entry.getKey(), getRandomText());
		      break;
		  case "varchar":
		      mapToInsert.put(entry.getKey(), getRandomText());
		      break;
		  case "bigint":
		      mapToInsert.put(entry.getKey(), String.valueOf(getRandomInt(1000)));
		      break;
		  case "smallInt":
		      mapToInsert.put(entry.getKey(), String.valueOf(getRandomInt(10)));
		      break;
		  case "timestamp without time zone":
		      mapToInsert.put(entry.getKey(), String.valueOf(getRandomTimestamp()));
		      break;
		  default:
		      log.info(String.format("Denied name of type : %s", entry.getValue()));
	       }
	   });
	   listMapsToInsert.add(mapToInsert);
        }
        return listMapsToInsert;
    }

    private String getRandomText() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private int getRandomInt(int randomCount) {
        return new Random().ints(1, randomCount + 1).findAny().getAsInt();
    }

    private Timestamp getRandomTimestamp() {
        long offset = Timestamp.valueOf("2025-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2026-01-01 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp random = new Timestamp(offset + (long) (Math.random() * diff));

        return random;
    }

    private void insertIntoRandomTable(String tableName,
	   List<Map<String, Object>> listMapsToInsert) {
        repository.insertInitialDataToRandomTable(tableName, listMapsToInsert);
    }


}
