package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.Attribute;
import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.EmptyVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;

public class LocalFieldVisitor
  implements FieldVisitor
{
  private static final EmptyVisitor emptyVisitor = new EmptyVisitor();
  private final FieldVisitor fv;
  private final FieldMeta fieldMeta;
  
  public LocalFieldVisitor(FieldMeta fieldMeta)
  {
    this.fv = null;
    this.fieldMeta = fieldMeta;
  }
  
  public LocalFieldVisitor(ClassVisitor cv, FieldVisitor fv, FieldMeta fieldMeta)
  {
    this.fv = fv;
    this.fieldMeta = fieldMeta;
  }
  
  public boolean isPersistentSetter(String methodDesc)
  {
    return this.fieldMeta.isPersistentSetter(methodDesc);
  }
  
  public boolean isPersistentGetter(String methodDesc)
  {
    return this.fieldMeta.isPersistentGetter(methodDesc);
  }
  
  public String getName()
  {
    return this.fieldMeta.getFieldName();
  }
  
  public FieldMeta getFieldMeta()
  {
    return this.fieldMeta;
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    this.fieldMeta.addAnnotationDesc(desc);
    if (this.fv != null) {
      return this.fv.visitAnnotation(desc, visible);
    }
    return emptyVisitor;
  }
  
  public void visitAttribute(Attribute attr)
  {
    if (this.fv != null) {
      this.fv.visitAttribute(attr);
    }
  }
  
  public void visitEnd()
  {
    if (this.fv != null) {
      this.fv.visitEnd();
    }
  }
}
