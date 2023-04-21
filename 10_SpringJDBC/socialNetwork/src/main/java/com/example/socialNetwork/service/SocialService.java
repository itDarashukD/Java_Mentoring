package com.example.socialNetwork.service;


import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SocialService {


    @Autowired
    private RowCreator rowCreator;

    @Autowired
    private TablesCreatorImpl tablesCreatorImpl;

    @Value("${tableCount}")
    private int tableCount;

    @Value("${columnsCount}")
    private int columnsCount;

    @Value("${typeCount}")
    private int typeCount;

    @Value("${desireRowCount}")
    private int desireRowCount;

    @Value("${desireNumberOfTableToInserts}")
    private int desireNumberOfTableToInserts;


    @PostConstruct
    public void runCreatingTableProcess() {
        List<String> tables = tablesCreatorImpl.createTable(tableCount, columnsCount, typeCount);

        List<Map<String, Object>> rows = rowCreator.createRows(desireRowCount,
                                                                desireNumberOfTableToInserts,
                                                                tables);

        log.info(String.format("the inserted into DB tables : %s", tables));
        log.info(String.format("the inserted into DB table row values : %s", rows));
    }

}



