import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * 一. 使用 NIO 完成网络通信的三个核心:
 *    1. 通道(Channel): 负责连接
 *       java.nio.channels.Channel (Interface)
 *           |--SelectableChannel  (Abstract Class)
 *              |--SocketChannel
 *              |--ServerSocketChannel
 *              |--DatagramChannel
 *              |--Pipe.SinkChannel
 *              |--Pipe.SourceChannel
 *       FileChannel不能切换到非阻塞模式
 *
 *    2. 缓冲区(Buffer): 负责数据存取
 *    3. 选择器(Selector): 是 SelectableChannel 的多路复用器, 用于监控 SelectableChannel 的 IO 状况
 *
 * @author dht925nerd@126.com
 */
public class TestNonBlockingIO_TCP {

    @Test
    public void client() throws IOException {
        //1. 获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 6789));

        //2. 切换到 Non-Blocking 模式
        socketChannel.configureBlocking(false);

        //3. 分配 Buffer
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //4. 发送给 Server
        buf.put(LocalDateTime.now().toString().getBytes());
        buf.flip();
        socketChannel.write(buf);
        buf.clear();

        //5. 关闭通道
        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        //1. 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //2. 切换到 Non-Blocking 模式
        serverSocketChannel.configureBlocking(false);

        //3. 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(6789));

        //4. 获取 Selector
        Selector selector = Selector.open();

        //5. 将 Selector 注册到 Channel 上, 并且指定监听 “ACCEPT(接收)” 事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6. 轮询式的获取 Selector 上的已经 already 的事件
        while (selector.select() > 0){
            //7. 获取当前 Selector 中所有注册的“选择键(已就绪监听事件)”
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()){
                //8. 获取准备就绪事件
                SelectionKey already = it.next();

                //9. 判断具体是是什么事件就绪
                if(already.isAcceptable()){
                    //10. 若是"ACCEPT", 获取连接
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    //11. 切换 Non-Blocking 模式
                    socketChannel.configureBlocking(false);

                    //12. 注册 Selector
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }else if (already.isReadable()){
                    //13. 获取当前 Selector 上的"读就绪"状态通道
                    SocketChannel socketChannel = (SocketChannel) already.channel();

                    //14. 读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = socketChannel.read(buf)) > 0){
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }
                //15. 取消 SelectionKey
                it.remove();
            }
        }
    }
}
