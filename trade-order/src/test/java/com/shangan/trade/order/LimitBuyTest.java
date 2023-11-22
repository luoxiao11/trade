package com.shangan.trade.order;

import com.shangan.trade.common.service.LimitBuyService;
import com.shangan.trade.order.service.impl.RiskBlackListService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LimitBuyTest {
    @Autowired
    public LimitBuyService limitBuyService;

    @Autowired
    public RiskBlackListService riskBlackListService;

    @Test
    public void addLimitMemberTest() {
        limitBuyService.addLimitMember(123456L, 668899L);
    }

    @Test
    public void isInLimitMemberTest() {
        limitBuyService.isInLimitMember(123456L, 668899L);
    }

    @Test
    public void removeLimitMemberTest() {
        limitBuyService.removeLimitMember(123456L, 668899L);
    }

    @Test
    public void addBlacklistTest() {

        riskBlackListService.addRiskBlackListMember(123456);
        riskBlackListService.isInRiskBlackListMember(123456);
    }

    @Test
    public void isInBlacklistTest() {
        riskBlackListService.isInRiskBlackListMember(123456);
    }

    @Test
    public void removeBlacklistTest() {
        riskBlackListService.removeRiskBlackListMember(123456);
        riskBlackListService.isInRiskBlackListMember(123456);
    }
}
