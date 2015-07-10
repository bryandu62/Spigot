package com.avaje.ebeaninternal.server.subclass;

import com.avaje.ebean.enhance.agent.ClassMeta;
import com.avaje.ebean.enhance.agent.EnhanceConstants;
import com.avaje.ebean.enhance.agent.VisitMethodParams;
import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;

public class SubClassConstructor
  implements Opcodes, EnhanceConstants
{
  public static void addDefault(ClassVisitor cv, ClassMeta meta)
  {
    VisitMethodParams params = new VisitMethodParams(cv, 1, "<init>", "()V", null, null);
    add(params, meta);
  }
  
  public static void add(VisitMethodParams params, ClassMeta meta)
  {
    String className = meta.getClassName();
    String superClassName = meta.getSuperClassName();
    if ((params.forcePublic()) && 
      (meta.isLog(0))) {
      meta.log(" forcing ACC_PUBLIC ");
    }
    MethodVisitor mv = params.visitMethod();
    
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(17, l0);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(183, superClassName, "<init>", "()V");
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLineNumber(18, l1);
    mv.visitVarInsn(25, 0);
    mv.visitTypeInsn(187, "com/avaje/ebean/bean/EntityBeanIntercept");
    mv.visitInsn(89);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(183, "com/avaje/ebean/bean/EntityBeanIntercept", "<init>", "(Ljava/lang/Object;)V");
    mv.visitFieldInsn(181, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLineNumber(19, l2);
    mv.visitInsn(177);
    Label l3 = new Label();
    mv.visitLabel(l3);
    mv.visitLocalVariable("this", "L" + className + ";", null, l0, l3, 0);
    mv.visitMaxs(4, 1);
    mv.visitEnd();
  }
}
