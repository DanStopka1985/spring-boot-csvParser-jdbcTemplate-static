package ru.rtlabs;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
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

    public void xlsTempWork() throws IOException, InvalidFormatException {
//        FileInputStream file = new FileInputStream(new File("d:\\projects\\loaders\\loader_little_files\\src\\x.xlsx"));
//
//        //Get the workbook instance for XLS file
//        HSSFWorkbook workbook = new HSSFWorkbook(file);
//
//        //Get first sheet from the workbook
//        HSSFSheet sheet = workbook.getSheetAt(0);
//
//        //Get iterator to all the rows in current sheet
//        Iterator<Row> rowIterator = sheet.iterator();
//
//
//        while (rowIterator.hasNext()){
//            Row row = rowIterator.next();
//            Iterator<Cell> cellIterator = row.cellIterator();
//            while (cellIterator.hasNext()){
//                Cell cell = cellIterator.next();
//                System.out.println(cell.getStringCellValue());
//            }
//        }


        InputStream inp = new FileInputStream("d:\\projects\\loaders\\loader_little_files\\src\\фарм.гр.xls");
        Workbook wb = WorkbookFactory.create(inp);

        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(1);
        System.out.println("level: " + row.getOutlineLevel());
        row = sheet.getRow(2);
        System.out.println("level: " + row.getOutlineLevel());
        row = sheet.getRow(3);
        System.out.println("level: " + row.getOutlineLevel());
        row = sheet.getRow(4);
        System.out.println("level: " + row.getOutlineLevel());

        Cell cell = row.getCell(3);
        if (cell == null)
            cell = row.createCell(3);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("a test");

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("d:\\projects\\loaders\\loader_little_files\\src\\фарм.гр.xls");
        wb.write(fileOut);
        fileOut.close();


    }
}
