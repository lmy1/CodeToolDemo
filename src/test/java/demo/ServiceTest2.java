package demo;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.huntoo.codemachine.dao.PersonDao;
import com.huntoo.codemachine.pojo.Person;
import com.huntoo.codemachine.pojo.QueryModel;
import com.huntoo.codemachine.pojo.QueryVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest2 {
	
	@Autowired
	private PersonDao personDao;
	
	@Test
	public void test1() {
		final QueryVo queryModels = new QueryVo();
		List<QueryModel> list = new ArrayList<>();
		QueryModel queryModel = new QueryModel("age", "20/60", "between");
		list.add(queryModel);
		queryModels.setQueryModels(list);
		queryModels.setPage(1);
		queryModels.setRows(10);
		queryModels.setDash("and");
		
		
		Specification<Person> specification = new Specification<Person>() {
			@Override
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				// 创建一个集合，将所有创建好的条件存进集合，一次查询。
				List<Predicate> ps = new ArrayList<Predicate>();
				// 获取条件封装集合
				List<QueryModel> list = queryModels.getQueryModels();
				for (QueryModel queryModel : list) {
					String qname = queryModel.getQname();
					String qvalue = queryModel.getQvalue();
					String sign = queryModel.getSign();

					if (StringUtils.isNotBlank(qvalue)) {
						Field field = null;
						try {
							field = Person.class.getDeclaredField(qname);
						} catch (Exception e) {
							// TODO: 异常处理
							e.printStackTrace();
						}
						if ("equals".equals(sign)) {
							// 设置查询条件
							Predicate predicate = cb.equal(root.get(qname).as(field.getType()), qvalue);
							ps.add(predicate);
						} else if ("like".equals(sign)) {
							Predicate predicate = cb.like(root.get(qname).as(String.class), "%" + qvalue + "%");
							ps.add(predicate);
						} else if ("between".equals(sign)) {
							String typeName = field.getType().getName();
							String[] data = qvalue.split("/");
							Predicate predicate = null;
							if ("java.util.Date".equals(typeName)) {
								// 如果传入是日期类型的区间
								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date start = null;
								Date end = null;

								try {
									start = dateFormat.parse(data[0]);
									end = dateFormat.parse(data[1]);
								} catch (ParseException e) {
									// TODO: 如果格式转换错误，通过response返回错误信息
									e.printStackTrace();
								}

								predicate = cb.between(root.get(qname).as(Date.class), start, end);
							} else {
								//TODO
//								predicate = cb.between(root.get(qname).as(field.getType()), data[0],
//										data[1]);
							}
							ps.add(predicate);
						} else if ("gt".equals(sign)) {
							// 如果大于
							Predicate predicate = cb.gt(root.get(qname).as(Double.class), Double.parseDouble(qvalue));
							ps.add(predicate);
						} else if ("lt".equals(sign)) {
							// 如果小于
							Predicate predicate = cb.lt(root.get(qname).as(Double.class), Double.parseDouble(qvalue));
							ps.add(predicate);
						} else if ("ge".equals(sign)) {
							// 如果大于等于
							Predicate predicate = cb.ge(root.get(qname).as(Double.class), Double.parseDouble(qvalue));
							ps.add(predicate);
						} else if ("le".equals(sign)) {
							// 如果小于等于
							Predicate predicate = cb.le(root.get(qname).as(Double.class), Double.parseDouble(qvalue));
							ps.add(predicate);
						}
					}
				}

				// 根据条件进行组合并查询，并将查询条件转为数组
				Predicate[] p = new Predicate[ps.size()];
				if ("or".equals(queryModels.getDash())) {
					// 如果多条件之间的关联关系是 or
					return cb.or(ps.toArray(p));
				} else {
					// 默认多条件关联关系为 and
					return cb.and(ps.toArray(p));
				}
			}
		};
		// 创建分页查询对象
		Pageable pageRequest = new PageRequest(queryModels.getPage() - 1, queryModels.getRows());
		Page<Person> page = personDao.findAll(specification, pageRequest);
		System.out.println(page);
	}
}
