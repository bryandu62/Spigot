package com.avaje.ebean.enhance.asm;

public abstract interface FieldVisitor
{
  public abstract AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean);
  
  public abstract void visitAttribute(Attribute paramAttribute);
  
  public abstract void visitEnd();
}
