package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;

public class ScalarTypeBytesVarbinary
  extends ScalarTypeBytesBase
{
  public ScalarTypeBytesVarbinary()
  {
    super(true, -3);
  }
  
  public byte[] read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getBytes();
  }
}
