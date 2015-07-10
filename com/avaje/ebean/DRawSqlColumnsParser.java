package com.avaje.ebean;

import java.util.ArrayList;
import java.util.Arrays;
import javax.persistence.PersistenceException;

final class DRawSqlColumnsParser
{
  private final int end;
  private final String sqlSelect;
  private int pos;
  private int indexPos;
  
  public static RawSql.ColumnMapping parse(String sqlSelect)
  {
    return new DRawSqlColumnsParser(sqlSelect).parse();
  }
  
  private DRawSqlColumnsParser(String sqlSelect)
  {
    this.sqlSelect = sqlSelect;
    this.end = sqlSelect.length();
  }
  
  private RawSql.ColumnMapping parse()
  {
    ArrayList<RawSql.ColumnMapping.Column> columns = new ArrayList();
    while (this.pos <= this.end)
    {
      RawSql.ColumnMapping.Column c = nextColumnInfo();
      columns.add(c);
    }
    return new RawSql.ColumnMapping(columns);
  }
  
  private RawSql.ColumnMapping.Column nextColumnInfo()
  {
    int start = this.pos;
    nextComma();
    String colInfo = this.sqlSelect.substring(start, this.pos++);
    colInfo = colInfo.trim();
    
    String[] split = colInfo.split(" ");
    if (split.length > 1)
    {
      ArrayList<String> tmp = new ArrayList(split.length);
      for (int i = 0; i < split.length; i++) {
        if (split[i].trim().length() > 0) {
          tmp.add(split[i].trim());
        }
      }
      split = (String[])tmp.toArray(new String[tmp.size()]);
    }
    if (split.length == 1) {
      return new RawSql.ColumnMapping.Column(this.indexPos++, split[0], null);
    }
    if (split.length == 2) {
      return new RawSql.ColumnMapping.Column(this.indexPos++, split[0], split[1]);
    }
    if (split.length == 3)
    {
      if (!split[1].equalsIgnoreCase("as"))
      {
        String msg = "Expecting AS keyword parsing column " + colInfo;
        throw new PersistenceException(msg);
      }
      return new RawSql.ColumnMapping.Column(this.indexPos++, split[0], split[2]);
    }
    String msg = "Expecting Max 3 words parsing column " + colInfo + ". Got " + Arrays.toString(split);
    throw new PersistenceException(msg);
  }
  
  private int nextComma()
  {
    boolean inQuote = false;
    while (this.pos < this.end)
    {
      char c = this.sqlSelect.charAt(this.pos);
      if (c == '\'') {
        inQuote = !inQuote;
      } else if ((!inQuote) && (c == ',')) {
        return this.pos;
      }
      this.pos += 1;
    }
    return this.pos;
  }
}
