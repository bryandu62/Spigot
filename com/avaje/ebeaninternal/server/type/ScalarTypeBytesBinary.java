package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;

public class ScalarTypeBytesBinary
  extends ScalarTypeBytesBase
{
  public ScalarTypeBytesBinary()
  {
    super(true, -2);
  }
  
  public byte[] read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getBytes();
  }
}
