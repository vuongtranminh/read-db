package com.readdb.app.dao;

import java.util.List;
import com.readdb.app.util.Pagination;

public interface BaseDao<T> {

	List<T> selectAll();
	T selectById(Long id);
	int insert(T t);
	int updateById(T t);
	List<T> pagination(Pagination pagination);
	Long countAll();

}