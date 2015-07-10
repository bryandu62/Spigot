package com.avaje.ebeaninternal.server.persist;

import java.sql.SQLException;

public abstract interface BatchPostExecute
{
  public abstract void checkRowCount(int paramInt)
    throws SQLException;
  
  public abstract void setGeneratedKey(Object paramObject);
  
  public abstract void postExecute()
    throws SQLException;
}
