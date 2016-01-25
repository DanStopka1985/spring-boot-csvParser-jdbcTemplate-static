package ru.rtlabs;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rtlabs.little_files_loader.Inn;

import java.io.*;
import java.sql.SQLException;

/**
 * Created by Stepan Danilov on 13.01.2016.
 */

@Component
public class Loader {

//    @Autowired
//    JdbcTemplate jt;
//    @Autowired
//    DataSource ds;
//
//    public void createSrc(String path) throws IOException, SQLException {
//
//        jt.execute("drop table if exists loader_l1_src;");
//
//        Connection connection = ds.getConnection();
//
//        ScriptUtils.executeSqlScript(connection, new FileSystemResource("d:\\projects\\loaders\\l1\\tmp\\create_table.sql"));
//
//        CSVParser csvFileParser = null;
//        FileReader fileReader = null;
//
//
//        fileReader = new FileReader(path);
//
//        CSVFormat csvFileFormat = CSVFormat.newFormat(';').withQuote('"').withHeader();
//        csvFileParser = new CSVParser(fileReader, csvFileFormat);
//
//        List csvRecords = csvFileParser.getRecords();
//
//
//        for (int i = 1; i < csvRecords.size(); i++) {
//            CSVRecord record = (CSVRecord) csvRecords.get(i);
//
//            System.out.println(record.get("DrugID"));
//        }
//    }
//
//    public void xlsTempWork() throws IOException, InvalidFormatException, URISyntaxException {
//
//        InputStream inp = new FileInputStream("d:\\projects\\loaders\\loader_little_files\\src\\атх.xls");
//        Workbook wb = WorkbookFactory.create(inp);
//
////        Sheet sheet = wb.getSheetAt(0);
////        Row row = sheet.getRow(1);
////        System.out.println("level: " + row.getOutlineLevel());
////        row = sheet.getRow(2);
////        System.out.println("level: " + row.getOutlineLevel());
////        row = sheet.getRow(3);
////        System.out.println("level: " + row.getOutlineLevel());
////        row = sheet.getRow(4);
////        System.out.println("level: " + row.getOutlineLevel());
//
////        Cell cell = row.getCell(3);
////        if (cell == null)
////            cell = row.createCell(3);
////        cell.setCellType(Cell.CELL_TYPE_STRING);
////        cell.setCellValue("a test");
//
//        // Write the output to a file
////        FileOutputStream fileOut = new FileOutputStream("d:\\projects\\loaders\\loader_little_files\\src\\1.xls");
////        wb.write(fileOut);
////        fileOut.close();
//
//        Sheet sheet = wb.getSheetAt(0);
//        Row row = sheet.getRow(2);
//
//        Cell cell = row.getCell(0);
//        System.out.println(cell.getStringCellValue());
//
//
//
//
//    }

    @Autowired
    Inn inn;

    public void execute() throws InvalidFormatException, SQLException, IOException {
        inn.create();
        inn.loadSrc();
        inn.isValid();
        inn.normalize();
        inn.isValid();
    }


}
