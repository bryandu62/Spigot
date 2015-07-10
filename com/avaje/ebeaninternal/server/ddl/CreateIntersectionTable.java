package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebean.config.dbplatform.DbDdlSyntax;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import com.avaje.ebeaninternal.server.deploy.TableJoinColumn;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;

public class CreateIntersectionTable
{
  private static final String NEW_LINE = "\n";
  private final DdlGenContext ctx;
  private final BeanPropertyAssocMany<?> manyProp;
  private final TableJoin intersectionTableJoin;
  private final TableJoin tableJoin;
  private StringBuilder sb = new StringBuilder();
  private StringBuilder pkeySb = new StringBuilder();
  private int foreignKeyCount;
  private int maxFkeyLength;
  
  public CreateIntersectionTable(DdlGenContext ctx, BeanPropertyAssocMany<?> manyProp)
  {
    this.ctx = ctx;
    this.manyProp = manyProp;
    this.intersectionTableJoin = manyProp.getIntersectionTableJoin();
    this.tableJoin = manyProp.getTableJoin();
    this.maxFkeyLength = (ctx.getDdlSyntax().getMaxConstraintNameLength() - 3);
  }
  
  public void build()
  {
    String createTable = buildCreateTable();
    this.ctx.addCreateIntersectionTable(createTable);
    
    this.foreignKeyCount = 0;
    buildFkConstraints();
  }
  
  private void buildFkConstraints()
  {
    BeanDescriptor<?> localDesc = this.manyProp.getBeanDescriptor();
    String fk1 = buildFkConstraints(localDesc, this.intersectionTableJoin.columns(), true);
    this.ctx.addIntersectionTableFk(fk1);
    
    BeanDescriptor<?> targetDesc = this.manyProp.getTargetDescriptor();
    String fk2 = buildFkConstraints(targetDesc, this.tableJoin.columns(), false);
    this.ctx.addIntersectionTableFk(fk2);
  }
  
  private String getFkNameSuffix()
  {
    this.foreignKeyCount += 1;
    if (this.foreignKeyCount > 9) {
      return "_" + this.foreignKeyCount;
    }
    return "_0" + this.foreignKeyCount;
  }
  
  private String getFkNameWithSuffix(String fkName)
  {
    if (fkName.length() > this.maxFkeyLength) {
      fkName = fkName.substring(0, this.maxFkeyLength);
    }
    return fkName + getFkNameSuffix();
  }
  
  private String buildFkConstraints(BeanDescriptor<?> desc, TableJoinColumn[] columns, boolean direction)
  {
    StringBuilder fkBuf = new StringBuilder();
    
    String fkName = "fk_" + this.intersectionTableJoin.getTable() + "_" + desc.getBaseTable();
    
    fkName = getFkNameWithSuffix(fkName);
    
    fkBuf.append("alter table ");
    fkBuf.append(this.intersectionTableJoin.getTable());
    fkBuf.append(" add constraint ").append(fkName);
    
    fkBuf.append(" foreign key (");
    for (int i = 0; i < columns.length; i++)
    {
      if (i > 0) {
        fkBuf.append(", ");
      }
      String col = direction ? columns[i].getForeignDbColumn() : columns[i].getLocalDbColumn();
      fkBuf.append(col);
    }
    fkBuf.append(") references ").append(desc.getBaseTable()).append(" (");
    for (int i = 0; i < columns.length; i++)
    {
      if (i > 0) {
        fkBuf.append(", ");
      }
      String col = !direction ? columns[i].getForeignDbColumn() : columns[i].getLocalDbColumn();
      fkBuf.append(col);
    }
    fkBuf.append(")");
    
    String fkeySuffix = this.ctx.getDdlSyntax().getForeignKeySuffix();
    if (fkeySuffix != null)
    {
      fkBuf.append(" ");
      fkBuf.append(fkeySuffix);
    }
    fkBuf.append(";").append("\n");
    
    return fkBuf.toString();
  }
  
  private String buildCreateTable()
  {
    BeanDescriptor<?> localDesc = this.manyProp.getBeanDescriptor();
    BeanDescriptor<?> targetDesc = this.manyProp.getTargetDescriptor();
    
    this.sb.append("create table ");
    this.sb.append(this.intersectionTableJoin.getTable());
    this.sb.append(" (").append("\n");
    
    TableJoinColumn[] columns = this.intersectionTableJoin.columns();
    for (int i = 0; i < columns.length; i++) {
      addColumn(localDesc, columns[i].getForeignDbColumn(), columns[i].getLocalDbColumn());
    }
    TableJoinColumn[] otherColumns = this.tableJoin.columns();
    for (int i = 0; i < otherColumns.length; i++) {
      addColumn(targetDesc, otherColumns[i].getLocalDbColumn(), otherColumns[i].getForeignDbColumn());
    }
    this.sb.append("  constraint pk_").append(this.intersectionTableJoin.getTable());
    this.sb.append(" primary key (").append(this.pkeySb.toString().substring(2));
    this.sb.append("))").append("\n").append(";").append("\n");
    
    return this.sb.toString();
  }
  
  private void addColumn(BeanDescriptor<?> desc, String column, String findPropColumn)
  {
    this.pkeySb.append(", ");
    this.pkeySb.append(column);
    
    writeColumn(column);
    
    BeanProperty p = desc.getIdBinder().findBeanProperty(findPropColumn);
    if (p == null) {
      throw new RuntimeException("Could not find id property for " + findPropColumn);
    }
    String columnDefn = this.ctx.getColumnDefn(p);
    this.sb.append(columnDefn);
    this.sb.append(" not null");
    this.sb.append(",").append("\n");
  }
  
  private void writeColumn(String columnName)
  {
    this.sb.append("  ").append(this.ctx.pad(columnName, 30)).append(" ");
  }
}
