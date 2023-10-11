package com.shangan.trade.goods.db.mappers;

import com.shangan.trade.goods.db.model.Goods;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}