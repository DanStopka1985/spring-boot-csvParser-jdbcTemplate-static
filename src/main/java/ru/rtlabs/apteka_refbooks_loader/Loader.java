package ru.rtlabs.apteka_refbooks_loader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rtlabs.apteka_refbooks_loader.loaders.GroupBuhUchet;
import ru.rtlabs.apteka_refbooks_loader.loaders.Inn;

import java.io.*;
import java.sql.SQLException;

/**
 * Created by Stepan Danilov on 13.01.2016.
 */

@Component
public class Loader {

    @Autowired
    Inn inn;
    @Autowired
    GroupBuhUchet groupBuhUchet;

    public void execute() throws InvalidFormatException, SQLException, IOException {
//        inn.create();
//        inn.loadSrc();
//        inn.isValid();
//        inn.normalize();
//        inn.isValid();
//        inn.loadToRmis();

        groupBuhUchet.create();
        groupBuhUchet.loadSrc();

    }


}
