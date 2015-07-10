package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.MethodVisitor;

public class VisitMethodParams
{
  private final ClassVisitor cv;
  private int access;
  private final String name;
  private final String desc;
  private final String signiture;
  private final String[] exceptions;
  
  public VisitMethodParams(ClassVisitor cv, int access, String name, String desc, String signiture, String[] exceptions)
  {
    this.cv = cv;
    this.access = access;
    this.name = name;
    this.desc = desc;
    this.exceptions = exceptions;
    this.signiture = signiture;
  }
  
  public boolean forcePublic()
  {
    if (this.access != 1)
    {
      this.access = 1;
      return true;
    }
    return false;
  }
  
  public MethodVisitor visitMethod()
  {
    return this.cv.visitMethod(this.access, this.name, this.desc, this.signiture, this.exceptions);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getDesc()
  {
    return this.desc;
  }
}
