import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 阻塞式 IO 实例
 *
 * @author dht925nerd@126.com
 */
public class TestBlockingIO {

    @Test
    public void client() throws IOException {
        //1. 获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 6789));
        FileChannel inChannel = FileChannel.open(Paths.get("galaxy.jpg"), StandardOpenOption.READ);

        //2. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //3. 读取本地文件, 并发送到 Server
        while (inChannel.read(buf) != -1) {
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
        }

        //4. 通知 Server 数据已经发送完毕, 否则线程将持续阻塞(另一种解决方式就是非阻塞式)
        socketChannel.shutdownOutput();

        //5. 接收 Server 反馈
        int len = 0;
        while ((len = socketChannel.read(buf)) != -1) {
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }

        //6. 关闭通道
        inChannel.close();
        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        //1. 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel outChannel = FileChannel.open(Paths.get("new_galaxy.jpg"),
                                                StandardOpenOption.WRITE,
                                                StandardOpenOption.CREATE);

        //2. 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(6789));

        //3. 获取 Client 连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        //4. 分配指定大小的 Buffer
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //5. 接收 Client 数据, 并保存到本地
        while (socketChannel.read(buf) != -1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        //6. 向 Client 发送反馈
        buf.put("Transmit complete!".getBytes());
        buf.flip();
        socketChannel.write(buf);

        //7. 关闭通道
        serverSocketChannel.close();
        socketChannel.close();
        outChannel.close();
    }

}
