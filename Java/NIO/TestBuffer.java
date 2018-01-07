import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * 一. 缓冲区(Buffer):
 *    在 Java NIO 中负责数据的存取, 缓冲区就是数组, 用于存储不同类型的数据
 *    根据数据类型的不同(boolean 除外), 提供了相应类型的缓冲区:
 *    ByteBuffer (最常用)
 *    CharBuffer
 *    IntBuffer
 *    FloatBuffer
 *    DoubleBuffer
 *    上述缓冲区的管理方式几乎一致, 都是通过 allocate() 获取缓冲区
 *
 * 二. 缓冲区存取数据的两个核心方法
 *    put(): 存入数据到缓冲区
 *    get(): 从缓冲区取出数据
 *
 * 三. 缓冲区中的四大属性:
 *    capacity: 缓冲区的最大存储容量, 一旦声明不能改变
 *    limit: 标识缓冲区中可以操作的数据大小 (limit 后的数据不能读/写)
 *    position: 当前正在操作的位置
 *    mark: 记录当前 position 的位置, 可以通过 reset() 回到 mark 位置
 *
 *    0 <= mark <= position <= limit <= capacity
 *
 * 四. 直接缓冲区与非直接缓冲区
 *    非直接缓冲区: 通过 allocate() 分配缓冲区, 将缓冲区建立在 JVM 的内存中
 *    直接缓冲区: 通过 allocateDirect() 分配缓冲区, 将缓冲区建立在物理内存中(只适用于 ByteBuffer)
 * @author dht925nerd@126.com
 */
public class TestBuffer {

    @Test
    public void test1(){
        String str = "abcde";

        //1. 分配指定大小Buffer
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("--------allocation()--------");
        System.out.println("capacity: " + buf.capacity());
        System.out.println("limit: " + buf.limit());
        System.out.println("position: " + buf.position());

        //2. 利用 put() 存入数据
        buf.put(str.getBytes());
        System.out.println("------------put()-----------");
        System.out.println("capacity: " + buf.capacity());
        System.out.println("limit: " + buf.limit());
        System.out.println("position: " + buf.position());

        //3. 使用 flip() 切换到读数据模式
        buf.flip();
        System.out.println("-----------flip()-----------");
        System.out.println("capacity: " + buf.capacity());
        System.out.println("limit: " + buf.limit());
        System.out.println("position: " + buf.position());

        //4. 使用 get() 读取缓冲区中的数据
        System.out.println("------------get()-----------");
        byte[] bytes = new byte[buf.limit()];
        buf.get(bytes);
        System.out.println(new String(bytes, 0, bytes.length));
        System.out.println("capacity: " + buf.capacity());
        System.out.println("limit: " + buf.limit());
        System.out.println("position: " + buf.position());

        //5. 使用 rewind() 可以重复读数据
        buf.rewind();
        System.out.println("----------rewind()-----------");
        System.out.println("capacity: " + buf.capacity());
        System.out.println("limit: " + buf.limit());
        System.out.println("position: " + buf.position());

        //6. 使用 clear() 清空缓冲区(但是缓冲区的数据还在, 只是处于“遗弃”状态)
        buf.clear();
        System.out.println("-----------clear()-----------");
        System.out.println("capacity: " + buf.capacity());
        System.out.println("limit: " + buf.limit());
        System.out.println("position: " + buf.position());
    }
}
