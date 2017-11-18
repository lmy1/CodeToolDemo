package demo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntoo.codemachine.pojo.Person;
import com.huntoo.codemachine.pojo.QueryModel;
import com.huntoo.codemachine.pojo.QueryVo;
import com.huntoo.codemachine.pojo.Response;


public class JsonTest {
	
	
	public void test2() {
		
		
		
		/*
		 * Field[] fields = Person.class.getDeclaredFields(); for (Field field : fields)
		 * { String ftype = field.getType().getName(); //field为反射出来的字段类型 String fstype =
		 * field.getType().getSimpleName(); if (field.getType() == String.class) return
		 * value.toString(); else if (ftype.indexOf("java.lang.") == 0) { //
		 * java.lang下面类型通用转换函数 Class<?> class1 = Class.forName(ftype); Method method =
		 * class1.getMethod("parse" + fixparse(fstype),String.class); if (method !=
		 * null) { Object ret = method.invoke(null, value.toString()); if (ret != null)
		 * return ret; } }
		 */
	}
	
	@Test
	public void test3() {
		Field[] declaredFields = Person.class.getDeclaredFields();
		for (Field field : declaredFields) {
			
			String type = field.getType().getName();
			System.out.println(type);
		}
	}

//	{"dash":"and","page":1,"queryModels":[{"qname":"name","qvalue":"张三","sign":"equals"},{"qname":"age","qvalue":"12","sign":"gt"}],"rows":10}
	@Test
	public void testJson() {
		ArrayList<QueryModel> list = new ArrayList<>();
		QueryModel qm1 = new QueryModel("name", "张三", "equals");
		QueryModel qm2 = new QueryModel("age", "12", "gt");
		list.add(qm1);
		list.add(qm2);
		QueryVo queryVo = new QueryVo();
		queryVo.setQueryModels(list);
		queryVo.setDash("and");
		queryVo.setPage(1);
		queryVo.setRows(10);
		
		String jsonString = JSONObject.toJSONString(queryVo);
		System.out.println(jsonString);
		
	}
	
	@Test
	public void testJson2() {
		Response response = new Response();
		response.setStatus(0);
		response.setMsg("成功");
		Person person = new Person();
		person.setId(1L);
		person.setName("张三");
		person.setAge(12);
//		response.setData(person);
		String jsonString = JSONObject.toJSONString(response);
		System.out.println(jsonString);
	}
	
	@Test
	public void testJson3() throws Exception {
		//class java.lang.String
		Field field = Person.class.getDeclaredField("birthday");
		Class<?> type = field.getType();
		Type genericType = field.getGenericType();
		System.out.println(type);	//class java.lang.String
//		System.out.println(genericType.getTypeName());	//java.lang.String
		System.out.println(type.getName());
		System.out.println(type.getSimpleName());
	}
	@Test
	public void test4() throws Exception {
		Field field = Person.class.getDeclaredField("name");
		Class<?> type = field.getType();
		System.out.println(type);
	}
	
	@Test
	public void test5() throws Exception {
		Person person = new Person();
		person.setName("abc");
		person.setAge(99);
		person.setBirthday(new Date());
		String jsonString = JSONObject.toJSONString(person);
		System.out.println(jsonString);
	}
	
	@Test
	public void test6() throws Exception {
//		double parseDouble = Double.parseDouble("10.1");	
//		int parseInt = Integer.parseInt("10.0");	//报错
		long parseLong = Long.parseLong("10");		//报错
		double parseDouble = Double.parseDouble("10");
		
		System.out.println(parseDouble);
//		System.out.println(parseInt);
//		System.out.println(String.class.getTypeName());
	}
	
	@Test
	public void test7() throws Exception {
//		Method method = JsonTest.class.getDeclaredMethod("getAge",);
		SimpleDateFormat longFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parse = longFormat.parse("1994-8-27 11:11:11");
		System.out.println(parse);
	}
	private int getAge(int age) {
		return 0;

	} 

}













