package com.avaje.ebeaninternal.server.persist.dml;

import java.sql.SQLException;

public abstract interface PersistHandler
{
  public abstract String getBindLog();
  
  public abstract void bind()
    throws SQLException;
  
  public abstract void addBatch()
    throws SQLException;
  
  public abstract void execute()
    throws SQLException;
  
  public abstract void close()
    throws SQLException;
}
