package com.avaje.ebean.config;

import java.sql.PreparedStatement;

public abstract interface PstmtDelegate
{
  public abstract PreparedStatement unwrap(PreparedStatement paramPreparedStatement);
}
