package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebeaninternal.server.core.InternString;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.deploy.id.ImportedId;
import com.avaje.ebeaninternal.server.deploy.id.ImportedIdEmbedded;
import com.avaje.ebeaninternal.server.deploy.id.ImportedIdMultiple;
import com.avaje.ebeaninternal.server.deploy.id.ImportedIdSimple;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssoc;
import com.avaje.ebeaninternal.server.el.ElPropertyChainBuilder;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public abstract class BeanPropertyAssoc<T>
  extends BeanProperty
{
  private static final Logger logger = Logger.getLogger(BeanPropertyAssoc.class.getName());
  BeanDescriptor<T> targetDescriptor;
  IdBinder targetIdBinder;
  InheritInfo targetInheritInfo;
  String targetIdProperty;
  final BeanCascadeInfo cascadeInfo;
  final TableJoin tableJoin;
  final Class<T> targetType;
  final BeanTable beanTable;
  final String mappedBy;
  final boolean isOuterJoin;
  String extraWhere;
  boolean saveRecurseSkippable;
  boolean deleteRecurseSkippable;
  
  public BeanPropertyAssoc(BeanDescriptorMap owner, BeanDescriptor<?> descriptor, DeployBeanPropertyAssoc<T> deploy)
  {
    super(owner, descriptor, deploy);
    this.extraWhere = InternString.intern(deploy.getExtraWhere());
    this.isOuterJoin = deploy.isOuterJoin();
    this.beanTable = deploy.getBeanTable();
    this.mappedBy = InternString.intern(deploy.getMappedBy());
    
    this.tableJoin = new TableJoin(deploy.getTableJoin(), null);
    
    this.targetType = deploy.getTargetType();
    this.cascadeInfo = deploy.getCascadeInfo();
  }
  
  public void initialise()
  {
    if (!this.isTransient)
    {
      this.targetDescriptor = this.descriptor.getBeanDescriptor(this.targetType);
      this.targetIdBinder = this.targetDescriptor.getIdBinder();
      this.targetInheritInfo = this.targetDescriptor.getInheritInfo();
      
      this.saveRecurseSkippable = this.targetDescriptor.isSaveRecurseSkippable();
      this.deleteRecurseSkippable = this.targetDescriptor.isDeleteRecurseSkippable();
      
      this.cascadeValidate = this.cascadeInfo.isValidate();
      if (!this.targetIdBinder.isComplexId()) {
        this.targetIdProperty = this.targetIdBinder.getIdProperty();
      }
    }
  }
  
  protected ElPropertyValue createElPropertyValue(String propName, String remainder, ElPropertyChainBuilder chain, boolean propertyDeploy)
  {
    BeanDescriptor<?> embDesc = getTargetDescriptor();
    if (chain == null) {
      chain = new ElPropertyChainBuilder(isEmbedded(), propName);
    }
    chain.add(this);
    if (containsMany()) {
      chain.setContainsMany(true);
    }
    return embDesc.buildElGetValue(remainder, chain, propertyDeploy);
  }
  
  public boolean addJoin(boolean forceOuterJoin, String prefix, DbSqlContext ctx)
  {
    return this.tableJoin.addJoin(forceOuterJoin, prefix, ctx);
  }
  
  public boolean addJoin(boolean forceOuterJoin, String a1, String a2, DbSqlContext ctx)
  {
    return this.tableJoin.addJoin(forceOuterJoin, a1, a2, ctx);
  }
  
  public void addInnerJoin(String a1, String a2, DbSqlContext ctx)
  {
    this.tableJoin.addInnerJoin(a1, a2, ctx);
  }
  
  public boolean isScalar()
  {
    return false;
  }
  
  public String getMappedBy()
  {
    return this.mappedBy;
  }
  
  public String getTargetIdProperty()
  {
    return this.targetIdProperty;
  }
  
  public BeanDescriptor<T> getTargetDescriptor()
  {
    return this.targetDescriptor;
  }
  
  public boolean isSaveRecurseSkippable(Object bean)
  {
    if (!this.saveRecurseSkippable) {
      return false;
    }
    if ((bean instanceof EntityBean)) {
      return !((EntityBean)bean)._ebean_getIntercept().isNewOrDirty();
    }
    return false;
  }
  
  public boolean isSaveRecurseSkippable()
  {
    return this.saveRecurseSkippable;
  }
  
  public boolean isDeleteRecurseSkippable()
  {
    return this.deleteRecurseSkippable;
  }
  
  public boolean hasId(Object bean)
  {
    BeanDescriptor<?> targetDesc = getTargetDescriptor();
    
    BeanProperty[] uids = targetDesc.propertiesId();
    for (int i = 0; i < uids.length; i++)
    {
      Object value = uids[i].getValue(bean);
      if (value == null) {
        return false;
      }
    }
    return true;
  }
  
  public Class<?> getTargetType()
  {
    return this.targetType;
  }
  
  public String getExtraWhere()
  {
    return this.extraWhere;
  }
  
  public boolean isOuterJoin()
  {
    return this.isOuterJoin;
  }
  
  public boolean isUpdateable()
  {
    if (this.tableJoin.columns().length > 0) {
      return this.tableJoin.columns()[0].isUpdateable();
    }
    return true;
  }
  
  public boolean isInsertable()
  {
    if (this.tableJoin.columns().length > 0) {
      return this.tableJoin.columns()[0].isInsertable();
    }
    return true;
  }
  
  public TableJoin getTableJoin()
  {
    return this.tableJoin;
  }
  
  public BeanTable getBeanTable()
  {
    return this.beanTable;
  }
  
  public BeanCascadeInfo getCascadeInfo()
  {
    return this.cascadeInfo;
  }
  
  protected ImportedId createImportedId(BeanPropertyAssoc<?> owner, BeanDescriptor<?> target, TableJoin join)
  {
    BeanProperty[] props = target.propertiesId();
    BeanProperty[] others = target.propertiesBaseScalar();
    if (this.descriptor.isSqlSelectBased())
    {
      String dbColumn = owner.getDbColumn();
      return new ImportedIdSimple(owner, dbColumn, props[0], 0);
    }
    TableJoinColumn[] cols = join.columns();
    if (props.length == 1)
    {
      if (!props[0].isEmbedded())
      {
        if (cols.length != 1)
        {
          String msg = "No Imported Id column for [" + props[0] + "] in table [" + join.getTable() + "]";
          logger.log(Level.SEVERE, msg);
          return null;
        }
        return createImportedScalar(owner, cols[0], props, others);
      }
      BeanPropertyAssocOne<?> embProp = (BeanPropertyAssocOne)props[0];
      BeanProperty[] embBaseProps = embProp.getTargetDescriptor().propertiesBaseScalar();
      ImportedIdSimple[] scalars = createImportedList(owner, cols, embBaseProps, others);
      
      return new ImportedIdEmbedded(owner, embProp, scalars);
    }
    ImportedIdSimple[] scalars = createImportedList(owner, cols, props, others);
    return new ImportedIdMultiple(owner, scalars);
  }
  
  private ImportedIdSimple[] createImportedList(BeanPropertyAssoc<?> owner, TableJoinColumn[] cols, BeanProperty[] props, BeanProperty[] others)
  {
    ArrayList<ImportedIdSimple> list = new ArrayList();
    for (int i = 0; i < cols.length; i++) {
      list.add(createImportedScalar(owner, cols[i], props, others));
    }
    return ImportedIdSimple.sort(list);
  }
  
  private ImportedIdSimple createImportedScalar(BeanPropertyAssoc<?> owner, TableJoinColumn col, BeanProperty[] props, BeanProperty[] others)
  {
    String matchColumn = col.getForeignDbColumn();
    String localColumn = col.getLocalDbColumn();
    for (int j = 0; j < props.length; j++) {
      if (props[j].getDbColumn().equalsIgnoreCase(matchColumn)) {
        return new ImportedIdSimple(owner, localColumn, props[j], j);
      }
    }
    for (int j = 0; j < others.length; j++) {
      if (others[j].getDbColumn().equalsIgnoreCase(matchColumn)) {
        return new ImportedIdSimple(owner, localColumn, others[j], j + props.length);
      }
    }
    String msg = "Error with the Join on [" + getFullBeanName() + "]. Could not find the local match for [" + matchColumn + "] " + " Perhaps an error in a @JoinColumn";
    
    throw new PersistenceException(msg);
  }
}
