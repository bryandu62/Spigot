package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebean.config.dbplatform.DbDdlSyntax;
import com.avaje.ebean.config.dbplatform.IdType;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompound;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import com.avaje.ebeaninternal.server.deploy.TableJoinColumn;
import com.avaje.ebeaninternal.server.deploy.id.ImportedId;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.type.ScalarType;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateTableColumnVisitor
  extends BaseTablePropertyVisitor
{
  private static final Logger logger = Logger.getLogger(CreateTableColumnVisitor.class.getName());
  private final DdlGenContext ctx;
  private final DbDdlSyntax ddl;
  private final CreateTableVisitor parent;
  
  public CreateTableColumnVisitor(CreateTableVisitor parent, DdlGenContext ctx)
  {
    this.parent = parent;
    this.ctx = ctx;
    this.ddl = ctx.getDdlSyntax();
  }
  
  public void visitMany(BeanPropertyAssocMany<?> p)
  {
    if ((p.isManyToMany()) && 
      (p.getMappedBy() == null))
    {
      TableJoin intersectionTableJoin = p.getIntersectionTableJoin();
      
      String intTable = intersectionTableJoin.getTable();
      if (this.ctx.isProcessIntersectionTable(intTable)) {
        new CreateIntersectionTable(this.ctx, p).build();
      }
    }
  }
  
  public void visitCompoundScalar(BeanPropertyCompound compound, BeanProperty p)
  {
    visitScalar(p);
  }
  
  public void visitCompound(BeanPropertyCompound p) {}
  
  public void visitEmbeddedScalar(BeanProperty p, BeanPropertyAssocOne<?> embedded)
  {
    visitScalar(p);
  }
  
  private StringBuilder createUniqueConstraintBuffer(String table, String column)
  {
    String uqConstraintName = "uq_" + table + "_" + column;
    if (uqConstraintName.length() > this.ddl.getMaxConstraintNameLength()) {
      uqConstraintName = uqConstraintName.substring(0, this.ddl.getMaxConstraintNameLength());
    }
    uqConstraintName = this.ctx.removeQuotes(uqConstraintName);
    uqConstraintName = StringHelper.replaceString(uqConstraintName, " ", "_");
    
    StringBuilder constraintExpr = new StringBuilder();
    constraintExpr.append("constraint ").append(uqConstraintName).append(" unique (");
    
    return constraintExpr;
  }
  
  public void visitOneImported(BeanPropertyAssocOne<?> p)
  {
    ImportedId importedId = p.getImportedId();
    
    TableJoinColumn[] columns = p.getTableJoin().columns();
    if (columns.length == 0)
    {
      String msg = "No join columns for " + p.getFullBeanName();
      throw new RuntimeException(msg);
    }
    StringBuilder constraintExpr = createUniqueConstraintBuffer(p.getBeanDescriptor().getBaseTable(), columns[0].getLocalDbColumn());
    for (int i = 0; i < columns.length; i++)
    {
      String dbCol = columns[i].getLocalDbColumn();
      if (i > 0) {
        constraintExpr.append(", ");
      }
      constraintExpr.append(dbCol);
      if (!this.parent.isDbColumnWritten(dbCol))
      {
        this.parent.writeColumnName(dbCol, p);
        
        BeanProperty importedProperty = importedId.findMatchImport(dbCol);
        if (importedProperty != null)
        {
          String columnDefn = this.ctx.getColumnDefn(importedProperty);
          this.ctx.write(columnDefn);
        }
        else
        {
          throw new RuntimeException("Imported BeanProperty not found?");
        }
        if (!p.isNullable()) {
          this.ctx.write(" not null");
        }
        this.ctx.write(",").writeNewLine();
      }
    }
    constraintExpr.append(")");
    if ((p.isOneToOne()) && 
      (this.ddl.isAddOneToOneUniqueContraint())) {
      this.parent.addUniqueConstraint(constraintExpr.toString());
    }
  }
  
  public void visitScalar(BeanProperty p)
  {
    if (p.isSecondaryTable()) {
      return;
    }
    if (this.parent.isDbColumnWritten(p.getDbColumn())) {
      return;
    }
    this.parent.writeColumnName(p.getDbColumn(), p);
    
    String columnDefn = this.ctx.getColumnDefn(p);
    this.ctx.write(columnDefn);
    if (isIdentity(p)) {
      writeIdentity();
    }
    if ((p.isId()) && (this.ddl.isInlinePrimaryKeyConstraint())) {
      this.ctx.write(" primary key");
    } else if ((!p.isNullable()) || (p.isDDLNotNull())) {
      this.ctx.write(" not null");
    }
    if ((p.isUnique()) && (!p.isId())) {
      this.parent.addUniqueConstraint(createUniqueConstraint(p));
    }
    this.parent.addCheckConstraint(p);
    
    this.ctx.write(",").writeNewLine();
  }
  
  private String createUniqueConstraint(BeanProperty p)
  {
    StringBuilder expr = createUniqueConstraintBuffer(p.getBeanDescriptor().getBaseTable(), p.getDbColumn());
    
    expr.append(p.getDbColumn()).append(")");
    
    return expr.toString();
  }
  
  protected void writeIdentity()
  {
    String identity = this.ddl.getIdentity();
    if ((identity != null) && (identity.length() > 0)) {
      this.ctx.write(" ").write(identity);
    }
  }
  
  protected boolean isIdentity(BeanProperty p)
  {
    if (p.isId()) {
      try
      {
        IdType idType = p.getBeanDescriptor().getIdType();
        if (idType.equals(IdType.IDENTITY))
        {
          int jdbcType = p.getScalarType().getJdbcType();
          if ((jdbcType == 4) || (jdbcType == -5) || (jdbcType == 5)) {
            return true;
          }
        }
      }
      catch (Exception e)
      {
        String msg = "Error determining identity on property " + p.getFullBeanName();
        logger.log(Level.SEVERE, msg, e);
      }
    }
    return false;
  }
}
