package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;

public abstract interface ScalarDataReader<T>
{
  public abstract T read(DataReader paramDataReader)
    throws SQLException;
  
  public abstract void loadIgnore(DataReader paramDataReader);
  
  public abstract void bind(DataBind paramDataBind, T paramT)
    throws SQLException;
  
  public abstract void accumulateScalarTypes(String paramString, CtCompoundTypeScalarList paramCtCompoundTypeScalarList);
}
