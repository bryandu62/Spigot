package com.avaje.ebean.enhance.asm;

import java.io.IOException;
import java.io.InputStream;

public class ClassReader
{
  static final boolean SIGNATURES = true;
  static final boolean ANNOTATIONS = true;
  static final boolean FRAMES = true;
  static final boolean WRITER = true;
  static final boolean RESIZE = true;
  public static final int SKIP_CODE = 1;
  public static final int SKIP_DEBUG = 2;
  public static final int SKIP_FRAMES = 4;
  public static final int EXPAND_FRAMES = 8;
  public final byte[] b;
  private final int[] items;
  private final String[] strings;
  private final int maxStringLength;
  public final int header;
  
  public ClassReader(byte[] b)
  {
    this(b, 0, b.length);
  }
  
  public ClassReader(byte[] b, int off, int len)
  {
    this.b = b;
    
    this.items = new int[readUnsignedShort(off + 8)];
    int n = this.items.length;
    this.strings = new String[n];
    int max = 0;
    int index = off + 10;
    for (int i = 1; i < n; i++)
    {
      this.items[i] = (index + 1);
      int size;
      switch (b[index])
      {
      case 3: 
      case 4: 
      case 9: 
      case 10: 
      case 11: 
      case 12: 
        size = 5;
        break;
      case 5: 
      case 6: 
        size = 9;
        i++;
        break;
      case 1: 
        size = 3 + readUnsignedShort(index + 1);
        if (size > max) {
          max = size;
        }
        break;
      case 2: 
      case 7: 
      case 8: 
      default: 
        size = 3;
      }
      index += size;
    }
    this.maxStringLength = max;
    
    this.header = index;
  }
  
  public int getAccess()
  {
    return readUnsignedShort(this.header);
  }
  
  public String getClassName()
  {
    return readClass(this.header + 2, new char[this.maxStringLength]);
  }
  
  public String getSuperName()
  {
    int n = this.items[readUnsignedShort(this.header + 4)];
    return n == 0 ? null : readUTF8(n, new char[this.maxStringLength]);
  }
  
  public String[] getInterfaces()
  {
    int index = this.header + 6;
    int n = readUnsignedShort(index);
    String[] interfaces = new String[n];
    if (n > 0)
    {
      char[] buf = new char[this.maxStringLength];
      for (int i = 0; i < n; i++)
      {
        index += 2;
        interfaces[i] = readClass(index, buf);
      }
    }
    return interfaces;
  }
  
  void copyPool(ClassWriter classWriter)
  {
    char[] buf = new char[this.maxStringLength];
    int ll = this.items.length;
    Item[] items2 = new Item[ll];
    for (int i = 1; i < ll; i++)
    {
      int index = this.items[i];
      int tag = this.b[(index - 1)];
      Item item = new Item(i);
      switch (tag)
      {
      case 9: 
      case 10: 
      case 11: 
        int nameType = this.items[readUnsignedShort(index + 2)];
        item.set(tag, readClass(index, buf), readUTF8(nameType, buf), readUTF8(nameType + 2, buf));
        
        break;
      case 3: 
        item.set(readInt(index));
        break;
      case 4: 
        item.set(Float.intBitsToFloat(readInt(index)));
        break;
      case 12: 
        item.set(tag, readUTF8(index, buf), readUTF8(index + 2, buf), null);
        
        break;
      case 5: 
        item.set(readLong(index));
        i++;
        break;
      case 6: 
        item.set(Double.longBitsToDouble(readLong(index)));
        i++;
        break;
      case 1: 
        String s = this.strings[i];
        if (s == null)
        {
          index = this.items[i];
          s = this.strings[i] = readUTF(index + 2, readUnsignedShort(index), buf);
        }
        item.set(tag, s, null, null);
        
        break;
      case 2: 
      case 7: 
      case 8: 
      default: 
        item.set(tag, readUTF8(index, buf), null, null);
      }
      int index2 = item.hashCode % items2.length;
      item.next = items2[index2];
      items2[index2] = item;
    }
    int off = this.items[1] - 1;
    classWriter.pool.putByteArray(this.b, off, this.header - off);
    classWriter.items = items2;
    classWriter.threshold = ((int)(0.75D * ll));
    classWriter.index = ll;
  }
  
  public ClassReader(InputStream is)
    throws IOException
  {
    this(readClass(is));
  }
  
