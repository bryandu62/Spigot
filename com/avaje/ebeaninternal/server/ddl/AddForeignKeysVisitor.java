package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebean.config.dbplatform.DbDdlSyntax;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompound;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import com.avaje.ebeaninternal.server.deploy.TableJoinColumn;

public class AddForeignKeysVisitor
  extends AbstractBeanVisitor
{
  final DdlGenContext ctx;
  final FkeyPropertyVisitor pv;
  
  public AddForeignKeysVisitor(DdlGenContext ctx)
  {
    this.ctx = ctx;
    this.pv = new FkeyPropertyVisitor(this, ctx);
  }
  
  public boolean visitBean(BeanDescriptor<?> descriptor)
  {
    if (!descriptor.isInheritanceRoot()) {
      return false;
    }
    return true;
  }
  
  public void visitBeanEnd(BeanDescriptor<?> descriptor)
  {
    visitInheritanceProperties(descriptor, this.pv);
  }
  
  public void visitBegin() {}
  
  public void visitEnd()
  {
    this.ctx.addIntersectionFkeys();
  }
  
  public PropertyVisitor visitProperty(BeanProperty p)
  {
    return this.pv;
  }
  
  public static class FkeyPropertyVisitor
    extends BaseTablePropertyVisitor
  {
    final DdlGenContext ctx;
    final AddForeignKeysVisitor parent;
    
    public FkeyPropertyVisitor(AddForeignKeysVisitor parent, DdlGenContext ctx)
    {
      this.parent = parent;
      this.ctx = ctx;
    }
    
    public void visitEmbeddedScalar(BeanProperty p, BeanPropertyAssocOne<?> embedded) {}
    
    public void visitOneImported(BeanPropertyAssocOne<?> p)
    {
      String baseTable = p.getBeanDescriptor().getBaseTable();
      
      TableJoin tableJoin = p.getTableJoin();
      
      TableJoinColumn[] columns = tableJoin.columns();
      
      String tableName = p.getBeanDescriptor().getBaseTable();
      String fkName = this.ctx.getDdlSyntax().getForeignKeyName(tableName, p.getName(), this.ctx.incrementFkCount());
      
      this.ctx.write("alter table ").write(baseTable).write(" add ");
      if (fkName != null) {
        this.ctx.write("constraint ").write(fkName).write(" ");
      }
      this.ctx.write("foreign key (");
      for (int i = 0; i < columns.length; i++)
      {
        if (i > 0) {
          this.ctx.write(",");
        }
        this.ctx.write(columns[i].getLocalDbColumn());
      }
      this.ctx.write(")");
      
      this.ctx.write(" references ");
      this.ctx.write(tableJoin.getTable());
      this.ctx.write(" (");
      for (int i = 0; i < columns.length; i++)
      {
        if (i > 0) {
          this.ctx.write(",");
        }
        this.ctx.write(columns[i].getForeignDbColumn());
      }
      this.ctx.write(")");
      
      String fkeySuffix = this.ctx.getDdlSyntax().getForeignKeySuffix();
      if (fkeySuffix != null) {
        this.ctx.write(" ").write(fkeySuffix);
      }
      this.ctx.write(";").writeNewLine();
      if (this.ctx.getDdlSyntax().isRenderIndexForFkey())
      {
        this.ctx.write("create index ");
        
        String idxName = this.ctx.getDdlSyntax().getIndexName(tableName, p.getName(), this.ctx.incrementIxCount());
        if (idxName != null) {
          this.ctx.write(idxName);
        }
        this.ctx.write(" on ").write(baseTable).write(" (");
        for (int i = 0; i < columns.length; i++)
        {
          if (i > 0) {
            this.ctx.write(",");
          }
          this.ctx.write(columns[i].getLocalDbColumn());
        }
        this.ctx.write(");").writeNewLine();
      }
    }
    
    public void visitScalar(BeanProperty p) {}
    
    public void visitCompound(BeanPropertyCompound p) {}
    
    public void visitCompoundScalar(BeanPropertyCompound compound, BeanProperty p) {}
  }
}
