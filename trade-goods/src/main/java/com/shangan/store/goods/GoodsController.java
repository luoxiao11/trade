package com.shangan.store.goods;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GoodsController {
    @RequestMapping("/goods/test")
    @ResponseBody
    public String test(){
        return "hello world Tom";
    }
}
