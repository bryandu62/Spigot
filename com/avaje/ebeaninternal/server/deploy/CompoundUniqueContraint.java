package com.avaje.ebeaninternal.server.deploy;

public class CompoundUniqueContraint
{
  private final String[] columns;
  
  public CompoundUniqueContraint(String[] columns)
  {
    this.columns = columns;
  }
  
  public String[] getColumns()
  {
    return this.columns;
  }
}
