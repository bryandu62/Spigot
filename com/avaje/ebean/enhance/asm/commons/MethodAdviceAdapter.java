package com.avaje.ebean.enhance.asm.commons;

import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;

public abstract class MethodAdviceAdapter
  extends GeneratorAdapter
  implements Opcodes
{
  protected int methodAccess;
  protected String methodName;
  protected String methodDesc;
  
  protected MethodAdviceAdapter(MethodVisitor mv, int access, String name, String desc)
  {
    super(mv, access, name, desc);
    this.methodAccess = access;
    this.methodDesc = desc;
    this.methodName = name;
  }
  
  public void visitCode()
  {
    this.mv.visitCode();
    onMethodEnter();
  }
  
  public void visitLabel(Label label)
  {
    this.mv.visitLabel(label);
  }
  
  public void visitInsn(int opcode)
  {
    switch (opcode)
    {
    case 172: 
    case 173: 
    case 174: 
    case 175: 
    case 176: 
    case 177: 
    case 191: 
      onMethodExit(opcode);
    }
    this.mv.visitInsn(opcode);
  }
  
  public void visitVarInsn(int opcode, int var)
  {
    super.visitVarInsn(opcode, var);
  }
  
  public void visitFieldInsn(int opcode, String owner, String name, String desc)
  {
    this.mv.visitFieldInsn(opcode, owner, name, desc);
  }
  
  public void visitIntInsn(int opcode, int operand)
  {
    this.mv.visitIntInsn(opcode, operand);
  }
  
  public void visitLdcInsn(Object cst)
  {
    this.mv.visitLdcInsn(cst);
  }
  
  public void visitMultiANewArrayInsn(String desc, int dims)
  {
    this.mv.visitMultiANewArrayInsn(desc, dims);
  }
  
  public void visitTypeInsn(int opcode, String type)
  {
    this.mv.visitTypeInsn(opcode, type);
  }
  
  public void visitMethodInsn(int opcode, String owner, String name, String desc)
  {
    this.mv.visitMethodInsn(opcode, owner, name, desc);
  }
  
  public void visitJumpInsn(int opcode, Label label)
  {
    this.mv.visitJumpInsn(opcode, label);
  }
  
  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
  {
    this.mv.visitLookupSwitchInsn(dflt, keys, labels);
  }
  
  public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels)
  {
    this.mv.visitTableSwitchInsn(min, max, dflt, labels);
  }
  
  protected void onMethodEnter() {}
  
  protected void onMethodExit(int opcode) {}
}
