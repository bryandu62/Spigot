package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.util.Set;

public class GenerateDmlRequest
{
  private static final String IS_NULL = " is null";
  private final boolean emptyStringAsNull;
  private final StringBuilder sb = new StringBuilder(100);
  private final Set<String> includeProps;
  private final Set<String> includeWhereProps;
  private final Object oldValues;
  private StringBuilder insertBindBuffer;
  private String prefix;
  private String prefix2;
  private int insertMode;
  private int bindColumnCount;
  
  public GenerateDmlRequest(boolean emptyStringAsNull, Set<String> includeProps, Object oldValues)
  {
    this(emptyStringAsNull, includeProps, includeProps, oldValues);
  }
  
  public GenerateDmlRequest(boolean emptyStringAsNull, Set<String> includeProps, Set<String> includeWhereProps, Object oldValues)
  {
    this.emptyStringAsNull = emptyStringAsNull;
    this.includeProps = includeProps;
    this.includeWhereProps = includeWhereProps;
    this.oldValues = oldValues;
  }
  
  public GenerateDmlRequest(boolean emptyStringAsNull)
  {
    this(emptyStringAsNull, null, null, null);
  }
  
  public GenerateDmlRequest append(String s)
  {
    this.sb.append(s);
    return this;
  }
  
  public boolean isDbNull(Object v)
  {
    return (v == null) || ((this.emptyStringAsNull) && ((v instanceof String)) && (((String)v).length() == 0));
  }
  
  public boolean isIncluded(BeanProperty prop)
  {
    return (this.includeProps == null) || (this.includeProps.contains(prop.getName()));
  }
  
  public boolean isIncludedWhere(BeanProperty prop)
  {
    return (this.includeWhereProps == null) || (this.includeWhereProps.contains(prop.getName()));
  }
  
  public void appendColumnIsNull(String column)
  {
    appendColumn(column, " is null");
  }
  
  public void appendColumn(String column)
  {
    String bind = this.insertMode > 0 ? "?" : "=?";
    appendColumn(column, bind);
  }
  
  public void appendColumn(String column, String suffik)
  {
    appendColumn(column, "", suffik);
  }
  
  public void appendColumn(String column, String expr, String suffik)
  {
    this.bindColumnCount += 1;
    
    this.sb.append(this.prefix);
    this.sb.append(column);
    this.sb.append(expr);
    if (this.insertMode > 0)
    {
      if (this.insertMode++ > 1) {
        this.insertBindBuffer.append(",");
      }
      this.insertBindBuffer.append(suffik);
    }
    else
    {
      this.sb.append(suffik);
    }
    if (this.prefix2 != null)
    {
      this.prefix = this.prefix2;
      this.prefix2 = null;
    }
  }
  
  public int getBindColumnCount()
  {
    return this.bindColumnCount;
  }
  
  public String getInsertBindBuffer()
  {
    return this.insertBindBuffer.toString();
  }
  
  public String toString()
  {
    return this.sb.toString();
  }
  
  public void setWhereMode()
  {
    this.prefix = " and ";
    this.prefix2 = " and ";
  }
  
  public void setWhereIdMode()
  {
    this.prefix = "";
    this.prefix2 = " and ";
  }
  
  public void setInsertSetMode()
  {
    this.insertBindBuffer = new StringBuilder(100);
    this.insertMode = 1;
    this.prefix = "";
    this.prefix2 = ", ";
  }
  
  public void setUpdateSetMode()
  {
    this.prefix = "";
    this.prefix2 = ", ";
  }
  
  public Object getOldValues()
  {
    return this.oldValues;
  }
}
