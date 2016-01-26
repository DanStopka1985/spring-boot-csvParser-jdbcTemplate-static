package ru.rtlabs.apteka_refbooks_loader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException, InvalidFormatException, SQLException {

		ApplicationContext app = SpringApplication.run(Application.class, args);
		Loader loader = app.getBean(Loader.class);

		if (args.length < 1) {
			System.out.println("\nНЕ УКАЗАН ПУТЬ К АРХИВУ!\n");
			System.out.println("\nЗАВЕРШЕНИЕ ЗАГРУЗКИ!\n");
			System.exit(-1);
		}

		UnZip unZip = new UnZip();
		unZip.unZipIt(args[0], System.getProperty("user.dir") + "/xls");

		String p;
		if (args.length > 1)
			p = args[1];
		else
			p = "";

		loader.execute(args[0], p);

	}
}
