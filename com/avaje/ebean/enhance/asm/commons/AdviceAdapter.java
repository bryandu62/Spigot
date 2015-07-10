package com.avaje.ebean.enhance.asm.commons;

import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;
import com.avaje.ebean.enhance.asm.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AdviceAdapter
  extends GeneratorAdapter
  implements Opcodes
{
  private static final Object THIS = new Object();
  private static final Object OTHER = new Object();
  protected int methodAccess;
  protected String methodDesc;
  private boolean constructor;
  private boolean superInitialized;
  private List stackFrame;
  private Map branches;
  
  protected AdviceAdapter(MethodVisitor mv, int access, String name, String desc)
  {
    super(mv, access, name, desc);
    this.methodAccess = access;
    this.methodDesc = desc;
    
    this.constructor = "<init>".equals(name);
  }
  
  public void visitCode()
  {
    this.mv.visitCode();
    if (this.constructor)
    {
      this.stackFrame = new ArrayList();
      this.branches = new HashMap();
    }
    else
    {
      this.superInitialized = true;
      onMethodEnter();
    }
  }
  
  public void visitLabel(Label label)
  {
    this.mv.visitLabel(label);
    if ((this.constructor) && (this.branches != null))
    {
      List frame = (List)this.branches.get(label);
      if (frame != null)
      {
        this.stackFrame = frame;
        this.branches.remove(label);
      }
    }
  }
  
  public void visitInsn(int opcode)
  {
    if (this.constructor)
    {
      int s;
      switch (opcode)
      {
      case 177: 
        onMethodExit(opcode);
        break;
      case 172: 
      case 174: 
      case 176: 
      case 191: 
        popValue();
        onMethodExit(opcode);
        break;
      case 173: 
      case 175: 
        popValue();
        popValue();
        onMethodExit(opcode);
        break;
      case 0: 
      case 47: 
      case 49: 
      case 116: 
      case 117: 
      case 118: 
      case 119: 
      case 134: 
      case 138: 
      case 139: 
      case 143: 
      case 145: 
      case 146: 
      case 147: 
      case 190: 
        break;
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 11: 
      case 12: 
      case 13: 
      case 133: 
      case 135: 
      case 140: 
      case 141: 
        pushValue(OTHER);
        break;
      case 9: 
      case 10: 
      case 14: 
      case 15: 
        pushValue(OTHER);
        pushValue(OTHER);
        break;
      case 46: 
      case 48: 
      case 50: 
      case 51: 
      case 52: 
      case 53: 
      case 87: 
      case 96: 
      case 98: 
      case 100: 
      case 102: 
      case 104: 
      case 106: 
      case 108: 
      case 110: 
      case 112: 
      case 114: 
      case 120: 
      case 121: 
      case 122: 
      case 123: 
      case 124: 
      case 125: 
      case 126: 
      case 128: 
      case 130: 
      case 136: 
      case 137: 
      case 142: 
      case 144: 
      case 149: 
      case 150: 
      case 194: 
      case 195: 
        popValue();
        break;
      case 88: 
      case 97: 
      case 99: 
      case 101: 
      case 103: 
      case 105: 
      case 107: 
      case 109: 
      case 111: 
      case 113: 
      case 115: 
      case 127: 
      case 129: 
      case 131: 
        popValue();
        popValue();
        break;
      case 79: 
      case 81: 
      case 83: 
      case 84: 
      case 85: 
      case 86: 
      case 148: 
      case 151: 
      case 152: 
        popValue();
        popValue();
        popValue();
        break;
      case 80: 
      case 82: 
        popValue();
        popValue();
        popValue();
        popValue();
        break;
      case 89: 
        pushValue(peekValue());
        break;
      case 90: 
        s = this.stackFrame.size();
        this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
        break;
      case 91: 
        s = this.stackFrame.size();
        this.stackFrame.add(s - 3, this.stackFrame.get(s - 1));
        break;
      case 92: 
        s = this.stackFrame.size();
        this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
        this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
        break;
      case 93: 
        s = this.stackFrame.size();
        this.stackFrame.add(s - 3, this.stackFrame.get(s - 1));
        this.stackFrame.add(s - 3, this.stackFrame.get(s - 1));
        break;
      case 94: 
        s = this.stackFrame.size();
        this.stackFrame.add(s - 4, this.stackFrame.get(s - 1));
        this.stackFrame.add(s - 4, this.stackFrame.get(s - 1));
        break;
      case 95: 
        s = this.stackFrame.size();
        this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
        this.stackFrame.remove(s);
      }
    }
    else
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
    }
    this.mv.visitInsn(opcode);
  }
  
  public void visitVarInsn(int opcode, int var)
  {
    super.visitVarInsn(opcode, var);
    if (this.constructor) {
      switch (opcode)
      {
      case 21: 
      case 23: 
        pushValue(OTHER);
        break;
      case 22: 
      case 24: 
        pushValue(OTHER);
        pushValue(OTHER);
        break;
      case 25: 
        pushValue(var == 0 ? THIS : OTHER);
        break;
      case 54: 
      case 56: 
      case 58: 
        popValue();
        break;
      case 55: 
      case 57: 
        popValue();
        popValue();
      }
    }
  }
  
  public void visitFieldInsn(int opcode, String owner, String name, String desc)
  {
    this.mv.visitFieldInsn(opcode, owner, name, desc);
    if (this.constructor)
    {
      char c = desc.charAt(0);
      boolean longOrDouble = (c == 'J') || (c == 'D');
      switch (opcode)
      {
      case 178: 
        pushValue(OTHER);
        if (longOrDouble) {
          pushValue(OTHER);
        }
        break;
      case 179: 
        popValue();
        if (longOrDouble) {
          popValue();
        }
        break;
      case 181: 
        popValue();
        if (longOrDouble)
        {
          popValue();
          popValue();
        }
        break;
      case 180: 
      default: 
        if (longOrDouble) {
          pushValue(OTHER);
        }
        break;
      }
    }
  }
  
  public void visitIntInsn(int opcode, int operand)
  {
    this.mv.visitIntInsn(opcode, operand);
    if ((this.constructor) && (opcode != 188)) {
      pushValue(OTHER);
    }
  }
  
  public void visitLdcInsn(Object cst)
  {
    this.mv.visitLdcInsn(cst);
    if (this.constructor)
    {
      pushValue(OTHER);
      if (((cst instanceof Double)) || ((cst instanceof Long))) {
        pushValue(OTHER);
      }
    }
  }
  
  public void visitMultiANewArrayInsn(String desc, int dims)
  {
    this.mv.visitMultiANewArrayInsn(desc, dims);
    if (this.constructor)
    {
      for (int i = 0; i < dims; i++) {
        popValue();
      }
      pushValue(OTHER);
    }
  }
  
  public void visitTypeInsn(int opcode, String type)
  {
    this.mv.visitTypeInsn(opcode, type);
    if ((this.constructor) && (opcode == 187)) {
      pushValue(OTHER);
    }
  }
  
  public void visitMethodInsn(int opcode, String owner, String name, String desc)
  {
    this.mv.visitMethodInsn(opcode, owner, name, desc);
    if (this.constructor)
    {
      Type[] types = Type.getArgumentTypes(desc);
      for (int i = 0; i < types.length; i++)
      {
        popValue();
        if (types[i].getSize() == 2) {
          popValue();
        }
      }
      switch (opcode)
      {
      case 182: 
      case 185: 
        popValue();
        break;
      case 183: 
        Object type = popValue();
        if ((type == THIS) && (!this.superInitialized))
        {
          onMethodEnter();
          this.superInitialized = true;
          
          this.constructor = false;
        }
        break;
      }
      Type returnType = Type.getReturnType(desc);
      if (returnType != Type.VOID_TYPE)
      {
        pushValue(OTHER);
        if (returnType.getSize() == 2) {
          pushValue(OTHER);
        }
      }
    }
  }
  
  public void visitJumpInsn(int opcode, Label label)
  {
    this.mv.visitJumpInsn(opcode, label);
    if (this.constructor)
    {
      switch (opcode)
      {
      case 153: 
      case 154: 
      case 155: 
      case 156: 
      case 157: 
      case 158: 
      case 198: 
      case 199: 
        popValue();
        break;
      case 159: 
      case 160: 
      case 161: 
      case 162: 
      case 163: 
      case 164: 
      case 165: 
      case 166: 
        popValue();
        popValue();
        break;
      case 168: 
        pushValue(OTHER);
      }
      addBranch(label);
    }
  }
  
  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
  {
    this.mv.visitLookupSwitchInsn(dflt, keys, labels);
    if (this.constructor)
    {
      popValue();
      addBranches(dflt, labels);
    }
  }
  
  public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels)
  {
    this.mv.visitTableSwitchInsn(min, max, dflt, labels);
    if (this.constructor)
    {
      popValue();
      addBranches(dflt, labels);
    }
  }
  
  private void addBranches(Label dflt, Label[] labels)
  {
    addBranch(dflt);
    for (int i = 0; i < labels.length; i++) {
      addBranch(labels[i]);
    }
  }
  
  private void addBranch(Label label)
  {
    if (this.branches.containsKey(label)) {
      return;
    }
    this.branches.put(label, new ArrayList(this.stackFrame));
  }
  
  private Object popValue()
  {
    return this.stackFrame.remove(this.stackFrame.size() - 1);
  }
  
  private Object peekValue()
  {
    return this.stackFrame.get(this.stackFrame.size() - 1);
  }
  
  private void pushValue(Object o)
  {
    this.stackFrame.add(o);
  }
  
  protected void onMethodEnter() {}
  
  protected void onMethodExit(int opcode) {}
}
