package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebean.config.dbplatform.DbDdlSyntax;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.TableJoin;

public class DropTableVisitor
  implements BeanVisitor
{
  final DdlGenContext ctx;
  final DbDdlSyntax ddlSyntax;
  
  public DropTableVisitor(DdlGenContext ctx)
  {
    this.ctx = ctx;
    this.ddlSyntax = ctx.getDdlSyntax();
  }
  
  protected void writeDropTable(BeanDescriptor<?> descriptor)
  {
    writeDropTable(descriptor.getBaseTable());
  }
  
  protected void writeDropTable(String tableName)
  {
    this.ctx.write("drop table ");
    if (this.ddlSyntax.getDropIfExists() != null) {
      this.ctx.write(this.ddlSyntax.getDropIfExists()).write(" ");
    }
    this.ctx.write(tableName);
    if (this.ddlSyntax.getDropTableCascade() != null) {
      this.ctx.write(" ").write(this.ddlSyntax.getDropTableCascade());
    }
    this.ctx.write(";").writeNewLine().writeNewLine();
  }
  
  public boolean visitBean(BeanDescriptor<?> descriptor)
  {
    if (!descriptor.isInheritanceRoot()) {
      return false;
    }
    writeDropTable(descriptor);
    
    dropIntersectionTables(descriptor);
    
    return true;
  }
  
  private void dropIntersectionTables(BeanDescriptor<?> descriptor)
  {
    BeanPropertyAssocMany<?>[] manyProps = descriptor.propertiesMany();
    for (int i = 0; i < manyProps.length; i++) {
      if (manyProps[i].isManyToMany())
      {
        String intTable = manyProps[i].getIntersectionTableJoin().getTable();
        if (this.ctx.isProcessIntersectionTable(intTable)) {
          writeDropTable(intTable);
        }
      }
    }
  }
  
  public void visitBeanEnd(BeanDescriptor<?> descriptor) {}
  
  public void visitBegin()
  {
    if (this.ddlSyntax.getDisableReferentialIntegrity() != null)
    {
      this.ctx.write(this.ddlSyntax.getDisableReferentialIntegrity());
      this.ctx.write(";").writeNewLine().writeNewLine();
    }
  }
  
  public void visitEnd()
  {
    if (this.ddlSyntax.getEnableReferentialIntegrity() != null)
    {
      this.ctx.write(this.ddlSyntax.getEnableReferentialIntegrity());
      this.ctx.write(";").writeNewLine().writeNewLine();
    }
  }
  
  public PropertyVisitor visitProperty(BeanProperty p)
  {
    return null;
  }
}
