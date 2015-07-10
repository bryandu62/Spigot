package com.avaje.ebean;

public enum TxType
{
  REQUIRED,  MANDATORY,  SUPPORTS,  REQUIRES_NEW,  NOT_SUPPORTED,  NEVER;
  
  private TxType() {}
}
