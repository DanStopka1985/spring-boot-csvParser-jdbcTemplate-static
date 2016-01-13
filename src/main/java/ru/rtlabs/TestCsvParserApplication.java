package ru.rtlabs;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.io.IOException;

public class TestCsvParserApplication {

	public static void main(String[] args) throws IOException {
		//SpringApplication.run(TestCsvParserApplication.class, args);
		ApplicationContext app = SpringApplication.run(TestCsvParserApplication.class, args);
		JdbcTemplate jdbcTemplate = app.getBean(JdbcTemplate.class);
		SqlRowSet rs = jdbcTemplate.queryForRowSet("select 10000 a");
		while (rs.next()){
			System.out.println(rs.getString("a"));

		}
	}
}
