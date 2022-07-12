package com.readdb.app.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.readdb.app.po.StudentPo;

@Mapper
public interface StudentDao extends BaseDao<StudentPo> {

}