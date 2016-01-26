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
 * Created by Stepan Danilov on 25.01.2016.
 */
@Component
public class PharmGroup {
    @Autowired
    JdbcTemplate jt;
    @Autowired
    DataSource ds;

    private String pharmGroupPath;
    private String createSQLPath;
    private String normilizeSQLPath;
    private String loadToRMISSQLPath;

    private Connection con;

    public boolean exists() throws SQLException {
        String jarPath = System.getProperty("user.dir");
        String xlsPath = jarPath + "/xls";
        String sqlPath = jarPath + "/sql";

        this.pharmGroupPath = xlsPath + "/pharm_group.xlsx";
        this.createSQLPath = sqlPath + "/pharm_group_src_create.sql";
        this.normilizeSQLPath = sqlPath + "/pharm_group_src_normalize.sql";
        this.loadToRMISSQLPath = sqlPath + "/pharm_group_load_to_rmis.sql";

        this.con = ds.getConnection();
        return (new FileSystemResource(pharmGroupPath)).exists();
    }

    public void create() throws SQLException {
        ScriptUtils.executeSqlScript(con, new FileSystemResource(createSQLPath));
    }

    public void loadSrc() throws IOException, InvalidFormatException, SQLException {

        InputStream inp = new FileInputStream(pharmGroupPath);
        Workbook wb = WorkbookFactory.create(inp);

        Sheet sheet = wb.getSheetAt(0);
        int rowCnt = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCnt; i++){
            Row row = sheet.getRow(i);
            switch (row.getCell(1).getCellType()){
                case Cell.CELL_TYPE_STRING:
                    jt.update("insert into public.loader_little_files_pharm_group(name, uid) values(?, ?)",
                    row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    jt.update("insert into public.loader_little_files_pharm_group(name, uid) values(?, ?)",
                            row.getCell(0).getStringCellValue(), String.valueOf(row.getCell(1).getNumericCellValue()));
                    break;
            }
        }

        jt.update("update public.loader_little_files_pharm_group set uid = substring(uid, 0, length(uid) - 1) where uid ~ '.0$'");
    }

    public boolean isValid(){
        System.out.println("Проверяется справочник фарм груп..");
        boolean isValid = true;
        SqlRowSet rs = jt.queryForRowSet("select * from public.loader_little_files_pharm_group where name is null;");
        if (rs.next()){
            isValid = false;
            System.out.println("Проверка справочника фарм груп, поле name обязательное, но не заполнено ");
            rs = jt.queryForRowSet("select count(1) cnt from public.loader_little_files_pharm_group where name is null;");
            System.out.println("не заполнено " + rs.getString("cnt") + "шт.");
        }

        rs = jt.queryForRowSet("select * from public.loader_little_files_pharm_group where uid is null;");
        if (rs.next()){
            isValid = false;
            System.out.println("Проверка справочника фарм груп, поле uid обязательное, но не заполнено ");
            rs = jt.queryForRowSet("select count(1) cnt from public.loader_little_files_pharm_group where uid is null;");
            System.out.println("не заполнено " + rs.getString("cnt") + "шт.");
        }

        rs = jt.queryForRowSet("select name, count(1) cnt from public.loader_little_files_pharm_group group by name having count(1) > 1;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка справочника фарм груп, поле name должно быть уникально, но обнаружены дубли:" + rs.getString("name") + " - " + rs.getString("cnt") + " шт.");
        }

        rs = jt.queryForRowSet("select uid, count(1) cnt from public.loader_little_files_pharm_group group by uid having count(1) > 1;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка справочника фарм груп, поле uid должно быть уникально, но обнаружены дубли:" + rs.getString("uid") + " - " + rs.getString("cnt") + " шт.");
        }

        if (isValid) System.out.println("справочник фарм груп валидный");
        else System.out.println("справочник фарм груп невалидный");

        //normalize there, because next checks needed
        ScriptUtils.executeSqlScript(con, new FileSystemResource(normilizeSQLPath));

        //todo next checks

        return isValid;
    }

    public void normalize(){
        System.out.println("справочник фарм груп - нормализация..");
        ScriptUtils.executeSqlScript(con, new FileSystemResource(normilizeSQLPath));
        System.out.println("справочник фарм груп -  нормализован");
    }

    public void loadToRmis(){
        System.out.println("Загружаентся справочник фарм груп..");
        ScriptUtils.executeSqlScript(con, new FileSystemResource(loadToRMISSQLPath));
        System.out.println("справочник фарм груп загружен!");
    }

}
