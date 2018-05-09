package com.norman.gevlet.client.annotations;

import java.lang.annotation.*;

/**
 * Name:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-07 20:07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestHandler {
    String value() default "";
}
