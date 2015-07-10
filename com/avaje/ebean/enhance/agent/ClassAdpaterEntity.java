package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.ClassAdapter;
import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;
import com.avaje.ebean.enhance.asm.MethodVisitor;

public class ClassAdpaterEntity
  extends ClassAdapter
  implements EnhanceConstants
{
  private final EnhanceContext enhanceContext;
  private final ClassLoader classLoader;
  private final ClassMeta classMeta;
  private boolean firstMethod = true;
  
  public ClassAdpaterEntity(ClassVisitor cv, ClassLoader classLoader, EnhanceContext context)
  {
    super(cv);
    this.classLoader = classLoader;
    this.enhanceContext = context;
    this.classMeta = context.createClassMeta();
  }
  
  public void logEnhanced()
  {
    this.classMeta.logEnhanced();
  }
  
  public boolean isLog(int level)
  {
    return this.classMeta.isLog(level);
  }
  
  public void log(String msg)
  {
    this.classMeta.log(msg);
  }
  
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    this.classMeta.setClassName(name, superName);
    
    int n = 1 + interfaces.length;
    String[] c = new String[n];
    for (int i = 0; i < interfaces.length; i++)
    {
      c[i] = interfaces[i];
      if (c[i].equals("com/avaje/ebean/bean/EntityBean")) {
        this.classMeta.setEntityBeanInterface(true);
      }
      if (c[i].equals("scala/ScalaObject")) {
        this.classMeta.setScalaInterface(true);
      }
      if (c[i].equals("groovy/lang/GroovyObject")) {
        this.classMeta.setGroovyInterface(true);
      }
    }
    if (this.classMeta.hasEntityBeanInterface()) {
      c = interfaces;
    } else {
      c[(c.length - 1)] = "com/avaje/ebean/bean/EntityBean";
    }
    if (!superName.equals("java/lang/Object"))
    {
      if (this.classMeta.isLog(7)) {
        this.classMeta.log("read information about superClasses " + superName + " to see if it is entity/embedded/mappedSuperclass");
      }
      ClassMeta superMeta = this.enhanceContext.getSuperMeta(superName, this.classLoader);
      if ((superMeta != null) && (superMeta.isEntity()))
      {
        this.classMeta.setSuperMeta(superMeta);
        if (this.classMeta.isLog(1)) {
          this.classMeta.log("entity extends " + superMeta.getDescription());
        }
      }
      else if (this.classMeta.isLog(7))
      {
        if (superMeta == null) {
          this.classMeta.log("unable to read superMeta for " + superName);
        } else {
          this.classMeta.log("superMeta " + superName + " is not an entity/embedded/mappedsuperclass " + superMeta.getClassAnnotations());
        }
      }
    }
    super.visit(version, access, name, signature, superName, c);
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    this.classMeta.addClassAnnotation(desc);
    return super.visitAnnotation(desc, visible);
  }
  
  private boolean isEbeanFieldMarker(String name, String desc, String signature)
  {
    if (name.equals("_EBEAN_MARKER"))
    {
      if (!desc.equals("Ljava/lang/String;"))
      {
        String m = "Error: _EBEAN_MARKER field of wrong type? " + desc;
        this.classMeta.log(m);
      }
      return true;
    }
    return false;
  }
  
  private boolean isPropertyChangeListenerField(String name, String desc, String signature)
  {
    if (desc.equals("Ljava/beans/PropertyChangeSupport;")) {
      return true;
    }
    return false;
  }
  
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    if ((access & 0x8) != 0)
    {
      if (isEbeanFieldMarker(name, desc, signature))
      {
        this.classMeta.setAlreadyEnhanced(true);
        if (isLog(2)) {
          log("Found ebean marker field " + name + " " + value);
        }
      }
      else if (isLog(2))
      {
        log("Skip intercepting static field " + name);
      }
      return super.visitField(access, name, desc, signature, value);
    }
    if (isPropertyChangeListenerField(name, desc, signature))
    {
      if (isLog(1)) {
        this.classMeta.log("Found existing PropertyChangeSupport field " + name);
      }
      return super.visitField(access, name, desc, signature, value);
    }
    if ((access & 0x80) != 0)
    {
      if (isLog(2)) {
        log("Skip intercepting transient field " + name);
      }
      return super.visitField(access, name, desc, signature, value);
    }
    FieldVisitor fv = super.visitField(access, name, desc, signature, value);
    
    return this.classMeta.createLocalFieldVisitor(this.cv, fv, name, desc);
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    if (this.firstMethod)
    {
      if (!this.classMeta.isEntityEnhancementRequired()) {
        throw new NoEnhancementRequiredException();
      }
      if (this.classMeta.hasEntityBeanInterface()) {
        log("Enhancing when EntityBean interface already exists!");
      }
      String marker = MarkerField.addField(this.cv, this.classMeta.getClassName());
      if (isLog(4)) {
        log("... add marker field \"" + marker + "\"");
      }
      if (!this.classMeta.isSuperClassEntity())
      {
        if (isLog(4)) {
          log("... add intercept and identity fields");
        }
        InterceptField.addField(this.cv, this.enhanceContext.isTransientInternalFields());
        MethodEquals.addIdentityField(this.cv);
      }
      this.firstMethod = false;
    }
    this.classMeta.addExistingMethod(name, desc);
    if (isDefaultConstructor(name, desc))
    {
      MethodVisitor mv = super.visitMethod(1, name, desc, signature, exceptions);
      
      return new ConstructorAdapter(mv, this.classMeta, desc);
    }
    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
    if (interceptEntityMethod(access, name, desc, signature, exceptions)) {
      return new MethodFieldAdapter(mv, this.classMeta, name + " " + desc);
    }
    return mv;
  }
  
  public void visitEnd()
  {
    if (!this.classMeta.isEntityEnhancementRequired()) {
      throw new NoEnhancementRequiredException();
    }
    if (!this.classMeta.hasDefaultConstructor()) {
      DefaultConstructor.add(this.cv, this.classMeta);
    }
    MarkerField.addGetMarker(this.cv, this.classMeta.getClassName());
    if (!this.classMeta.isSuperClassEntity())
    {
      InterceptField.addGetterSetter(this.cv, this.classMeta.getClassName());
      
      MethodPropertyChangeListener.addMethod(this.cv, this.classMeta);
    }
    this.classMeta.addFieldGetSetMethods(this.cv);
    
    IndexFieldWeaver.addMethods(this.cv, this.classMeta);
    
    MethodSetEmbeddedLoaded.addMethod(this.cv, this.classMeta);
    MethodIsEmbeddedNewOrDirty.addMethod(this.cv, this.classMeta);
    MethodNewInstance.addMethod(this.cv, this.classMeta);
    
    this.enhanceContext.addClassMeta(this.classMeta);
    
    super.visitEnd();
  }
  
  private boolean isDefaultConstructor(String name, String desc)
  {
    if (name.equals("<init>"))
    {
      if (desc.equals("()V")) {
        this.classMeta.setHasDefaultConstructor(true);
      }
      return true;
    }
    return false;
  }
  
  private boolean interceptEntityMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    if ((access & 0x8) != 0)
    {
      if (isLog(2)) {
        log("Skip intercepting static method " + name);
      }
      return false;
    }
    if ((name.equals("hashCode")) && (desc.equals("()I")))
    {
      this.classMeta.setHasEqualsOrHashcode(true);
      return true;
    }
    if ((name.equals("equals")) && (desc.equals("(Ljava/lang/Object;)Z")))
    {
      this.classMeta.setHasEqualsOrHashcode(true);
      return true;
    }
    if ((name.equals("toString")) && (desc.equals("()Ljava/lang/String;"))) {
      return false;
    }
    return true;
  }
}
