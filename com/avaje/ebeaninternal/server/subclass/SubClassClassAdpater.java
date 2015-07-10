package com.avaje.ebeaninternal.server.subclass;

import com.avaje.ebean.enhance.agent.AlreadyEnhancedException;
import com.avaje.ebean.enhance.agent.ClassMeta;
import com.avaje.ebean.enhance.agent.EnhanceConstants;
import com.avaje.ebean.enhance.agent.EnhanceContext;
import com.avaje.ebean.enhance.agent.IndexFieldWeaver;
import com.avaje.ebean.enhance.agent.InterceptField;
import com.avaje.ebean.enhance.agent.MarkerField;
import com.avaje.ebean.enhance.agent.MethodEquals;
import com.avaje.ebean.enhance.agent.MethodIsEmbeddedNewOrDirty;
import com.avaje.ebean.enhance.agent.MethodNewInstance;
import com.avaje.ebean.enhance.agent.MethodPropertyChangeListener;
import com.avaje.ebean.enhance.agent.MethodSetEmbeddedLoaded;
import com.avaje.ebean.enhance.agent.NoEnhancementRequiredException;
import com.avaje.ebean.enhance.agent.VisitMethodParams;
import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.ClassAdapter;
import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.FieldVisitor;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import java.util.logging.Logger;

public class SubClassClassAdpater
  extends ClassAdapter
  implements EnhanceConstants
{
  static final Logger logger = Logger.getLogger(SubClassClassAdpater.class.getName());
  final EnhanceContext enhanceContext;
  final ClassLoader classLoader;
  final ClassMeta classMeta;
  final String subClassSuffix;
  boolean firstMethod = true;
  
  public SubClassClassAdpater(String subClassSuffix, ClassVisitor cv, ClassLoader classLoader, EnhanceContext context)
  {
    super(cv);
    this.subClassSuffix = subClassSuffix;
    this.classLoader = classLoader;
    this.enhanceContext = context;
    this.classMeta = context.createClassMeta();
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
    int n = 1 + interfaces.length;
    String[] c = new String[n];
    for (int i = 0; i < interfaces.length; i++)
    {
      c[i] = interfaces[i];
      if (c[i].equals("com/avaje/ebean/bean/EntityBean")) {
        throw new AlreadyEnhancedException(name);
      }
      if (c[i].equals("scala/ScalaObject")) {
        this.classMeta.setScalaInterface(true);
      }
      if (c[i].equals("groovy/lang/GroovyObject")) {
        this.classMeta.setGroovyInterface(true);
      }
    }
    c[(c.length - 1)] = "com/avaje/ebean/bean/EntityBean";
    if (!superName.equals("java/lang/Object"))
    {
      ClassMeta superMeta = this.enhanceContext.getSuperMeta(superName, this.classLoader);
      if (superMeta != null)
      {
        this.classMeta.setSuperMeta(superMeta);
        if (this.classMeta.isLog(2)) {
          this.classMeta.log("entity inheritance " + superMeta.getDescription());
        }
      }
    }
    superName = name;
    name = name + this.subClassSuffix;
    
    this.classMeta.setClassName(name, superName);
    
    super.visit(version, access, name, signature, superName, c);
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    this.classMeta.addClassAnnotation(desc);
    return super.visitAnnotation(desc, visible);
  }
  
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    if ((access & 0x8) != 0)
    {
      if (isLog(2)) {
        log("Skip intercepting static field " + name);
      }
      return null;
    }
    if ((access & 0x80) != 0)
    {
      if (this.classMeta.isLog(2)) {
        this.classMeta.log("Skip intercepting transient field " + name);
      }
      return null;
    }
    if (this.classMeta.isLog(5)) {
      this.classMeta.log(" ... reading field:" + name + " desc:" + desc);
    }
    return this.classMeta.createLocalFieldVisitor(name, desc);
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    if (this.firstMethod)
    {
      if (!this.classMeta.isEntityEnhancementRequired()) {
        throw new NoEnhancementRequiredException();
      }
      String marker = MarkerField.addField(this.cv, this.classMeta.getClassName());
      if (isLog(4))
      {
        log("... add marker field \"" + marker + "\"");
        log("... add intercept and identity fields");
      }
      InterceptField.addField(this.cv, this.enhanceContext.isTransientInternalFields());
      MethodEquals.addIdentityField(this.cv);
      this.firstMethod = false;
    }
    VisitMethodParams params = new VisitMethodParams(this.cv, access, name, desc, signature, exceptions);
    if (isDefaultConstructor(access, name, desc, signature, exceptions))
    {
      SubClassConstructor.add(params, this.classMeta);
      return null;
    }
    if (isSpecialMethod(access, name, desc)) {
      return null;
    }
    this.classMeta.addExistingSuperMethod(name, desc);
    
    return null;
  }
  
  public void visitEnd()
  {
    if (!this.classMeta.isEntityEnhancementRequired()) {
      throw new NoEnhancementRequiredException();
    }
    if (!this.classMeta.hasDefaultConstructor())
    {
      if (isLog(2)) {
        log("... adding default constructor");
      }
      SubClassConstructor.addDefault(this.cv, this.classMeta);
    }
    MarkerField.addGetMarker(this.cv, this.classMeta.getClassName());
    
    InterceptField.addGetterSetter(this.cv, this.classMeta.getClassName());
    
    MethodPropertyChangeListener.addMethod(this.cv, this.classMeta);
    
    GetterSetterMethods.add(this.cv, this.classMeta);
    
    IndexFieldWeaver.addMethods(this.cv, this.classMeta);
    
    MethodSetEmbeddedLoaded.addMethod(this.cv, this.classMeta);
    MethodIsEmbeddedNewOrDirty.addMethod(this.cv, this.classMeta);
    MethodNewInstance.addMethod(this.cv, this.classMeta);
    
    MethodWriteReplace.add(this.cv, this.classMeta);
    
    this.enhanceContext.addClassMeta(this.classMeta);
    
    super.visitEnd();
  }
  
  private boolean isDefaultConstructor(int access, String name, String desc, String signature, String[] exceptions)
  {
    if ((name.equals("<init>")) && (desc.equals("()V")))
    {
      this.classMeta.setHasDefaultConstructor(true);
      return true;
    }
    return false;
  }
  
  private boolean isSpecialMethod(int access, String name, String desc)
  {
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
    return false;
  }
}