  public ClassReader(String name)
    throws IOException
  {
    this(ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"));
  }
  
  private static byte[] readClass(InputStream is)
    throws IOException
  {
    if (is == null) {
      throw new IOException("Class not found");
    }
    byte[] b = new byte[is.available()];
    int len = 0;
    for (;;)
    {
      int n = is.read(b, len, b.length - len);
      if (n == -1)
      {
        if (len < b.length)
        {
          byte[] c = new byte[len];
          System.arraycopy(b, 0, c, 0, len);
          b = c;
        }
        return b;
      }
      len += n;
      if (len == b.length)
      {
        byte[] c = new byte[b.length + 1000];
        System.arraycopy(b, 0, c, 0, len);
        b = c;
      }
    }
  }
  
  public void accept(ClassVisitor classVisitor, int flags)
  {
    accept(classVisitor, new Attribute[0], flags);
  }
  
  public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags)
  {
    byte[] b = this.b;
    char[] c = new char[this.maxStringLength];
    
    int anns = 0;
    int ianns = 0;
    Attribute cattrs = null;
    
    int u = this.header;
    int access = readUnsignedShort(u);
    String name = readClass(u + 2, c);
    int v = this.items[readUnsignedShort(u + 4)];
    String superClassName = v == 0 ? null : readUTF8(v, c);
    String[] implementedItfs = new String[readUnsignedShort(u + 6)];
    int w = 0;
    u += 8;
    for (int i = 0; i < implementedItfs.length; i++)
    {
      implementedItfs[i] = readClass(u, c);
      u += 2;
    }
    boolean skipCode = (flags & 0x1) != 0;
    boolean skipDebug = (flags & 0x2) != 0;
    boolean unzip = (flags & 0x8) != 0;
    
    v = u;
    i = readUnsignedShort(v);
    v += 2;
    for (; i > 0; i--)
    {
      int j = readUnsignedShort(v + 6);
      v += 8;
      for (; j > 0; j--) {
        v += 6 + readInt(v + 2);
      }
    }
    i = readUnsignedShort(v);
    v += 2;
    for (; i > 0; i--)
    {
      int j = readUnsignedShort(v + 6);
      v += 8;
      for (; j > 0; j--) {
        v += 6 + readInt(v + 2);
      }
    }
    String signature = null;
    String sourceFile = null;
    String sourceDebug = null;
    String enclosingOwner = null;
    String enclosingName = null;
    String enclosingDesc = null;
    
    i = readUnsignedShort(v);
    v += 2;
    for (; i > 0; i--)
    {
      String attrName = readUTF8(v, c);
      if ("SourceFile".equals(attrName))
      {
        sourceFile = readUTF8(v + 6, c);
      }
      else if ("InnerClasses".equals(attrName))
      {
        w = v + 6;
      }
      else if ("EnclosingMethod".equals(attrName))
      {
        enclosingOwner = readClass(v + 6, c);
        int item = readUnsignedShort(v + 8);
        if (item != 0)
        {
          enclosingName = readUTF8(this.items[item], c);
          enclosingDesc = readUTF8(this.items[item] + 2, c);
        }
      }
      else if ("Signature".equals(attrName))
      {
        signature = readUTF8(v + 6, c);
      }
      else if ("RuntimeVisibleAnnotations".equals(attrName))
      {
        anns = v + 6;
      }
      else if ("Deprecated".equals(attrName))
      {
        access |= 0x20000;
      }
      else if ("Synthetic".equals(attrName))
      {
        access |= 0x1000;
      }
      else if ("SourceDebugExtension".equals(attrName))
      {
        int len = readInt(v + 2);
        sourceDebug = readUTF(v + 6, len, new char[len]);
      }
      else if ("RuntimeInvisibleAnnotations".equals(attrName))
      {
        ianns = v + 6;
      }
      else
      {
        Attribute attr = readAttribute(attrs, attrName, v + 6, readInt(v + 2), c, -1, null);
        if (attr != null)
        {
          attr.next = cattrs;
          cattrs = attr;
        }
      }
      v += 6 + readInt(v + 2);
    }
    classVisitor.visit(readInt(4), access, name, signature, superClassName, implementedItfs);
    if ((!skipDebug) && ((sourceFile != null) || (sourceDebug != null))) {
      classVisitor.visitSource(sourceFile, sourceDebug);
    }
    if (enclosingOwner != null) {
      classVisitor.visitOuterClass(enclosingOwner, enclosingName, enclosingDesc);
    }
    for (i = 1; i >= 0; i--)
    {
      v = i == 0 ? ianns : anns;
      if (v != 0)
      {
        int j = readUnsignedShort(v);
        v += 2;
        for (; j > 0; j--) {
          v = readAnnotationValues(v + 2, c, true, classVisitor.visitAnnotation(readUTF8(v, c), i != 0));
        }
      }
    }
    while (cattrs != null)
    {
      Attribute attr = cattrs.next;
      cattrs.next = null;
      classVisitor.visitAttribute(cattrs);
      cattrs = attr;
    }
    if (w != 0)
    {
      i = readUnsignedShort(w);
      w += 2;
      for (; i > 0; i--)
      {
        classVisitor.visitInnerClass(readUnsignedShort(w) == 0 ? null : readClass(w, c), readUnsignedShort(w + 2) == 0 ? null : readClass(w + 2, c), readUnsignedShort(w + 4) == 0 ? null : readUTF8(w + 4, c), readUnsignedShort(w + 6));
        
        w += 8;
      }
    }
    i = readUnsignedShort(u);
    u += 2;
    for (; i > 0; i--)
    {
      access = readUnsignedShort(u);
      name = readUTF8(u + 2, c);
      String desc = readUTF8(u + 4, c);
      
      int fieldValueItem = 0;
      signature = null;
      anns = 0;
      ianns = 0;
      cattrs = null;
      
      int j = readUnsignedShort(u + 6);
      u += 8;
      for (; j > 0; j--)
      {
        String attrName = readUTF8(u, c);
        if ("ConstantValue".equals(attrName))
        {
          fieldValueItem = readUnsignedShort(u + 6);
        }
        else if ("Signature".equals(attrName))
        {
          signature = readUTF8(u + 6, c);
        }
        else if ("Deprecated".equals(attrName))
        {
          access |= 0x20000;
        }
        else if ("Synthetic".equals(attrName))
        {
          access |= 0x1000;
        }
        else if ("RuntimeVisibleAnnotations".equals(attrName))
        {
          anns = u + 6;
        }
        else if ("RuntimeInvisibleAnnotations".equals(attrName))
        {
          ianns = u + 6;
        }
        else
        {
          Attribute attr = readAttribute(attrs, attrName, u + 6, readInt(u + 2), c, -1, null);
          if (attr != null)
          {
            attr.next = cattrs;
            cattrs = attr;
          }
        }
        u += 6 + readInt(u + 2);
      }
      FieldVisitor fv = classVisitor.visitField(access, name, desc, signature, fieldValueItem == 0 ? null : readConst(fieldValueItem, c));
      if (fv != null)
      {
        for (j = 1; j >= 0; j--)
        {
          v = j == 0 ? ianns : anns;
          if (v != 0)
          {
            int k = readUnsignedShort(v);
            v += 2;
            for (; k > 0; k--) {
              v = readAnnotationValues(v + 2, c, true, fv.visitAnnotation(readUTF8(v, c), j != 0));
            }
          }
        }
        while (cattrs != null)
        {
          Attribute attr = cattrs.next;
          cattrs.next = null;
          fv.visitAttribute(cattrs);
          cattrs = attr;
        }
        fv.visitEnd();
      }
    }
    i = readUnsignedShort(u);
    u += 2;
    for (; i > 0; i--)
    {
      int u0 = u + 6;
      access = readUnsignedShort(u);
      name = readUTF8(u + 2, c);
      String desc = readUTF8(u + 4, c);
      signature = null;
      anns = 0;
      ianns = 0;
      int dann = 0;
      int mpanns = 0;
      int impanns = 0;
      cattrs = null;
      v = 0;
      w = 0;
      
      int j = readUnsignedShort(u + 6);
      u += 8;
      for (; j > 0; j--)
      {
        String attrName = readUTF8(u, c);
        int attrSize = readInt(u + 2);
        u += 6;
        if ("Code".equals(attrName))
        {
          if (!skipCode) {
            v = u;
          }
        }
        else if ("Exceptions".equals(attrName))
        {
          w = u;
        }
        else if ("Signature".equals(attrName))
        {
          signature = readUTF8(u, c);
        }
        else if ("Deprecated".equals(attrName))
        {
          access |= 0x20000;
        }
        else if ("RuntimeVisibleAnnotations".equals(attrName))
        {
          anns = u;
        }
        else if ("AnnotationDefault".equals(attrName))
        {
          dann = u;
        }
        else if ("Synthetic".equals(attrName))
        {
          access |= 0x1000;
        }
        else if ("RuntimeInvisibleAnnotations".equals(attrName))
        {
          ianns = u;
        }
        else if ("RuntimeVisibleParameterAnnotations".equals(attrName))
        {
          mpanns = u;
        }
        else if ("RuntimeInvisibleParameterAnnotations".equals(attrName))
        {
          impanns = u;
        }
        else
        {
          Attribute attr = readAttribute(attrs, attrName, u, attrSize, c, -1, null);
          if (attr != null)
          {
            attr.next = cattrs;
            cattrs = attr;
          }
        }
        u += attrSize;
      }
      String[] exceptions;
      String[] exceptions;
      if (w == 0)
      {
        exceptions = null;
      }
      else
      {
        exceptions = new String[readUnsignedShort(w)];
        w += 2;
        for (j = 0; j < exceptions.length; j++)
        {
          exceptions[j] = readClass(w, c);
          w += 2;
        }
      }
      MethodVisitor mv = classVisitor.visitMethod(access, name, desc, signature, exceptions);
      if (mv != null)
      {
        if ((mv instanceof MethodWriter))
        {
          MethodWriter mw = (MethodWriter)mv;
          if ((mw.cw.cr == this) && 
            (signature == mw.signature))
          {
            boolean sameExceptions = false;
            if (exceptions == null)
            {
              sameExceptions = mw.exceptionCount == 0;
            }
            else if (exceptions.length == mw.exceptionCount)
            {
              sameExceptions = true;
              for (j = exceptions.length - 1; j >= 0; j--)
              {
                w -= 2;
                if (mw.exceptions[j] != readUnsignedShort(w))
                {
                  sameExceptions = false;
                  break;
                }
              }
            }
            if (sameExceptions)
            {
              mw.classReaderOffset = u0;
              mw.classReaderLength = (u - u0);
              continue;
            }
          }
        }
        if (dann != 0)
        {
          AnnotationVisitor dv = mv.visitAnnotationDefault();
          readAnnotationValue(dann, c, null, dv);
          if (dv != null) {
            dv.visitEnd();
          }
        }
        for (j = 1; j >= 0; j--)
        {
          w = j == 0 ? ianns : anns;
          if (w != 0)
          {
            int k = readUnsignedShort(w);
            w += 2;
            for (; k > 0; k--) {
              w = readAnnotationValues(w + 2, c, true, mv.visitAnnotation(readUTF8(w, c), j != 0));
            }
          }
        }
        if (mpanns != 0) {
          readParameterAnnotations(mpanns, desc, c, true, mv);
        }
        if (impanns != 0) {
          readParameterAnnotations(impanns, desc, c, false, mv);
        }
        while (cattrs != null)
        {
          Attribute attr = cattrs.next;
          cattrs.next = null;
          mv.visitAttribute(cattrs);
          cattrs = attr;
        }
      }
      else
      {
        if ((mv != null) && (v != 0))
        {
          int maxStack = readUnsignedShort(v);
          int maxLocals = readUnsignedShort(v + 2);
          int codeLength = readInt(v + 4);
          v += 8;
          
          int codeStart = v;
          int codeEnd = v + codeLength;
          
          mv.visitCode();
          
          Label[] labels = new Label[codeLength + 2];
          readLabel(codeLength + 1, labels);
          while (v < codeEnd)
          {
            w = v - codeStart;
            int opcode = b[v] & 0xFF;
            switch (ClassWriter.TYPE[opcode])
            {
            case 0: 
            case 4: 
              v++;
              break;
            case 8: 
              readLabel(w + readShort(v + 1), labels);
              v += 3;
              break;
            case 9: 
              readLabel(w + readInt(v + 1), labels);
              v += 5;
              break;
            case 16: 
              opcode = b[(v + 1)] & 0xFF;
              if (opcode == 132) {
                v += 6;
              } else {
                v += 4;
              }
              break;
            case 13: 
              v = v + 4 - (w & 0x3);
              
              readLabel(w + readInt(v), labels);
              j = readInt(v + 8) - readInt(v + 4) + 1;
              v += 12;
            case 14: 
            case 1: 
            case 3: 
            case 10: 
            case 2: 
            case 5: 
            case 6: 
            case 11: 
            case 12: 
            case 7: 
            case 15: 
            default: 
              while (j > 0)
              {
                readLabel(w + readInt(v), labels);
                v += 4;j--; continue;
                
                v = v + 4 - (w & 0x3);
                
                readLabel(w + readInt(v), labels);
                j = readInt(v + 4);
                v += 8;
                while (j > 0)
                {
                  readLabel(w + readInt(v + 4), labels);
                  v += 8;j--; continue;
                  
                  v += 2;
                  break;
                  
                  v += 3;
                  break;
                  
                  v += 5;
                  break;
                  
                  v += 4;
                }
              }
            }
          }
          j = readUnsignedShort(v);
          v += 2;
          for (; j > 0; j--)
          {
            Label start = readLabel(readUnsignedShort(v), labels);
            Label end = readLabel(readUnsignedShort(v + 2), labels);
            Label handler = readLabel(readUnsignedShort(v + 4), labels);
            int type = readUnsignedShort(v + 6);
            if (type == 0) {
              mv.visitTryCatchBlock(start, end, handler, null);
            } else {
              mv.visitTryCatchBlock(start, end, handler, readUTF8(this.items[type], c));
            }
            v += 8;
          }
          int varTable = 0;
          int varTypeTable = 0;
          int stackMap = 0;
          int frameCount = 0;
          int frameMode = 0;
          int frameOffset = 0;
          int frameLocalCount = 0;
          int frameLocalDiff = 0;
          int frameStackCount = 0;
          Object[] frameLocal = null;
          Object[] frameStack = null;
          boolean zip = true;
          cattrs = null;
          j = readUnsignedShort(v);
          v += 2;
          for (; j > 0; j--)
          {
            String attrName = readUTF8(v, c);
            if ("LocalVariableTable".equals(attrName))
            {
              if (!skipDebug)
              {
                varTable = v + 6;
                int k = readUnsignedShort(v + 6);
                w = v + 8;
                for (; k > 0; k--)
                {
                  int label = readUnsignedShort(w);
                  if (labels[label] == null) {
                    readLabel(label, labels).status |= 0x1;
                  }
                  label += readUnsignedShort(w + 2);
                  if (labels[label] == null) {
                    readLabel(label, labels).status |= 0x1;
                  }
                  w += 10;
                }
              }
            }
            else if ("LocalVariableTypeTable".equals(attrName)) {
              varTypeTable = v + 6;
            } else if ("LineNumberTable".equals(attrName))
            {
              if (!skipDebug)
              {
                int k = readUnsignedShort(v + 6);
                w = v + 8;
                for (; k > 0; k--)
                {
                  int label = readUnsignedShort(w);
                  if (labels[label] == null) {
                    readLabel(label, labels).status |= 0x1;
                  }
                  labels[label].line = readUnsignedShort(w + 2);
                  w += 4;
                }
              }
            }
            else if ("StackMapTable".equals(attrName))
            {
              if ((flags & 0x4) == 0)
              {
                stackMap = v + 8;
                frameCount = readUnsignedShort(v + 6);
              }
            }
            else if ("StackMap".equals(attrName))
            {
              if ((flags & 0x4) == 0)
              {
                stackMap = v + 8;
                frameCount = readUnsignedShort(v + 6);
                zip = false;
              }
            }
            else {
              for (int k = 0; k < attrs.length; k++) {
                if (attrs[k].type.equals(attrName))
                {
                  Attribute attr = attrs[k].read(this, v + 6, readInt(v + 2), c, codeStart - 8, labels);
                  if (attr != null)
                  {
                    attr.next = cattrs;
                    cattrs = attr;
                  }
                }
              }
            }
            v += 6 + readInt(v + 2);
          }
          if (stackMap != 0)
          {
            frameLocal = new Object[maxLocals];
            frameStack = new Object[maxStack];
            if (unzip)
            {
              int local = 0;
              if ((access & 0x8) == 0) {
                if ("<init>".equals(name)) {
                  frameLocal[(local++)] = Opcodes.UNINITIALIZED_THIS;
                } else {
                  frameLocal[(local++)] = readClass(this.header + 2, c);
                }
              }
              j = 1;
              for (;;)
              {
                int k = j;
                switch (desc.charAt(j++))
                {
                case 'B': 
                case 'C': 
                case 'I': 
                case 'S': 
                case 'Z': 
                  frameLocal[(local++)] = Opcodes.INTEGER;
                  break;
                case 'F': 
                  frameLocal[(local++)] = Opcodes.FLOAT;
                  break;
                case 'J': 
                  frameLocal[(local++)] = Opcodes.LONG;
                  break;
                case 'D': 
                  frameLocal[(local++)] = Opcodes.DOUBLE;
                  break;
                case '[': 
                  while (desc.charAt(j) == '[') {
                    j++;
                  }
                  if (desc.charAt(j) == 'L')
                  {
                    j++;
                    while (desc.charAt(j) != ';') {
                      j++;
                    }
                  }
                  frameLocal[(local++)] = desc.substring(k, ++j);
                  break;
                case 'L': 
                  while (desc.charAt(j) != ';') {
                    j++;
                  }
                  frameLocal[(local++)] = desc.substring(k + 1, j++);
                }
              }
              frameLocalCount = local;
            }
            frameOffset = -1;
          }
          v = codeStart;
          while (v < codeEnd)
          {
            w = v - codeStart;
            
            Label l = labels[w];
            if (l != null)
            {
              mv.visitLabel(l);
              if ((!skipDebug) && (l.line > 0)) {
                mv.visitLineNumber(l.line, l);
              }
            }
            while ((frameLocal != null) && ((frameOffset == w) || (frameOffset == -1)))
            {
              if ((!zip) || (unzip)) {
                mv.visitFrame(-1, frameLocalCount, frameLocal, frameStackCount, frameStack);
              } else if (frameOffset != -1) {
                mv.visitFrame(frameMode, frameLocalDiff, frameLocal, frameStackCount, frameStack);
              }
              if (frameCount > 0)
              {
                int tag;
                int tag;
                if (zip)
                {
                  tag = b[(stackMap++)] & 0xFF;
                }
                else
                {
                  tag = 255;
                  frameOffset = -1;
                }
                frameLocalDiff = 0;
                int delta;
                if (tag < 64)
                {
                  int delta = tag;
                  frameMode = 3;
                  frameStackCount = 0;
                }
                else if (tag < 128)
                {
                  int delta = tag - 64;
                  
                  stackMap = readFrameType(frameStack, 0, stackMap, c, labels);
                  
                  frameMode = 4;
                  frameStackCount = 1;
                }
                else
                {
                  delta = readUnsignedShort(stackMap);
                  stackMap += 2;
                  if (tag == 247)
                  {
                    stackMap = readFrameType(frameStack, 0, stackMap, c, labels);
                    
                    frameMode = 4;
                    frameStackCount = 1;
                  }
                  else if ((tag >= 248) && (tag < 251))
                  {
                    frameMode = 2;
                    frameLocalDiff = 251 - tag;
                    
                    frameLocalCount -= frameLocalDiff;
                    frameStackCount = 0;
                  }
                  else if (tag == 251)
                  {
                    frameMode = 3;
                    frameStackCount = 0;
                  }
                  else if (tag < 255)
                  {
                    j = unzip ? frameLocalCount : 0;
                    for (int k = tag - 251; k > 0; k--) {
                      stackMap = readFrameType(frameLocal, j++, stackMap, c, labels);
                    }
                    frameMode = 1;
                    frameLocalDiff = tag - 251;
                    
                    frameLocalCount += frameLocalDiff;
                    frameStackCount = 0;
                  }
                  else
                  {
                    frameMode = 0;
                    int n = frameLocalDiff = frameLocalCount = readUnsignedShort(stackMap);
                    stackMap += 2;
                    for (j = 0; n > 0; n--) {
                      stackMap = readFrameType(frameLocal, j++, stackMap, c, labels);
                    }
                    n = frameStackCount = readUnsignedShort(stackMap);
                    stackMap += 2;
                    for (j = 0; n > 0; n--) {
                      stackMap = readFrameType(frameStack, j++, stackMap, c, labels);
                    }
                  }
                }
                frameOffset += delta + 1;
                readLabel(frameOffset, labels);
                
                frameCount--;
              }
              else
              {
                frameLocal = null;
              }
            }
            int opcode = b[v] & 0xFF;
            int label;
            switch (ClassWriter.TYPE[opcode])
            {
            case 0: 
              mv.visitInsn(opcode);
              v++;
              break;
            case 4: 
              if (opcode > 54)
              {
                opcode -= 59;
                mv.visitVarInsn(54 + (opcode >> 2), opcode & 0x3);
              }
              else
              {
                opcode -= 26;
                mv.visitVarInsn(21 + (opcode >> 2), opcode & 0x3);
              }
              v++;
              break;
            case 8: 
              mv.visitJumpInsn(opcode, labels[(w + readShort(v + 1))]);
              
              v += 3;
              break;
            case 9: 
              mv.visitJumpInsn(opcode - 33, labels[(w + readInt(v + 1))]);
              
              v += 5;
              break;
            case 16: 
              opcode = b[(v + 1)] & 0xFF;
              if (opcode == 132)
              {
                mv.visitIincInsn(readUnsignedShort(v + 2), readShort(v + 4));
                
                v += 6;
              }
              else
              {
                mv.visitVarInsn(opcode, readUnsignedShort(v + 2));
                
                v += 4;
              }
              break;
            case 13: 
              v = v + 4 - (w & 0x3);
              
              label = w + readInt(v);
              int min = readInt(v + 4);
              int max = readInt(v + 8);
              v += 12;
              Label[] table = new Label[max - min + 1];
              for (j = 0; j < table.length; j++)
              {
                table[j] = labels[(w + readInt(v))];
                v += 4;
              }
              mv.visitTableSwitchInsn(min, max, labels[label], table);
              
              break;
            case 14: 
              v = v + 4 - (w & 0x3);
              
              label = w + readInt(v);
              j = readInt(v + 4);
              v += 8;
              int[] keys = new int[j];
              Label[] values = new Label[j];
              for (j = 0; j < keys.length; j++)
              {
                keys[j] = readInt(v);
                values[j] = labels[(w + readInt(v + 4))];
                v += 8;
              }
              mv.visitLookupSwitchInsn(labels[label], keys, values);
              
              break;
            case 3: 
              mv.visitVarInsn(opcode, b[(v + 1)] & 0xFF);
              v += 2;
              break;
            case 1: 
              mv.visitIntInsn(opcode, b[(v + 1)]);
              v += 2;
              break;
            case 2: 
              mv.visitIntInsn(opcode, readShort(v + 1));
              v += 3;
              break;
            case 10: 
              mv.visitLdcInsn(readConst(b[(v + 1)] & 0xFF, c));
              v += 2;
              break;
            case 11: 
              mv.visitLdcInsn(readConst(readUnsignedShort(v + 1), c));
              
              v += 3;
              break;
            case 6: 
            case 7: 
              int cpIndex = this.items[readUnsignedShort(v + 1)];
              String iowner = readClass(cpIndex, c);
              cpIndex = this.items[readUnsignedShort(cpIndex + 2)];
              String iname = readUTF8(cpIndex, c);
              String idesc = readUTF8(cpIndex + 2, c);
              if (opcode < 182) {
                mv.visitFieldInsn(opcode, iowner, iname, idesc);
              } else {
                mv.visitMethodInsn(opcode, iowner, iname, idesc);
              }
              if (opcode == 185) {
                v += 5;
              } else {
                v += 3;
              }
              break;
            case 5: 
              mv.visitTypeInsn(opcode, readClass(v + 1, c));
              v += 3;
              break;
            case 12: 
              mv.visitIincInsn(b[(v + 1)] & 0xFF, b[(v + 2)]);
              v += 3;
              break;
            case 15: 
            default: 
              mv.visitMultiANewArrayInsn(readClass(v + 1, c), b[(v + 3)] & 0xFF);
              
              v += 4;
            }
          }
          Label l = labels[(codeEnd - codeStart)];
          if (l != null) {
            mv.visitLabel(l);
          }
          if ((!skipDebug) && (varTable != 0))
          {
            int[] typeTable = null;
            if (varTypeTable != 0)
            {
              int k = readUnsignedShort(varTypeTable) * 3;
              w = varTypeTable + 2;
              typeTable = new int[k];
              while (k > 0)
              {
                typeTable[(--k)] = (w + 6);
                typeTable[(--k)] = readUnsignedShort(w + 8);
                typeTable[(--k)] = readUnsignedShort(w);
                w += 10;
              }
            }
            int k = readUnsignedShort(varTable);
            w = varTable + 2;
            for (; k > 0; k--)
            {
              int start = readUnsignedShort(w);
              int length = readUnsignedShort(w + 2);
              int index = readUnsignedShort(w + 8);
              String vsignature = null;
              if (typeTable != null) {
                for (int a = 0; a < typeTable.length; a += 3) {
                  if ((typeTable[a] == start) && (typeTable[(a + 1)] == index))
                  {
                    vsignature = readUTF8(typeTable[(a + 2)], c);
                    break;
                  }
                }
              }
              mv.visitLocalVariable(readUTF8(w + 4, c), readUTF8(w + 6, c), vsignature, labels[start], labels[(start + length)], index);
              
              w += 10;
            }
          }
          while (cattrs != null)
          {
            Attribute attr = cattrs.next;
            cattrs.next = null;
            mv.visitAttribute(cattrs);
            cattrs = attr;
          }
          mv.visitMaxs(maxStack, maxLocals);
        }
        if (mv != null) {
          mv.visitEnd();
        }
      }
    }
    classVisitor.visitEnd();
  }
  
