package com.avaje.ebean.enhance.asm.commons;

import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodAdapter;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;
import com.avaje.ebean.enhance.asm.Type;

public class LocalVariablesSorter
  extends MethodAdapter
{
  private static final Type OBJECT_TYPE = Type.getObjectType("java/lang/Object");
  private int[] mapping = new int[40];
  private Object[] newLocals = new Object[20];
  protected final int firstLocal;
  protected int nextLocal;
  private boolean changed;
  
  public LocalVariablesSorter(int access, String desc, MethodVisitor mv)
  {
    super(mv);
    Type[] args = Type.getArgumentTypes(desc);
    this.nextLocal = ((0x8 & access) == 0 ? 1 : 0);
    for (int i = 0; i < args.length; i++) {
      this.nextLocal += args[i].getSize();
    }
    this.firstLocal = this.nextLocal;
  }
  
  public void visitVarInsn(int opcode, int var)
  {
    Type type;
    switch (opcode)
    {
    case 22: 
    case 55: 
      type = Type.LONG_TYPE;
      break;
    case 24: 
    case 57: 
      type = Type.DOUBLE_TYPE;
      break;
    case 23: 
    case 56: 
      type = Type.FLOAT_TYPE;
      break;
    case 21: 
    case 54: 
      type = Type.INT_TYPE;
      break;
    case 25: 
    case 58: 
      type = OBJECT_TYPE;
      break;
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    default: 
      type = Type.VOID_TYPE;
    }
    this.mv.visitVarInsn(opcode, remap(var, type));
  }
  
  public void visitIincInsn(int var, int increment)
  {
    this.mv.visitIincInsn(remap(var, Type.INT_TYPE), increment);
  }
  
  public void visitMaxs(int maxStack, int maxLocals)
  {
    this.mv.visitMaxs(maxStack, this.nextLocal);
  }
  
  public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
  {
    int newIndex = remap(index, Type.getType(desc));
    this.mv.visitLocalVariable(name, desc, signature, start, end, newIndex);
  }
  
  public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
  {
    if (type != -1) {
      throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
    }
    if (!this.changed)
    {
      this.mv.visitFrame(type, nLocal, local, nStack, stack);
      return;
    }
    Object[] oldLocals = new Object[this.newLocals.length];
    System.arraycopy(this.newLocals, 0, oldLocals, 0, oldLocals.length);
    
    int index = 0;
    for (int number = 0; number < nLocal; number++)
    {
      Object t = local[number];
      int size = (t == Opcodes.LONG) || (t == Opcodes.DOUBLE) ? 2 : 1;
      if (t != Opcodes.TOP) {
        setFrameLocal(remap(index, size), t);
      }
      index += size;
    }
    index = 0;
    number = 0;
    for (int i = 0; index < this.newLocals.length; i++)
    {
      Object t = this.newLocals[(index++)];
      if ((t != null) && (t != Opcodes.TOP))
      {
        this.newLocals[i] = t;
        number = i + 1;
        if ((t == Opcodes.LONG) || (t == Opcodes.DOUBLE)) {
          index++;
        }
      }
      else
      {
        this.newLocals[i] = Opcodes.TOP;
      }
    }
    this.mv.visitFrame(type, number, this.newLocals, nStack, stack);
    
    this.newLocals = oldLocals;
  }
  
  public int newLocal(Type type)
  {
    Object t;
    switch (type.getSort())
    {
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      t = Opcodes.INTEGER;
      break;
    case 6: 
      t = Opcodes.FLOAT;
      break;
    case 7: 
      t = Opcodes.LONG;
      break;
    case 8: 
      t = Opcodes.DOUBLE;
      break;
    case 9: 
      t = type.getDescriptor();
      break;
    default: 
      t = type.getInternalName();
    }
    int local = this.nextLocal;
    this.nextLocal += type.getSize();
    setLocalType(local, type);
    setFrameLocal(local, t);
    return local;
  }
  
  protected void setLocalType(int local, Type type) {}
  
  private void setFrameLocal(int local, Object type)
  {
    int l = this.newLocals.length;
    if (local >= l)
    {
      Object[] a = new Object[Math.max(2 * l, local + 1)];
      System.arraycopy(this.newLocals, 0, a, 0, l);
      this.newLocals = a;
    }
    this.newLocals[local] = type;
  }
  
  private int remap(int var, Type type)
  {
    if (var < this.firstLocal) {
      return var;
    }
    int key = 2 * var + type.getSize() - 1;
    int size = this.mapping.length;
    if (key >= size)
    {
      int[] newMapping = new int[Math.max(2 * size, key + 1)];
      System.arraycopy(this.mapping, 0, newMapping, 0, size);
      this.mapping = newMapping;
    }
    int value = this.mapping[key];
    if (value == 0)
    {
      value = newLocalMapping(type);
      setLocalType(value, type);
      this.mapping[key] = (value + 1);
    }
    else
    {
      value--;
    }
    if (value != var) {
      this.changed = true;
    }
    return value;
  }
  
  protected int newLocalMapping(Type type)
  {
    int local = this.nextLocal;
    this.nextLocal += type.getSize();
    return local;
  }
  
  private int remap(int var, int size)
  {
    if ((var < this.firstLocal) || (!this.changed)) {
      return var;
    }
    int key = 2 * var + size - 1;
    int value = key < this.mapping.length ? this.mapping[key] : 0;
    if (value == 0) {
      throw new IllegalStateException("Unknown local variable " + var);
    }
    return value - 1;
  }
}
