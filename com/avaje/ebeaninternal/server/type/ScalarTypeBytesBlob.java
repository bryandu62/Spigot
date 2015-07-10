package com.avaje.ebeaninternal.server.type;

import java.sql.SQLException;

public class ScalarTypeBytesBlob
  extends ScalarTypeBytesBase
{
  public ScalarTypeBytesBlob()
  {
    super(true, 2004);
  }
  
  public byte[] read(DataReader dataReader)
    throws SQLException
  {
    return dataReader.getBlobBytes();
  }
}
