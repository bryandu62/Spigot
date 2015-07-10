package com.avaje.ebeaninternal.server.type.reflect;

public class CheckImmutableResponse
{
  private boolean immutable = true;
  private String reasonNotImmutable;
  private boolean compoundType;
  
  public String toString()
  {
    if (this.immutable) {
      return "immutable";
    }
    return "not immutable due to:" + this.reasonNotImmutable;
  }
  
  public boolean isCompoundType()
  {
    return this.compoundType;
  }
  
  public void setCompoundType(boolean compoundType)
  {
    this.compoundType = compoundType;
  }
  
  public String getReasonNotImmutable()
  {
    return this.reasonNotImmutable;
  }
  
  public void setReasonNotImmutable(String error)
  {
    this.immutable = false;
    this.reasonNotImmutable = error;
  }
  
  public boolean isImmutable()
  {
    return this.immutable;
  }
}
