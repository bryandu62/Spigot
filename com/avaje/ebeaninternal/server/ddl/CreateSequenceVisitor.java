package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.DbIdentity;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.util.logging.Logger;

public class CreateSequenceVisitor
  implements BeanVisitor
{
  private static final Logger logger = Logger.getLogger(DropSequenceVisitor.class.getName());
  private final DdlGenContext ctx;
  private final boolean supportsSequence;
  
  public CreateSequenceVisitor(DdlGenContext ctx)
  {
    this.ctx = ctx;
    this.supportsSequence = ctx.getDbPlatform().getDbIdentity().isSupportsSequence();
  }
  
  public boolean visitBean(BeanDescriptor<?> descriptor)
  {
    if (!descriptor.isInheritanceRoot()) {
      return false;
    }
    if (descriptor.getSequenceName() == null) {
      return false;
    }
    if (!this.supportsSequence)
    {
      String msg = "Not creating sequence " + descriptor.getSequenceName() + " on Bean " + descriptor.getName() + " as DatabasePlatform does not support sequences";
      
      logger.warning(msg);
      return false;
    }
    if (descriptor.getSequenceName() != null)
    {
      this.ctx.write("create sequence ");
      this.ctx.write(descriptor.getSequenceName());
      this.ctx.write(";").writeNewLine().writeNewLine();
    }
    return true;
  }
  
  public void visitBeanEnd(BeanDescriptor<?> descriptor) {}
  
  public void visitBegin() {}
  
  public void visitEnd() {}
  
  public PropertyVisitor visitProperty(BeanProperty p)
  {
    return null;
  }
}
