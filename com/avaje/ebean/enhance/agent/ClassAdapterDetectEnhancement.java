package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.ClassAdapter;
import com.avaje.ebean.enhance.asm.EmptyVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;
import com.avaje.ebean.enhance.asm.MethodAdapter;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.HashSet;

public class ClassAdapterDetectEnhancement
  extends ClassAdapter
{
  private final ClassLoader classLoader;
  private final EnhanceContext enhanceContext;
  private final HashSet<String> classAnnotation = new HashSet();
  private final ArrayList<DetectMethod> methods = new ArrayList();
  private String className;
  private boolean entity;
  private boolean entityInterface;
  private boolean entityField;
  private boolean transactional;
  private boolean enhancedTransactional;
  
  public ClassAdapterDetectEnhancement(ClassLoader classLoader, EnhanceContext context)
  {
    super(new EmptyVisitor());
    this.classLoader = classLoader;
    this.enhanceContext = context;
  }
  
  public boolean isEntityOrTransactional()
  {
    return (this.entity) || (isTransactional());
  }
  
  public String getStatus()
  {
    String s = "class: " + this.className;
    if (isEntity())
    {
      s = s + " entity:true  enhanced:" + this.entityField;
      s = "*" + s;
    }
    else if (isTransactional())
    {
      s = s + " transactional:true  enhanced:" + this.enhancedTransactional;
      s = "*" + s;
    }
    else
    {
      s = " " + s;
    }
    return s;
  }
  
  public boolean isLog(int level)
  {
    return this.enhanceContext.isLog(level);
  }
  
  public void log(String msg)
  {
    this.enhanceContext.log(this.className, msg);
  }
  
  public void log(int level, String msg)
  {
    if (isLog(level)) {
      log(msg);
    }
  }
  
  public boolean isEnhancedEntity()
  {
    return this.entityField;
  }
  
  public boolean isEnhancedTransactional()
  {
    return this.enhancedTransactional;
  }
  
  public boolean isEntity()
  {
    return this.entity;
  }
  
  public boolean isTransactional()
  {
    if (this.transactional) {
      return this.transactional;
    }
    for (int i = 0; i < this.methods.size(); i++)
    {
      DetectMethod m = (DetectMethod)this.methods.get(i);
      if (m.isTransactional()) {
        return true;
      }
    }
    return false;
  }
  
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    if ((access & 0x200) != 0) {
      throw new NoEnhancementRequiredException(name + " is an Interface");
    }
    this.className = name;
    for (int i = 0; i < interfaces.length; i++) {
      if (interfaces[i].equals("com/avaje/ebean/bean/EntityBean"))
      {
        this.entityInterface = true;
        this.entity = true;
      }
      else if (interfaces[i].equals("com/avaje/ebean/enhance/agent/EnhancedTransactional"))
      {
        this.enhancedTransactional = true;
      }
      else
      {
        ClassMeta intefaceMeta = this.enhanceContext.getInterfaceMeta(interfaces[i], this.classLoader);
        if ((intefaceMeta != null) && (intefaceMeta.isTransactional()))
        {
          this.transactional = true;
          if (isLog(9)) {
            log("detected implements tranactional interface " + intefaceMeta);
          }
        }
      }
    }
    if (isLog(2)) {
      log("interfaces:  entityInterface[" + this.entityInterface + "] transactional[" + this.enhancedTransactional + "]");
    }
    super.visit(version, access, name, signature, superName, interfaces);
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    if (isLog(8)) {
      log("visitAnnotation " + desc);
    }
    this.classAnnotation.add(desc);
    if (isEntityAnnotation(desc))
    {
      if (isLog(5)) {
        log("found entity annotation " + desc);
      }
      this.entity = true;
    }
    else if (desc.equals("Lcom/avaje/ebean/annotation/Transactional;"))
    {
      if (isLog(5)) {
        log("found transactional annotation " + desc);
      }
      this.transactional = true;
    }
    return super.visitAnnotation(desc, visible);
  }
  
  private boolean isEntityAnnotation(String desc)
  {
    if (desc.equals("Ljavax/persistence/Entity;")) {
      return true;
    }
    if (desc.equals("Ljavax/persistence/Embeddable;")) {
      return true;
    }
    if (desc.equals("Ljavax/persistence/MappedSuperclass;")) {
      return true;
    }
    return false;
  }
  
  private boolean isEbeanFieldMarker(String name, String desc, String signature)
  {
    if (name.equals("_EBEAN_MARKER"))
    {
      if (!desc.equals("Ljava/lang/String;"))
      {
        String m = "Error: _EBEAN_MARKER field of wrong type? " + desc;
        log(m);
      }
      return true;
    }
    return false;
  }
  
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    if (isLog(8)) {
      log("visitField " + name + " " + value);
    }
    if ((access & 0x8) != 0) {
      if (isEbeanFieldMarker(name, desc, signature))
      {
        this.entityField = true;
        if (isLog(1)) {
          log("Found ebean marker field " + name + " " + value);
        }
      }
    }
    return super.visitField(access, name, desc, signature, value);
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    if (isLog(9)) {
      log("visitMethod " + name + " " + desc);
    }
    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
    DetectMethod dmv = new DetectMethod(mv);
    
    this.methods.add(dmv);
    return dmv;
  }
  
  private static class DetectMethod
    extends MethodAdapter
  {
    boolean transactional;
    
    public DetectMethod(MethodVisitor mv)
    {
      super();
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
      return super.visitAnnotation(desc, visible);
    }
  }
}
