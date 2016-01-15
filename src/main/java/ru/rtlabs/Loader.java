package ru.rtlabs;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Stepan Danilov on 13.01.2016.
 */

@Component
public class Loader {

    @Autowired
    JdbcTemplate jt;
    @Autowired
    DataSource ds;

    public void createSrc(String path) throws IOException, SQLException {

        jt.execute("drop table if exists loader_l1_src;");

        Connection connection = ds.getConnection();

        ScriptUtils.executeSqlScript(connection, new FileSystemResource("d:\\projects\\loaders\\l1\\tmp\\create_table.sql"));

        CSVParser csvFileParser = null;
        FileReader fileReader = null;


        fileReader = new FileReader(path);

        CSVFormat csvFileFormat = CSVFormat.newFormat(';').withQuote('"').withHeader();
        csvFileParser = new CSVParser(fileReader, csvFileFormat);

        List csvRecords = csvFileParser.getRecords();


        for (int i = 1; i < csvRecords.size(); i++) {
            CSVRecord record = (CSVRecord) csvRecords.get(i);

            System.out.println(record.get("DrugID"));
        }
    }
}
