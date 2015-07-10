package com.avaje.ebean.enhance.agent;

public abstract interface ClassBytesReader
{
  public abstract byte[] getClassBytes(String paramString, ClassLoader paramClassLoader);
}
