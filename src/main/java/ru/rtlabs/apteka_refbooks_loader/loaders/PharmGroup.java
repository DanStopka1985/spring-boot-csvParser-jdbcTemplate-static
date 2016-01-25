package ru.rtlabs.apteka_refbooks_loader.loaders;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
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
 * Created by Stepan Danilov on 25.01.2016.
 */
@Component
public class PharmGroup {
    @Autowired
    JdbcTemplate jt;
    @Autowired
    DataSource ds;

    private String PharmGroupPath;
    private String createSQLPath;
    private String normilizeSQLPath;
    private String loadToRMISSQLPath;

    private Connection con;

    public void create() throws SQLException {
        String jarPath = System.getProperty("user.dir");
        String xlsPath = jarPath + "/xls";
        String sqlPath = jarPath + "/sql";

        this.PharmGroupPath = xlsPath + "/фарм+группы+нов.xlsx";
        this.createSQLPath = sqlPath + "/pharm_group_src_create.sql";
        this.normilizeSQLPath = sqlPath + "/pharm_group_src_normalize.sql";
        this.loadToRMISSQLPath = sqlPath + "/pharm_group_load_to_rmis.sql";

        this.con = ds.getConnection();

        ScriptUtils.executeSqlScript(con, new FileSystemResource(createSQLPath));
    }

    public void loadSrc() throws IOException, InvalidFormatException, SQLException {

        InputStream inp = new FileInputStream(PharmGroupPath);
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
}
