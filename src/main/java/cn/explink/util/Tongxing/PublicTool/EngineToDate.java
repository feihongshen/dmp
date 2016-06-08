package cn.explink.util.Tongxing.PublicTool;
/**
 * 
 * @author 一个普通的工具类,对解码和外部类起到一个桥梁的作用
 *
 */
public class EngineToDate {
	public byte State = 0;//状态码
	public byte[] DateByte = null;//字节数据
    public String Datestring = null;//文本数据
    public int SendDateLabel = 0;//发送数据的标签
    public byte[] ReplyDate = null;//直接回复的数据
    /**
     * 发送文本数据的时候需要用到的
     * @param 文本代码
     * @param 文本内容
     */
    public EngineToDate(byte i, String str)
    {
        State = i;
        Datestring = str;
    }
    /**
     * 当接收到正确的文本数据的时候要用到的
     * @param 文件代号
     * @param 文本内容
     * @param 回复的数据
     */
    public EngineToDate(byte i, String str,byte[] replyDate)
    {
        State = i;
        Datestring = str;
        ReplyDate = replyDate;
    }
    /**
     * 当接收到正确的字节集数据的时候要用到的
     * @param i
     * @param dateByte
     * @param replyDate
     */
    public EngineToDate(byte i, byte[] dateByte, byte[] replyDate)
    {
        State = i;
        DateByte = dateByte;
        ReplyDate = replyDate;
    }
    /**
     * 用于只有状态码和字节集
     * @param 归类2,3
     * @param b字节数组b
     */
    public EngineToDate(byte i,byte[] b)
    {
        State = i;
        DateByte = b;
    }
    /**
     * 对数据进行直接回复
     * @param 字节集
     */
    EngineToDate(byte[] replyDate)
    {
        ReplyDate = replyDate;
    }
    /**
     * 用与只有已发数据和编号
     * @param 编号
     * @param 已发数据
     */
    EngineToDate(int Label, byte[] dateByte)
    {
        SendDateLabel = Label;
        DateByte = dateByte;
    }
    /**
     * 用与状态码和已发数据编号
     * @param state
     * @param Label
     */
    EngineToDate(byte state,int Label)
    {
        State = state;
        SendDateLabel = Label;
    }
    /**
     * 有些不需要数据
     * @param 归类3,4
     */
    public EngineToDate(byte i)
    {
        State = i;
    }
}
