package com.shangan.trade.lightning.deal.db.mappers;

import com.shangan.trade.lightning.deal.db.model.SeckillActivity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeckillActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivity record);

    int insertSelective(SeckillActivity record);

    SeckillActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivity record);

    int updateByPrimaryKey(SeckillActivity record);

    List<SeckillActivity> queryActivityByStatus(int status);

    int updateAvailableStockByPrimaryKey(Long id);

    int lockStock(Long id);

    int deductStock(Long id);

    int revertStock(Long id);
}