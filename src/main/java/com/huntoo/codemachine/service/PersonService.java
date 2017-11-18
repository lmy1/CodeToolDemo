package com.huntoo.codemachine.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.huntoo.codemachine.dao.PersonDao;
import com.huntoo.codemachine.pojo.Person;
import com.huntoo.codemachine.pojo.QueryModel;
import com.huntoo.codemachine.pojo.QueryVo;
import com.huntoo.codemachine.pojo.Response;
import com.huntoo.codemachine.pojo.UpdateModel;
import com.huntoo.codemachine.utils.common.StringUtil;

@Service
public class PersonService {

	@Autowired
	private PersonDao personDao;

	/**
	 * 新增
	 * 
	 * @return
	 */
	@Transactional
	public Response addPerson(Person person) {
		personDao.save(person);
		return null;
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	public Response updatePerson(Person person) {
		personDao.saveAndFlush(person);
		return null;
	}

	/**
	 * 根据ID查找
	 * 
	 * @return
	 */
	public Response findPersonById(Long id) {
		Person person = personDao.findOne(id);
		Response response = new Response();
		response.setData(person);
		return response;
	}

	/**
	 * 乐观删除
	 * 
	 * @param ids
	 * @return
	 */
	public Response deletePerson4Optimistic(Long[] ids) {
		List<Person> list = new ArrayList<>();
		for (Long id : ids) {
			Person person = new Person();
			person.setId(id);
			try {
				// TODO: 测试删除
				// if (id == 3) {
				// throw new RuntimeException("报错了");
				// }
				personDao.delete(id);
			} catch (Exception e) {
				list.add(person);
			}
		}
		Response response = new Response();
		response.setData(list);
		return response;
	}

	/**
	 * 悲观删除
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional
	public Response deletePerson4Pessimistic(Long[] ids) {
		Response response = new Response();
		List<Person> list = new ArrayList<>();
		for (Long id : ids) {
			Person person = new Person();
			person.setId(id);
			list.add(person);
		}
		try {
			// TODO: 测试删除
			// int x = 1;
			personDao.deleteInBatch(list);
			// if (x == 1) {
			// throw new RuntimeException();
			// }

		} catch (RuntimeException e) {
			response.setStatus(0);
			// 手动回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return response;
	}

	/**
	 * 根据条件查询
	 * 
	 * 
	 * @param queryModels
	 * @return
	 */
	public Response findPersonByPageAndConditions(final QueryVo queryModels) {
		final Response response = new Response();
		// 创建条件查询对象
		Specification<Person> specification = new Specification<Person>() {

			@SuppressWarnings("unchecked")
			@Override
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				// 创建一个集合，将所有创建好的条件存进集合，一次查询。
				List<Predicate> ps = new ArrayList<Predicate>();
				// 获取条件封装集合
				List<QueryModel> list = queryModels.getQueryModels();
				// 如果查询集合没有数据直接结束
				if (null == list || list.size() == 0) {
					return null;
				}
				for (QueryModel queryModel : list) {
					String qname = queryModel.getQname();
					String qvalue = queryModel.getQvalue();
					String sign = queryModel.getSign();
					// TODO 万一查值是null的怎么办
					if (StringUtils.isNotBlank(qname) && StringUtils.isNotBlank(sign)) {
						Class<?> fieldType = null;
						try {
							Field field = Person.class.getDeclaredField(qname);
							fieldType = field.getType();
						} catch (Exception e) {
							e.printStackTrace();
							response.setStatus(1);
							response.setMsg("要查询的条件字段不存在");
						}
						if ("equals".equals(sign)) {
							// 设置查询条件
							Predicate predicate = null;
							if (null != qvalue) {
								if (fieldType == Date.class) {
									Date date = StringUtil.StringToDate(qvalue, response);
									predicate = cb.equal(root.get(qname).as(Date.class), date);
								} else {
									predicate = cb.equal(root.get(qname).as(fieldType), qvalue);
								}
							} else {
								predicate = cb.isNull(root.get(qname).as(fieldType));
							}
							ps.add(predicate);
						} else if ("like".equals(sign) && StringUtils.isNotBlank(qvalue)) {
							Predicate predicate = cb.like(root.get(qname).as(String.class), "%" + qvalue + "%");
							ps.add(predicate);
						} else if ("between".equals(sign) && StringUtils.isNotBlank(qvalue)) {
							String[] data = qvalue.split("/");
							Predicate predicate = null;
							if (Date.class == fieldType) {
								// 如果传入是日期类型的区间
								Date start = null;
								Date end = null;
								start = StringUtil.StringToDate(qvalue, response);
								end = StringUtil.StringToDate(qvalue, response);
								predicate = cb.between(root.get(qname).as(Date.class), start, end);
							} else if (fieldType == double.class || fieldType == Double.class) {
								predicate = cb.between(root.get(qname).as(Double.class), Double.parseDouble(data[0]),
										Double.parseDouble(data[1]));
							} else if (fieldType == float.class || fieldType == Float.class) {
								predicate = cb.between(root.get(qname).as(Float.class), Float.parseFloat(data[0]),
										Float.parseFloat(data[1]));
							} else if (fieldType == long.class || fieldType == Long.class) {
								predicate = cb.between(root.get(qname).as(Long.class), Long.parseLong(data[0]),
										Long.parseLong(data[1]));
							} else if (fieldType == int.class || fieldType == Integer.class) {
								predicate = cb.between(root.get(qname).as(Integer.class), Integer.parseInt(data[0]),
										Integer.parseInt(data[1]));
							} else if (fieldType == short.class || fieldType == Short.class) {
								predicate = cb.between(root.get(qname).as(Short.class), Short.parseShort(data[0]),
										Short.parseShort(data[1]));
							} else if (fieldType == byte.class || fieldType == Byte.class) {
								predicate = cb.between(root.get(qname).as(Byte.class), Byte.parseByte(data[0]),
										Byte.parseByte(data[1]));
							}
							ps.add(predicate);
						} else if ("gt".equals(sign) && StringUtils.isNotBlank(qvalue)) {
							// 如果大于
							// TODO 日期咋办
							Predicate predicate = null;
							if (fieldType == Date.class) {
								Date date = StringUtil.StringToDate(qvalue, response);
								predicate = cb.greaterThan(root.get(qname).as(Date.class),date);
							}else {
								predicate = cb.gt((Expression<? extends Number>) root.get(qname).as(fieldType),
										Double.parseDouble(qvalue));
							}
							ps.add(predicate);
						} else if ("lt".equals(sign) && StringUtils.isNotBlank(qvalue)) {
							// 如果小于
							Predicate predicate = null;
							if (fieldType == Date.class) {
								Date date = StringUtil.StringToDate(qvalue, response);
								predicate = cb.lessThan(root.get(qname).as(Date.class),date);
							}else {
								predicate = cb.lt((Expression<? extends Number>) root.get(qname).as(fieldType),
										Double.parseDouble(qvalue));
							}
							ps.add(predicate);
						} else if ("ge".equals(sign) && StringUtils.isNotBlank(qvalue)) {
							// 如果大于等于
							Predicate predicate = null;
							if (fieldType == Date.class) {
								Date date = StringUtil.StringToDate(qvalue, response);
								predicate = cb.greaterThanOrEqualTo(root.get(qname).as(Date.class),date);
							}else {
								predicate = cb.ge((Expression<? extends Number>) root.get(qname).as(fieldType),
										Double.parseDouble(qvalue));
							}
							ps.add(predicate);
						} else if ("le".equals(sign) && StringUtils.isNotBlank(qvalue)) {
							// 如果小于等于
							Predicate predicate = null;
							if (fieldType == Date.class) {
								Date date = StringUtil.StringToDate(qvalue, response);
								predicate = cb.lessThanOrEqualTo(root.get(qname).as(Date.class),date);
							}else {
								predicate = cb.le((Expression<? extends Number>) root.get(qname).as(fieldType),
										Double.parseDouble(qvalue));
							}
							ps.add(predicate);
						} else {
							response.setStatus(1);
							response.setMsg("暂无匹配的符号 or 查询数据格式错误");
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
		if (response.getStatus() == 1) {
			return response;
		}
		response.setData(page);
		response.setStatus(0);
		response.setMsg("success");
		return response;
	}

	/**
	 * 修改单个字段值
	 * 
	 * @param updateModel 封装查询的条件
	 * @return
	 */
	@Transactional
	public Response updateFieldById(UpdateModel updateModel) {

		Response response = new Response();

		// 获取修改条件
		String id = updateModel.getId();
		String fieldName = updateModel.getFieldName();
		String fieldValue = updateModel.getFieldValue();
		// 如果修改条件为空
		if (StringUtils.isBlank(id) || StringUtils.isBlank(fieldName)) {
			response.setStatus(1);
			response.setMsg("更新条件不能为空");
			return response;
		}
		// TODO 主键类型自动生成
		Person person = personDao.findOne(Long.parseLong(id));
		String methodName = "set" + StringUtil.firstToUpperCase(fieldName);

		try {
			Field field = Person.class.getDeclaredField(fieldName);
			Class<?> fieldType = field.getType();
			Method method = Person.class.getDeclaredMethod(methodName, fieldType);
			// TODO 类型怎么处理
			convertType(fieldValue, person, fieldType, method);
			personDao.saveAndFlush(person);
		} catch (NoSuchFieldException e) {
			response.setStatus(1);
			response.setMsg("字段信息错误");
			e.printStackTrace();
			return response;
		} catch (NoSuchMethodException e) {
			response.setStatus(1);
			response.setMsg("方法获取错误，可能是字段或方法命名不规范");
			e.printStackTrace();
			return response;
		} catch (Exception e) {
			response.setStatus(1);
			response.setMsg("请查看异常信息");
			e.printStackTrace();
			return response;
		}

		response.setStatus(0);
		response.setMsg("success");
		return response;

	}

	/**
	 * 根据条件执行方法
	 * 
	 * @param fieldValue	操作的方法的参数(参数数量为1)
	 * @param person	要执行方法的对象
	 * @param fieldType	要执行方法的参数类型(参数数量为1)
	 * @param method 	要执行的方法
	 * @return
	 * @throws Exception
	 */
	private Object convertType(String fieldValue, Person person, Class<?> fieldType, Method method) throws Exception {
		Object result = null;
		if (fieldType == String.class) {
			result = method.invoke(person, fieldValue);
		} else if (fieldType == Date.class) {
			Date val = StringUtil.StringToDate(fieldValue, null);
			result = method.invoke(person, val);
		} else if (fieldType == double.class || fieldType == Double.class) {
			double val = Double.parseDouble(fieldValue);
			result = method.invoke(person, val);
		} else if (fieldType == float.class || fieldType == Float.class) {
			float val = Float.parseFloat(fieldValue);
			result = method.invoke(person, val);
		} else if (fieldType == long.class || fieldType == Long.class) {
			long val = Long.parseLong(fieldValue);
			result = method.invoke(person, val);
		} else if (fieldType == int.class || fieldType == Integer.class) {
			int val = Integer.parseInt(fieldValue);
			result = method.invoke(person, val);
		} else if (fieldType == short.class || fieldType == Short.class) {
			short val = Short.parseShort(fieldValue);
			result = method.invoke(person, val);
		} else if (fieldType == byte.class || fieldType == Byte.class) {
			byte val = Byte.parseByte(fieldValue);
			result = method.invoke(person, val);
		} else if (fieldType == boolean.class || fieldType == Boolean.class) {
			boolean val = Boolean.parseBoolean(fieldValue);
			result = method.invoke(person, val);
		} else {
			throw new TypeMismatchException("没有匹配的类型");
		}
		return result;
	}

}
