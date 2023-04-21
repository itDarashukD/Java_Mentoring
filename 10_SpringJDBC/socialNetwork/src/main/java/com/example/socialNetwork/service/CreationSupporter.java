package com.example.socialNetwork.service;

import java.util.HashMap;
import java.util.Map;

public abstract class CreationSupporter {

    protected static Map<Integer, String> tableNames = new HashMap<>() {{
        put(1, "firstTable");
        put(2, "secondTable");
        put(3, "thirdTable");
        put(4, "fourthTable");
        put(5, "fiveTable");
        put(6, "sixTable");
    }};
    protected static Map<Integer, String> columnNames = new HashMap<>() {{
        put(1, "name");
        put(2, "surname");
        put(3, "id");
        put(4, "birthdate");
        put(5, "phone_number");
        put(6, "address");
    }};
    protected static Map<Integer, String> types = new HashMap<>() {{
        put(1, "text");
        put(2, "timestamp without time zone");
        put(3, "bigint");
        put(4, "varchar");
        put(5, "smallInt");
    }};

}
