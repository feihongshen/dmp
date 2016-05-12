package cn.explink.util.Tongxing.PublicTool;

import java.util.Random;


/**
 * 
 * @author 一共公共静态类;有随机数啊 延迟等
 *
 */
public class RandomPublic {
	private static Random _randomNumber = new Random(500);
    private static Random _randomTime = new Random();
    /**
     * 根据指定种子取一个随机数
     * @param 最大值
     * @return 随机数
     */
    public static int RandomNumber(int number)
    {
        return _randomNumber.nextInt(number);
    }
    /**
     * 根据时间为种子取一个随机数
     * @param 最大值
     * @return 随机数
     */
    public static int RandomTime(int number)
    {
        return _randomTime.nextInt(number);
    }
    /**
     * 延迟多久;单位毫秒
     * @param 多少毫秒
     */
    public static void TheadSleep(int millise)
    {
    	try {
			Thread.sleep(millise);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    }
}