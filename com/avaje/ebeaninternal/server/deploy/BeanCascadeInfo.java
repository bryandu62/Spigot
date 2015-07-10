package com.avaje.ebeaninternal.server.deploy;

import javax.persistence.CascadeType;

public class BeanCascadeInfo
{
  boolean delete;
  boolean save;
  boolean validate;
  
  public void setAttribute(String attr)
  {
    if (attr == null) {
      return;
    }
    attr = attr.toLowerCase();
    this.delete = (attr.indexOf("delete") > -1);
    if (!this.delete) {
      this.delete = (attr.indexOf("remove") > -1);
    }
    this.save = (attr.indexOf("save") > -1);
    if (!this.save) {
      this.save = (attr.indexOf("persist") > -1);
    }
    if (attr.indexOf("validate") > -1) {
      this.validate = true;
    }
    if (attr.indexOf("all") > -1)
    {
      this.delete = true;
      this.save = true;
      this.validate = true;
    }
  }
  
  public void setTypes(CascadeType[] types)
  {
    for (int i = 0; i < types.length; i++) {
      setType(types[i]);
    }
  }
  
  private void setType(CascadeType type)
  {
    if (type.equals(CascadeType.ALL))
    {
      this.save = true;
      this.delete = true;
    }
    if (type.equals(CascadeType.REMOVE)) {
      this.delete = true;
    }
    if (type.equals(CascadeType.PERSIST)) {
      this.save = true;
    }
    if (type.equals(CascadeType.MERGE)) {
      this.save = true;
    }
    if ((this.save) || (this.delete)) {
      this.validate = true;
    }
  }
  
  public boolean isDelete()
  {
    return this.delete;
  }
  
  public void setDelete(boolean isDelete)
  {
    this.delete = isDelete;
  }
  
  public boolean isSave()
  {
    return this.save;
  }
  
  public void setSave(boolean isUpdate)
  {
    this.save = isUpdate;
  }
  
  public boolean isValidate()
  {
    return this.validate;
  }
  
  public void setValidate(boolean isValidate)
  {
    this.validate = isValidate;
  }
}
