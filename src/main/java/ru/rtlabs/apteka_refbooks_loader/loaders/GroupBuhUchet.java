package ru.rtlabs.apteka_refbooks_loader.loaders;

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
