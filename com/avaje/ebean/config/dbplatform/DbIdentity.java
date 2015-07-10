package com.avaje.ebean.config.dbplatform;

public class DbIdentity
{
  private boolean supportsSequence;
  private boolean supportsIdentity;
  private boolean supportsGetGeneratedKeys;
  private String selectLastInsertedIdTemplate;
  private IdType idType = IdType.IDENTITY;
  
  public boolean isSupportsGetGeneratedKeys()
  {
    return this.supportsGetGeneratedKeys;
  }
  
  public void setSupportsGetGeneratedKeys(boolean supportsGetGeneratedKeys)
  {
    this.supportsGetGeneratedKeys = supportsGetGeneratedKeys;
  }
  
  public String getSelectLastInsertedId(String table)
  {
    if (this.selectLastInsertedIdTemplate == null) {
      return null;
    }
    return this.selectLastInsertedIdTemplate.replace("{table}", table);
  }
  
  public void setSelectLastInsertedIdTemplate(String selectLastInsertedIdTemplate)
  {
    this.selectLastInsertedIdTemplate = selectLastInsertedIdTemplate;
  }
  
  public boolean isSupportsSequence()
  {
    return this.supportsSequence;
  }
  
  public void setSupportsSequence(boolean supportsSequence)
  {
    this.supportsSequence = supportsSequence;
  }
  
  public boolean isSupportsIdentity()
  {
    return this.supportsIdentity;
  }
  
  public void setSupportsIdentity(boolean supportsIdentity)
  {
    this.supportsIdentity = supportsIdentity;
  }
  
  public IdType getIdType()
  {
    return this.idType;
  }
  
  public void setIdType(IdType idType)
  {
    this.idType = idType;
  }
}
