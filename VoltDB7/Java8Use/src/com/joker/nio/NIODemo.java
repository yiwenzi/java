package com.joker.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import static com.joker.util.Print.print;
import static com.joker.util.Print.printnb;
/**
 * Created by hunter on 2017/10/24.
 */
public class NIODemo {
    public static final String PATH = "C:\\Users\\hunter\\Desktop\\Data.txt";
    public static final String PATH2 = "C:\\Users\\hunter\\Desktop\\Data2.txt";
    public static final int BSIZE = 1024;
    public static final int length = 0x8FFFFFF; //128M
    public static void main(String[] args) throws IOException {
        //byteBufferAsCharBuffer();
        //getData();
        //viewBuffers();
        largeMappedFiles();
    }
    public static void largeMappedFiles() throws IOException {
        MappedByteBuffer out = new RandomAccessFile(PATH2, "rw")
                                .getChannel()
                                .map(FileChannel.MapMode.READ_WRITE, 0, length);
        for (int i = 0; i < length; i++) {
            out.put((byte) 'x');
        }
        print("Finished writing");
        for(int i = length/2; i < length/2 + 6; i++) {
            printnb((char)out.get(i));
        }
    }
    public static void symmetricScramle(CharBuffer charBuffer) {
        while (charBuffer.hasRemaining()) {//这里并不是一个一个字符的遍历，只是在计算
            //limit 和 position 之间是否还有元素
            charBuffer.mark();
            char c1 = charBuffer.get();
            char c2 = charBuffer.get();
            charBuffer.reset();
            charBuffer.put(c2).put(c1);
        }
    }
    public static void viewBuffers () {
        ByteBuffer bb = ByteBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, 0, 'a'});
        bb.rewind();

        printnb("Byte Buffer ");
        while (bb.hasRemaining()) {
            printnb(bb.position() + " -> " + bb.get() + ", ");
        }
        print();
        CharBuffer cb = ((ByteBuffer) bb.rewind()).asCharBuffer();
        printnb("Char Buffer ");
        while(cb.hasRemaining())
            printnb(cb.position() + " -> " + cb.get() + ", ");
        print();
        FloatBuffer fb =
                ((ByteBuffer)bb.rewind()).asFloatBuffer();
        printnb("Float Buffer ");
        while(fb.hasRemaining())
            printnb(fb.position()+ " -> " + fb.get() + ", ");
        print();
        IntBuffer ib =
                ((ByteBuffer)bb.rewind()).asIntBuffer();
        printnb("Int Buffer ");
        while(ib.hasRemaining())
            printnb(ib.position()+ " -> " + ib.get() + ", ");
        print();
        LongBuffer lb =
                ((ByteBuffer)bb.rewind()).asLongBuffer();
        printnb("Long Buffer ");
        while(lb.hasRemaining())
            printnb(lb.position()+ " -> " + lb.get() + ", ");
        print();
        ShortBuffer sb =
                ((ByteBuffer)bb.rewind()).asShortBuffer();
        printnb("Short Buffer ");
        while(sb.hasRemaining())
            printnb(sb.position()+ " -> " + sb.get() + ", ");
        print();
        DoubleBuffer db =
                ((ByteBuffer)bb.rewind()).asDoubleBuffer();
        printnb("Double Buffer ");
        while(db.hasRemaining())
            printnb(db.position()+ " -> " + db.get() + ", ");
    }
    //从byteBuffer中获取各种类型
    public static void getData() {
        ByteBuffer bb = ByteBuffer.allocate(BSIZE);
        int i = 0;
        while (i++ < bb.limit()) {//检查自动分配的值是否为0
            if (bb.get() != 0)
                print("nonzero");
        }
        print("i = " + i);
        bb.rewind();
        // Store and read a char array:
        bb.asCharBuffer().put("Howdy!");
        char c;
        while((c = bb.getChar()) != 0)
            printnb(c + " ");
        print();
        bb.rewind();
        // Store and read a short: 获得该缓冲器上的视图
        bb.asShortBuffer().put((short)471142);
        print(bb.getShort());
        bb.rewind();
        // Store and read an int:
        bb.asIntBuffer().put(99471142);
        print(bb.getInt());
        bb.rewind();
        // Store and read a long:
        bb.asLongBuffer().put(99471142);
        print(bb.getLong());
        bb.rewind();
        // Store and read a float:
        bb.asFloatBuffer().put(99471142);
        print(bb.getFloat());
        bb.rewind();
        // Store and read a double:
        bb.asDoubleBuffer().put(99471142);
        print(bb.getDouble());
        bb.rewind();
    }
    public static void byteBufferAsCharBuffer() throws IOException{
        FileChannel fc = new FileOutputStream(PATH).getChannel();
        fc.write(ByteBuffer.wrap("Some text ".getBytes()));
        fc.close();

        fc = new FileInputStream(PATH).getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        buff.flip();
        System.out.println(buff.asCharBuffer());
        buff.rewind();
        String encoding = System.getProperty("file.encoding");
        //对buff输出时进行解码
        System.out.println("Decoding using " + encoding + ": " +
                Charset.forName(encoding).decode(buff));

        // Or, we could encode with something that will print:
        fc = new FileOutputStream(PATH).getChannel();
        //在输入时进行编码
        fc.write(ByteBuffer.wrap("Some text".getBytes("UTF-16BE")));
        fc.close();
        // Now try reading again:
        fc = new FileInputStream(PATH).getChannel();
        buff.clear();
        fc.read(buff);
        buff.flip();
        System.out.println(buff.asCharBuffer());

        fc = new FileOutputStream(PATH).getChannel();
        buff = ByteBuffer.allocate(24); // More than needed
        //直接读入字符
        buff.asCharBuffer().put("Some text");
        fc.write(buff);
        fc.close();
        // Read and display:
        fc = new FileInputStream(PATH).getChannel();
        buff.clear();
        fc.read(buff);
        buff.flip();
        System.out.println(buff.asCharBuffer());
    }
}
