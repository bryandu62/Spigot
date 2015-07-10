package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodAdapter;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;

public class MethodFieldAdapter
  extends MethodAdapter
  implements Opcodes
{
  final ClassMeta meta;
  final String className;
  final String methodDescription;
  boolean transientAnnotation = false;
  
  public MethodFieldAdapter(MethodVisitor mv, ClassMeta meta, String methodDescription)
  {
    super(mv);
    this.meta = meta;
    this.className = meta.getClassName();
    this.methodDescription = methodDescription;
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    if (desc.equals("Ljavax/persistence/Transient;")) {
      this.transientAnnotation = true;
    }
    return super.visitAnnotation(desc, visible);
  }
  
  public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
  {
    super.visitLocalVariable(name, desc, signature, start, end, index);
  }
  
  public void visitMethodInsn(int opcode, String owner, String name, String desc)
  {
    super.visitMethodInsn(opcode, owner, name, desc);
  }
  
  public void visitFieldInsn(int opcode, String owner, String name, String desc)
  {
    if (this.transientAnnotation)
    {
      super.visitFieldInsn(opcode, owner, name, desc);
      return;
    }
    if ((opcode == 178) || (opcode == 179))
    {
      if (this.meta.isLog(3)) {
        this.meta.log(" ... info: skip static field " + owner + " " + name + " in " + this.methodDescription);
      }
      super.visitFieldInsn(opcode, owner, name, desc);
      return;
    }
    if (!this.meta.isFieldPersistent(name))
    {
      if (this.meta.isLog(2)) {
        this.meta.log(" ... info: non-persistent field " + owner + " " + name + " in " + this.methodDescription);
      }
      super.visitFieldInsn(opcode, owner, name, desc);
      return;
    }
    if (opcode == 180)
    {
      String methodName = "_ebean_get_" + name;
      String methodDesc = "()" + desc;
      if (this.meta.isLog(4)) {
        this.meta.log("GETFIELD method:" + this.methodDescription + " field:" + name + " > " + methodName + " " + methodDesc);
      }
      super.visitMethodInsn(182, this.className, methodName, methodDesc);
    }
    else if (opcode == 181)
    {
      String methodName = "_ebean_set_" + name;
      String methodDesc = "(" + desc + ")V";
      if (this.meta.isLog(4)) {
        this.meta.log("PUTFIELD method:" + this.methodDescription + " field:" + name + " > " + methodName + " " + methodDesc);
      }
      super.visitMethodInsn(182, this.className, methodName, methodDesc);
    }
    else
    {
      this.meta.log("Warning adapting method:" + this.methodDescription + "; unexpected static access to a persistent field?? " + name + " opCode not GETFIELD or PUTFIELD??  opCode:" + opcode + "");
      
      super.visitFieldInsn(opcode, owner, name, desc);
    }
  }
}
