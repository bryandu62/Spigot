package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.EmptyVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;
import com.avaje.ebean.enhance.asm.MethodVisitor;

public class ClassMetaReaderVisitor
  extends EmptyVisitor
  implements EnhanceConstants
{
  private final ClassMeta classMeta;
  private final boolean readMethodMeta;
  
  public ClassMetaReaderVisitor(boolean readMethodMeta, EnhanceContext context)
  {
    this.readMethodMeta = readMethodMeta;
    this.classMeta = context.createClassMeta();
  }
  
  public ClassMeta getClassMeta()
  {
    return this.classMeta;
  }
  
  public boolean isLog(int level)
  {
    return this.classMeta.isLog(level);
  }
  
  public void log(String msg)
  {
    this.classMeta.log(msg);
  }
  
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    this.classMeta.setClassName(name, superName);
    super.visit(version, access, name, signature, superName, interfaces);
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    this.classMeta.addClassAnnotation(desc);
    
    AnnotationVisitor av = super.visitAnnotation(desc, visible);
    if (desc.equals("Lcom/avaje/ebean/annotation/Transactional;")) {
      return new AnnotationInfoVisitor(null, this.classMeta.getAnnotationInfo(), av);
    }
    return av;
  }
  
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    if ((access & 0x8) != 0)
    {
      if (isLog(2)) {
        log("Skip static field " + name);
      }
      return super.visitField(access, name, desc, signature, value);
    }
    if ((access & 0x80) != 0)
    {
      if (isLog(2)) {
        log("Skip transient field " + name);
      }
      return super.visitField(access, name, desc, signature, value);
    }
    return this.classMeta.createLocalFieldVisitor(name, desc);
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    boolean staticAccess = (access & 0x8) != 0;
    
    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
    if ((!staticAccess) && (this.readMethodMeta)) {
      return this.classMeta.createMethodVisitor(mv, access, name, desc);
    }
    return mv;
  }
}
