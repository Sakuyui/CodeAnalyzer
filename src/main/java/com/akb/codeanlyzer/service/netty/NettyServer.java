package com.akb.codeanlyzer.service.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        EventLoopGroup group = new NioEventLoopGroup();
        //负责处理数据传输的工作线程。线程数默认为CPU核心数乘以2
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            //在ServerChannelInitializer中初始化ChannelPipeline责任链，并添加到serverBootstrap中
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                    //添加HTTP编解码
                    channel.pipeline().addLast("decoder", new HttpRequestDecoder());
                    channel.pipeline().addLast("encoder", new HttpResponseEncoder());
                    //消息聚合器，将消息聚合成FullHttpRequest
                    channel.pipeline().addLast("aggregator", new HttpObjectAggregator(1024*1024*5));
                    //支持大文件传输
                    channel.pipeline().addLast("chunked", new ChunkedWriteHandler());
                    //自定义Handler
                    //channel.pipeline().addLast("dispatchHandler", new DispatchHandler());
                    //channel.pipeline().addLast("httpHandler", new HttpHandler());
                   // channel.pipeline().addLast("webSocketHandler", new WebSocketHandler());
                  // channel.pipeline().addLast("fileUploadHandler", new FileUploadHandler());
                }
            });
            //标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            //Netty4使用对象池，重用缓冲区
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            //是否启用心跳保活机制
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //禁止使用Nagle算法，便于小数据即时传输
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

            //绑定端口后，开启监听
            ChannelFuture future = bootstrap.bind(port).sync();
            future.addListener(f -> {
                if (f.isSuccess()) {
                    System.out.println("服务启动成功");
                } else {
                    System.out.println("服务启动失败");
                }
            });
            //等待服务监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            //释放资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
