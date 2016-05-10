package com.pjbest.splitting.routing;

import java.util.Stack;

/**
 * 
 * @author pengfei.wan
 * @date 2016-03-15
 * 
 */
public class DatabaseContextHolder {

	private static final ThreadLocal<Stack<DatabaseType>> contextHolder = new ThreadLocal<Stack<DatabaseType>>();

	public static void pushDatabaseType(DatabaseType databaseType) {
		if (databaseType == null) {
			throw new NullPointerException();
		}
		Stack<DatabaseType> stack = contextHolder.get();
		if (stack == null) {
			stack = new Stack<DatabaseType>();
			contextHolder.set(stack);
		}
		if (stack != null) {
			stack.push(databaseType);
		}
	}

	public static DatabaseType peekDatabaseType() {
		Stack<DatabaseType> stack = contextHolder.get();
		if (stack != null && !stack.isEmpty()) {
			return stack.peek();
		}
		return DatabaseType.MASTER;
	}

	public static void popDatabaseType() {
		Stack<DatabaseType> stack = contextHolder.get();
		if (stack != null) {
			if (!stack.isEmpty()) {
				stack.pop();
			}
			if (stack.isEmpty()) {
				contextHolder.remove();
			}
		}
	}

}
