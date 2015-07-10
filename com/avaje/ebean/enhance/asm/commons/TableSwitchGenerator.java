package com.avaje.ebean.enhance.asm.commons;

import com.avaje.ebean.enhance.asm.Label;

public abstract interface TableSwitchGenerator
{
  public abstract void generateCase(int paramInt, Label paramLabel);
  
  public abstract void generateDefault();
}
