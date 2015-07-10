package com.avaje.ebean.enhance.asm.commons;

import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Label;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import com.avaje.ebean.enhance.asm.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratorAdapter
  extends LocalVariablesSorter
{
  private static final String CLDESC = "Ljava/lang/Class;";
  private static final Type BYTE_TYPE = Type.getObjectType("java/lang/Byte");
  private static final Type BOOLEAN_TYPE = Type.getObjectType("java/lang/Boolean");
  private static final Type SHORT_TYPE = Type.getObjectType("java/lang/Short");
  private static final Type CHARACTER_TYPE = Type.getObjectType("java/lang/Character");
  private static final Type INTEGER_TYPE = Type.getObjectType("java/lang/Integer");
  private static final Type FLOAT_TYPE = Type.getObjectType("java/lang/Float");
  private static final Type LONG_TYPE = Type.getObjectType("java/lang/Long");
  private static final Type DOUBLE_TYPE = Type.getObjectType("java/lang/Double");
  private static final Type NUMBER_TYPE = Type.getObjectType("java/lang/Number");
  private static final Type OBJECT_TYPE = Type.getObjectType("java/lang/Object");
  private static final Method BOOLEAN_VALUE = Method.getMethod("boolean booleanValue()");
  private static final Method CHAR_VALUE = Method.getMethod("char charValue()");
  private static final Method INT_VALUE = Method.getMethod("int intValue()");
  private static final Method FLOAT_VALUE = Method.getMethod("float floatValue()");
  private static final Method LONG_VALUE = Method.getMethod("long longValue()");
  private static final Method DOUBLE_VALUE = Method.getMethod("double doubleValue()");
  public static final int ADD = 96;
  public static final int SUB = 100;
  public static final int MUL = 104;
  public static final int DIV = 108;
  public static final int REM = 112;
  public static final int NEG = 116;
  public static final int SHL = 120;
  public static final int SHR = 122;
  public static final int USHR = 124;
  public static final int AND = 126;
  public static final int OR = 128;
  public static final int XOR = 130;
  public static final int EQ = 153;
  public static final int NE = 154;
  public static final int LT = 155;
  public static final int GE = 156;
  public static final int GT = 157;
  public static final int LE = 158;
  private final int access;
  private final Type returnType;
  private final Type[] argumentTypes;
  private final List localTypes = new ArrayList();
  
  public GeneratorAdapter(MethodVisitor mv, int access, String name, String desc)
  {
    super(access, desc, mv);
    this.access = access;
    this.returnType = Type.getReturnType(desc);
    this.argumentTypes = Type.getArgumentTypes(desc);
  }
  
  public GeneratorAdapter(int access, Method method, MethodVisitor mv)
  {
    super(access, method.getDescriptor(), mv);
    this.access = access;
    this.returnType = method.getReturnType();
    this.argumentTypes = method.getArgumentTypes();
  }
  
  public GeneratorAdapter(int access, Method method, String signature, Type[] exceptions, ClassVisitor cv)
  {
    this(access, method, cv.visitMethod(access, method.getName(), method.getDescriptor(), signature, getInternalNames(exceptions)));
  }
  
  private static String[] getInternalNames(Type[] types)
  {
    if (types == null) {
      return null;
    }
    String[] names = new String[types.length];
    for (int i = 0; i < names.length; i++) {
      names[i] = types[i].getInternalName();
    }
    return names;
  }
  
  public void push(boolean value)
  {
    push(value ? 1 : 0);
  }
  
  public void push(int value)
  {
    if ((value >= -1) && (value <= 5)) {
      this.mv.visitInsn(3 + value);
    } else if ((value >= -128) && (value <= 127)) {
      this.mv.visitIntInsn(16, value);
    } else if ((value >= 32768) && (value <= 32767)) {
      this.mv.visitIntInsn(17, value);
    } else {
      this.mv.visitLdcInsn(Integer.valueOf(value));
    }
  }
  
  public void push(long value)
  {
    if ((value == 0L) || (value == 1L)) {
      this.mv.visitInsn(9 + (int)value);
    } else {
      this.mv.visitLdcInsn(Long.valueOf(value));
    }
  }
  
  public void push(float value)
  {
    int bits = Float.floatToIntBits(value);
    if ((bits == 0L) || (bits == 1065353216) || (bits == 1073741824)) {
      this.mv.visitInsn(11 + (int)value);
    } else {
      this.mv.visitLdcInsn(new Float(value));
    }
  }
  
  public void push(double value)
  {
    long bits = Double.doubleToLongBits(value);
    if ((bits == 0L) || (bits == 4607182418800017408L)) {
      this.mv.visitInsn(14 + (int)value);
    } else {
      this.mv.visitLdcInsn(new Double(value));
    }
  }
  
  public void push(String value)
  {
    if (value == null) {
      this.mv.visitInsn(1);
    } else {
      this.mv.visitLdcInsn(value);
    }
  }
  
  public void push(Type value)
  {
    if (value == null) {
      this.mv.visitInsn(1);
    } else {
      switch (value.getSort())
      {
      case 1: 
        this.mv.visitFieldInsn(178, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
        
        break;
      case 2: 
        this.mv.visitFieldInsn(178, "java/lang/Char", "TYPE", "Ljava/lang/Class;");
        
        break;
      case 3: 
        this.mv.visitFieldInsn(178, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
        
        break;
      case 4: 
        this.mv.visitFieldInsn(178, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
        
        break;
      case 5: 
        this.mv.visitFieldInsn(178, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
        
        break;
      case 6: 
        this.mv.visitFieldInsn(178, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
        
        break;
      case 7: 
        this.mv.visitFieldInsn(178, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
        
        break;
      case 8: 
        this.mv.visitFieldInsn(178, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
        
        break;
      default: 
        this.mv.visitLdcInsn(value);
      }
    }
  }
  
  private int getArgIndex(int arg)
  {
    int index = (this.access & 0x8) == 0 ? 1 : 0;
    for (int i = 0; i < arg; i++) {
      index += this.argumentTypes[i].getSize();
    }
    return index;
  }
  
  private void loadInsn(Type type, int index)
  {
    this.mv.visitVarInsn(type.getOpcode(21), index);
  }
  
  private void storeInsn(Type type, int index)
  {
    this.mv.visitVarInsn(type.getOpcode(54), index);
  }
  
  public void loadThis()
  {
    if ((this.access & 0x8) != 0) {
      throw new IllegalStateException("no 'this' pointer within static method");
    }
    this.mv.visitVarInsn(25, 0);
  }
  
  public void loadArg(int arg)
  {
    loadInsn(this.argumentTypes[arg], getArgIndex(arg));
  }
  
  public void loadArgs(int arg, int count)
  {
    int index = getArgIndex(arg);
    for (int i = 0; i < count; i++)
    {
      Type t = this.argumentTypes[(arg + i)];
      loadInsn(t, index);
      index += t.getSize();
    }
  }
  
  public void loadArgs()
  {
    loadArgs(0, this.argumentTypes.length);
  }
  
  public void loadArgArray()
  {
    push(this.argumentTypes.length);
    newArray(OBJECT_TYPE);
    for (int i = 0; i < this.argumentTypes.length; i++)
    {
      dup();
      push(i);
      loadArg(i);
      box(this.argumentTypes[i]);
      arrayStore(OBJECT_TYPE);
    }
  }
  
  public void storeArg(int arg)
  {
    storeInsn(this.argumentTypes[arg], getArgIndex(arg));
  }
  
  public Type getLocalType(int local)
  {
    return (Type)this.localTypes.get(local - this.firstLocal);
  }
  
  protected void setLocalType(int local, Type type)
  {
    int index = local - this.firstLocal;
    while (this.localTypes.size() < index + 1) {
      this.localTypes.add(null);
    }
    this.localTypes.set(index, type);
  }
  
  public void loadLocal(int local)
  {
    loadInsn(getLocalType(local), local);
  }
  
  public void loadLocal(int local, Type type)
  {
    setLocalType(local, type);
    loadInsn(type, local);
  }
  
  public void storeLocal(int local)
  {
    storeInsn(getLocalType(local), local);
  }
  
  public void storeLocal(int local, Type type)
  {
    setLocalType(local, type);
    storeInsn(type, local);
  }
  
  public void arrayLoad(Type type)
  {
    this.mv.visitInsn(type.getOpcode(46));
  }
  
  public void arrayStore(Type type)
  {
    this.mv.visitInsn(type.getOpcode(79));
  }
  
  public void pop()
  {
    this.mv.visitInsn(87);
  }
  
  public void pop2()
  {
    this.mv.visitInsn(88);
  }
  
  public void dup()
  {
    this.mv.visitInsn(89);
  }
  
  public void dup2()
  {
    this.mv.visitInsn(92);
  }
  
  public void dupX1()
  {
    this.mv.visitInsn(90);
  }
  
  public void dupX2()
  {
    this.mv.visitInsn(91);
  }
  
  public void dup2X1()
  {
    this.mv.visitInsn(93);
  }
  
  public void dup2X2()
  {
    this.mv.visitInsn(94);
  }
  
  public void swap()
  {
    this.mv.visitInsn(95);
  }
  
  public void swap(Type prev, Type type)
  {
    if (type.getSize() == 1)
    {
      if (prev.getSize() == 1)
      {
        swap();
      }
      else
      {
        dupX2();
        pop();
      }
    }
    else if (prev.getSize() == 1)
    {
      dup2X1();
      pop2();
    }
    else
    {
      dup2X2();
      pop2();
    }
  }
  
  public void math(int op, Type type)
  {
    this.mv.visitInsn(type.getOpcode(op));
  }
  
  public void not()
  {
    this.mv.visitInsn(4);
    this.mv.visitInsn(130);
  }
  
  public void iinc(int local, int amount)
  {
    this.mv.visitIincInsn(local, amount);
  }
  
  public void cast(Type from, Type to)
  {
    if (from != to) {
      if (from == Type.DOUBLE_TYPE)
      {
        if (to == Type.FLOAT_TYPE)
        {
          this.mv.visitInsn(144);
        }
        else if (to == Type.LONG_TYPE)
        {
          this.mv.visitInsn(143);
        }
        else
        {
          this.mv.visitInsn(142);
          cast(Type.INT_TYPE, to);
        }
      }
      else if (from == Type.FLOAT_TYPE)
      {
        if (to == Type.DOUBLE_TYPE)
        {
          this.mv.visitInsn(141);
        }
        else if (to == Type.LONG_TYPE)
        {
          this.mv.visitInsn(140);
        }
        else
        {
          this.mv.visitInsn(139);
          cast(Type.INT_TYPE, to);
        }
      }
      else if (from == Type.LONG_TYPE)
      {
        if (to == Type.DOUBLE_TYPE)
        {
          this.mv.visitInsn(138);
        }
        else if (to == Type.FLOAT_TYPE)
        {
          this.mv.visitInsn(137);
        }
        else
        {
          this.mv.visitInsn(136);
          cast(Type.INT_TYPE, to);
        }
      }
      else if (to == Type.BYTE_TYPE) {
        this.mv.visitInsn(145);
      } else if (to == Type.CHAR_TYPE) {
        this.mv.visitInsn(146);
      } else if (to == Type.DOUBLE_TYPE) {
        this.mv.visitInsn(135);
      } else if (to == Type.FLOAT_TYPE) {
        this.mv.visitInsn(134);
      } else if (to == Type.LONG_TYPE) {
        this.mv.visitInsn(133);
      } else if (to == Type.SHORT_TYPE) {
        this.mv.visitInsn(147);
      }
    }
  }
  
  public void box(Type type)
  {
    if ((type.getSort() == 10) || (type.getSort() == 9)) {
      return;
    }
    if (type == Type.VOID_TYPE)
    {
      push((String)null);
    }
    else
    {
      Type boxed = type;
      switch (type.getSort())
      {
      case 3: 
        boxed = BYTE_TYPE;
        break;
      case 1: 
        boxed = BOOLEAN_TYPE;
        break;
      case 4: 
        boxed = SHORT_TYPE;
        break;
      case 2: 
        boxed = CHARACTER_TYPE;
        break;
      case 5: 
        boxed = INTEGER_TYPE;
        break;
      case 6: 
        boxed = FLOAT_TYPE;
        break;
      case 7: 
        boxed = LONG_TYPE;
        break;
      case 8: 
        boxed = DOUBLE_TYPE;
      }
      newInstance(boxed);
      if (type.getSize() == 2)
      {
        dupX2();
        dupX2();
        pop();
      }
      else
      {
        dupX1();
        swap();
      }
      invokeConstructor(boxed, new Method("<init>", Type.VOID_TYPE, new Type[] { type }));
    }
  }
  
  public void unbox(Type type)
  {
    Type t = NUMBER_TYPE;
    Method sig = null;
    switch (type.getSort())
    {
    case 0: 
      return;
    case 2: 
      t = CHARACTER_TYPE;
      sig = CHAR_VALUE;
      break;
    case 1: 
      t = BOOLEAN_TYPE;
      sig = BOOLEAN_VALUE;
      break;
    case 8: 
      sig = DOUBLE_VALUE;
      break;
    case 6: 
      sig = FLOAT_VALUE;
      break;
    case 7: 
      sig = LONG_VALUE;
      break;
    case 3: 
    case 4: 
    case 5: 
      sig = INT_VALUE;
    }
    if (sig == null)
    {
      checkCast(type);
    }
    else
    {
      checkCast(t);
      invokeVirtual(t, sig);
    }
  }
  
  public Label newLabel()
  {
    return new Label();
  }
  
  public void mark(Label label)
  {
    this.mv.visitLabel(label);
  }
  
  public Label mark()
  {
    Label label = new Label();
    this.mv.visitLabel(label);
    return label;
  }
  
  public void ifCmp(Type type, int mode, Label label)
  {
    int intOp = -1;
    switch (type.getSort())
    {
    case 7: 
      this.mv.visitInsn(148);
      break;
    case 8: 
      this.mv.visitInsn(152);
      break;
    case 6: 
      this.mv.visitInsn(150);
      break;
    case 9: 
    case 10: 
      switch (mode)
      {
      case 153: 
        this.mv.visitJumpInsn(165, label);
        return;
      case 154: 
        this.mv.visitJumpInsn(166, label);
        return;
      }
      throw new IllegalArgumentException("Bad comparison for type " + type);
    default: 
      switch (mode)
      {
      case 153: 
        intOp = 159;
        break;
      case 154: 
        intOp = 160;
        break;
      case 156: 
        intOp = 162;
        break;
      case 155: 
        intOp = 161;
        break;
      case 158: 
        intOp = 164;
        break;
      case 157: 
        intOp = 163;
      }
      this.mv.visitJumpInsn(intOp, label);
      return;
    }
    int jumpMode = mode;
    switch (mode)
    {
    case 156: 
      jumpMode = 155;
      break;
    case 158: 
      jumpMode = 157;
    }
    this.mv.visitJumpInsn(jumpMode, label);
  }
  
  public void ifICmp(int mode, Label label)
  {
    ifCmp(Type.INT_TYPE, mode, label);
  }
  
  public void ifZCmp(int mode, Label label)
  {
    this.mv.visitJumpInsn(mode, label);
  }
  
  public void ifNull(Label label)
  {
    this.mv.visitJumpInsn(198, label);
  }
  
  public void ifNonNull(Label label)
  {
    this.mv.visitJumpInsn(199, label);
  }
  
  public void goTo(Label label)
  {
    this.mv.visitJumpInsn(167, label);
  }
  
  public void ret(int local)
  {
    this.mv.visitVarInsn(169, local);
  }
  
  public void tableSwitch(int[] keys, TableSwitchGenerator generator)
  {
    float density;
    float density;
    if (keys.length == 0) {
      density = 0.0F;
    } else {
      density = keys.length / (keys[(keys.length - 1)] - keys[0] + 1);
    }
    tableSwitch(keys, generator, density >= 0.5F);
  }
  
  public void tableSwitch(int[] keys, TableSwitchGenerator generator, boolean useTable)
  {
    for (int i = 1; i < keys.length; i++) {
      if (keys[i] < keys[(i - 1)]) {
        throw new IllegalArgumentException("keys must be sorted ascending");
      }
    }
    Label def = newLabel();
    Label end = newLabel();
    if (keys.length > 0)
    {
      int len = keys.length;
      int min = keys[0];
      int max = keys[(len - 1)];
      int range = max - min + 1;
      if (useTable)
      {
        Label[] labels = new Label[range];
        Arrays.fill(labels, def);
        for (int i = 0; i < len; i++) {
          labels[(keys[i] - min)] = newLabel();
        }
        this.mv.visitTableSwitchInsn(min, max, def, labels);
        for (int i = 0; i < range; i++)
        {
          Label label = labels[i];
          if (label != def)
          {
            mark(label);
            generator.generateCase(i + min, end);
          }
        }
      }
      else
      {
        Label[] labels = new Label[len];
        for (int i = 0; i < len; i++) {
          labels[i] = newLabel();
        }
        this.mv.visitLookupSwitchInsn(def, keys, labels);
        for (int i = 0; i < len; i++)
        {
          mark(labels[i]);
          generator.generateCase(keys[i], end);
        }
      }
    }
    mark(def);
    generator.generateDefault();
    mark(end);
  }
  
  public void returnValue()
  {
    this.mv.visitInsn(this.returnType.getOpcode(172));
  }
  
  private void fieldInsn(int opcode, Type ownerType, String name, Type fieldType)
  {
    this.mv.visitFieldInsn(opcode, ownerType.getInternalName(), name, fieldType.getDescriptor());
  }
  
  public void getStatic(Type owner, String name, Type type)
  {
    fieldInsn(178, owner, name, type);
  }
  
  public void putStatic(Type owner, String name, Type type)
  {
    fieldInsn(179, owner, name, type);
  }
  
  public void getField(Type owner, String name, Type type)
  {
    fieldInsn(180, owner, name, type);
  }
  
  public void putField(Type owner, String name, Type type)
  {
    fieldInsn(181, owner, name, type);
  }
  
  private void invokeInsn(int opcode, Type type, Method method)
  {
    String owner = type.getSort() == 9 ? type.getDescriptor() : type.getInternalName();
    
    this.mv.visitMethodInsn(opcode, owner, method.getName(), method.getDescriptor());
  }
  
  public void invokeVirtual(Type owner, Method method)
  {
    invokeInsn(182, owner, method);
  }
  
  public void invokeConstructor(Type type, Method method)
  {
    invokeInsn(183, type, method);
  }
  
  public void invokeStatic(Type owner, Method method)
  {
    invokeInsn(184, owner, method);
  }
  
  public void invokeInterface(Type owner, Method method)
  {
    invokeInsn(185, owner, method);
  }
  
  private void typeInsn(int opcode, Type type)
  {
    this.mv.visitTypeInsn(opcode, type.getInternalName());
  }
  
  public void newInstance(Type type)
  {
    typeInsn(187, type);
  }
  
  public void newArray(Type type)
  {
    int typ;
    switch (type.getSort())
    {
    case 1: 
      typ = 4;
      break;
    case 2: 
      typ = 5;
      break;
    case 3: 
      typ = 8;
      break;
    case 4: 
      typ = 9;
      break;
    case 5: 
      typ = 10;
      break;
    case 6: 
      typ = 6;
      break;
    case 7: 
      typ = 11;
      break;
    case 8: 
      typ = 7;
      break;
    default: 
      typeInsn(189, type);
      return;
    }
    this.mv.visitIntInsn(188, typ);
  }
  
  public void arrayLength()
  {
    this.mv.visitInsn(190);
  }
  
  public void throwException()
  {
    this.mv.visitInsn(191);
  }
  
  public void throwException(Type type, String msg)
  {
    newInstance(type);
    dup();
    push(msg);
    invokeConstructor(type, Method.getMethod("void <init> (String)"));
    throwException();
  }
  
  public void checkCast(Type type)
  {
    if (!type.equals(OBJECT_TYPE)) {
      typeInsn(192, type);
    }
  }
  
  public void instanceOf(Type type)
  {
    typeInsn(193, type);
  }
  
  public void monitorEnter()
  {
    this.mv.visitInsn(194);
  }
  
  public void monitorExit()
  {
    this.mv.visitInsn(195);
  }
  
  public void endMethod()
  {
    if ((this.access & 0x400) == 0) {
      this.mv.visitMaxs(0, 0);
    }
    this.mv.visitEnd();
  }
  
  public void catchException(Label start, Label end, Type exception)
  {
    this.mv.visitTryCatchBlock(start, end, mark(), exception.getInternalName());
  }
}
