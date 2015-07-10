package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;

public class MethodNewInstance
{
  public static void addMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    MethodVisitor mv = cv.visitMethod(1, "_ebean_newInstance", "()Ljava/lang/Object;", null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(10, l0);
    mv.visitTypeInsn(187, classMeta.getClassName());
    mv.visitInsn(89);
    mv.visitMethodInsn(183, classMeta.getClassName(), "<init>", "()V");
    mv.visitInsn(176);
    
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLocalVariable("this", "L" + classMeta.getClassName() + ";", null, l0, l1, 0);
    mv.visitMaxs(2, 1);
    mv.visitEnd();
  }
}
