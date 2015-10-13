package cn.explink.b2c.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 把堆栈异常转化为字符串，以便存入日志中。
 * 
 * @author Administrator
 *
 */
public class ExceptionTrace {

	/**
	 * 输出堆栈异常
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionTrace(Throwable e, String extraContent) {
		if (e != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return extraContent + sw.toString();
		}
		return "No Exception";
	}

}
