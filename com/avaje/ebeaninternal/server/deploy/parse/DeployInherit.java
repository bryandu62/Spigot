package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebeaninternal.server.core.BootupClasses;
import com.avaje.ebeaninternal.server.deploy.InheritInfo;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Inheritance;

public class DeployInherit
{
  private final Map<Class<?>, DeployInheritInfo> deployMap = new LinkedHashMap();
  private final Map<Class<?>, InheritInfo> finalMap = new LinkedHashMap();
  private final BootupClasses bootupClasses;
  
  public DeployInherit(BootupClasses bootupClasses)
  {
    this.bootupClasses = bootupClasses;
    initialise();
  }
  
  public void process(DeployBeanDescriptor<?> desc)
  {
    InheritInfo inheritInfo = (InheritInfo)this.finalMap.get(desc.getBeanType());
    desc.setInheritInfo(inheritInfo);
  }
  
  private void initialise()
  {
    List<Class<?>> entityList = this.bootupClasses.getEntities();
    
    findInheritClasses(entityList);
    buildDeployTree();
    buildFinalTree();
  }
  
  private void findInheritClasses(List<Class<?>> entityList)
  {
    Iterator<Class<?>> it = entityList.iterator();
    while (it.hasNext())
    {
      Class<?> cls = (Class)it.next();
      if (isInheritanceClass(cls))
      {
        DeployInheritInfo info = createInfo(cls);
        this.deployMap.put(cls, info);
      }
    }
  }
  
  private void buildDeployTree()
  {
    Iterator<DeployInheritInfo> it = this.deployMap.values().iterator();
    while (it.hasNext())
    {
      DeployInheritInfo info = (DeployInheritInfo)it.next();
      if (!info.isRoot())
      {
        DeployInheritInfo parent = getInfo(info.getParent());
        parent.addChild(info);
      }
    }
  }
  
  private void buildFinalTree()
  {
    Iterator<DeployInheritInfo> it = this.deployMap.values().iterator();
    while (it.hasNext())
    {
      DeployInheritInfo deploy = (DeployInheritInfo)it.next();
      if (deploy.isRoot()) {
        createFinalInfo(null, null, deploy);
      }
    }
  }
  
  private InheritInfo createFinalInfo(InheritInfo root, InheritInfo parent, DeployInheritInfo deploy)
  {
    InheritInfo node = new InheritInfo(root, parent, deploy);
    if (parent != null) {
      parent.addChild(node);
    }
    this.finalMap.put(node.getType(), node);
    if (root == null) {
      root = node;
    }
    Iterator<DeployInheritInfo> it = deploy.children();
    while (it.hasNext())
    {
      DeployInheritInfo childDeploy = (DeployInheritInfo)it.next();
      
      createFinalInfo(root, node, childDeploy);
    }
    return node;
  }
  
  private DeployInheritInfo getInfo(Class<?> cls)
  {
    return (DeployInheritInfo)this.deployMap.get(cls);
  }
  
  private DeployInheritInfo createInfo(Class<?> cls)
  {
    DeployInheritInfo info = new DeployInheritInfo(cls);
    
    Class<?> parent = findParent(cls);
    if (parent != null) {
      info.setParent(parent);
    }
    Inheritance ia = (Inheritance)cls.getAnnotation(Inheritance.class);
    if (ia != null) {
      ia.strategy();
    }
    DiscriminatorColumn da = (DiscriminatorColumn)cls.getAnnotation(DiscriminatorColumn.class);
    if (da != null)
    {
      info.setDiscriminatorColumn(da.name());
      DiscriminatorType discriminatorType = da.discriminatorType();
      if (discriminatorType.equals(DiscriminatorType.INTEGER)) {
        info.setDiscriminatorType(4);
      } else {
        info.setDiscriminatorType(12);
      }
      info.setDiscriminatorLength(da.length());
    }
    DiscriminatorValue dv = (DiscriminatorValue)cls.getAnnotation(DiscriminatorValue.class);
    if (dv != null) {
      info.setDiscriminatorValue(dv.value());
    }
    return info;
  }
  
  private Class<?> findParent(Class<?> cls)
  {
    Class<?> superCls = cls.getSuperclass();
    if (isInheritanceClass(superCls)) {
      return superCls;
    }
    return null;
  }
  
  private boolean isInheritanceClass(Class<?> cls)
  {
    if (cls.equals(Object.class)) {
      return false;
    }
    Annotation a = cls.getAnnotation(Inheritance.class);
    if (a != null) {
      return true;
    }
    return isInheritanceClass(cls.getSuperclass());
  }
}
