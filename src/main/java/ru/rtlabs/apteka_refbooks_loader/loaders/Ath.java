package ru.rtlabs.apteka_refbooks_loader.loaders;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
 * Created by Stepan Danilov on 25.01.2016.
 */
@Component
public class Ath {
    @Autowired
    JdbcTemplate jt;
    @Autowired
    DataSource ds;

    private String athPath;
    private String createSQLPath;
    private String normilizeSQLPath;
    private String loadToRMISSQLPath;

    private Connection con;

    public boolean exists() throws SQLException {
        String jarPath = System.getProperty("user.dir");
        String xlsPath = jarPath + "/xls";
        String sqlPath = jarPath + "/sql";

        this.athPath = xlsPath + "/atc.xlsx";
        this.createSQLPath = sqlPath + "/ath_src_create.sql";
        this.normilizeSQLPath = sqlPath + "/ath_src_normalize.sql";
        this.loadToRMISSQLPath = sqlPath + "/ath_load_to_rmis.sql";

        this.con = ds.getConnection();

        return (new FileSystemResource(athPath)).exists();
    }

    public void create() throws SQLException {
        ScriptUtils.executeSqlScript(con, new FileSystemResource(createSQLPath));
    }

    public void loadSrc() throws IOException, InvalidFormatException, SQLException {

        InputStream inp = new FileInputStream(athPath);
        Workbook wb = WorkbookFactory.create(inp);

        Sheet sheet = wb.getSheetAt(0);
        int rowCnt = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCnt; i++){
            Row row = sheet.getRow(i);
            jt.update("insert into loader_little_files_ath(name, code) values(?, ?)",
                    row.getCell(0).getStringCellValue(), row.getCell(1).getNumericCellValue());
        }
    }

    public boolean isValid(){
        System.out.println("Проверяется АТХ..");
        boolean isValid = true;
        SqlRowSet rs = jt.queryForRowSet("select * from public.loader_little_files_ath where name is null;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка АТХ, поле name обязательное, но не заполнено, code:" + rs.getString("code"));
        }
        jt.update("delete from public.loader_little_files_ath where name is null;");
        jt.update("alter table public.loader_little_files_ath add column own_code text, add column own_name text, add column parent_own_code text, add column parent_id integer;");
        jt.update("update public.loader_little_files_ath set own_code = substring(name, '^\\S*'), own_name = regexp_replace(name, '^\\S*\\s', '');");

        rs = jt.queryForRowSet("select own_code, count(1) cnt from public.loader_little_files_ath group by own_code having count(1) > 1;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка АТХ, код должен быть уникальным, но обнаружены дубли:" + rs.getString("own_code") + " - " + rs.getString("cnt") + " шт.");
        }

        if (isValid) System.out.println("АТХ валидный");
        else System.out.println("АТХ невалидный");

        return isValid;
    }

    public void normalize(){
        System.out.println("АТХ нормализация..");
        ScriptUtils.executeSqlScript(con, new FileSystemResource(normilizeSQLPath));
        System.out.println("АТХ нормализован");
    }

    public void loadToRmis(){
        System.out.println("Загружаентся АТХ..");
        ScriptUtils.executeSqlScript(con, new FileSystemResource(loadToRMISSQLPath));
        System.out.println("АТХ загружен!");
    }
}
