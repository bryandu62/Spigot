package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;

public class MethodPropertyChangeListener
  implements Opcodes, EnhanceConstants
{
  public static void addMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    addAddListenerMethod(cv, classMeta);
    addAddPropertyListenerMethod(cv, classMeta);
    addRemoveListenerMethod(cv, classMeta);
    addRemovePropertyListenerMethod(cv, classMeta);
  }
  
  private static boolean alreadyExisting(ClassMeta classMeta, String method, String desc)
  {
    if (classMeta.isExistingMethod(method, desc))
    {
      if (classMeta.isLog(1)) {
        classMeta.log("Existing method... " + method + desc + "  - not adding Ebean's implementation");
      }
      return true;
    }
    return false;
  }
  
  private static void addAddListenerMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    String desc = "(Ljava/beans/PropertyChangeListener;)V";
    if (alreadyExisting(classMeta, "addPropertyChangeListener", desc)) {
      return;
    }
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(1, "addPropertyChangeListener", desc, null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    mv.visitVarInsn(25, 1);
    mv.visitMethodInsn(182, "com/avaje/ebean/bean/EntityBeanIntercept", "addPropertyChangeListener", "(Ljava/beans/PropertyChangeListener;)V");
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLineNumber(2, l1);
    mv.visitInsn(177);
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLocalVariable("this", "L" + className + ";", null, l0, l2, 0);
    mv.visitLocalVariable("listener", "Ljava/beans/PropertyChangeListener;", null, l0, l2, 1);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }
  
  private static void addAddPropertyListenerMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    String desc = "(Ljava/lang/String;Ljava/beans/PropertyChangeListener;)V";
    if (alreadyExisting(classMeta, "addPropertyChangeListener", desc)) {
      return;
    }
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(1, "addPropertyChangeListener", desc, null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    mv.visitVarInsn(25, 1);
    mv.visitVarInsn(25, 2);
    mv.visitMethodInsn(182, "com/avaje/ebean/bean/EntityBeanIntercept", "addPropertyChangeListener", "(Ljava/lang/String;Ljava/beans/PropertyChangeListener;)V");
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLineNumber(2, l1);
    mv.visitInsn(177);
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLocalVariable("this", "L" + className + ";", null, l0, l2, 0);
    mv.visitLocalVariable("name", "Ljava/lang/String;", null, l0, l2, 1);
    mv.visitLocalVariable("listener", "Ljava/beans/PropertyChangeListener;", null, l0, l2, 2);
    mv.visitMaxs(3, 3);
    mv.visitEnd();
  }
  
  private static void addRemoveListenerMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    String desc = "(Ljava/beans/PropertyChangeListener;)V";
    if (alreadyExisting(classMeta, "removePropertyChangeListener", desc)) {
      return;
    }
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(1, "removePropertyChangeListener", desc, null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    mv.visitVarInsn(25, 1);
    mv.visitMethodInsn(182, "com/avaje/ebean/bean/EntityBeanIntercept", "removePropertyChangeListener", "(Ljava/beans/PropertyChangeListener;)V");
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLineNumber(2, l1);
    mv.visitInsn(177);
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLocalVariable("this", "L" + className + ";", null, l0, l2, 0);
    mv.visitLocalVariable("listener", "Ljava/beans/PropertyChangeListener;", null, l0, l2, 1);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }
  
  private static void addRemovePropertyListenerMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    String desc = "(Ljava/lang/String;Ljava/beans/PropertyChangeListener;)V";
    if (alreadyExisting(classMeta, "removePropertyChangeListener", desc)) {
      return;
    }
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(1, "removePropertyChangeListener", desc, null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
    mv.visitVarInsn(25, 1);
    mv.visitVarInsn(25, 2);
    mv.visitMethodInsn(182, "com/avaje/ebean/bean/EntityBeanIntercept", "removePropertyChangeListener", "(Ljava/lang/String;Ljava/beans/PropertyChangeListener;)V");
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLineNumber(2, l1);
    mv.visitInsn(177);
    Label l2 = new Label();
    mv.visitLabel(l2);
    mv.visitLocalVariable("this", "L" + className + ";", null, l0, l2, 0);
    mv.visitLocalVariable("name", "Ljava/lang/String;", null, l0, l2, 1);
    mv.visitLocalVariable("listener", "Ljava/beans/PropertyChangeListener;", null, l0, l2, 2);
    mv.visitMaxs(3, 3);
    mv.visitEnd();
  }
}
