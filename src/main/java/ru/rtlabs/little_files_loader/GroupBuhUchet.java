package ru.rtlabs.little_files_loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by Stepan Danilov on 22.01.2016.
 */

@Component
public class GroupBuhUchet {
    @Autowired
    JdbcTemplate jt;
    @Autowired
    DataSource ds;
}
