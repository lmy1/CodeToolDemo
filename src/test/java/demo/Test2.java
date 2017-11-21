package demo;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;

public class Test2 {
	public static void main(String[] args) {
		System.out.println(JDBCTypesUtils.getJdbcName(JDBCTypesUtils.getJdbcCode("INT")));
	}
	
	@Test
	public void test1() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		TimeZone timeZone = dateFormat.getTimeZone();
		System.out.println(timeZone);
	}
	
	@Test
	public void test2() {
		
	}
}
