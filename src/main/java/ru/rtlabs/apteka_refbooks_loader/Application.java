package ru.rtlabs.apteka_refbooks_loader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

import java.sql.SQLException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException, InvalidFormatException, SQLException {

		ApplicationContext app = SpringApplication.run(Application.class, args);
		Loader loader = app.getBean(Loader.class);
		loader.execute();

	}
}
