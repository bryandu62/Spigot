package com.avaje.ebean.bean;

import java.io.Serializable;

public final class ObjectGraphOrigin
  implements Serializable
{
  private static final long serialVersionUID = 410937765287968707L;
  private final CallStack callStack;
  private final String key;
  private final String beanType;
  
  public ObjectGraphOrigin(int queryHash, CallStack callStack, String beanType)
  {
    this.callStack = callStack;
    this.beanType = beanType;
    this.key = callStack.getOriginKey(queryHash);
  }
  
  public String getKey()
  {
    return this.key;
  }
  
  public String getBeanType()
  {
    return this.beanType;
  }
  
  public CallStack getCallStack()
  {
    return this.callStack;
  }
  
  public String getFirstStackElement()
  {
    return this.callStack.getFirstStackTraceElement().toString();
  }
  
  public String toString()
  {
    return this.key + " " + this.beanType + " " + this.callStack.getFirstStackTraceElement();
  }
}
