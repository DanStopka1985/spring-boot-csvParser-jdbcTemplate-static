package ru.rtlabs.apteka_refbooks_loader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rtlabs.apteka_refbooks_loader.loaders.*;

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
    @Autowired
    Ath ath;
    @Autowired
    PharmGroup pharmGroup;
    @Autowired
    FormType formType;

    public void execute(String p) throws InvalidFormatException, SQLException, IOException {

        boolean ignore = false;
        if ("i".equals(p)){
            ignore = true;
            System.out.println("\nВЫБРАНО - ИГНОРИРОВАТЬ ДУБЛИ!\n");
        }

        if (inn.exists()){
            inn.create();
            inn.loadSrc();
            if (inn.isValid() || ignore){
                inn.normalize();
                inn.loadToRmis();
            }
        }

        if (ath.exists()){
            ath.create();
            ath.loadSrc();
            if (ath.isValid() || ignore){
                ath.normalize();
                ath.loadToRmis();
            }
        }

        if (groupBuhUchet.exists()){
            groupBuhUchet.create();
            groupBuhUchet.loadSrc();
            if (groupBuhUchet.isValid() || ignore){
                groupBuhUchet.normalize();
                groupBuhUchet.loadToRmis();
            }
        }

        if (pharmGroup.exists()){
            pharmGroup.create();
            pharmGroup.loadSrc();
            if (pharmGroup.isValid() || ignore){
                pharmGroup.normalize();
                pharmGroup.loadToRmis();
            }
        }

        if (formType.exists()){
            formType.create();
            formType.loadSrc();
            if (formType.isValid() || ignore){
                formType.normalize();
                formType.loadToRmis();
            }
        }


        if (formType.exists()){
            formType.create();
            formType.loadSrc();
            formType.isValid();
        }


        System.out.println("ЗАГРУЗКА ЗАВЕРШЕНА!");
    }


}
