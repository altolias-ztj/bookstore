package com.bookstore.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookstore.common.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    void updateState(String orderId, int state);

    List<Order> getByUserId(Long userId);

    int cancelIfNotPaid(Long orderId);
}
