package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.AnnotationVisitor;
import com.avaje.ebean.enhance.asm.ClassAdapter;
import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassAdapterTransactional
  extends ClassAdapter
{
  static final Logger logger = Logger.getLogger(ClassAdapterTransactional.class.getName());
  final ArrayList<String> transactionalMethods = new ArrayList();
  final EnhanceContext enhanceContext;
  final ClassLoader classLoader;
  ArrayList<ClassMeta> transactionalInterfaces = new ArrayList();
  AnnotationInfo classAnnotationInfo;
  String className;
  
  public ClassAdapterTransactional(ClassVisitor cv, ClassLoader classLoader, EnhanceContext context)
  {
    super(cv);
    this.classLoader = classLoader;
    this.enhanceContext = context;
  }
  
  public boolean isLog(int level)
  {
    return this.enhanceContext.isLog(level);
  }
  
  public void log(String msg)
  {
    this.enhanceContext.log(this.className, msg);
  }
  
  public AnnotationInfo getInterfaceTransactionalInfo(String methodName, String methodDesc)
  {
    AnnotationInfo interfaceAnnotationInfo = null;
    for (int i = 0; i < this.transactionalInterfaces.size(); i++)
    {
      ClassMeta interfaceMeta = (ClassMeta)this.transactionalInterfaces.get(i);
      AnnotationInfo ai = interfaceMeta.getInterfaceTransactionalInfo(methodName, methodDesc);
      if (ai != null) {
        if (interfaceAnnotationInfo != null)
        {
          String msg = "Error in [" + this.className + "] searching the transactional interfaces [" + this.transactionalInterfaces + "] found more than one match for the transactional method:" + methodName + " " + methodDesc;
          
          logger.log(Level.SEVERE, msg);
        }
        else
        {
          interfaceAnnotationInfo = ai;
          if (isLog(2)) {
            log("inherit transactional from interface [" + interfaceMeta + "] method[" + methodName + " " + methodDesc + "]");
          }
        }
      }
    }
    return interfaceAnnotationInfo;
  }
  
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    this.className = name;
    
    int n = 1 + interfaces.length;
    String[] newInterfaces = new String[n];
    for (int i = 0; i < interfaces.length; i++)
    {
      newInterfaces[i] = interfaces[i];
      if (newInterfaces[i].equals("com/avaje/ebean/enhance/agent/EnhancedTransactional")) {
        throw new AlreadyEnhancedException(name);
      }
      ClassMeta interfaceMeta = this.enhanceContext.getInterfaceMeta(newInterfaces[i], this.classLoader);
      if ((interfaceMeta != null) && (interfaceMeta.isTransactional()))
      {
        this.transactionalInterfaces.add(interfaceMeta);
        if (isLog(6)) {
          log(" implements tranactional interface " + interfaceMeta.getDescription());
        }
      }
    }
    newInterfaces[(newInterfaces.length - 1)] = "com/avaje/ebean/enhance/agent/EnhancedTransactional";
    
    super.visit(version, access, name, signature, superName, newInterfaces);
  }
  
  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    AnnotationVisitor av = super.visitAnnotation(desc, visible);
    if (desc.equals("Lcom/avaje/ebean/annotation/Transactional;"))
    {
      this.classAnnotationInfo = new AnnotationInfo(null);
      return new AnnotationInfoVisitor(null, this.classAnnotationInfo, av);
    }
    return av;
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
    if (name.equals("<init>")) {
      return mv;
    }
    return new ScopeTransAdapter(this, mv, access, name, desc);
  }
  
  public void visitEnd()
  {
    if (!isLog(3)) {
      if (isLog(2)) {
        log("methods:" + this.transactionalMethods);
      }
    }
    super.visitEnd();
  }
  
  void transactionalMethod(String methodName, String methodDesc, AnnotationInfo annoInfo)
  {
    this.transactionalMethods.add(methodName);
    if (isLog(4)) {
      log("method:" + methodName + " " + methodDesc + " transactional " + annoInfo);
    } else if (isLog(3)) {
      log("method:" + methodName);
    }
  }
}
