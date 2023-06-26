package com.bookstore.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.common.entity.Person;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
