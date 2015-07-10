package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;

public class AnnotationInfoVisitor
  implements AnnotationVisitor
{
  final AnnotationVisitor av;
  final AnnotationInfo info;
  final String prefix;
  
  public AnnotationInfoVisitor(String prefix, AnnotationInfo info, AnnotationVisitor av)
  {
    this.av = av;
    this.info = info;
    this.prefix = prefix;
  }
  
  public void visit(String name, Object value)
  {
    this.info.add(this.prefix, name, value);
  }
  
  public AnnotationVisitor visitAnnotation(String name, String desc)
  {
    return create(name);
  }
  
  public AnnotationVisitor visitArray(String name)
  {
    return create(name);
  }
  
  private AnnotationInfoVisitor create(String name)
  {
    String newPrefix = this.prefix + "." + name;
    return new AnnotationInfoVisitor(newPrefix, this.info, this.av);
  }
  
  public void visitEnd()
  {
    this.av.visitEnd();
  }
  
  public void visitEnum(String name, String desc, String value)
  {
    this.info.addEnum(this.prefix, name, desc, value);
    this.av.visitEnum(name, desc, value);
  }
}
