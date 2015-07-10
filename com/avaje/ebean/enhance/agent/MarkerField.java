package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;

public class MarkerField
  implements Opcodes, EnhanceConstants
{
  public static final String _EBEAN_MARKER = "_EBEAN_MARKER";
  
  public static String addField(ClassVisitor cv, String className)
  {
    String cn = className.replace('/', '.');
    
    FieldVisitor fv = cv.visitField(10, "_EBEAN_MARKER", "Ljava/lang/String;", null, cn);
    fv.visitEnd();
    
    return cn;
  }
  
  public static void addGetMarker(ClassVisitor cv, String className)
  {
    MethodVisitor mv = cv.visitMethod(1, "_ebean_getMarker", "()Ljava/lang/String;", null, null);
    mv.visitCode();
    Label l0 = new Label();
    mv.visitLabel(l0);
    mv.visitLineNumber(1, l0);
    mv.visitFieldInsn(178, className, "_EBEAN_MARKER", "Ljava/lang/String;");
    mv.visitInsn(176);
    Label l1 = new Label();
    mv.visitLabel(l1);
    mv.visitLocalVariable("this", "L" + className + ";", null, l0, l1, 0);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }
}
