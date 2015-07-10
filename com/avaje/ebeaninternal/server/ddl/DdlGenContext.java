package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.DbDdlSyntax;
import com.avaje.ebean.config.dbplatform.DbType;
import com.avaje.ebean.config.dbplatform.DbTypeMap;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.type.ScalarType;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DdlGenContext
{
  private final StringWriter stringWriter = new StringWriter();
  private final DbTypeMap dbTypeMap;
  private final DbDdlSyntax ddlSyntax;
  private final String newLine;
  private final List<String> contentBuffer = new ArrayList();
  private Set<String> intersectionTables = new HashSet();
  private List<String> intersectionTablesCreateDdl = new ArrayList();
  private List<String> intersectionTablesFkDdl = new ArrayList();
  private final DatabasePlatform dbPlatform;
  private final NamingConvention namingConvention;
  private int fkCount;
  private int ixCount;
  
  public DdlGenContext(DatabasePlatform dbPlatform, NamingConvention namingConvention)
  {
    this.dbPlatform = dbPlatform;
    this.dbTypeMap = dbPlatform.getDbTypeMap();
    this.ddlSyntax = dbPlatform.getDbDdlSyntax();
    this.newLine = this.ddlSyntax.getNewLine();
    this.namingConvention = namingConvention;
  }
  
  public DatabasePlatform getDbPlatform()
  {
    return this.dbPlatform;
  }
  
  public boolean isProcessIntersectionTable(String tableName)
  {
    return this.intersectionTables.add(tableName);
  }
  
  public void addCreateIntersectionTable(String createTableDdl)
  {
    this.intersectionTablesCreateDdl.add(createTableDdl);
  }
  
  public void addIntersectionTableFk(String intTableFk)
  {
    this.intersectionTablesFkDdl.add(intTableFk);
  }
  
  public void addIntersectionCreateTables()
  {
    for (String intTableCreate : this.intersectionTablesCreateDdl)
    {
      write(this.newLine);
      write(intTableCreate);
    }
  }
  
  public void addIntersectionFkeys()
  {
    write(this.newLine);
    write(this.newLine);
    for (String intTableFk : this.intersectionTablesFkDdl)
    {
      write(this.newLine);
      write(intTableFk);
    }
  }
  
  public String getContent()
  {
    return this.stringWriter.toString();
  }
  
  public DbTypeMap getDbTypeMap()
  {
    return this.dbTypeMap;
  }
  
  public DbDdlSyntax getDdlSyntax()
  {
    return this.ddlSyntax;
  }
  
  public String getColumnDefn(BeanProperty p)
  {
    DbType dbType = getDbType(p);
    return p.renderDbType(dbType);
  }
  
  private DbType getDbType(BeanProperty p)
  {
    ScalarType<?> scalarType = p.getScalarType();
    if (scalarType == null) {
      throw new RuntimeException("No scalarType for " + p.getFullBeanName());
    }
    if (p.isDbEncrypted()) {
      return this.dbTypeMap.get(p.getDbEncryptedType());
    }
    int jdbcType = scalarType.getJdbcType();
    if ((p.isLob()) && (jdbcType == 12)) {
      jdbcType = 2005;
    }
    return this.dbTypeMap.get(jdbcType);
  }
  
  public DdlGenContext write(String content, int minWidth)
  {
    content = pad(content, minWidth);
    
    this.contentBuffer.add(content);
    
    return this;
  }
  
  public DdlGenContext write(String content)
  {
    return write(content, 0);
  }
  
  public DdlGenContext writeNewLine()
  {
    write(this.newLine);
    return this;
  }
  
  public DdlGenContext removeLast()
  {
    if (!this.contentBuffer.isEmpty()) {
      this.contentBuffer.remove(this.contentBuffer.size() - 1);
    } else {
      throw new RuntimeException("No lastContent to remove?");
    }
    return this;
  }
  
  public DdlGenContext flush()
  {
    if (!this.contentBuffer.isEmpty())
    {
      for (String s : this.contentBuffer) {
        if (s != null) {
          this.stringWriter.write(s);
        }
      }
      this.contentBuffer.clear();
    }
    return this;
  }
  
  private String padding(int length)
  {
    StringBuffer sb = new StringBuffer(length);
    for (int i = 0; i < length; i++) {
      sb.append(" ");
    }
    return sb.toString();
  }
  
  public String pad(String content, int minWidth)
  {
    if ((minWidth > 0) && (content.length() < minWidth))
    {
      int padding = minWidth - content.length();
      return content + padding(padding);
    }
    return content;
  }
  
  public NamingConvention getNamingConvention()
  {
    return this.namingConvention;
  }
  
  public int incrementFkCount()
  {
    return ++this.fkCount;
  }
  
  public int incrementIxCount()
  {
    return ++this.ixCount;
  }
  
  public String removeQuotes(String dbColumn)
  {
    dbColumn = StringHelper.replaceString(dbColumn, this.dbPlatform.getOpenQuote(), "");
    dbColumn = StringHelper.replaceString(dbColumn, this.dbPlatform.getCloseQuote(), "");
    
    return dbColumn;
  }
}
