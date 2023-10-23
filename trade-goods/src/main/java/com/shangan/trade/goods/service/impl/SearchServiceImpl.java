package com.shangan.trade.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.db.model.Goods;
import com.shangan.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public void addGoodsToES(Goods goods) {
        try {
            String data = JSON.toJSONString(goods);
            IndexRequest request = new IndexRequest("goods").source(data, XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info("addGoodsToES goods:{} result:{}", data, response);
        } catch (Exception e) {
            log.error("SearchService addGoods error", e);
        }
    }

    @Override
    public List<Goods> searchGoodsList(String keyword, int from, int size) {
        try {
            //构建查询请求，指定查询的索引库
            SearchRequest searchRequest = new SearchRequest("goods");

            //创建查询条件构造器 SearchSourceBuilder
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "description", "keywords");
            //指定查询条件
            searchSourceBuilder.query(queryBuilder);

            /*
             * 指定分页查询信息
             * 从哪里开始查
             */
            searchSourceBuilder.from(from);
            //每次查询的数量
            searchSourceBuilder.size(size);

            /*
             * 设置排序规则
             * 按照销量排序
             */
            searchSourceBuilder.sort("saleNum", SortOrder.DESC);

            searchRequest.source(searchSourceBuilder);

            //查询获取查询结果
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(JSON.toJSONString(searchResponse));

            //获取命中对象
            SearchHits searchHits = searchResponse.getHits();
            long totalNum = searchHits.getTotalHits().value;
            log.info("serarch 总记录数： {}", totalNum);

            List<Goods> goodsList = new ArrayList<>();
            //获取命中的hits数据,搜索结果数据
            SearchHit[] hits = searchHits.getHits();
            for (SearchHit searchHit : hits) {
                //获取json字符串格式的数据
                String sourceAsString = searchHit.getSourceAsString();
                Goods goods = JSON.parseObject(sourceAsString, Goods.class);
                goodsList.add(goods);
            }
            log.info("serarch result {}", JSON.toJSONString(goodsList));
            return goodsList;
        } catch (Exception e) {
            log.error("SearchService searchGoodsList error", e);
            return null;
        }
    }
}
