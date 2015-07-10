package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;

public class ScalarTypeLongVarchar
  extends ScalarTypeClob
{
  public ScalarTypeLongVarchar()
  {
    super(true, -1);
  }
  
  public String read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getStringFromStream();
  }
}
