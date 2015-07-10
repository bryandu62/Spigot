package com.avaje.ebeaninternal.server.jdbc;

import com.avaje.ebean.config.PstmtDelegate;
import com.avaje.ebeaninternal.server.lib.sql.ExtendedPreparedStatement;
import java.sql.PreparedStatement;

public class StandardPstmtDelegate
  implements PstmtDelegate
{
  public PreparedStatement unwrap(PreparedStatement pstmt)
  {
    return ((ExtendedPreparedStatement)pstmt).getDelegate();
  }
}
