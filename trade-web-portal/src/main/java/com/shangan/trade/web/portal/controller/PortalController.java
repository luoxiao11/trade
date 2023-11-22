package com.shangan.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.common.model.TradeResultDTO;
import com.shangan.trade.common.utils.RedisWorker;
import com.shangan.trade.web.portal.client.GoodsFeignClient;
import com.shangan.trade.web.portal.client.OrderFeignClient;
import com.shangan.trade.web.portal.client.SeckillActivityFeignClient;
import com.shangan.trade.web.portal.client.model.Goods;
import com.shangan.trade.web.portal.client.model.Order;
import com.shangan.trade.web.portal.client.model.SeckillActivity;
import com.shangan.trade.web.portal.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.rmi.CORBA.Util;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PortalController {
    @Autowired
    private GoodsFeignClient goodsFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private SeckillActivityFeignClient seckillActivityFeignClient;

    @Autowired
    private RedisWorker redisWorker;
    /**
     * 商品详情页
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/goods/{goodsId}")
    public ModelAndView itemPage(@PathVariable long goodsId) {
        Goods goods = goodsFeignClient.queryGoodsById(goodsId);
        log.info("goodsId={},good s={}", goodsId, JSON.toJSON(goods));
        String showPrice = CommonUtils.changeF2Y(goods.getPrice());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("goods", goods);
        modelAndView.addObject("showPrice", showPrice);
        modelAndView.setViewName("goods_detail");
        return modelAndView;
    }


    /**
     * 跳转到搜索页
     *
     * @return
     */
    @RequestMapping("/search")
    public String searchPage() {
        return "search";
    }

    /**
     * 搜索查询
     *
     * @return
     */
    @RequestMapping("/searchAction")
    public String search(@RequestParam("searchWords") String searchWords, Map<String, Object> resultMap) {
        log.info("search searchWords:{}", searchWords);
        List<Goods> goodsList = goodsFeignClient.searchGoodsList(searchWords, 0, 10);
        resultMap.put("goodsList", goodsList);
        return "search";
    }

    /**
     * 购买请求处理
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @RequestMapping("/buy/{userId}/{goodsId}")
    public String buy(Map<String, Object> resultMap, @PathVariable long userId, @PathVariable long goodsId) {
        log.info("userId={}, goodsId={}", userId, goodsId);
        try {
            Order order = orderFeignClient.createOrder(userId, goodsId);
            log.info(order.toString());
            resultMap.put("order", order);
            resultMap.put("resultInfo", "下单成功");
        } catch (Exception e) {
            resultMap.put("resultInfo", "下单失败,原因" + e.getMessage());
            log.error("buy error", e);

            resultMap.put("errorInfo", e.getMessage());
        }
        return "buy_result";
    }
    @RequestMapping("/order/query/{orderId}")
    public String orderQuery(Map<String, Object> resultMap, @PathVariable long orderId) {
        Order order = orderFeignClient.queryOrder(orderId);
        log.info("orderId={} order={}", orderId, JSON.toJSON(order));
        String orderShowPrice = CommonUtils.changeF2Y(order.getPayPrice());
        resultMap.put("order", order);
        resultMap.put("orderShowPrice", orderShowPrice);
        return "order_detail";
    }
    /**
     * 订单支付
     *
     * @return
     */
    @RequestMapping("/order/payOrder/{orderId}")
    public String payOrder(Map<String, Object> resultMap, @PathVariable long orderId) throws Exception {
        try {
            orderFeignClient.payOrder(orderId);
            return "redirect:/order/query/" + orderId;
        } catch (Exception e) {
            log.error("payOrder error,errorMessage:{}", e.getMessage());
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        }
    }

    /**
     * 秒杀活动详情页
     *
     * @param resultMap
     * @param seckillId
     * @return
     */
    @RequestMapping("/seckill/{seckillId}")
    public String seckillInfo(Map<String, Object> resultMap, @PathVariable long seckillId) {
        long startTime = System.nanoTime();
        try {
            SeckillActivity seckillActivity;
            String seckillActivityInfo = redisWorker.getValueByKey("seckillActivity:" +seckillId);
            if(!StringUtils.isEmpty(seckillActivityInfo)) {
                //从redis查询到数据
                seckillActivity = JSON.parseObject(seckillActivityInfo, SeckillActivity.class);
                log.info("命中秒杀活动缓存:{}", seckillActivityInfo);

            } else {
                seckillActivity = seckillActivityFeignClient.querySeckillActivityById(seckillId);
            }
            if (seckillActivity == null) {
                log.error("秒杀的对应的活动信息 没有查询到 seckillId:{} ", seckillId);
                throw new RuntimeException("秒杀的对应的活动信息 没有查询到");
            }
            log.info("seckillId={},seckillActivity={}", seckillId, JSON.toJSON(seckillActivity));
            String seckillPrice = CommonUtils.changeF2Y(seckillActivity.getSeckillPrice());
            String oldPrice = CommonUtils.changeF2Y(seckillActivity.getOldPrice());

            // 查询商品信息
            Goods goods;
            String goodsInfo = redisWorker.getValueByKey("seckillActivity_goods:" + seckillActivity.getGoodsId());
            if (!StringUtils.isEmpty(goodsInfo)) {
                //从redis查询到数据
                goods = JSON.parseObject(goodsInfo, Goods.class);
                log.info("命中商品缓存:{}", goodsInfo);
            } else {
                goods = goodsFeignClient.queryGoodsById(seckillActivity.getGoodsId());
            }
            if (goods == null) {
                log.error("秒杀的对应的商品信息 没有查询到 seckillId:{} goodsId:{}", seckillId, seckillActivity.getGoodsId());
                throw new RuntimeException("秒杀的对应的商品信息 没有查询到");
            }

            resultMap.put("seckillActivity", seckillActivity);
            resultMap.put("seckillPrice", seckillPrice);
            resultMap.put("oldPrice", oldPrice);
            resultMap.put("goods", goods);
            return "seckill_item";
        } catch (Exception e) {
            log.error("获取秒杀信息详情页失败 get seckillInfo error,errorMessage:{}", e.getMessage());
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        } finally {
            long endTime = System.nanoTime();
            log.info("seckillInfo process time : {}", endTime - startTime);


        }
    }
    /**
     * 获取秒杀活动列表
     *
     * @param resultMap
     * @return
     */
    @RequestMapping("/seckill/list")
    public String activityList(Map<String, Object> resultMap) {
        List<SeckillActivity> seckillActivities = seckillActivityFeignClient.queryActivityByStatus(1);
        resultMap.put("seckillActivities", seckillActivities);
        return "seckill_activity_list";
    }

    @RequestMapping("/seckill/buy/{userId}/{seckillId}")
    public ModelAndView seckill(@PathVariable long userId, @PathVariable long seckillId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            TradeResultDTO<Order> result = seckillActivityFeignClient.processSeckill(userId, seckillId);
            if (result.getCode() == 200) {
                modelAndView.addObject("resultInfo", "秒杀抢购成功");
                modelAndView.addObject("order", result.getData());
                modelAndView.setViewName("buy_result");
            } else {
                modelAndView.addObject("errorInfo", result.getErrorMessage());
                modelAndView.setViewName("error");
            }

        } catch (Exception e) {
            modelAndView.addObject("errorInfo", e.getMessage());
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }


    /**
     * 秒杀活动静态详情页
     *
     * @param seckillId
     * @return
     */
    @RequestMapping("/seckill/static/{seckillId}")
    public String seckillInfoStatic (@PathVariable long seckillId){
        long startTime = System.nanoTime();
        try {
            return "seckill_item_" + seckillId;
        }finally {
            long endTime = System.nanoTime();
            log.info("seckillInfo process time : {}", endTime - startTime);
        }
    }
}
