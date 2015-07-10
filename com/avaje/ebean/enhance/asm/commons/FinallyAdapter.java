package com.avaje.ebean.enhance.asm.commons;

import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;

public class FinallyAdapter
  extends AdviceAdapter
{
  private String name;
  private Label startFinally = new Label();
  
  public FinallyAdapter(MethodVisitor mv, int acc, String name, String desc)
  {
    super(mv, acc, name, desc);
    this.name = name;
  }
  
  public void visitCode()
  {
    super.visitCode();
    this.mv.visitLabel(this.startFinally);
  }
  
  public void visitMaxs(int maxStack, int maxLocals)
  {
    Label endFinally = new Label();
    this.mv.visitTryCatchBlock(this.startFinally, endFinally, endFinally, null);
    this.mv.visitLabel(endFinally);
    onFinally(191);
    this.mv.visitInsn(191);
    this.mv.visitMaxs(maxStack, maxLocals);
  }
  
  protected void onMethodExit(int opcode)
  {
    if (opcode != 191) {
      onFinally(opcode);
    }
  }
  
  private void onFinally(int opcode)
  {
    this.mv.visitFieldInsn(178, "java/lang/System", "err", "Ljava/io/PrintStream;");
    this.mv.visitLdcInsn("Exiting " + this.name);
    this.mv.visitMethodInsn(182, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
  }
}
