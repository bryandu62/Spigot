package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Type;
import com.avaje.ebean.enhance.asm.commons.MethodAdviceAdapter;
import java.util.ArrayList;

public class ScopeTransAdapter
  extends MethodAdviceAdapter
  implements EnhanceConstants
{
  private static final Type txScopeType = Type.getType("Lcom/avaje/ebean/TxScope;");
  private static final Type scopeTransType = Type.getType("Lcom/avaje/ebeaninternal/api/ScopeTrans;");
  private static final Type helpScopeTrans = Type.getType("Lcom/avaje/ebeaninternal/api/HelpScopeTrans;");
  private final AnnotationInfo annotationInfo;
  private final ClassAdapterTransactional owner;
  private boolean transactional;
  private int posTxScope;
  private int posScopeTrans;
  
  public ScopeTransAdapter(ClassAdapterTransactional owner, MethodVisitor mv, int access, String name, String desc)
  {
    super(mv, access, name, desc);
    this.owner = owner;
    
    AnnotationInfo parentInfo = owner.classAnnotationInfo;
    
    AnnotationInfo interfaceInfo = owner.getInterfaceTransactionalInfo(name, desc);
    if (parentInfo == null) {
      parentInfo = interfaceInfo;
    } else {
      parentInfo.setParent(interfaceInfo);
    }
    this.annotationInfo = new AnnotationInfo(parentInfo);
    
    this.transactional = (parentInfo != null);
  }
  
  public boolean isTransactional()
  {
    return this.transactional;
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    if (desc.equals("Lcom/avaje/ebean/annotation/Transactional;")) {
      this.transactional = true;
    }
    AnnotationVisitor av = super.visitAnnotation(desc, visible);
    return new AnnotationInfoVisitor(null, this.annotationInfo, av);
  }
  
  private void setTxType(Object txType)
  {
    this.mv.visitVarInsn(25, this.posTxScope);
    this.mv.visitLdcInsn(txType.toString());
    this.mv.visitMethodInsn(184, "com/avaje/ebean/TxType", "valueOf", "(Ljava/lang/String;)Lcom/avaje/ebean/TxType;");
    this.mv.visitMethodInsn(182, "com/avaje/ebean/TxScope", "setType", "(Lcom/avaje/ebean/TxType;)Lcom/avaje/ebean/TxScope;");
    this.mv.visitInsn(87);
  }
  
  private void setTxIsolation(Object txIsolation)
  {
    this.mv.visitVarInsn(25, this.posTxScope);
    this.mv.visitLdcInsn(txIsolation.toString());
    this.mv.visitMethodInsn(184, "com/avaje/ebean/TxIsolation", "valueOf", "(Ljava/lang/String;)Lcom/avaje/ebean/TxIsolation;");
    this.mv.visitMethodInsn(182, "com/avaje/ebean/TxScope", "setIsolation", "(Lcom/avaje/ebean/TxIsolation;)Lcom/avaje/ebean/TxScope;");
    this.mv.visitInsn(87);
  }
  
  private void setServerName(Object serverName)
  {
    this.mv.visitVarInsn(25, this.posTxScope);
    this.mv.visitLdcInsn(serverName.toString());
    this.mv.visitMethodInsn(182, "com/avaje/ebean/TxScope", "setServerName", "(Ljava/lang/String;)Lcom/avaje/ebean/TxScope;");
    this.mv.visitInsn(87);
  }
  
  private void setReadOnly(Object readOnlyObj)
  {
    boolean readOnly = ((Boolean)readOnlyObj).booleanValue();
    this.mv.visitVarInsn(25, this.posTxScope);
    if (readOnly) {
      this.mv.visitInsn(4);
    } else {
      this.mv.visitInsn(3);
    }
    this.mv.visitMethodInsn(182, "com/avaje/ebean/TxScope", "setReadOnly", "(Z)Lcom/avaje/ebean/TxScope;");
  }
  
  private void setNoRollbackFor(Object noRollbackFor)
  {
    ArrayList<?> list = (ArrayList)noRollbackFor;
    for (int i = 0; i < list.size(); i++)
    {
      Type throwType = (Type)list.get(i);
      
      this.mv.visitVarInsn(25, this.posTxScope);
      this.mv.visitLdcInsn(throwType);
      this.mv.visitMethodInsn(182, txScopeType.getInternalName(), "setNoRollbackFor", "(Ljava/lang/Class;)Lcom/avaje/ebean/TxScope;");
      this.mv.visitInsn(87);
    }
  }
  
  private void setRollbackFor(Object rollbackFor)
  {
    ArrayList<?> list = (ArrayList)rollbackFor;
    for (int i = 0; i < list.size(); i++)
    {
      Type throwType = (Type)list.get(i);
      
      this.mv.visitVarInsn(25, this.posTxScope);
      this.mv.visitLdcInsn(throwType);
      this.mv.visitMethodInsn(182, txScopeType.getInternalName(), "setRollbackFor", "(Ljava/lang/Class;)Lcom/avaje/ebean/TxScope;");
      this.mv.visitInsn(87);
    }
  }
  
  protected void onMethodEnter()
  {
    if (!this.transactional) {
      return;
    }
    this.owner.transactionalMethod(this.methodName, this.methodDesc, this.annotationInfo);
    
    this.posTxScope = newLocal(txScopeType);
    this.posScopeTrans = newLocal(scopeTransType);
    
    this.mv.visitTypeInsn(187, txScopeType.getInternalName());
    this.mv.visitInsn(89);
    this.mv.visitMethodInsn(183, txScopeType.getInternalName(), "<init>", "()V");
    this.mv.visitVarInsn(58, this.posTxScope);
    
    Object txType = this.annotationInfo.getValue("type");
    if (txType != null) {
      setTxType(txType);
    }
    Object txIsolation = this.annotationInfo.getValue("isolation");
    if (txIsolation != null) {
      setTxIsolation(txIsolation);
    }
    Object readOnly = this.annotationInfo.getValue("readOnly");
    if (readOnly != null) {
      setReadOnly(readOnly);
    }
    Object noRollbackFor = this.annotationInfo.getValue("noRollbackFor");
    if (noRollbackFor != null) {
      setNoRollbackFor(noRollbackFor);
    }
    Object rollbackFor = this.annotationInfo.getValue("rollbackFor");
    if (rollbackFor != null) {
      setRollbackFor(rollbackFor);
    }
    Object serverName = this.annotationInfo.getValue("serverName");
    if ((serverName != null) && (!serverName.equals(""))) {
      setServerName(serverName);
    }
    this.mv.visitVarInsn(25, this.posTxScope);
    this.mv.visitMethodInsn(184, helpScopeTrans.getInternalName(), "createScopeTrans", "(" + txScopeType.getDescriptor() + ")" + scopeTransType.getDescriptor());
    
    this.mv.visitVarInsn(58, this.posScopeTrans);
  }
  
  protected void onMethodExit(int opcode)
  {
    if (!this.transactional) {
      return;
    }
    if (opcode == 177)
    {
      visitInsn(1);
    }
    else if ((opcode == 176) || (opcode == 191))
    {
      dup();
    }
    else
    {
      if ((opcode == 173) || (opcode == 175)) {
        dup2();
      } else {
        dup();
      }
      box(Type.getReturnType(this.methodDesc));
    }
    visitIntInsn(17, opcode);
    loadLocal(this.posScopeTrans);
    
    visitMethodInsn(184, helpScopeTrans.getInternalName(), "onExitScopeTrans", "(Ljava/lang/Object;I" + scopeTransType.getDescriptor() + ")V");
  }
}
