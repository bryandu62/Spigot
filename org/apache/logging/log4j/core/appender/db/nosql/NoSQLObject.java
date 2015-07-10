package org.apache.logging.log4j.core.appender.db.nosql;

public abstract interface NoSQLObject<W>
{
  public abstract void set(String paramString, Object paramObject);
  
  public abstract void set(String paramString, NoSQLObject<W> paramNoSQLObject);
  
  public abstract void set(String paramString, Object[] paramArrayOfObject);
  
  public abstract void set(String paramString, NoSQLObject<W>[] paramArrayOfNoSQLObject);
  
  public abstract W unwrap();
}
