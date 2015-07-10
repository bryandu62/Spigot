package com.avaje.ebean.config.dbplatform;

public enum IdType
{
  IDENTITY,  SEQUENCE,  GENERATOR;
  
  private IdType() {}
}
