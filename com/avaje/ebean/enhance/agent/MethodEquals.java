package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;

public class MethodEquals
  implements Opcodes, EnhanceConstants
{
  private static final String _EBEAN_GET_IDENTITY = "_ebean_getIdentity";
  
  public static void addMethods(ClassVisitor cv, ClassMeta meta, int idFieldIndex, FieldMeta idFieldMeta)
  {
    if (meta.hasEqualsOrHashCode())
    {
      if (meta.isLog(1)) {
        meta.log("already has a equals() or hashCode() method. Not adding the identity based one.");
      }
    }
    else
    {
      if (meta.isLog(2)) {
        meta.log("adding equals() hashCode() and _ebean_getIdentity() with Id field " + idFieldMeta.getName() + " index:" + idFieldIndex + " primative:" + idFieldMeta.isPrimativeType());
      }
      if (idFieldMeta.isPrimativeType()) {
        addGetIdentityPrimitive(cv, meta, idFieldIndex, idFieldMeta);
      } else {
        addGetIdentityObject(cv, meta, idFieldIndex);
      }
      addEquals(cv, meta);
      addHashCode(cv, meta);
    }
  }
  
  public static void addIdentityField(ClassVisitor cv)
  {
    int access = 132;
    FieldVisitor f0 = cv.visitField(access, "_ebean_identity", "Ljava/lang/Object;", null, null);
    f0.visitEnd();
  }
  
  private static void addGetIdentityPrimitive(ClassVisitor cv, ClassMeta classMeta, int idFieldIndex, FieldMeta idFieldMeta)
  {
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(2, "_ebean_getIdentity", "()Ljava/lang/Object;", null, null);
    mv.visitCode();
    
    Label l0 = new Label();
    Label l1 = new Label();
    Label l2 = new Label();
    mv.visitTryCatchBlock(l0, l1, l2, null);
    Label l3 = new Label();
    Label l4 = new Label();
    mv.visitTryCatchBlock(l3, l4, l2, null);
    Label l5 = new Label();
    mv.visitTryCatchBlock(l2, l5, l2, null);
    Label l6 = new Label();
    mv.visitLabel(l6);
    mv.visitLineNumber(1, l6);
    mv.visitVarInsn(25, 0);
    mv.visitInsn(89);
    mv.visitVarInsn(58, 1);
    mv.visitInsn(194);
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitJumpInsn(198, l3);
    Label l7 = new Label();
    mv.visitLabel(l7);
    mv.visitLineNumber(1, l7);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitVarInsn(25, 1);
    mv.visitInsn(195);
    mv.visitLabel(l1);
    mv.visitInsn(176);
    mv.visitLabel(l3);
    mv.visitLineNumber(1, l3);
    mv.visitVarInsn(25, 0);
    idFieldMeta.appendGetPrimitiveIdValue(mv, classMeta);
    idFieldMeta.appendCompare(mv, classMeta);
    
    Label l8 = new Label();
    mv.visitJumpInsn(153, l8);
    Label l9 = new Label();
    mv.visitLabel(l9);
    mv.visitLineNumber(1, l9);
    mv.visitVarInsn(25, 0);
    mv.visitVarInsn(25, 0);
    idFieldMeta.appendGetPrimitiveIdValue(mv, classMeta);
    idFieldMeta.appendValueOf(mv, classMeta);
    mv.visitFieldInsn(181, className, "_ebean_identity", "Ljava/lang/Object;");
    Label l10 = new Label();
    mv.visitJumpInsn(167, l10);
    mv.visitLabel(l8);
    mv.visitLineNumber(1, l8);
    mv.visitVarInsn(25, 0);
    mv.visitTypeInsn(187, "java/lang/Object");
    mv.visitInsn(89);
    mv.visitMethodInsn(183, "java/lang/Object", "<init>", "()V");
    mv.visitFieldInsn(181, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitLabel(l10);
    mv.visitLineNumber(1, l10);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitVarInsn(25, 1);
    mv.visitInsn(195);
    mv.visitLabel(l4);
    mv.visitInsn(176);
    mv.visitLabel(l2);
    mv.visitLineNumber(1, l2);
    mv.visitVarInsn(25, 1);
    mv.visitInsn(195);
    mv.visitLabel(l5);
    mv.visitInsn(191);
    Label l11 = new Label();
    mv.visitLabel(l11);
    mv.visitLocalVariable("this", "L" + className + ";", null, l6, l11, 0);
    mv.visitMaxs(3, 2);
    mv.visitEnd();
  }
  
  private static void addGetIdentityObject(ClassVisitor cv, ClassMeta classMeta, int idFieldIndex)
  {
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(2, "_ebean_getIdentity", "()Ljava/lang/Object;", null, null);
    mv.visitCode();
    
    Label l0 = new Label();
    Label l1 = new Label();
    Label l2 = new Label();
    mv.visitTryCatchBlock(l0, l1, l2, null);
    Label l3 = new Label();
    Label l4 = new Label();
    mv.visitTryCatchBlock(l3, l4, l2, null);
    Label l5 = new Label();
    mv.visitTryCatchBlock(l2, l5, l2, null);
    Label l6 = new Label();
    mv.visitLabel(l6);
    mv.visitLineNumber(1, l6);
    mv.visitVarInsn(25, 0);
    mv.visitInsn(89);
    mv.visitVarInsn(58, 1);
    mv.visitInsn(194);
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitJumpInsn(198, l3);
    Label l7 = new Label();
    mv.visitLabel(l7);
    mv.visitLineNumber(1, l7);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitVarInsn(25, 1);
    mv.visitInsn(195);
    mv.visitLabel(l1);
    mv.visitInsn(176);
    
    mv.visitLabel(l3);
    mv.visitLineNumber(1, l3);
    mv.visitVarInsn(25, 0);
    IndexFieldWeaver.visitIntInsn(mv, idFieldIndex);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(183, className, "_ebean_getField", "(ILjava/lang/Object;)Ljava/lang/Object;");
    mv.visitVarInsn(58, 2);
    
    Label l8 = new Label();
    mv.visitLabel(l8);
    mv.visitLineNumber(1, l8);
    mv.visitVarInsn(25, 2);
    Label l9 = new Label();
    mv.visitJumpInsn(198, l9);
    Label l10 = new Label();
    mv.visitLabel(l10);
    mv.visitLineNumber(1, l10);
    mv.visitVarInsn(25, 0);
    mv.visitVarInsn(25, 2);
    mv.visitFieldInsn(181, className, "_ebean_identity", "Ljava/lang/Object;");
    Label l11 = new Label();
    mv.visitJumpInsn(167, l11);
    mv.visitLabel(l9);
    mv.visitLineNumber(1, l9);
    mv.visitVarInsn(25, 0);
    mv.visitTypeInsn(187, "java/lang/Object");
    mv.visitInsn(89);
    mv.visitMethodInsn(183, "java/lang/Object", "<init>", "()V");
    mv.visitFieldInsn(181, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitLabel(l11);
    mv.visitLineNumber(1, l11);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_identity", "Ljava/lang/Object;");
    mv.visitVarInsn(25, 1);
    mv.visitInsn(195);
    mv.visitLabel(l4);
    mv.visitInsn(176);
    mv.visitLabel(l2);
    mv.visitLineNumber(1, l2);
    mv.visitVarInsn(25, 1);
    mv.visitInsn(195);
    mv.visitLabel(l5);
    mv.visitInsn(191);
    Label l12 = new Label();
    mv.visitLabel(l12);
    mv.visitLocalVariable("this", "L" + className + ";", null, l6, l12, 0);
    mv.visitLocalVariable("tmpId", "Ljava/lang/Object;", null, l8, l2, 2);
    mv.visitMaxs(3, 3);
    mv.visitEnd();
  }
  
  private static void addEquals(ClassVisitor cv, ClassMeta classMeta)
  {
    MethodVisitor mv = cv.visitMethod(1, "equals", "(Ljava/lang/Object;)Z", null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 1);
    Label l1 = new Label();
    mv.visitJumpInsn(199, l1);
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLineNumber(2, l2);
    mv.visitInsn(3);
    mv.visitInsn(172);
    mv.visitLabel(l1);
    mv.visitLineNumber(3, l1);
    mv.visitFrame(3, 0, null, 0, null);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
    mv.visitVarInsn(25, 1);
    mv.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
    mv.visitMethodInsn(182, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
    Label l3 = new Label();
    mv.visitJumpInsn(154, l3);
    Label l4 = new Label();
    mv.visitLabel(l4);
    mv.visitLineNumber(4, l4);
    mv.visitInsn(3);
    mv.visitInsn(172);
    mv.visitLabel(l3);
    mv.visitLineNumber(5, l3);
    mv.visitFrame(3, 0, null, 0, null);
    mv.visitVarInsn(25, 1);
    mv.visitVarInsn(25, 0);
    Label l5 = new Label();
    mv.visitJumpInsn(166, l5);
    Label l6 = new Label();
    mv.visitLabel(l6);
    mv.visitLineNumber(6, l6);
    mv.visitInsn(4);
    mv.visitInsn(172);
    mv.visitLabel(l5);
    mv.visitLineNumber(7, l5);
    mv.visitFrame(3, 0, null, 0, null);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(182, classMeta.getClassName(), "_ebean_getIdentity", "()Ljava/lang/Object;");
    mv.visitVarInsn(25, 1);
    mv.visitTypeInsn(192, classMeta.getClassName());
    mv.visitMethodInsn(182, classMeta.getClassName(), "_ebean_getIdentity", "()Ljava/lang/Object;");
    mv.visitMethodInsn(182, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
    mv.visitInsn(172);
    Label l7 = new Label();
    mv.visitLabel(l7);
    mv.visitLocalVariable("this", "L" + classMeta.getClassName() + ";", null, l0, l7, 0);
    mv.visitLocalVariable("obj", "Ljava/lang/Object;", null, l0, l7, 1);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }
  
  private static void addHashCode(ClassVisitor cv, ClassMeta meta)
  {
    MethodVisitor mv = cv.visitMethod(1, "hashCode", "()I", null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(183, meta.getClassName(), "_ebean_getIdentity", "()Ljava/lang/Object;");
    mv.visitMethodInsn(182, "java/lang/Object", "hashCode", "()I");
    mv.visitInsn(172);
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLocalVariable("this", "L" + meta.getClassName() + ";", null, l0, l1, 0);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }
}
