package com.avaje.ebean;

public enum LikeType
{
  RAW,  STARTS_WITH,  ENDS_WITH,  CONTAINS,  EQUAL_TO;
  
  private LikeType() {}
}
