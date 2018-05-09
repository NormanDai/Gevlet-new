package com.gevlet.coop.core;

import com.alibaba.fastjson.JSON;
import com.gevlet.coop.connector.protocol.NetMessagetProtocol;
import com.gevlet.coop.executor.ServerThreadPoolExecutor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.net.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 20:42
 */
public class HttpRestServerHandler implements ServerHandler {


    // 本应用的类加载器
    private ClassLoader serverClassLoader;

    private static volatile ServerHandler serverHandler;

    private Class<?> applicationContextClass;

    private Object conextInstance;

    private ConcurrentHashMap<String, ServiceWrapper> serviceWrapperMapping = new ConcurrentHashMap<>();

    private HttpRestServerHandler() {
    }

    public static synchronized ServerHandler getServer(ClassLoader classLoader) {
        if (null == serverHandler) {
            serverHandler = new HttpRestServerHandler();
            serverHandler.start(classLoader);
        }
        return serverHandler;
    }


    /**
     * 处理业务请求
     *
     * @param message
     */
    @Override
    public void handleRequest(NetMessagetProtocol message) {


        Thread requestHandlerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isFail = false;
                String errorMsg = "";
                String requestPath = "/" + message.getPathUrl() + "/" + message.getHandlerUrl();
                ServiceWrapper serviceWrapper = serviceWrapperMapping.get(requestPath);
                if (null == serviceWrapper) {
                    isFail = true;
                    errorMsg = "未找到可用的服务！";
                }
                String responseStr = "";
                FullHttpResponse response = null;
                try {
                    Object service = serviceWrapper.getService();
                    Method serviceMethod = serviceWrapper.getServiceMethod();
                    Class<?>[] parameterTypes = serviceMethod.getParameterTypes();
                    if (null != parameterTypes && parameterTypes.length == 1) {
                        Class<?> parameterType = parameterTypes[0];
                        Object requestBody = message.getBody();
                        String jsonString = JSON.toJSONString(requestBody);
                        Object parameter = JSON.parseObject(jsonString, parameterType);
                        Object invokeResult = serviceMethod.invoke(service, parameter);
                        responseStr = JSON.toJSONString(invokeResult);

                    }
                } catch (Exception ex) {
                    isFail = true;
                    errorMsg = ex.getCause().getMessage();
                } finally {
                    if (!isFail) {
                        response = getResponse(responseStr);
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("{\"success\":false,\"error\":\"").append(errorMsg).append("\"}");
                        response = getResponse(builder.toString());
                    }
                    ChannelHandlerContext handlerContext = message.getHandlerContext();
                    handlerContext.write(response);
                    handlerContext.flush();
                }

            }
        });
        ServerThreadPoolExecutor.getExecutor().execute(requestHandlerThread);

    }


    /**
     * 应用服务启动方法
     *
     * @param classLoader
     */
    @Override
    public void start(ClassLoader classLoader) {

        this.serverClassLoader = classLoader;
        Thread serverHandlerThrad = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setContextClassLoader(serverClassLoader);
                try {
                    Class<?> applicationContextClass = classLoader.loadClass("org.springframework.context.support.ClassPathXmlApplicationContext");
                    //设置spring 上下文class
                    setApplicationContextClass(applicationContextClass);
                    Constructor<?> constructor = applicationContextClass.getConstructor(String.class);
                    constructor.setAccessible(true);
                    Object conextInstance = constructor.newInstance("application.xml");
                    //设置spring 上下文实例
                    setConextInstance(conextInstance);
                    //启动spring
                    Method startMethod = applicationContextClass.getMethod("start");
                    startMethod.invoke(conextInstance);

                    Thread.sleep(5);
                    loadServiceMappingWrapper();

                    for (; ; ) {
                        Thread.sleep(1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //启动应用服务
        serverHandlerThrad.start();
    }


    private void loadServiceMappingWrapper() {
        Class mappingAntClass = getTypeRequestMappingAntClass();
        if (null != mappingAntClass) {
            try {
                Method getBeansWithAnnotation = applicationContextClass.getMethod("getBeansWithAnnotation", Class.class);
                getBeansWithAnnotation.setAccessible(true);
                Map<String, Object> requestMapping = (Map<String, Object>) getBeansWithAnnotation.invoke(conextInstance, mappingAntClass);

                //获取到的 requestMapping
                for (Map.Entry<String, Object> entry : requestMapping.entrySet()) {
                    //服务名称
                    String serviceName = entry.getKey();
                    //服务实例
                    Object service = entry.getValue();
                    Annotation requestMappingAnnotation = service.getClass().getAnnotation(mappingAntClass);
                    Method valueMethod = requestMappingAnnotation.getClass().getMethod("value");
                    valueMethod.setAccessible(true);
                    // 类型 RequestMapping 注解值
                    String[] invokeResult = (String[]) valueMethod.invoke(requestMappingAnnotation);
                    Map<String, ServiceWrapper> serviceWrapperMap = getMethodMapping(serviceName, invokeResult[0], service);
                    serviceWrapperMapping.putAll(serviceWrapperMap);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private Map<String, ServiceWrapper> getMethodMapping(String serviceName, String antValueResult, Object service) throws Exception {
        Map<String, ServiceWrapper> methodMapping = new HashMap<>();
        if (null != service) {
            Method[] methods = service.getClass().getMethods();

            Class methodRequestMappingAnt = getMethodRequestMappingAntClass();

            for (Method method : methods) {
                Annotation methodAnnotation = method.getAnnotation(methodRequestMappingAnt);
                if (null != methodAnnotation) {
                    Method valueMethod = methodAnnotation.getClass().getMethod("value");
                    valueMethod.setAccessible(true);
                    String[] invokeResult = (String[]) valueMethod.invoke(methodAnnotation);
                    ServiceWrapper wrapper = new ServiceWrapper();
                    wrapper.setPath(antValueResult + invokeResult[0]);
                    wrapper.setService(service);
                    wrapper.setServiceMethod(method);
                    wrapper.setServiceName(serviceName);
                    methodMapping.put(antValueResult + invokeResult[0], wrapper);
                }
            }
        }
        return methodMapping;
    }


    private Class<?> getTypeRequestMappingAntClass() {
        try {
            Class<?> aClass = serverClassLoader.loadClass("org.springframework.web.bind.annotation.RequestMapping");
            return aClass;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Class<?> getMethodRequestMappingAntClass() {
        try {
            Class<?> aClass = serverClassLoader.loadClass("org.springframework.web.bind.annotation.RequestMapping");
            return aClass;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void setApplicationContextClass(Class<?> contextClass) {
        applicationContextClass = contextClass;
    }


    private void setConextInstance(Object instance) {
        conextInstance = instance;
    }


    private FullHttpResponse getResponse(String resultJson) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.wrappedBuffer(resultJson
                .getBytes()));
        response.headers().set(CONTENT_TYPE, "text/json");
        response.headers().set(CONTENT_LENGTH,
                response.content().readableBytes());
        response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        return response;
    }
}
