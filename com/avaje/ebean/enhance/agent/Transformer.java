package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassReader;
import com.avaje.ebean.enhance.asm.ClassWriter;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.ProtectionDomain;

public class Transformer
  implements ClassFileTransformer
{
  private static final int CLASS_WRITER_COMPUTEFLAGS = 3;
  private final EnhanceContext enhanceContext;
  private boolean performDetect;
  private boolean transformTransactional;
  private boolean transformEntityBeans;
  
  public static void premain(String agentArgs, Instrumentation inst)
  {
    Transformer t = new Transformer("", agentArgs);
    inst.addTransformer(t);
    if (t.getLogLevel() > 0) {
      System.out.println("premain loading Transformer args:" + agentArgs);
    }
  }
  
  public Transformer(String extraClassPath, String agentArgs)
  {
    this(parseClassPaths(extraClassPath), agentArgs);
  }
  
  public Transformer(URL[] extraClassPath, String agentArgs)
  {
    this(new ClassPathClassBytesReader(extraClassPath), agentArgs);
  }
  
  public Transformer(ClassBytesReader r, String agentArgs)
  {
    this.enhanceContext = new EnhanceContext(r, false, agentArgs);
    this.performDetect = this.enhanceContext.getPropertyBoolean("detect", true);
    this.transformTransactional = this.enhanceContext.getPropertyBoolean("transactional", true);
    this.transformEntityBeans = this.enhanceContext.getPropertyBoolean("entity", true);
  }
  
  protected ClassWriter createClassWriter()
  {
    return new ClassWriter(3);
  }
  
  public void setLogout(PrintStream logout)
  {
    this.enhanceContext.setLogout(logout);
  }
  
  public void log(int level, String msg)
  {
    this.enhanceContext.log(level, msg);
  }
  
  public int getLogLevel()
  {
    return this.enhanceContext.getLogLevel();
  }
  
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
    throws IllegalClassFormatException
  {
    try
    {
      if (this.enhanceContext.isIgnoreClass(className)) {
        return null;
      }
      ClassAdapterDetectEnhancement detect = null;
      if (this.performDetect)
      {
        this.enhanceContext.log(5, "performing detection on " + className);
        detect = detect(loader, classfileBuffer);
      }
      if (detect == null)
      {
        this.enhanceContext.log(1, "no detection so enhancing entity " + className);
        return entityEnhancement(loader, classfileBuffer);
      }
      if ((this.transformEntityBeans) && (detect.isEntity())) {
        if (detect.isEnhancedEntity())
        {
          detect.log(1, "already enhanced entity");
        }
        else
        {
          detect.log(2, "performing entity transform");
          return entityEnhancement(loader, classfileBuffer);
        }
      }
      if ((this.transformTransactional) && (detect.isTransactional())) {
        if (detect.isEnhancedTransactional())
        {
          detect.log(1, "already enhanced transactional");
        }
        else
        {
          detect.log(2, "performing transactional transform");
          return transactionalEnhancement(loader, classfileBuffer);
        }
      }
      return null;
    }
    catch (NoEnhancementRequiredException e)
    {
      log(8, "No Enhancement required " + e.getMessage());
      return null;
    }
    catch (Exception e)
    {
      this.enhanceContext.log(e);
    }
    return null;
  }
  
  private byte[] entityEnhancement(ClassLoader loader, byte[] classfileBuffer)
  {
    ClassReader cr = new ClassReader(classfileBuffer);
    ClassWriter cw = createClassWriter();
    ClassAdpaterEntity ca = new ClassAdpaterEntity(cw, loader, this.enhanceContext);
    try
    {
      cr.accept(ca, 0);
      if (ca.isLog(1)) {
        ca.logEnhanced();
      }
      if (this.enhanceContext.isReadOnly()) {
        return null;
      }
      return cw.toByteArray();
    }
    catch (AlreadyEnhancedException e)
    {
      if (ca.isLog(1)) {
        ca.log("already enhanced entity");
      }
      return null;
    }
    catch (NoEnhancementRequiredException e)
    {
      if (ca.isLog(2)) {
        ca.log("skipping... no enhancement required");
      }
    }
    return null;
  }
  
  private byte[] transactionalEnhancement(ClassLoader loader, byte[] classfileBuffer)
  {
    ClassReader cr = new ClassReader(classfileBuffer);
    ClassWriter cw = createClassWriter();
    ClassAdapterTransactional ca = new ClassAdapterTransactional(cw, loader, this.enhanceContext);
    try
    {
      cr.accept(ca, 8);
      if (ca.isLog(1)) {
        ca.log("enhanced");
      }
      if (this.enhanceContext.isReadOnly()) {
        return null;
      }
      return cw.toByteArray();
    }
    catch (AlreadyEnhancedException e)
    {
      if (ca.isLog(1)) {
        ca.log("already enhanced");
      }
      return null;
    }
    catch (NoEnhancementRequiredException e)
    {
      if (ca.isLog(0)) {
        ca.log("skipping... no enhancement required");
      }
    }
    return null;
  }
  
  public static URL[] parseClassPaths(String extraClassPath)
  {
    if (extraClassPath == null) {
      return new URL[0];
    }
    String[] stringPaths = extraClassPath.split(";");
    return UrlPathHelper.convertToUrl(stringPaths);
  }
  
  private ClassAdapterDetectEnhancement detect(ClassLoader classLoader, byte[] classfileBuffer)
  {
    ClassAdapterDetectEnhancement detect = new ClassAdapterDetectEnhancement(classLoader, this.enhanceContext);
    
    ClassReader cr = new ClassReader(classfileBuffer);
    cr.accept(detect, 7);
    
    return detect;
  }
}
