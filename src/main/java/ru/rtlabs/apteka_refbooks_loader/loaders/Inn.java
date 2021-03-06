package ru.rtlabs.apteka_refbooks_loader.loaders;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Stepan Danilov on 22.01.2016.
 */
@Component
public class Inn {
    @Autowired
    private JdbcTemplate jt;
    @Autowired
    private DataSource ds;

    private String innPath;
    private String createSQLPath;
    private String normilizeSQLPath;
    private String loadToRMISSQLPath;
    private Connection con;

    public boolean exists() throws SQLException {
        String jarPath = System.getProperty("user.dir");
        String xlsPath = jarPath + "/xls";
        String sqlPath = jarPath + "/sql";

        this.innPath = xlsPath + "/inn.xlsx";
        this.createSQLPath = sqlPath + "/inn_src_create.sql";
        this.normilizeSQLPath = sqlPath + "/inn_src_normalize.sql";
        this.loadToRMISSQLPath = sqlPath + "/inn_load_to_rmis.sql";

        this.con = ds.getConnection();

        return (new FileSystemResource(innPath)).exists();
    }

    public void create() throws SQLException {
        ScriptUtils.executeSqlScript(con, new FileSystemResource(createSQLPath));
    }

    public void loadSrc() throws IOException, InvalidFormatException, SQLException {

        InputStream inp = new FileInputStream(innPath);
        Workbook wb = WorkbookFactory.create(inp);

        Sheet sheet = wb.getSheetAt(0);
        int rowCnt = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCnt; i++){
            Row row = sheet.getRow(i);
            jt.update("insert into loader_little_files_mnn(name_rus, code, name_latin) values(?, ?, ?)",
                    row.getCell(0).getStringCellValue(), row.getCell(1).getNumericCellValue(), row.getCell(2).getStringCellValue());
        }
    }

    public boolean isValid(){
        System.out.println("Проверяется МНН..");
        boolean isValid = true;
        SqlRowSet rs = jt.queryForRowSet("select * from public.loader_little_files_mnn where name_rus is null;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка МНН, поле name_rus обязательное, но не заполнено, code:" + rs.getString("code") + " name_latin:" + rs.getString("name_latin"));
        }

        rs = jt.queryForRowSet("select * from public.loader_little_files_mnn where name_latin is null;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка МНН, поле name_latin обязательное, но не заполнено, code:" + rs.getString("code") + " name_rus:" + rs.getString("name_rus"));
        }

        rs = jt.queryForRowSet("select name_rus, count(1) cnt, array_to_string(array_agg( distinct code), ', ' ) codes from public.loader_little_files_mnn group by name_rus having count(1) > 1;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка МНН, поле name_rus должно быть уникально, но обнаружены дубли, codes:" + rs.getString("codes") + " - " + rs.getString("cnt") + " шт.");
        }

        if (isValid) System.out.println("МНН валидный");
        else System.out.println("МНН невалидный");

        return isValid;
    }

    public void normalize(){
        System.out.println("МНН нормализация..");
        ScriptUtils.executeSqlScript(con, new FileSystemResource(normilizeSQLPath));
        System.out.println("МНН нормализован");
    }

    public void loadToRmis(){
        System.out.println("Загружаентся МНН..");
        ScriptUtils.executeSqlScript(con, new FileSystemResource(loadToRMISSQLPath));
        System.out.println("МНН загружен!");
    }


}
