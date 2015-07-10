package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.InheritInfo;
import com.avaje.ebeaninternal.server.deploy.InheritInfoVisitor;

public abstract class AbstractBeanVisitor
  implements BeanVisitor
{
  public void visitInheritanceProperties(BeanDescriptor<?> descriptor, PropertyVisitor pv)
  {
    InheritInfo inheritInfo = descriptor.getInheritInfo();
    if ((inheritInfo != null) && (inheritInfo.isRoot()))
    {
      InheritChildVisitor childVisitor = new InheritChildVisitor(pv);
      inheritInfo.visitChildren(childVisitor);
    }
  }
  
  protected static class InheritChildVisitor
    implements InheritInfoVisitor
  {
    final PropertyVisitor pv;
    
    protected InheritChildVisitor(PropertyVisitor pv)
    {
      this.pv = pv;
    }
    
    public void visit(InheritInfo inheritInfo)
    {
      BeanProperty[] propertiesLocal = inheritInfo.getBeanDescriptor().propertiesLocal();
      VisitorUtil.visit(propertiesLocal, this.pv);
    }
  }
}
