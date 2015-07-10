package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;

public class DefaultConstructor
{
  public static void add(ClassVisitor cw, ClassMeta classMeta)
  {
    if (classMeta.isLog(3)) {
      classMeta.log("... adding default constructor, super class: " + classMeta.getSuperClassName());
    }
    MethodVisitor underlyingMV = cw.visitMethod(1, "<init>", "()V", null, null);
    
    ConstructorAdapter mv = new ConstructorAdapter(underlyingMV, classMeta, "()V");
    
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(183, classMeta.getSuperClassName(), "<init>", "()V");
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLineNumber(2, l1);
    mv.visitInsn(177);
    
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLocalVariable("this", "L" + classMeta.getClassName() + ";", null, l0, l2, 0);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }
}
