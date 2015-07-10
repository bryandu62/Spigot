package com.avaje.ebeaninternal.server.deploy.meta;

import com.avaje.ebean.bean.BeanCollection.ModifyListenMode;
import com.avaje.ebeaninternal.server.deploy.ManyType;
import com.avaje.ebeaninternal.server.deploy.TableJoin;

public class DeployBeanPropertyAssocMany<T>
  extends DeployBeanPropertyAssoc<T>
{
  BeanCollection.ModifyListenMode modifyListenMode = BeanCollection.ModifyListenMode.NONE;
  boolean manyToMany;
  boolean unidirectional;
  DeployTableJoin intersectionJoin;
  DeployTableJoin inverseJoin;
  String fetchOrderBy;
  String mapKey;
  ManyType manyType;
  
  public DeployBeanPropertyAssocMany(DeployBeanDescriptor<?> desc, Class<T> targetType, ManyType manyType)
  {
    super(desc, targetType);
    this.manyType = manyType;
  }
  
  public void setTargetType(Class<?> cls)
  {
    this.targetType = cls;
  }
  
  public ManyType getManyType()
  {
    return this.manyType;
  }
  
  public boolean isManyToMany()
  {
    return this.manyToMany;
  }
  
  public void setManyToMany(boolean isManyToMany)
  {
    this.manyToMany = isManyToMany;
  }
  
  public BeanCollection.ModifyListenMode getModifyListenMode()
  {
    return this.modifyListenMode;
  }
  
  public void setModifyListenMode(BeanCollection.ModifyListenMode modifyListenMode)
  {
    this.modifyListenMode = modifyListenMode;
  }
  
  public boolean isUnidirectional()
  {
    return this.unidirectional;
  }
  
  public void setUnidirectional(boolean unidirectional)
  {
    this.unidirectional = unidirectional;
  }
  
  public TableJoin createIntersectionTableJoin()
  {
    if (this.intersectionJoin != null) {
      return new TableJoin(this.intersectionJoin, null);
    }
    return null;
  }
  
  public TableJoin createInverseTableJoin()
  {
    if (this.inverseJoin != null) {
      return new TableJoin(this.inverseJoin, null);
    }
    return null;
  }
  
  public DeployTableJoin getIntersectionJoin()
  {
    return this.intersectionJoin;
  }
  
  public DeployTableJoin getInverseJoin()
  {
    return this.inverseJoin;
  }
  
  public void setIntersectionJoin(DeployTableJoin intersectionJoin)
  {
    this.intersectionJoin = intersectionJoin;
  }
  
  public void setInverseJoin(DeployTableJoin inverseJoin)
  {
    this.inverseJoin = inverseJoin;
  }
  
  public String getFetchOrderBy()
  {
    return this.fetchOrderBy;
  }
  
  public String getMapKey()
  {
    return this.mapKey;
  }
  
  public void setMapKey(String mapKey)
  {
    if ((mapKey != null) && (mapKey.length() > 0)) {
      this.mapKey = mapKey;
    }
  }
  
  public void setFetchOrderBy(String orderBy)
  {
    if ((orderBy != null) && (orderBy.length() > 0)) {
      this.fetchOrderBy = orderBy;
    }
  }
}
