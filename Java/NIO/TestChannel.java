import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
 * 一. 通道(Channel)
 *    用于源节点与目标节点的连接, 在 Java NIO 中负责缓冲区数据的传输
 *    Channel 本身不存储数据, 因此必须配合 Buffer 使用, Channel=轨道, Buffer=列车
 *
 * 二. Channel 的主要实现类
 *    java.nio.channels.Channel 接口:
 *        |--FileChannel
 *        |--SocketChannel
 *        |--ServerSocketChannel
 *        |--DatagramChannel
 *
 * 三. 获取 Channel
 *    1. Java 针对支持通道的类提供了 getChannel() 方法
 *    2. 在 JDK 1.7 中的 NIO.2 针对各个通道提供了静态方法 open()
 *    3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 *
 * 四. 通道之间的数据传输
 *    transferFrom()
 *    transferTo()
 *
 * 五. 分散(Scatter), 聚集(Gather)
 *    分散读取(Scattering Reads): 将一个 Channel 中的数据分散到多个 Buffer
 *    聚集写入(Gathering Writes): 将多个 Buffer 的数据写入到一个 Channel 中
 *
 * 六. 字符集(Charset)
 *    编码: String -> byte[]
 *    解码: byte[] -> String
 *
 *
 * @author dht925nerd@126.com
 */
public class TestChannel {

    /**
     * 1. 利用 Channel 完成文件的复制
     */
    @Test
    public void test1(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fis = new FileInputStream("dog.jpg");
            fos = new FileOutputStream("temp.jpg");

            //获取通道
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();

            //分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            //将 channel 中的数据存入 buffer
            while (inChannel.read(buf) != -1){
                buf.flip();
                //将 buffer 中的数据写入 channel
                outChannel.write(buf);
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll(inChannel, outChannel, fis, fos);
        }

    }

    /**
     * 2. 使用直接缓冲区完成文件的复制(内存映射文件)
     *    效率很高, 但是不稳定, 因为在物理内存中的文件的控制权属于操作系统
     */
    @Test
    public void test2() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("galaxy.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("temp2.jpg"),
                                                    StandardOpenOption.WRITE,
                                                    StandardOpenOption.READ,
                                                    StandardOpenOption.CREATE_NEW);

        //内存映射文件
        MappedByteBuffer inMappedBuf =
                inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBuf =
                outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接对缓冲区读数据读/写
        byte[] bytes = new byte[inMappedBuf.limit()];
        inMappedBuf.get(bytes);
        outMappedBuf.put(bytes);

        inChannel.close();
        outChannel.close();
    }

    /**
     * 3. 通道之间的数据传输(直接缓冲区)
     */
    @Test
    public void test3() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("galaxy.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("temp2.jpg"),
                StandardOpenOption.WRITE,
                StandardOpenOption.READ,
                StandardOpenOption.CREATE_NEW);

//        inChannel.transferTo(0, inChannel.size(), outChannel);
        outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    /**
     * 4. 分散和聚集
     */
    @Test
    public void test4() throws IOException {
        RandomAccessFile raf1 = new RandomAccessFile("src/TestBuffer.java", "rw");

        //获取通道
        FileChannel channel1 = raf1.getChannel();

        //分配多个缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(50);
        ByteBuffer buf3 = ByteBuffer.allocate(150);
        ByteBuffer buf4 = ByteBuffer.allocate(1024);

        //分散读取
        ByteBuffer[] buffers = {buf1, buf2, buf3, buf4};
        channel1.read(buffers);

        System.out.println(new String(buffers[0].array(), 0, buffers[0].limit()));
        System.out.println("-------------------------");
        System.out.println(new String(buffers[1].array(), 0, buffers[1].limit()));
        System.out.println("-------------------------");
        System.out.println(new String(buffers[2].array(), 0, buffers[2].limit()));
        System.out.println("-------------------------");
        System.out.println(new String(buffers[3].array(), 0, buffers[3].limit()));

        //聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("Test.java", "rw");
        FileChannel channel2 = raf2.getChannel();
        channel2.write(buffers);
    }

    /**
     * 5. 字符集(Charset)
     */
    @Test
    public void test5(){
        int sum = 0;
        Map<String, Charset> charsetMap = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> chartSets = charsetMap.entrySet();
        for (Map.Entry<String, Charset> entry : chartSets){
            sum++;
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("Total: " + sum);
    }

    /**
     * 5.1 编码器(Encoder)与解码器(Decoder)
     */
    @Test
    public void test6() throws CharacterCodingException {
        Charset charset = Charset.forName("UTF-8");

        //获取编码器
        CharsetEncoder encoder = charset.newEncoder();

        //获取解码器
        CharsetDecoder decoder = charset.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("中华人民共和国万岁!");
        charBuffer.flip();

        //编码
        ByteBuffer buf = encoder.encode(charBuffer);
        for (int i = 0; i < buf.limit(); i++){
            System.out.println(buf.get());
        }
        System.out.println("-------------------------------");

        //解码
        buf.flip();
        CharBuffer cbf = decoder.decode(buf);
        System.out.println(cbf.toString());
    }

    private void closeAll(Channel inChannel, Channel outChannel,
                          FileInputStream inStream, FileOutputStream outStream){
        if (inChannel != null){
            try {
                inChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outChannel != null){
            try {
                outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inStream != null){
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outStream != null){
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
