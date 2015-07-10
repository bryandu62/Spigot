package com.avaje.ebean.text.json;

import java.sql.Date;
import java.sql.Timestamp;

public abstract interface JsonValueAdapter
{
  public abstract String jsonFromDate(Date paramDate);
  
  public abstract String jsonFromTimestamp(Timestamp paramTimestamp);
  
  public abstract Date jsonToDate(String paramString);
  
  public abstract Timestamp jsonToTimestamp(String paramString);
}
