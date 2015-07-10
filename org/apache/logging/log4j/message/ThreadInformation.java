package org.apache.logging.log4j.message;

abstract interface ThreadInformation
{
  public abstract void printThreadInfo(StringBuilder paramStringBuilder);
  
  public abstract void printStack(StringBuilder paramStringBuilder, StackTraceElement[] paramArrayOfStackTraceElement);
}
