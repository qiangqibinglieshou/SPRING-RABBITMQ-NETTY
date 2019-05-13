package com.nsk.cms.common.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DBConnectUtil implements ConnectionSource{

	@Autowired
	private DataSource dataSource;
	
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
//		log.info("获取一个数据库连接");
		return this.dataSource.getConnection();
	}
}
