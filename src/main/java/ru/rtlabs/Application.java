package ru.rtlabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException, SQLException {

		ApplicationContext app = SpringApplication.run(Application.class, args);
//		JdbcTemplate jdbcTemplate = app.getBean(JdbcTemplate.class);
//		SqlRowSet rs = jdbcTemplate.queryForRowSet("select 10000 a");
//		while (rs.next()){
//			System.out.println(rs.getString("a"));
//
//		}
//
//		A a = app.getBean(A.class);
		Loader loader = app.getBean(Loader.class);
		loader.createSrc("d:/src/Действующий_справочник_ЛС.csv");

	}
}
