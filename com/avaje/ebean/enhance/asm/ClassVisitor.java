package com.avaje.ebean.enhance.asm;

public abstract interface ClassVisitor
{
  public abstract void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString);
  
  public abstract void visitSource(String paramString1, String paramString2);
  
  public abstract void visitOuterClass(String paramString1, String paramString2, String paramString3);
  
  public abstract AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean);
  
  public abstract void visitAttribute(Attribute paramAttribute);
  
  public abstract void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt);
  
  public abstract FieldVisitor visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject);
  
  public abstract MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString);
  
  public abstract void visitEnd();
}
