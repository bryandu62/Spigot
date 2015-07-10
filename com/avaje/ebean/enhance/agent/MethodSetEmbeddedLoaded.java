package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;
import java.util.List;

public class MethodSetEmbeddedLoaded
  implements Opcodes, EnhanceConstants
{
  public static void addMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(1, "_ebean_setEmbeddedLoaded", "()V", null, null);
    mv.visitCode();
    
    Label labelBegin = null;
    List<FieldMeta> allFields = classMeta.getAllFields();
    for (int i = 0; i < allFields.size(); i++)
    {
      FieldMeta fieldMeta = (FieldMeta)allFields.get(i);
      if (fieldMeta.isEmbedded())
      {
        Label l0 = new Label();
        if (labelBegin == null) {
          labelBegin = l0;
        }
        mv.visitLabel(l0);
        mv.visitLineNumber(0, l0);
        mv.visitVarInsn(25, 0);
        mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
        mv.visitVarInsn(25, 0);
        fieldMeta.appendSwitchGet(mv, classMeta, false);
        mv.visitMethodInsn(182, "com/avaje/ebean/bean/EntityBeanIntercept", "setEmbeddedLoaded", "(Ljava/lang/Object;)V");
      }
    }
    Label l2 = new Label();
    if (labelBegin == null) {
      labelBegin = l2;
    }
    mv.visitLabel(l2);
    mv.visitLineNumber(1, l2);
    mv.visitInsn(177);
    Label l3 = new Label();
    mv.visitLabel(l3);
    mv.visitLocalVariable("this", "L" + className + ";", null, labelBegin, l3, 0);
    mv.visitMaxs(2, 1);
    mv.visitEnd();
  }
}
