package com.norman.gevlet.test.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-07 20:08
 */
@Controller
@RequestMapping("/rest")
public class RestTestHandler {

    @Autowired
    private RestTestHandlerBiz biz;


    @RequestMapping("/echo")
    public ResponseBo echo(BaseRequestBo param) {
        System.out.println("RestTestHandler:"+param);
        ResponseBo responseBo = biz.echo(param);
        return responseBo;
    }

}
