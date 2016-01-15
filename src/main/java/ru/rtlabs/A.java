package ru.rtlabs;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Stepan Danilov on 31.12.2015.
 */
//@Repository
@Component
public class A {
//    @Autowired
//    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void go() throws IOException {

        System.out.println(123);
        CSVParser csvFileParser = null;
        FileReader fileReader = null;

        fileReader = new FileReader("d:/src/Действующий_справочник_ЛС.csv");

        CSVFormat csvFileFormat = CSVFormat.newFormat(';').withQuote('"').withHeader();
        csvFileParser = new CSVParser(fileReader, csvFileFormat);

        List csvRecords = csvFileParser.getRecords();


        for (int i = 1; i < csvRecords.size(); i++) {
            CSVRecord record = (CSVRecord) csvRecords.get(i);
            System.out.println(record.get("DrugID"));
        }

        //JdbcTemplate jt = new JdbcTemplate(dataSource);

        //jdbcTemplate.queryForRowSet("select 1 a");


//        if (dataSource == null) {
//            System.out.println("dataSource is null");
//        }

        if (jdbcTemplate == null) {
            System.out.println("jdbcTemplate is null");
        }

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select 090909090 a");

        while (rs.next()){
            System.out.println(rs.getString("a"));

        }



//        SqlRowSet rs = jt.queryForRowSet("SELECT 154000000000 a ");
//        while (rs.next()){
//            System.out.println(rs.getString("a"));
//        }

    }
}

