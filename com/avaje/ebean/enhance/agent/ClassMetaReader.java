package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.ClassReader;
import java.util.HashMap;
import java.util.Map;

public class ClassMetaReader
{
  private Map<String, ClassMeta> cache = new HashMap();
  private final EnhanceContext enhanceContext;
  
  public ClassMetaReader(EnhanceContext enhanceContext)
  {
    this.enhanceContext = enhanceContext;
  }
  
  public ClassMeta get(boolean readMethodAnnotations, String name, ClassLoader classLoader)
    throws ClassNotFoundException
  {
    return getWithCache(readMethodAnnotations, name, classLoader);
  }
  
  private ClassMeta getWithCache(boolean readMethodAnnotations, String name, ClassLoader classLoader)
    throws ClassNotFoundException
  {
    synchronized (this.cache)
    {
      ClassMeta meta = (ClassMeta)this.cache.get(name);
      if (meta == null)
      {
        meta = readFromResource(readMethodAnnotations, name, classLoader);
        if (meta != null)
        {
          if (meta.isCheckSuperClassForEntity())
          {
            ClassMeta superMeta = getWithCache(readMethodAnnotations, meta.getSuperClassName(), classLoader);
            if ((superMeta != null) && (superMeta.isEntity())) {
              meta.setSuperMeta(superMeta);
            }
          }
          this.cache.put(name, meta);
        }
      }
      return meta;
    }
  }
  
  private ClassMeta readFromResource(boolean readMethodAnnotations, String className, ClassLoader classLoader)
    throws ClassNotFoundException
  {
    byte[] classBytes = this.enhanceContext.getClassBytes(className, classLoader);
    if (classBytes == null)
    {
      this.enhanceContext.log(1, "Class [" + className + "] not found.");
      return null;
    }
    if (this.enhanceContext.isLog(3)) {
      this.enhanceContext.log(className, "read ClassMeta");
    }
    ClassReader cr = new ClassReader(classBytes);
    ClassMetaReaderVisitor ca = new ClassMetaReaderVisitor(readMethodAnnotations, this.enhanceContext);
    
    cr.accept(ca, 0);
    
    return ca.getClassMeta();
  }
}
