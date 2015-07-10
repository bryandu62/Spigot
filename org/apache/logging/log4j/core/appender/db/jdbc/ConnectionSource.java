package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public abstract interface ConnectionSource
{
  public abstract Connection getConnection()
    throws SQLException;
  
  public abstract String toString();
}
