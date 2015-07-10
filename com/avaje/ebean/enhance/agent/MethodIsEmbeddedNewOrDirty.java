package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;
import java.util.List;

public class MethodIsEmbeddedNewOrDirty
  implements Opcodes, EnhanceConstants
{
  public static void addMethod(ClassVisitor cv, ClassMeta classMeta)
  {
    String className = classMeta.getClassName();
    
    MethodVisitor mv = cv.visitMethod(1, "_ebean_isEmbeddedNewOrDirty", "()Z", null, null);
    mv.visitCode();
    
    Label labelBegin = null;
    
    Label labelNext = null;
    
    List<FieldMeta> allFields = classMeta.getAllFields();
    for (int i = 0; i < allFields.size(); i++)
    {
      FieldMeta fieldMeta = (FieldMeta)allFields.get(i);
      if (fieldMeta.isEmbedded())
      {
        Label l0 = labelNext;
        if (l0 == null) {
          l0 = new Label();
        }
        if (labelBegin == null) {
          labelBegin = l0;
        }
        mv.visitLabel(l0);
        mv.visitLineNumber(0, l0);
        mv.visitVarInsn(25, 0);
        mv.visitFieldInsn(180, className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
        mv.visitVarInsn(25, 0);
        fieldMeta.appendSwitchGet(mv, classMeta, false);
        mv.visitMethodInsn(182, "com/avaje/ebean/bean/EntityBeanIntercept", "isEmbeddedNewOrDirty", "(Ljava/lang/Object;)Z");
        
        labelNext = new Label();
        mv.visitJumpInsn(153, labelNext);
        mv.visitInsn(4);
        mv.visitInsn(172);
      }
    }
    if (labelNext == null) {
      labelNext = new Label();
    }
    if (labelBegin == null) {
      labelBegin = labelNext;
    }
    mv.visitLabel(labelNext);
    mv.visitLineNumber(1, labelNext);
    mv.visitInsn(3);
    mv.visitInsn(172);
    
    Label l3 = new Label();
    mv.visitLabel(l3);
    mv.visitLocalVariable("this", "L" + className + ";", null, labelBegin, l3, 0);
    mv.visitMaxs(2, 1);
    mv.visitEnd();
  }
}
