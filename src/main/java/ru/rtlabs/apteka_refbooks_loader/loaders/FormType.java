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
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Stepan Danilov on 26.01.2016.
 */
@Component
public class FormType {
    @Autowired
    JdbcTemplate jt;
    @Autowired
    DataSource ds;

    private String formTypePath;
    private String createSQLPath;
    private String normilizeSQLPath;
    private String loadToRMISSQLPath;

    private Connection con;

    public boolean exists() throws SQLException {
        String jarPath = System.getProperty("user.dir");
        String xlsPath = jarPath + "/xls";
        String sqlPath = jarPath + "/sql";

        this.formTypePath = xlsPath + "/form_type.xlsx";
        this.createSQLPath = sqlPath + "/form_type_src_create.sql";
        this.normilizeSQLPath = sqlPath + "/form_type_src_normalize.sql";
        this.loadToRMISSQLPath = sqlPath + "/form_type_load_to_rmis.sql";

        this.con = ds.getConnection();

        return (new FileSystemResource(formTypePath)).exists();
    }

    public void create() throws SQLException {
        ScriptUtils.executeSqlScript(con, new FileSystemResource(createSQLPath));
    }

    public void loadSrc() throws IOException, InvalidFormatException, SQLException {
        InputStream inp = new FileInputStream(formTypePath);
        Workbook wb = WorkbookFactory.create(inp);

        Sheet sheet = wb.getSheetAt(0);
        int rowCnt = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCnt; i++){
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null && row.getCell(1) == null)
                break;
                jt.update("insert into loader_little_files_form_type(short_name, full_name) values(?, ?)",
                        row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());

        }
    }
}
