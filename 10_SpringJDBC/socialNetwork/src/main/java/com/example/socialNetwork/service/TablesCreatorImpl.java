package com.example.socialNetwork.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Service;


@Service
public class TablesCreatorImpl extends CreationSupporter implements TablesCreator {

    private static final String CREATE_TABLE_TEMPLATE = "CREATE TABLE IF NOT EXISTS public.";

    @Override
    public List<String> createTable(int tableCount, int columnsCount, int typeCount) {
        List<String> listOfTables = new ArrayList<>();
        String sqlTable = "";

        List<String> tableNames = getRandomTableNames(tableCount);

        for (int i = 0; i < tableCount; i++) {
	   List<String> columns = getRandomColumns(columnsCount);

	   sqlTable = buildTableScript(tableNames, columns, typeCount);
	   listOfTables.add(sqlTable);
        }
        return listOfTables;
    }

    private String buildTableScript(List<String> tableNames, List<String> columns, int typeCount) {
        int extraPunctuation = 2;
        StringBuilder builder = new StringBuilder();
        String tableName = getNextTableName(tableNames);

        builder.append(CREATE_TABLE_TEMPLATE).append(tableName).append("( ");

        for (String column : columns) {
	   builder.append(column).append(" ").append(getRandomType(typeCount)).append(", ");
        }
        String script = builder.substring(0, builder.length() - extraPunctuation);

        return script + " );";
    }

    private String getNextTableName(List<String> tableNames) {
        String tableName = tableNames.stream()
	       .findFirst()
	       .orElseThrow(IllegalArgumentException::new);

        tableNames.remove(tableName);

        return tableName;
    }

    private List<String> getRandomColumns(int columnsCount) {
        Set<String> columnNamesToTable = new HashSet<>();

        if (columnsCount > columnNames.size()) {
	   throw new IllegalArgumentException();
        }

        while (columnNamesToTable.size() != columnsCount) {
	   int randomColumnNumber = getRandomInt(columnsCount);

	   String columnName = columnNames.get(randomColumnNumber);
	   columnNamesToTable.add(columnName);
        }
        return new ArrayList<>(columnNamesToTable);
    }

    private String getRandomType(int typeCount) {
        if (typeCount > types.size()) {
	   throw new IllegalArgumentException();
        }
        int randomTypeNumber = getRandomInt(typeCount);

        return types.get(randomTypeNumber);
    }


    private List<String> getRandomTableNames(int tableCount) {
        Set<String> tableNamesToTable = new HashSet<>();

        if (tableCount > tableNames.size()) {
	   throw new IllegalArgumentException();
        }

        while (tableNamesToTable.size() != tableCount) {
	   int randomTableNumber = getRandomInt(tableCount);

	   String tableName = tableNames.get(randomTableNumber);
	   tableNamesToTable.add(tableName);
        }
        return new ArrayList<>(tableNamesToTable);
    }

    private int getRandomInt(int randomCount) {
        return new Random().ints(1, randomCount + 1).findAny().getAsInt();
    }

}
