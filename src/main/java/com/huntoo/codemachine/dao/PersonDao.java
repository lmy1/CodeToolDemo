package com.huntoo.codemachine.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huntoo.codemachine.pojo.Person;

public interface PersonDao extends JpaRepository<Person, Long>,JpaSpecificationExecutor<Person>{
	public List<Person> findByNameLike(String name);
	
	/**
	 * 根据ID逻辑删除
	 * @param id
	 */
	@Query(value="update person set delflag = 1 where id = ?",nativeQuery=true)
	@Modifying
	public void deleteByLogic(Long id);
	
	/**
	 * 批量逻辑删除
	 * @param ids
	 */
	@Query(value="update person set delflag = 1 where id in (?)",nativeQuery=true)
	@Modifying
	public void deleteInBatchByLogic(String ids);
}









