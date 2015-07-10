package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.MethodAdapter;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;
import java.io.PrintStream;

public class ConstructorAdapter
  extends MethodAdapter
  implements EnhanceConstants, Opcodes
{
  private final ClassMeta meta;
  private final String className;
  private final String constructorDesc;
  private boolean constructorInitializationDone;
  
  public ConstructorAdapter(MethodVisitor mv, ClassMeta meta, String constructorDesc)
  {
    super(mv);
    this.meta = meta;
    this.className = meta.getClassName();
    this.constructorDesc = constructorDesc;
  }
  
  public void visitMethodInsn(int opcode, String owner, String name, String desc)
  {
    super.visitMethodInsn(opcode, owner, name, desc);
    addInitialisationIfRequired(opcode, owner, name, desc);
  }
  
  public void addInitialisationIfRequired(int opcode, String owner, String name, String desc)
  {
    if ((opcode == 183) && (name.equals("<init>")) && (desc.equals("()V"))) {
      if (this.meta.isSuperClassEntity())
      {
        if (this.meta.isLog(3)) {
          this.meta.log("... skipping intercept <init> ... handled by super class... CONSTRUCTOR:" + this.constructorDesc);
        }
      }
      else if (owner.equals(this.meta.getClassName()))
      {
        if (this.meta.isLog(3)) {
          this.meta.log("... skipping intercept <init> ... handled by other constructor... CONSTRUCTOR:" + this.constructorDesc);
        }
      }
      else if (owner.equals(this.meta.getSuperClassName()))
      {
        if (this.meta.isLog(2)) {
          this.meta.log("... adding intercept <init> in CONSTRUCTOR:" + this.constructorDesc + " OWNER/SUPER:" + owner);
        }
        if (this.constructorInitializationDone)
        {
          String msg = "Error in Enhancement. Only expecting to add <init> of intercept object once but it is trying to add it twice for " + this.meta.getClassName() + " CONSTRUCTOR:" + this.constructorDesc + " OWNER:" + owner;
          
          System.err.println(msg);
        }
        else
        {
          super.visitVarInsn(25, 0);
          super.visitTypeInsn(187, "com/avaje/ebean/bean/EntityBeanIntercept");
          super.visitInsn(89);
          super.visitVarInsn(25, 0);
          
          super.visitMethodInsn(183, "com/avaje/ebean/bean/EntityBeanIntercept", "<init>", "(Ljava/lang/Object;)V");
          super.visitFieldInsn(181, this.className, "_ebean_intercept", "Lcom/avaje/ebean/bean/EntityBeanIntercept;");
          
          this.constructorInitializationDone = true;
        }
      }
      else if (this.meta.isLog(3))
      {
        this.meta.log("... skipping intercept <init> ... incorrect type " + owner);
      }
    }
  }
}
