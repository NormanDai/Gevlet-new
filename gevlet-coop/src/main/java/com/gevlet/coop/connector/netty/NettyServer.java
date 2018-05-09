package com.gevlet.coop.connector.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 15:50
 */
public class NettyServer {

    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();// 引导辅助程序
        EventLoopGroup group = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接
        try {
            serverBootstrap.group(group);
            serverBootstrap.channel(NioServerSocketChannel.class);// 设置nio类型的channel
            serverBootstrap.localAddress(new InetSocketAddress(1208));// 设置监听端口
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {//有连接到达时会创建一个channel
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new HttpResponseEncoder());
                    // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                    channel.pipeline().addLast(new HttpRequestDecoder());
                    channel.pipeline().addLast("myHandler", new NettyServerHandlerAdapter() {
                    });
                }
            }).childOption(ChannelOption.SO_KEEPALIVE, true);
//                    .option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = serverBootstrap.bind().sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            f.channel().closeFuture().sync();// 应用程序会一直等待，直到channel关闭
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();//关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }
    public static void main(String[] args) {
        try {
            new NettyServer().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