  private void readParameterAnnotations(int v, String desc, char[] buf, boolean visible, MethodVisitor mv)
  {
    int n = this.b[(v++)] & 0xFF;
    
    int synthetics = Type.getArgumentTypes(desc).length - n;
    for (int i = 0; i < synthetics; i++)
    {
      AnnotationVisitor av = mv.visitParameterAnnotation(i, "Ljava/lang/Synthetic;", false);
      if (av != null) {
        av.visitEnd();
      }
    }
    for (; i < n + synthetics; i++)
    {
      int j = readUnsignedShort(v);
      v += 2;
      for (; j > 0; j--)
      {
        AnnotationVisitor av = mv.visitParameterAnnotation(i, readUTF8(v, buf), visible);
        v = readAnnotationValues(v + 2, buf, true, av);
      }
    }
  }
  
  private int readAnnotationValues(int v, char[] buf, boolean named, AnnotationVisitor av)
  {
    int i = readUnsignedShort(v);
    v += 2;
    if (named) {
      for (; i > 0; i--) {
        v = readAnnotationValue(v + 2, buf, readUTF8(v, buf), av);
      }
    }
    for (; i > 0; i--) {
      v = readAnnotationValue(v, buf, null, av);
    }
    if (av != null) {
      av.visitEnd();
    }
    return v;
  }
  
