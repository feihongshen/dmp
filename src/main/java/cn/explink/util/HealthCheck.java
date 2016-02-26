package cn.explink.util;

/**
 * 
 * @author pengfei.wan
 *
 */
public interface HealthCheck {
	
	/**
	 * 健康检查的方法 
	 * level == 1, 检查数据库 
	 * level == 2, 检查数据库、Redis (目前没有实现)
	 * @param level
	 * @return
	 */
	int doCheck(int level);
	
}
