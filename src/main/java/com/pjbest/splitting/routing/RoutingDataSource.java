package com.pjbest.splitting.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @author pengfei.wan
 * @date 2016-03-15
 * 
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

	private static final Logger logger = LoggerFactory.getLogger(RoutingDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		logger.debug(">>> RoutingDataSource determineCurrentLookupKey thread : {}", Thread.currentThread().getName());
		logger.debug(">>> RoutingDataSource : {}", DatabaseContextHolder.peekDatabaseType());
		return DatabaseContextHolder.peekDatabaseType();
	}

}
