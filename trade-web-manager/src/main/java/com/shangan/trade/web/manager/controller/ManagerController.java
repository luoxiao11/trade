package com.shangan.trade.web.manager.controller;


import com.shangan.trade.goods.db.model.Goods;
import com.shangan.trade.goods.service.GoodsService;
import com.shangan.trade.lightning.deal.db.model.SeckillActivity;
import com.shangan.trade.lightning.deal.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
public class ManagerController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SeckillActivityService seckillActivityService;

    /**
     * 跳转到主页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    /**
     * 跳转商品添加页面
     * @return
     */
    @RequestMapping("/add_goods")
    public String addGoods() {
        return "add_goods";
    }

    /**
     * 处理商品添加
     * @param title
     * @param number
     * @param brand
     * @param image
     * @param description
     * @param price
     * @param keywords
     * @param category
     * @param stock
     * @param /resultMap
     * @return
     */
    @RequestMapping("/addGoodsAction")
    public String addGoodsAction(@RequestParam("title") String title,
                                 @RequestParam("number") String number,
                                 @RequestParam("brand") String brand,
                                 @RequestParam("image") String image,
                                 @RequestParam("description") String description,
                                 @RequestParam("price") int price,
                                 @RequestParam("keywords") String keywords,
                                 @RequestParam("category") String category,
                                 @RequestParam("stock") int stock, Map<String, Object> resultMap) {
        Goods goods = new Goods();
        goods.setTitle(title);
        goods.setNumber(number);
        goods.setBrand(brand);
        goods.setImage(image);
        goods.setDescription(description);
        goods.setPrice(price);
        goods.setKeywords(keywords);
        goods.setCategory(category);
        goods.setAvailableStock(stock);
        goods.setLockStock(0);
        //初始为上架状态
        goods.setStatus(1);
        //初始的销售数量为0
        goods.setSaleNum(0);
        goods.setCreateTime(new Date());
        boolean result = goodsService.insertGoods(goods);
        log.info("add goods /result={}", result);
        resultMap.put("goodsInfo", goods);
        return "add_goods";
    }


    /**
     * 跳转到秒杀活动页面
     *
     * @return
     */
    @RequestMapping("/addSkillActivity")
    public String addSkillActivity() {
        return "add_skill_activity";
    }

    /**
     * 添加秒杀活动信息
     *
     * @param activityName
     * @param goodsId
     * @param startTime
     * @param endTime
     * @param availableStock
     * @param seckillPrice
     * @param oldPrice
     * @param resultMap
     * @return
     */
    @RequestMapping("/addSkillActivityAction")
    public String addSkillActivityAction(@RequestParam("activityName") String activityName,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam("startTime") String startTime,
                                         @RequestParam("endTime") String endTime,
                                         @RequestParam("availableStock") int availableStock,
                                         @RequestParam("seckillPrice") int seckillPrice,
                                         @RequestParam("oldPrice") int oldPrice,
                                         Map<String, Object> resultMap) {
        try {
            System.out.println(" activityName :" + activityName);
            SeckillActivity seckillActivity = new SeckillActivity();
            seckillActivity.setActivityName(activityName);
            seckillActivity.setGoodsId(goodsId);

            //获取到的startTime时间格式  2023-10-05T22:51
            startTime = startTime.substring(0, 10) + " " + startTime.substring(11);
            endTime = endTime.substring(0, 10) + " " + endTime.substring(11);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            seckillActivity.setStartTime(format.parse(startTime));
            seckillActivity.setEndTime(format.parse(endTime));
            seckillActivity.setAvailableStock(availableStock);
            //默认上架
            seckillActivity.setActivityStatus(1);
            //初始为0
            seckillActivity.setLockStock(0);
            seckillActivity.setSeckillPrice(seckillPrice);
            seckillActivity.setOldPrice(oldPrice);
            seckillActivity.setCreateTime(new Date());
            System.out.println(seckillActivity);
            seckillActivityService.insertSeckillActivity(seckillActivity);
            System.out.println("=====2");
            resultMap.put("seckillActivity", seckillActivity);
            System.out.println("=====3");
            //return "add_skill_activity";
            return "add_goods";

        } catch (Exception e) {
            log.error("addSkillActivityAction error", e);
            return "500";
        }
        //return "add_skill_activity";
    }

}
