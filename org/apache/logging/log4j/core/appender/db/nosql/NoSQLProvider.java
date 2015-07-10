package org.apache.logging.log4j.core.appender.db.nosql;

public abstract interface NoSQLProvider<C extends NoSQLConnection<?, ? extends NoSQLObject<?>>>
{
  public abstract C getConnection();
  
  public abstract String toString();
}
