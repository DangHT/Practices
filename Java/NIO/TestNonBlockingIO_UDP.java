import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/**
 * DatagramChannel 实例
 *
 * @author dht925nerd@126.com
 */
public class TestNonBlockingIO_UDP {

    @Test
    public void client() throws IOException {

        DatagramChannel datagramChannel = DatagramChannel.open();

        datagramChannel.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        Scanner in = new Scanner(System.in);

        while (in.hasNext()){
            String str = in.next();
            buf.put((LocalDateTime.now().toString() + "\n" + str).getBytes());
            buf.flip();
            datagramChannel.send(buf, new InetSocketAddress("localhost",6789));
            buf.clear();
        }

        datagramChannel.close();
    }

    @Test
    public void server() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();

        datagramChannel.configureBlocking(false);

        datagramChannel.bind(new InetSocketAddress(6789));

        Selector selector = Selector.open();

        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()){
                SelectionKey already = it.next();
                if (already.isReadable()){
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    datagramChannel.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    buf.clear();
                }
            }
            it.remove();
        }
    }

}
