package com.avaje.ebeaninternal.server.persist;

public abstract interface Constant
{
  public static final int IN_INSERT = 1;
  public static final int IN_UPDATE_SET = 2;
  public static final int IN_UPDATE_WHERE = 3;
  public static final int IN_DELETE_WHERE = 4;
}