  private int readAnnotationValue(int v, char[] buf, String name, AnnotationVisitor av)
  {
    if (av == null)
    {
      switch (this.b[v] & 0xFF)
      {
      case 101: 
        return v + 5;
      case 64: 
        return readAnnotationValues(v + 3, buf, true, null);
      case 91: 
        return readAnnotationValues(v + 1, buf, false, null);
      }
      return v + 3;
    }
    switch (this.b[(v++)] & 0xFF)
    {
    case 68: 
    case 70: 
    case 73: 
    case 74: 
      av.visit(name, readConst(readUnsignedShort(v), buf));
      v += 2;
      break;
    case 66: 
      av.visit(name, Byte.valueOf((byte)readInt(this.items[readUnsignedShort(v)])));
      
      v += 2;
      break;
    case 90: 
      av.visit(name, readInt(this.items[readUnsignedShort(v)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
      
      v += 2;
      break;
    case 83: 
      av.visit(name, Short.valueOf((short)readInt(this.items[readUnsignedShort(v)])));
      
      v += 2;
      break;
    case 67: 
      av.visit(name, Character.valueOf((char)readInt(this.items[readUnsignedShort(v)])));
      
      v += 2;
      break;
    case 115: 
      av.visit(name, readUTF8(v, buf));
      v += 2;
      break;
    case 101: 
      av.visitEnum(name, readUTF8(v, buf), readUTF8(v + 2, buf));
      v += 4;
      break;
    case 99: 
      av.visit(name, Type.getType(readUTF8(v, buf)));
      v += 2;
      break;
    case 64: 
      v = readAnnotationValues(v + 2, buf, true, av.visitAnnotation(name, readUTF8(v, buf)));
      
      break;
    case 91: 
      int size = readUnsignedShort(v);
      v += 2;
      if (size == 0) {
        return readAnnotationValues(v - 2, buf, false, av.visitArray(name));
      }
      int i;
      switch (this.b[(v++)] & 0xFF)
      {
      case 66: 
        byte[] bv = new byte[size];
        for (i = 0; i < size; i++)
        {
          bv[i] = ((byte)readInt(this.items[readUnsignedShort(v)]));
          v += 3;
        }
        av.visit(name, bv);
        v--;
        break;
      case 90: 
        boolean[] zv = new boolean[size];
        for (i = 0; i < size; i++)
        {
          zv[i] = (readInt(this.items[readUnsignedShort(v)]) != 0 ? 1 : false);
          v += 3;
        }
        av.visit(name, zv);
        v--;
        break;
      case 83: 
        short[] sv = new short[size];
        for (i = 0; i < size; i++)
        {
          sv[i] = ((short)readInt(this.items[readUnsignedShort(v)]));
          v += 3;
        }
        av.visit(name, sv);
        v--;
        break;
      case 67: 
        char[] cv = new char[size];
        for (i = 0; i < size; i++)
        {
          cv[i] = ((char)readInt(this.items[readUnsignedShort(v)]));
          v += 3;
        }
        av.visit(name, cv);
        v--;
        break;
      case 73: 
        int[] iv = new int[size];
        for (i = 0; i < size; i++)
        {
          iv[i] = readInt(this.items[readUnsignedShort(v)]);
          v += 3;
        }
        av.visit(name, iv);
        v--;
        break;
      case 74: 
        long[] lv = new long[size];
        for (i = 0; i < size; i++)
        {
          lv[i] = readLong(this.items[readUnsignedShort(v)]);
          v += 3;
        }
        av.visit(name, lv);
        v--;
        break;
      case 70: 
        float[] fv = new float[size];
        for (i = 0; i < size; i++)
        {
          fv[i] = Float.intBitsToFloat(readInt(this.items[readUnsignedShort(v)]));
          v += 3;
        }
        av.visit(name, fv);
        v--;
        break;
      case 68: 
        double[] dv = new double[size];
        for (i = 0; i < size; i++)
        {
          dv[i] = Double.longBitsToDouble(readLong(this.items[readUnsignedShort(v)]));
          v += 3;
        }
        av.visit(name, dv);
        v--;
        break;
      case 69: 
      case 71: 
      case 72: 
      case 75: 
      case 76: 
      case 77: 
      case 78: 
      case 79: 
      case 80: 
      case 81: 
      case 82: 
      case 84: 
      case 85: 
      case 86: 
      case 87: 
      case 88: 
      case 89: 
      default: 
        v = readAnnotationValues(v - 3, buf, false, av.visitArray(name));
      }
      break;
    }
    return v;
  }
  
  private int readFrameType(Object[] frame, int index, int v, char[] buf, Label[] labels)
  {
    int type = this.b[(v++)] & 0xFF;
    switch (type)
    {
    case 0: 
      frame[index] = Opcodes.TOP;
      break;
    case 1: 
      frame[index] = Opcodes.INTEGER;
      break;
    case 2: 
      frame[index] = Opcodes.FLOAT;
      break;
    case 3: 
      frame[index] = Opcodes.DOUBLE;
      break;
    case 4: 
      frame[index] = Opcodes.LONG;
      break;
    case 5: 
      frame[index] = Opcodes.NULL;
      break;
    case 6: 
      frame[index] = Opcodes.UNINITIALIZED_THIS;
      break;
    case 7: 
      frame[index] = readClass(v, buf);
      v += 2;
      break;
    default: 
      frame[index] = readLabel(readUnsignedShort(v), labels);
      v += 2;
    }
    return v;
  }
  
  protected Label readLabel(int offset, Label[] labels)
  {
    if (labels[offset] == null) {
      labels[offset] = new Label();
    }
    return labels[offset];
  }
  
  private Attribute readAttribute(Attribute[] attrs, String type, int off, int len, char[] buf, int codeOff, Label[] labels)
  {
    for (int i = 0; i < attrs.length; i++) {
      if (attrs[i].type.equals(type)) {
        return attrs[i].read(this, off, len, buf, codeOff, labels);
      }
    }
    return new Attribute(type).read(this, off, len, null, -1, null);
  }
  
  public int getItem(int item)
  {
    return this.items[item];
  }
  
  public int readByte(int index)
  {
    return this.b[index] & 0xFF;
  }
  
  public int readUnsignedShort(int index)
  {
    byte[] b = this.b;
    return (b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF;
  }
  
  public short readShort(int index)
  {
    byte[] b = this.b;
    return (short)((b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF);
  }
  
  public int readInt(int index)
  {
    byte[] b = this.b;
    return (b[index] & 0xFF) << 24 | (b[(index + 1)] & 0xFF) << 16 | (b[(index + 2)] & 0xFF) << 8 | b[(index + 3)] & 0xFF;
  }
  
  public long readLong(int index)
  {
    long l1 = readInt(index);
    long l0 = readInt(index + 4) & 0xFFFFFFFF;
    return l1 << 32 | l0;
  }
  
  public String readUTF8(int index, char[] buf)
  {
    int item = readUnsignedShort(index);
    String s = this.strings[item];
    if (s != null) {
      return s;
    }
    index = this.items[item];
    return this.strings[item] = readUTF(index + 2, readUnsignedShort(index), buf);
  }
  
  private String readUTF(int index, int utfLen, char[] buf)
  {
    int endIndex = index + utfLen;
    byte[] b = this.b;
    int strLen = 0;
    while (index < endIndex)
    {
      int c = b[(index++)] & 0xFF;
      int d;
      switch (c >> 4)
      {
      case 0: 
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
        buf[(strLen++)] = ((char)c);
        break;
      case 12: 
      case 13: 
        d = b[(index++)];
        buf[(strLen++)] = ((char)((c & 0x1F) << 6 | d & 0x3F));
        break;
      case 8: 
      case 9: 
      case 10: 
      case 11: 
      default: 
        d = b[(index++)];
        int e = b[(index++)];
        buf[(strLen++)] = ((char)((c & 0xF) << 12 | (d & 0x3F) << 6 | e & 0x3F));
      }
    }
    return new String(buf, 0, strLen);
  }
  
  public String readClass(int index, char[] buf)
  {
    return readUTF8(this.items[readUnsignedShort(index)], buf);
  }
  
  public Object readConst(int item, char[] buf)
  {
    int index = this.items[item];
    switch (this.b[(index - 1)])
    {
    case 3: 
      return Integer.valueOf(readInt(index));
    case 4: 
      return new Float(Float.intBitsToFloat(readInt(index)));
    case 5: 
      return Long.valueOf(readLong(index));
    case 6: 
      return new Double(Double.longBitsToDouble(readLong(index)));
    case 7: 
      return Type.getObjectType(readUTF8(index, buf));
    }
    return readUTF8(index, buf);
  }
}
