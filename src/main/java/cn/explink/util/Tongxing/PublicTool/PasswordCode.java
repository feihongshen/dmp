package cn.explink.util.Tongxing.PublicTool;
/**
 * 
 * @author 一个数据交换暗号的常量中心
 *
 */
public class PasswordCode {
       //[start] 普通文件部分 EncryptionDecrypt使用
	/**
	 * 发送普通信息的代码
	 */
       public final static byte _commonCode = 33;
       /**
   	 * 发送文本的代码
   	 */
        public final static byte _textCode = 34;
        /**
       	 * 发送的图片的代码
       	 */
        public final static byte _photographCode = 35;
        /**
       	 * 数据已经发送成功的代码;
       	 */
        public final static byte _dateSuccess = 36;
       //[end]
        //[start] 大数据处理部分 EncryptionDecryptFileTool使用
        /**
       	 *分包数据使用的代码
       	 */
        public final static byte _bigDateCode = 37;
        /**
       	 *大文件的包头代码
       	 */
        public final static byte _fileHeadCode = 38;
        /**
       	 *大文件的主体代码
       	 */
        public final static byte _fileSubjectCode = 39;
        /**
       	 *同意接收文件的代码
       	 */
        public final static byte _fileAgreeReceive = 40;
        /**
       	 * 一般验证需要用的代号
       	 */
        public final static byte _verificationCode = 41;
        /**
       	 * 发送心跳的代码
       	 */
        public final static byte _heartbeatCode = 42;
        /**
       	 * 服务器发给客户端的登录信息
       	 */
        public final static byte _serverToClient = 43;
        /**
       	 * 客户端回给服务器的登录信息
       	 */
        public final static byte _clientToServer = 44;
        /**
       	 * 客户端收到这个信息就会不重连的关掉
       	 */
        public final static byte _clientCloseCode = 45;
        //[end]
        //[start] 粘包处理中心的代码 StickPackage使用
        /**
       	 * TCP粘包协议的代码
       	 */
        public final static int _stickPackageCode = 14354;
        //[end]
        //[start] 文件处理部分 EncryptionDecryptFile使用
        /**
       	 * 文件的标示符号
       	 */
        public final static byte _fileCode = 46;
        /**
       	 * 对方拒绝
       	 */
        public final static byte _fileRefuse = 47;
        /**
       	 * 对方同意
       	 */
        public final static byte _fileOk = 48;
        /**
       	 * 发送方的数据
       	 */
        public final static byte _sendUser = 49;
        /**
       	 * 接收方的数据
       	 */
        public final static byte _receiveUser = 50;
        /**
       	 * 暂停发送
       	 */
        public final static byte _sendStop = 51;
       /**
       * 对方文件已经取消
       */
        public final static byte _fileCancel = 52;
       /**
       * 代表是续传数据的代码
       */
        public final static byte _fileContinue = 53;
       /**
       * 同意续传
       */
        public final static byte _fileContinueOk = 54;
       /**
       * 拒绝续传
       */
        public final static byte _fileContinueNo = 55;
        //[end]
   }
