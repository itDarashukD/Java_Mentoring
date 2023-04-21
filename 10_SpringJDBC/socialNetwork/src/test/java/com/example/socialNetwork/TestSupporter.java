package com.example.socialNetwork;

import com.example.socialNetwork.model.User;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class TestSupporter {


    protected static final User user111 = new User() {{
        setName("fakeName111");
        setSurname("fakeSurName111");
        setBirthDate(Date.valueOf("2011-01-01"));
    }};

    protected static final User user222 = new User() {{
        setName("fakeName222");
        setSurname("fakeSurName222");
        setBirthDate(Date.valueOf("2022-02-02"));
    }};

    protected static final User user222ToUpdate = new User() {{
        setName("updatedName");
        setSurname("fakeSurName222");
        setBirthDate(Date.valueOf("2012-12-12"));
    }};

    protected static final Map<String, Object> initialDataWithUser111 = new HashMap<>() {{
        put("name", user111.getName());
        put("surname", user111.getSurname());
        put("birthdate", user111.getBirthDate());
    }};

    protected static final Map<String, Object> initialDataWithUser222 = new HashMap<>() {{
        put("name", user222.getName());
        put("surname", user222.getSurname());
        put("birthdate", user222.getBirthDate());
    }};

    protected static final Optional<User> optionalDummyUser333 = Optional.of(new User() {{
        setName("optionalName333");
        setSurname("optionalSurName333");
        setBirthDate(Date.valueOf("2033-03-03"));
    }});

    protected static final Integer dummyReturnInt = 1;
    protected static final String DELETE_FROM_USER = "DELETE FROM public.\"User\" WHERE surname = ?";
    protected static final String GET_BY_SURE_NAME_FROM_USER = "SELECT * FROM public.\"User\" WHERE surname = ?";
    protected static final String IS_EXIST_IN_USER_TABLE = "SELECT count(*) FROM public.\"User\" WHERE surname = ?";
    protected static final String GET_ALL_STORED_PROCEDURES = "SELECT routine_name FROM information_schema.routines WHERE routine_type = 'PROCEDURE'";


}
