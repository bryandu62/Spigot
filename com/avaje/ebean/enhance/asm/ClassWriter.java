package com.avaje.ebean.enhance.asm;

public class ClassWriter
  implements ClassVisitor
{
  public static final int COMPUTE_MAXS = 1;
  public static final int COMPUTE_FRAMES = 2;
  static final int NOARG_INSN = 0;
  static final int SBYTE_INSN = 1;
  static final int SHORT_INSN = 2;
  static final int VAR_INSN = 3;
  static final int IMPLVAR_INSN = 4;
  static final int TYPE_INSN = 5;
  static final int FIELDORMETH_INSN = 6;
  static final int ITFMETH_INSN = 7;
  static final int LABEL_INSN = 8;
  static final int LABELW_INSN = 9;
  static final int LDC_INSN = 10;
  static final int LDCW_INSN = 11;
  static final int IINC_INSN = 12;
  static final int TABL_INSN = 13;
  static final int LOOK_INSN = 14;
  static final int MANA_INSN = 15;
  static final int WIDE_INSN = 16;
  static final byte[] TYPE;
  static final int CLASS = 7;
  static final int FIELD = 9;
  static final int METH = 10;
  static final int IMETH = 11;
  static final int STR = 8;
  static final int INT = 3;
  static final int FLOAT = 4;
  static final int LONG = 5;
  static final int DOUBLE = 6;
  static final int NAME_TYPE = 12;
  static final int UTF8 = 1;
  static final int TYPE_NORMAL = 13;
  static final int TYPE_UNINIT = 14;
  static final int TYPE_MERGED = 15;
  ClassReader cr;
  int version;
  int index;
  final ByteVector pool;
  Item[] items;
  int threshold;
  final Item key;
  final Item key2;
  final Item key3;
  Item[] typeTable;
  private short typeCount;
  private int access;
  private int name;
  String thisName;
  private int signature;
  private int superName;
  private int interfaceCount;
  private int[] interfaces;
  private int sourceFile;
  private ByteVector sourceDebug;
  private int enclosingMethodOwner;
  private int enclosingMethod;
  private AnnotationWriter anns;
  private AnnotationWriter ianns;
  private Attribute attrs;
  private int innerClassesCount;
  private ByteVector innerClasses;
  FieldWriter firstField;
  FieldWriter lastField;
  MethodWriter firstMethod;
  MethodWriter lastMethod;
  private final boolean computeMaxs;
  private final boolean computeFrames;
  boolean invalidFrames;
  
  static
  {
    byte[] b = new byte['Ü'];
    String s = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHAFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
    for (int i = 0; i < b.length; i++) {
      b[i] = ((byte)(s.charAt(i) - 'A'));
    }
    TYPE = b;
  }
  
  public ClassWriter(int flags)
  {
    this.index = 1;
    this.pool = new ByteVector();
    this.items = new Item['Ā'];
    this.threshold = ((int)(0.75D * this.items.length));
    this.key = new Item();
    this.key2 = new Item();
    this.key3 = new Item();
    this.computeMaxs = ((flags & 0x1) != 0);
    this.computeFrames = ((flags & 0x2) != 0);
  }
  
  public ClassWriter(ClassReader classReader, int flags)
  {
    this(flags);
    classReader.copyPool(this);
    this.cr = classReader;
  }
  
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    this.version = version;
    this.access = access;
    this.name = newClass(name);
    this.thisName = name;
    if (signature != null) {
      this.signature = newUTF8(signature);
    }
    this.superName = (superName == null ? 0 : newClass(superName));
    if ((interfaces != null) && (interfaces.length > 0))
    {
      this.interfaceCount = interfaces.length;
      this.interfaces = new int[this.interfaceCount];
      for (int i = 0; i < this.interfaceCount; i++) {
        this.interfaces[i] = newClass(interfaces[i]);
      }
    }
  }
  
  public void visitSource(String file, String debug)
  {
    if (file != null) {
      this.sourceFile = newUTF8(file);
    }
    if (debug != null) {
      this.sourceDebug = new ByteVector().putUTF8(debug);
    }
  }
  
  public void visitOuterClass(String owner, String name, String desc)
  {
    this.enclosingMethodOwner = newClass(owner);
    if ((name != null) && (desc != null)) {
      this.enclosingMethod = newNameType(name, desc);
    }
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    ByteVector bv = new ByteVector();
    
    bv.putShort(newUTF8(desc)).putShort(0);
    AnnotationWriter aw = new AnnotationWriter(this, true, bv, bv, 2);
    if (visible)
    {
      aw.next = this.anns;
      this.anns = aw;
    }
    else
    {
      aw.next = this.ianns;
      this.ianns = aw;
    }
    return aw;
  }
  
  public void visitAttribute(Attribute attr)
  {
    attr.next = this.attrs;
    this.attrs = attr;
  }
  
  public void visitInnerClass(String name, String outerName, String innerName, int access)
  {
    if (this.innerClasses == null) {
      this.innerClasses = new ByteVector();
    }
    this.innerClassesCount += 1;
    this.innerClasses.putShort(name == null ? 0 : newClass(name));
    this.innerClasses.putShort(outerName == null ? 0 : newClass(outerName));
    this.innerClasses.putShort(innerName == null ? 0 : newUTF8(innerName));
    this.innerClasses.putShort(access);
  }
  
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    return new FieldWriter(this, access, name, desc, signature, value);
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    return new MethodWriter(this, access, name, desc, signature, exceptions, this.computeMaxs, this.computeFrames);
  }
  
  public byte[] toByteArray()
  {
    int size = 24 + 2 * this.interfaceCount;
    int nbFields = 0;
    FieldWriter fb = this.firstField;
    while (fb != null)
    {
      nbFields++;
      size += fb.getSize();
      fb = fb.next;
    }
    int nbMethods = 0;
    MethodWriter mb = this.firstMethod;
    while (mb != null)
    {
      nbMethods++;
      size += mb.getSize();
      mb = mb.next;
    }
    int attributeCount = 0;
    if (this.signature != 0)
    {
      attributeCount++;
      size += 8;
      newUTF8("Signature");
    }
    if (this.sourceFile != 0)
    {
      attributeCount++;
      size += 8;
      newUTF8("SourceFile");
    }
    if (this.sourceDebug != null)
    {
      attributeCount++;
      size += this.sourceDebug.length + 4;
      newUTF8("SourceDebugExtension");
    }
    if (this.enclosingMethodOwner != 0)
    {
      attributeCount++;
      size += 10;
      newUTF8("EnclosingMethod");
    }
    if ((this.access & 0x20000) != 0)
    {
      attributeCount++;
      size += 6;
      newUTF8("Deprecated");
    }
    if (((this.access & 0x1000) != 0) && ((this.version & 0xFFFF) < 49))
    {
      attributeCount++;
      size += 6;
      newUTF8("Synthetic");
    }
    if (this.innerClasses != null)
    {
      attributeCount++;
      size += 8 + this.innerClasses.length;
      newUTF8("InnerClasses");
    }
    if (this.anns != null)
    {
      attributeCount++;
      size += 8 + this.anns.getSize();
      newUTF8("RuntimeVisibleAnnotations");
    }
    if (this.ianns != null)
    {
      attributeCount++;
      size += 8 + this.ianns.getSize();
      newUTF8("RuntimeInvisibleAnnotations");
    }
    if (this.attrs != null)
    {
      attributeCount += this.attrs.getCount();
      size += this.attrs.getSize(this, null, 0, -1, -1);
    }
    size += this.pool.length;
    
    ByteVector out = new ByteVector(size);
    out.putInt(-889275714).putInt(this.version);
    out.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
    out.putShort(this.access).putShort(this.name).putShort(this.superName);
    out.putShort(this.interfaceCount);
    for (int i = 0; i < this.interfaceCount; i++) {
      out.putShort(this.interfaces[i]);
    }
    out.putShort(nbFields);
    fb = this.firstField;
    while (fb != null)
    {
      fb.put(out);
      fb = fb.next;
    }
    out.putShort(nbMethods);
    mb = this.firstMethod;
    while (mb != null)
    {
      mb.put(out);
      mb = mb.next;
    }
    out.putShort(attributeCount);
    if (this.signature != 0) {
      out.putShort(newUTF8("Signature")).putInt(2).putShort(this.signature);
    }
    if (this.sourceFile != 0) {
      out.putShort(newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
    }
    if (this.sourceDebug != null)
    {
      int len = this.sourceDebug.length - 2;
      out.putShort(newUTF8("SourceDebugExtension")).putInt(len);
      out.putByteArray(this.sourceDebug.data, 2, len);
    }
    if (this.enclosingMethodOwner != 0)
    {
      out.putShort(newUTF8("EnclosingMethod")).putInt(4);
      out.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
    }
    if ((this.access & 0x20000) != 0) {
      out.putShort(newUTF8("Deprecated")).putInt(0);
    }
    if (((this.access & 0x1000) != 0) && ((this.version & 0xFFFF) < 49)) {
      out.putShort(newUTF8("Synthetic")).putInt(0);
    }
    if (this.innerClasses != null)
    {
      out.putShort(newUTF8("InnerClasses"));
      out.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
      out.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
    }
    if (this.anns != null)
    {
      out.putShort(newUTF8("RuntimeVisibleAnnotations"));
      this.anns.put(out);
    }
    if (this.ianns != null)
    {
      out.putShort(newUTF8("RuntimeInvisibleAnnotations"));
      this.ianns.put(out);
    }
    if (this.attrs != null) {
      this.attrs.put(this, null, 0, -1, -1, out);
    }
    if (this.invalidFrames)
    {
      ClassWriter cw = new ClassWriter(2);
      new ClassReader(out.data).accept(cw, 4);
      return cw.toByteArray();
    }
    return out.data;
  }
  
  Item newConstItem(Object cst)
  {
    if ((cst instanceof Integer))
    {
      int val = ((Integer)cst).intValue();
      return newInteger(val);
    }
    if ((cst instanceof Byte))
    {
      int val = ((Byte)cst).intValue();
      return newInteger(val);
    }
    if ((cst instanceof Character))
    {
      int val = ((Character)cst).charValue();
      return newInteger(val);
    }
    if ((cst instanceof Short))
    {
      int val = ((Short)cst).intValue();
      return newInteger(val);
    }
    if ((cst instanceof Boolean))
    {
      int val = ((Boolean)cst).booleanValue() ? 1 : 0;
      return newInteger(val);
    }
    if ((cst instanceof Float))
    {
      float val = ((Float)cst).floatValue();
      return newFloat(val);
    }
    if ((cst instanceof Long))
    {
      long val = ((Long)cst).longValue();
      return newLong(val);
    }
    if ((cst instanceof Double))
    {
      double val = ((Double)cst).doubleValue();
      return newDouble(val);
    }
    if ((cst instanceof String)) {
      return newString((String)cst);
    }
    if ((cst instanceof Type))
    {
      Type t = (Type)cst;
      return newClassItem(t.getSort() == 10 ? t.getInternalName() : t.getDescriptor());
    }
    throw new IllegalArgumentException("value " + cst);
  }
  
  public int newConst(Object cst)
  {
    return newConstItem(cst).index;
  }
  
  public int newUTF8(String value)
  {
    this.key.set(1, value, null, null);
    Item result = get(this.key);
    if (result == null)
    {
      this.pool.putByte(1).putUTF8(value);
      result = new Item(this.index++, this.key);
      put(result);
    }
    return result.index;
  }
  
  Item newClassItem(String value)
  {
    this.key2.set(7, value, null, null);
    Item result = get(this.key2);
    if (result == null)
    {
      this.pool.put12(7, newUTF8(value));
      result = new Item(this.index++, this.key2);
      put(result);
    }
    return result;
  }
  
  public int newClass(String value)
  {
    return newClassItem(value).index;
  }
  
  Item newFieldItem(String owner, String name, String desc)
  {
    this.key3.set(9, owner, name, desc);
    Item result = get(this.key3);
    if (result == null)
    {
      put122(9, newClass(owner), newNameType(name, desc));
      result = new Item(this.index++, this.key3);
      put(result);
    }
    return result;
  }
  
  public int newField(String owner, String name, String desc)
  {
    return newFieldItem(owner, name, desc).index;
  }
  
  Item newMethodItem(String owner, String name, String desc, boolean itf)
  {
    int type = itf ? 11 : 10;
    this.key3.set(type, owner, name, desc);
    Item result = get(this.key3);
    if (result == null)
    {
      put122(type, newClass(owner), newNameType(name, desc));
      result = new Item(this.index++, this.key3);
      put(result);
    }
    return result;
  }
  
  public int newMethod(String owner, String name, String desc, boolean itf)
  {
    return newMethodItem(owner, name, desc, itf).index;
  }
  
  Item newInteger(int value)
  {
    this.key.set(value);
    Item result = get(this.key);
    if (result == null)
    {
      this.pool.putByte(3).putInt(value);
      result = new Item(this.index++, this.key);
      put(result);
    }
    return result;
  }
  
  Item newFloat(float value)
  {
    this.key.set(value);
    Item result = get(this.key);
    if (result == null)
    {
      this.pool.putByte(4).putInt(this.key.intVal);
      result = new Item(this.index++, this.key);
      put(result);
    }
    return result;
  }
  
  Item newLong(long value)
  {
    this.key.set(value);
    Item result = get(this.key);
    if (result == null)
    {
      this.pool.putByte(5).putLong(value);
      result = new Item(this.index, this.key);
      put(result);
      this.index += 2;
    }
    return result;
  }
  
  Item newDouble(double value)
  {
    this.key.set(value);
    Item result = get(this.key);
    if (result == null)
    {
      this.pool.putByte(6).putLong(this.key.longVal);
      result = new Item(this.index, this.key);
      put(result);
      this.index += 2;
    }
    return result;
  }
  
  private Item newString(String value)
  {
    this.key2.set(8, value, null, null);
    Item result = get(this.key2);
    if (result == null)
    {
      this.pool.put12(8, newUTF8(value));
      result = new Item(this.index++, this.key2);
      put(result);
    }
    return result;
  }
  
  public int newNameType(String name, String desc)
  {
    this.key2.set(12, name, desc, null);
    Item result = get(this.key2);
    if (result == null)
    {
      put122(12, newUTF8(name), newUTF8(desc));
      result = new Item(this.index++, this.key2);
      put(result);
    }
    return result.index;
  }
  
  int addType(String type)
  {
    this.key.set(13, type, null, null);
    Item result = get(this.key);
    if (result == null) {
      result = addType(this.key);
    }
    return result.index;
  }
  
  int addUninitializedType(String type, int offset)
  {
    this.key.type = 14;
    this.key.intVal = offset;
    this.key.strVal1 = type;
    this.key.hashCode = (0x7FFFFFFF & 14 + type.hashCode() + offset);
    Item result = get(this.key);
    if (result == null) {
      result = addType(this.key);
    }
    return result.index;
  }
  
  private Item addType(Item item)
  {
    this.typeCount = ((short)(this.typeCount + 1));
    Item result = new Item(this.typeCount, this.key);
    put(result);
    if (this.typeTable == null) {
      this.typeTable = new Item[16];
    }
    if (this.typeCount == this.typeTable.length)
    {
      Item[] newTable = new Item[2 * this.typeTable.length];
      System.arraycopy(this.typeTable, 0, newTable, 0, this.typeTable.length);
      this.typeTable = newTable;
    }
    this.typeTable[this.typeCount] = result;
    return result;
  }
  
  int getMergedType(int type1, int type2)
  {
    this.key2.type = 15;
    this.key2.longVal = (type1 | type2 << 32);
    this.key2.hashCode = (0x7FFFFFFF & 15 + type1 + type2);
    Item result = get(this.key2);
    if (result == null)
    {
      String t = this.typeTable[type1].strVal1;
      String u = this.typeTable[type2].strVal1;
      this.key2.intVal = addType(getCommonSuperClass(t, u));
      result = new Item(0, this.key2);
      put(result);
    }
    return result.intVal;
  }
  
  protected Class<?> classForName(String name)
    throws ClassNotFoundException
  {
    try
    {
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      if (contextClassLoader != null) {
        return Class.forName(name, false, contextClassLoader);
      }
    }
    catch (Throwable e) {}
    return Class.forName(name, false, getClass().getClassLoader());
  }
  
  protected String getCommonSuperClass(String type1, String type2)
  {
    Class c;
    Class d;
    try
    {
      c = classForName(type1.replace('/', '.'));
      d = classForName(type2.replace('/', '.'));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.toString());
    }
    if (c.isAssignableFrom(d)) {
      return type1;
    }
    if (d.isAssignableFrom(c)) {
      return type2;
    }
    if ((c.isInterface()) || (d.isInterface())) {
      return "java/lang/Object";
    }
    do
    {
      c = c.getSuperclass();
    } while (!c.isAssignableFrom(d));
    return c.getName().replace('.', '/');
  }
  
  private Item get(Item key)
  {
    Item i = this.items[(key.hashCode % this.items.length)];
    while ((i != null) && (!key.isEqualTo(i))) {
      i = i.next;
    }
    return i;
  }
  
  private void put(Item i)
  {
    if (this.index > this.threshold)
    {
      int ll = this.items.length;
      int nl = ll * 2 + 1;
      Item[] newItems = new Item[nl];
      for (int l = ll - 1; l >= 0; l--)
      {
        Item j = this.items[l];
        while (j != null)
        {
          int index = j.hashCode % newItems.length;
          Item k = j.next;
          j.next = newItems[index];
          newItems[index] = j;
          j = k;
        }
      }
      this.items = newItems;
      this.threshold = ((int)(nl * 0.75D));
    }
    int index = i.hashCode % this.items.length;
    i.next = this.items[index];
    this.items[index] = i;
  }
  
  private void put122(int b, int s1, int s2)
  {
    this.pool.put12(b, s1).putShort(s2);
  }
  
  public void visitEnd() {}
}
