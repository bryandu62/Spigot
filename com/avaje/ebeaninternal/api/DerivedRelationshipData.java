package com.avaje.ebeaninternal.api;

public final class DerivedRelationshipData
{
  private final Object assocBean;
  private final String logicalName;
  private final Object bean;
  
  public DerivedRelationshipData(Object assocBean, String logicalName, Object bean)
  {
    this.assocBean = assocBean;
    this.logicalName = logicalName;
    this.bean = bean;
  }
  
  public Object getAssocBean()
  {
    return this.assocBean;
  }
  
  public String getLogicalName()
  {
    return this.logicalName;
  }
  
  public Object getBean()
  {
    return this.bean;
  }
}
