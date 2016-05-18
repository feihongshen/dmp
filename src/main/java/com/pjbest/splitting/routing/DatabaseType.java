package com.pjbest.splitting.routing;

/**
 * 
 * @author pengfei.wan
 * @date 2016-03-15
 * 
 */
public enum DatabaseType {
	/**
	 * 主库，可进行读写操作
	 */
	MASTER,
	/**
	 * 从库，只能进行读操作
	 */
	REPLICA
}
