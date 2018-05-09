package com.norman.gevlet.test.app;

import org.springframework.stereotype.Service;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-09 10:23
 */

@Service
public class RestTestHandlerBiz {


    public ResponseBo echo(BaseRequestBo param){
        System.out.println("RestTestHandlerBiz:"+param);
        ResponseBo<BaseRequestBo> boResponseBo = new ResponseBo<>();
        boResponseBo.setResult(param);
        return boResponseBo;
    }
}
