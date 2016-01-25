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
 * Created by Stepan Danilov on 22.01.2016.
 */

@Component
public class GroupBuhUchet {
    @Autowired
    JdbcTemplate jt;
    @Autowired
    DataSource ds;

    private String GrBuhUchPath;
    private String createSQLPath;
    private String normilizeSQLPath;
    private String loadToRMISSQLPath;

    private Connection con;

    public void create() throws SQLException {
        String jarPath = System.getProperty("user.dir");
        String xlsPath = jarPath + "/xls";
        String sqlPath = jarPath + "/sql";

        this.GrBuhUchPath = xlsPath + "/группы бух учета.xls";
        this.createSQLPath = sqlPath + "/gr_buh_uch_src_create.sql";
        this.normilizeSQLPath = sqlPath + "/gr_buh_uch_src_normalize.sql";
        this.loadToRMISSQLPath = sqlPath + "/gr_buh_uch_load_to_rmis.sql";

        this.con = ds.getConnection();

        ScriptUtils.executeSqlScript(con, new FileSystemResource(createSQLPath));
    }

    public void loadSrc() throws IOException, InvalidFormatException, SQLException {

        InputStream inp = new FileInputStream(GrBuhUchPath);
        Workbook wb = WorkbookFactory.create(inp);

        Sheet sheet = wb.getSheetAt(0);
        int rowCnt = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCnt; i++){
            Row row = sheet.getRow(i);
            jt.update("insert into public.loader_little_files_gr_buh_uch(name) values(?)",
                    row.getCell(1).getStringCellValue());
        }
    }

    public boolean isValid(){
        System.out.println("Проверяется справочник группы бух. учета..");
        boolean isValid = true;
        SqlRowSet rs = jt.queryForRowSet("select * from public.loader_little_files_gr_buh_uch where name is null;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка справочника группы бух. учета, поле name обязательное, но не заполнено ");
            rs = jt.queryForRowSet("select count(1) cnt from public.loader_little_files_gr_buh_uch where name is null;");
            System.out.println("не заполнено " + rs.getString("cnt") + "шт.");
            break;
        }

        rs = jt.queryForRowSet("select name, count(1) cnt from public.loader_little_files_gr_buh_uch group by name having count(1) > 1;");
        while (rs.next()){
            isValid = false;
            System.out.println("Проверка справочника группы бух. учета, поле name должно быть уникально, но обнаружены дубли:" + rs.getString("name") + " - " + rs.getString("cnt") + " шт.");
        }

        if (isValid) System.out.println("справочник группы бух. учета валидный");
        else System.out.println("справочник группы бух. учета невалидный");

        return isValid;
    }

    public void normalize(){
        ScriptUtils.executeSqlScript(con, new FileSystemResource(normilizeSQLPath));
    }

    public void loadToRmis(){
        System.out.println("Загружаентся справочник группы бух. учета..");
        ScriptUtils.executeSqlScript(con, new FileSystemResource(loadToRMISSQLPath));
        System.out.println("справочник группы бух. учета загружен!");
    }

}
