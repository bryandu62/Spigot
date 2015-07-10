package com.avaje.ebean.config.dbplatform;

public class DbDdlSyntax
{
  private boolean renderIndexForFkey = true;
  private boolean inlinePrimaryKeyConstraint = false;
  private boolean addOneToOneUniqueContraint = false;
  private int maxConstraintNameLength = 32;
  private int columnNameWidth = 25;
  private String dropTableCascade;
  private String dropIfExists;
  private String newLine = "\r\n";
  private String identity = "auto_increment";
  private String pkPrefix = "pk_";
  private String disableReferentialIntegrity;
  private String enableReferentialIntegrity;
  private String foreignKeySuffix;
  
  public String getPrimaryKeyName(String tableName)
  {
    String pk = this.pkPrefix + tableName;
    if (pk.length() > this.maxConstraintNameLength) {
      pk = pk.substring(0, this.maxConstraintNameLength);
    }
    return pk;
  }
  
  public String getIdentity()
  {
    return this.identity;
  }
  
  public void setIdentity(String identity)
  {
    this.identity = identity;
  }
  
  public int getColumnNameWidth()
  {
    return this.columnNameWidth;
  }
  
  public void setColumnNameWidth(int columnNameWidth)
  {
    this.columnNameWidth = columnNameWidth;
  }
  
  public String getNewLine()
  {
    return this.newLine;
  }
  
  public void setNewLine(String newLine)
  {
    this.newLine = newLine;
  }
  
  public String getPkPrefix()
  {
    return this.pkPrefix;
  }
  
  public void setPkPrefix(String pkPrefix)
  {
    this.pkPrefix = pkPrefix;
  }
  
  public String getDisableReferentialIntegrity()
  {
    return this.disableReferentialIntegrity;
  }
  
  public void setDisableReferentialIntegrity(String disableReferentialIntegrity)
  {
    this.disableReferentialIntegrity = disableReferentialIntegrity;
  }
  
  public String getEnableReferentialIntegrity()
  {
    return this.enableReferentialIntegrity;
  }
  
  public void setEnableReferentialIntegrity(String enableReferentialIntegrity)
  {
    this.enableReferentialIntegrity = enableReferentialIntegrity;
  }
  
  public boolean isRenderIndexForFkey()
  {
    return this.renderIndexForFkey;
  }
  
  public void setRenderIndexForFkey(boolean renderIndexForFkey)
  {
    this.renderIndexForFkey = renderIndexForFkey;
  }
  
  public String getDropIfExists()
  {
    return this.dropIfExists;
  }
  
  public void setDropIfExists(String dropIfExists)
  {
    this.dropIfExists = dropIfExists;
  }
  
  public String getDropTableCascade()
  {
    return this.dropTableCascade;
  }
  
  public void setDropTableCascade(String dropTableCascade)
  {
    this.dropTableCascade = dropTableCascade;
  }
  
  public String getForeignKeySuffix()
  {
    return this.foreignKeySuffix;
  }
  
  public void setForeignKeySuffix(String foreignKeySuffix)
  {
    this.foreignKeySuffix = foreignKeySuffix;
  }
  
  public int getMaxConstraintNameLength()
  {
    return this.maxConstraintNameLength;
  }
  
  public void setMaxConstraintNameLength(int maxFkeyLength)
  {
    this.maxConstraintNameLength = maxFkeyLength;
  }
  
  public boolean isAddOneToOneUniqueContraint()
  {
    return this.addOneToOneUniqueContraint;
  }
  
  public void setAddOneToOneUniqueContraint(boolean addOneToOneUniqueContraint)
  {
    this.addOneToOneUniqueContraint = addOneToOneUniqueContraint;
  }
  
  public boolean isInlinePrimaryKeyConstraint()
  {
    return this.inlinePrimaryKeyConstraint;
  }
  
  public void setInlinePrimaryKeyConstraint(boolean inlinePrimaryKeyConstraint)
  {
    this.inlinePrimaryKeyConstraint = inlinePrimaryKeyConstraint;
  }
  
  public String getIndexName(String table, String propName, int ixCount)
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("ix_");
    buffer.append(table);
    buffer.append("_");
    buffer.append(propName);
    
    addSuffix(buffer, ixCount);
    
    return buffer.toString();
  }
  
  public String getForeignKeyName(String table, String propName, int fkCount)
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("fk_");
    buffer.append(table);
    buffer.append("_");
    buffer.append(propName);
    
    addSuffix(buffer, fkCount);
    
    return buffer.toString();
  }
  
  protected void addSuffix(StringBuilder buffer, int count)
  {
    String suffixNr = Integer.toString(count);
    int suffixLen = suffixNr.length() + 1;
    if (buffer.length() + suffixLen > this.maxConstraintNameLength) {
      buffer.setLength(this.maxConstraintNameLength - suffixLen);
    }
    buffer.append("_");
    buffer.append(suffixNr);
  }
}
