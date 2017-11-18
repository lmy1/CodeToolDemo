package com.huntoo.codemachine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huntoo.codemachine.pojo.Person;
import com.huntoo.codemachine.pojo.QueryVo;
import com.huntoo.codemachine.pojo.Response;
import com.huntoo.codemachine.pojo.UpdateModel;
import com.huntoo.codemachine.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
	@Autowired 
	private PersonService personService;

	/**
	 * 新增
	 * 
	 * @param person	新增项的信息
	 * @return	
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Response addPerson(@RequestBody Person person) {
		Response response = personService.addPerson(person);
		return response;
	}


	/**
	 * 修改
	 * @param person	
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Response updatePerson(@RequestBody Person person) {
		Response response = personService.updatePerson(person);
		return response;
	}
	
	/**
	 * 批量乐观删除
	 * 
	 * @param ids	要乐观删除的ID数组
	 * @return
	 */
	@RequestMapping(value = "/{ids}/opti", method = RequestMethod.DELETE)
	public Response deletePerson4Optimistic(@PathVariable("ids") Long[] ids) {
		
		Response response = personService.deletePerson4Optimistic(ids);
		return response;
	}

	/**
	 * 批量悲观删除
	 * 
	 * @param ids	要悲观删除的ID数组
	 * @return
	 */
	@RequestMapping(value = "/{ids}/pess", method = RequestMethod.DELETE)
	public Response deletePerson4Pessimistic(@PathVariable("ids") Long[] ids) {
		
		Response response = personService.deletePerson4Pessimistic(ids);
		return response;
	}
	
	/**
	 * 根据ID查找
	 * 
	 * @param id	需要查询的ID
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Response findPersonById(@PathVariable("id") Long id) {
		
		Response response = personService.findPersonById(id);
		return response;
	}

	
	/**
	 * 条件分页查询
	 * 
	 * @param queryModels	查询条件封装对象
	 * 		参数可选项：
	 * 			String dash: and,or
	 *			List<QueryModel> queryModels泛型中的sign:	equals,like,between(两个数据格式为小/大，可以是数字和日期),
	 *			gt(大于),lt(小于),ge(大于等于),le(小于等于)
	 * @return
	 */
	@RequestMapping(value="/conditions",method = RequestMethod.POST)
	public Response findPersonByConditions (@RequestBody QueryVo  queryModels){
		
		Response response = personService.findPersonByPageAndConditions(queryModels);
		return response;
		
	}
	
	/**
	 * 根据ID修改单个字段
	 * @param id			需要修改字段所属的ID
	 * @param fieldName		字段名
	 * @param fieldValue	字段值
	 * @return
	 */
	@RequestMapping(value="/field",method = RequestMethod.PUT)
	public Response updateFieldById (@RequestBody UpdateModel updateModel){
		
		Response response = personService.updateFieldById(updateModel);
		return response;
	
	}
}


























