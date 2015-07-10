package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;

public class InterceptField
  implements Opcodes, EnhanceConstants
{
  public static void addField(ClassVisitor cv, boolean transientInternalFields)
  {
    int access = 4 + (transientInternalFields ? 128 : 0);
    FieldVisitor f1 = cv.visitField(access, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;", null, null);
    f1.visitEnd();
  }
  
  public static void addGetterSetter(ClassVisitor cv, String className)
  {
    String lClassName = "L" + className + ";";
    
    MethodVisitor mv = cv.visitMethod(1, "_ebean_getIntercept", "()Lcom/avaje/ebean/bean/EntityBeanIntercept;", null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    mv.visitInsn(176);
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLocalVariable("this", lClassName, null, l0, l1, 0);
    mv.visitMaxs(0, 0);
    mv.visitEnd();
    
    addInitInterceptMethod(cv, className);
  }
  
  private static void addInitInterceptMethod(ClassVisitor cv, String className)
  {
    MethodVisitor mv = cv.visitMethod(1, "_ebean_intercept", "()Lcom/avaje/ebean/bean/EntityBeanIntercept;", null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    Label l1 = new Label();
    mv.visitJumpInsn(199, l1);
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLineNumber(2, l2);
    mv.visitVarInsn(25, 0);
    mv.visitTypeInsn(187, "com/avaje/ebean/bean/EntityBeanIntercept");
    mv.visitInsn(89);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(183, "com/avaje/ebean/bean/EntityBeanIntercept", "<init>", "(Ljava/lang/Object;)V");
    mv.visitFieldInsn(181, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    mv.visitLabel(l1);
    mv.visitLineNumber(3, l1);
    mv.visitFrame(3, 0, null, 0, null);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    mv.visitInsn(176);
    Label l3 = new Label();
    mv.visitLabel(l3);
    mv.visitLocalVariable("this", "L" + className + ";", null, l0, l3, 0);
    mv.visitMaxs(4, 1);
    mv.visitEnd();
  }
}
