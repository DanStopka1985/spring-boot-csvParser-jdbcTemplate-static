package ru.rtlabs.apteka_refbooks_loader;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Stepan Danilov on 26.01.2016.
 */
public class UnZip
{
    public void unZipIt(String zipFile, String outputFolder) throws IOException {
        byte[] buffer = new byte[1024];

        //create output directory is not exists
        File folder = new File(System.getProperty("user.dir") + "/xls");
        if(!folder.exists()){
            folder.mkdir();
        }

        //get the zip file content
        ZipInputStream zis =
                new ZipInputStream(new FileInputStream(zipFile));
        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while(ze!=null){

            String fileName = ze.getName();
            File newFile = new File(System.getProperty("user.dir") + "/xls" + File.separator + fileName);

            System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        System.out.println("Done");
    }
}