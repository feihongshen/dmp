package cn.explink.util.Tongxing.PublicTool;

import java.util.Arrays;


public class ByteToDate {
	/**
	 * 把一个整数和二个字节组成新的字节数组
	 * @param 字节
	 * @param 字节
	 * @param 整数
	 * @return 完成后的数组
	 */
    public static byte[] CombinationTwo(byte a, byte b, int c)
    {
        byte[] dateOverall = new byte[6];
        dateOverall[0] = a;
        dateOverall[1] = b;
        IntToByte(c, 2, dateOverall);
        return dateOverall;
    }
    /**
     * 从一个字节数组里面取出一个整数
     * @param 起始位置
     * @param 字节数组
     * @return 取到的整数
     */
    public static int ByteToInt(int a, byte[] b)
    {
        byte[] inta = Arrays.copyOfRange(b, a, a+4);
        int dl = byte2int(inta);
        return dl;
    }
    /**
    * int32转换为二进制（4个字节）
    * @param i 待转换的整数
    * @return 返回4字节二进制数
    */
    private static byte[] int2byte(int i){
    byte[] res = new byte[4];
    res[0] = (byte)i;
    res[1] = (byte)(i>>>8);
    res[2] = (byte)(i>>>16);
    res[3] = (byte)(i>>>24);
    return res;
    }
    /**
    * 4字节二进制数转换为int32的整数
    * @param bytes 4字节的二进制数
    * @return int32整数
    */
    private static int byte2int(byte[] bytes){
    int res = (((bytes[3]<<24)>>>24)<<24)|(((bytes[2]<<24)>>>24)<<16)|(((bytes[1]<<24)>>>24)<<8)|((bytes[0]<<24)>>>24);
    return res;
    }
    /**
    * 长整型long转换为8字节的二进制数
    * @param l 长整型long
    * @return 8字节的二进制数
    */
    private static byte[] long2byte(long l){
    byte[] res = new byte[8];
    res[0] = (byte)l;
    res[1] = (byte)(l>>>8);
    res[2] = (byte)(l>>>16);
    res[3] = (byte)(l>>>24);
    res[4] = (byte)(l>>>32);
    res[5] = (byte)(l>>>40);
    res[6] = (byte)(l>>>48);
    res[7] = (byte)(l>>>56);
    return res;
    }
    /**
    * 8字节的二进制数转换为长整型long
    * @param bytes 8字节的二进制数
    * @return 长整型long
    */
    private static long byte2long(byte[] bytes){
    long l0 = bytes[7];
    long l1 = bytes[6];
    long l2 = bytes[5];
    long l3 = bytes[4];
    long l4 = bytes[3];
    long l5 = bytes[2];
    long l6 = bytes[1];
    long l7 = bytes[0];
    long res = (l0<<56)|(((l1<<56)>>>56)<<48)|(((l2<<56)>>>56)<<40)|(((l3<<56)>>>56)<<32)|(((l4<<56)>>>56)<<24)|(((l5<<56)>>>56)<<16)|(((l6<<56)>>>56)<<8)|((l7<<56)>>>56);
    return res;
    }
    /**
     * 从一个字节数组里面取出一个长整数
     * @param a起始位置
     * @param 字节数组
     * @return 长整数
     */
    public static long ByteToLong(int a, byte[] b)
    {
        byte[] inta =Arrays.copyOfRange(b,a,a+8);
        long dl = byte2long(inta);
        return dl;
    }
    /**
     * 把一个整数Copy到一个字节数组的指定位置
     * @param 整数
     * @param 起始位置
     * @param c字节数组
     */
    public static void IntToByte(int a, int b, byte[] c)
    {
        byte[] inta = int2byte(a);
        BytesToBytes(inta,b,c);
    }
    /**
     * 把一个短数组复制到一个长数组的指定位置
     * @param 短数组
     * @param 指定位置
     * @param 长数组
     */
    public static void BytesToBytes(byte[] from,int of,byte[] to)
    {
    	for(byte dd : from)
        {
    		to[of]=dd;
        	of++;
        }
    }
    /**
     * 把一个长整数Copy到一个字节数组的指定位置
     * @param 长整数
     * @param 起始位置
     * @param 字节数组
     */
    public static void IntToByte(long a, int b, byte[] c)
    {
        byte[] inta = long2byte(a);
        BytesToBytes(inta,b,c);
    }
    /**
     * 把一个数组取出指定长度
     * @param 数据
     * @param 长度
     * @param 起始位置
     * @return 返回的数据
     */
    public static byte[] ByteToByte(byte[] a, int b,int index)
    {
            byte[] haveDate =Arrays.copyOfRange(a,index,b);
            return haveDate;
    }
    /**
     * 通过偏移量对一段数据进行加密；把标签和数据当做一个整体；
     * @param 需要加密的数据
     * @param 标签
     * @param 偏移量
     * @return 加密完成的数据
     */
    public static byte[] OffsetEncryption(byte[] date, int sendDateLabel, int offset)
    {
        byte[] dateOverall = new byte[date.length + 4 + offset];
        IntToByte(sendDateLabel, offset, dateOverall);
        BytesToBytes(date,4 + offset,dateOverall);
        return dateOverall;
    }
    /**
     * 通过偏移量对一段数据进行解密；
     * @param 待解密的数据
     * @param 偏移量
     * @return 解密出来的数据和标签形成新的EngineToDate调用了其中的DateByte   SendDateLabel
     */
    public static EngineToDate OffsetDecrypt(byte[] date,int offset)
    {
    	int sendDateLabel = ByteToInt(offset, date);
        byte[] dateOverall = Arrays.copyOfRange(date, 4 + offset, date.length);
        return new EngineToDate(sendDateLabel,dateOverall);
    }
}
