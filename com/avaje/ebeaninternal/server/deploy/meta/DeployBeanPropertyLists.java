package com.avaje.ebeaninternal.server.deploy.meta;

import com.avaje.ebean.bean.BeanCollection.ModifyListenMode;
import com.avaje.ebean.validation.factory.Validator;
import com.avaje.ebeaninternal.server.core.CacheOptions;
import com.avaje.ebeaninternal.server.deploy.BeanCascadeInfo;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorMap;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompound;
import com.avaje.ebeaninternal.server.deploy.BeanPropertySimpleCollection;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class DeployBeanPropertyLists
{
  private BeanProperty derivedFirstVersionProp;
  private final BeanDescriptor<?> desc;
  private final LinkedHashMap<String, BeanProperty> propertyMap;
  private final ArrayList<BeanProperty> ids = new ArrayList();
  private final ArrayList<BeanProperty> version = new ArrayList();
  private final ArrayList<BeanProperty> local = new ArrayList();
  private final ArrayList<BeanProperty> manys = new ArrayList();
  private final ArrayList<BeanProperty> nonManys = new ArrayList();
  private final ArrayList<BeanProperty> ones = new ArrayList();
  private final ArrayList<BeanProperty> onesExported = new ArrayList();
  private final ArrayList<BeanProperty> onesImported = new ArrayList();
  private final ArrayList<BeanProperty> embedded = new ArrayList();
  private final ArrayList<BeanProperty> baseScalar = new ArrayList();
  private final ArrayList<BeanPropertyCompound> baseCompound = new ArrayList();
  private final ArrayList<BeanProperty> transients = new ArrayList();
  private final ArrayList<BeanProperty> nonTransients = new ArrayList();
  private final TableJoin[] tableJoins;
  private final BeanPropertyAssocOne<?> unidirectional;
  
  public DeployBeanPropertyLists(BeanDescriptorMap owner, BeanDescriptor<?> desc, DeployBeanDescriptor<?> deploy)
  {
    this.desc = desc;
    
    DeployBeanPropertyAssocOne<?> deployUnidirectional = deploy.getUnidirectional();
    if (deployUnidirectional == null) {
      this.unidirectional = null;
    } else {
      this.unidirectional = new BeanPropertyAssocOne(owner, desc, deployUnidirectional);
    }
    this.propertyMap = new LinkedHashMap();
    
    Iterator<DeployBeanProperty> deployIt = deploy.propertiesAll();
    while (deployIt.hasNext())
    {
      DeployBeanProperty deployProp = (DeployBeanProperty)deployIt.next();
      BeanProperty beanProp = createBeanProperty(owner, deployProp);
      this.propertyMap.put(beanProp.getName(), beanProp);
    }
    Iterator<BeanProperty> it = this.propertyMap.values().iterator();
    
    int order = 0;
    while (it.hasNext())
    {
      BeanProperty prop = (BeanProperty)it.next();
      prop.setDeployOrder(order++);
      allocateToList(prop);
    }
    List<DeployTableJoin> deployTableJoins = deploy.getTableJoins();
    this.tableJoins = new TableJoin[deployTableJoins.size()];
    for (int i = 0; i < deployTableJoins.size(); i++) {
      this.tableJoins[i] = new TableJoin((DeployTableJoin)deployTableJoins.get(i), this.propertyMap);
    }
  }
  
  public BeanPropertyAssocOne<?> getUnidirectional()
  {
    return this.unidirectional;
  }
  
  private void allocateToList(BeanProperty prop)
  {
    if (prop.isTransient())
    {
      this.transients.add(prop);
      return;
    }
    if (prop.isId())
    {
      this.ids.add(prop);
      return;
    }
    this.nonTransients.add(prop);
    if ((this.desc.getInheritInfo() != null) && (prop.isLocal())) {
      this.local.add(prop);
    }
    if ((prop instanceof BeanPropertyAssocMany))
    {
      this.manys.add(prop);
    }
    else
    {
      this.nonManys.add(prop);
      if ((prop instanceof BeanPropertyAssocOne))
      {
        if (prop.isEmbedded())
        {
          this.embedded.add(prop);
        }
        else
        {
          this.ones.add(prop);
          BeanPropertyAssocOne<?> assocOne = (BeanPropertyAssocOne)prop;
          if (assocOne.isOneToOneExported()) {
            this.onesExported.add(prop);
          } else {
            this.onesImported.add(prop);
          }
        }
      }
      else
      {
        if (prop.isVersion())
        {
          this.version.add(prop);
          if (this.derivedFirstVersionProp == null) {
            this.derivedFirstVersionProp = prop;
          }
        }
        if ((prop instanceof BeanPropertyCompound)) {
          this.baseCompound.add((BeanPropertyCompound)prop);
        } else {
          this.baseScalar.add(prop);
        }
      }
    }
  }
  
  public BeanProperty getFirstVersion()
  {
    return this.derivedFirstVersionProp;
  }
  
  public BeanProperty[] getPropertiesWithValidators(boolean recurse)
  {
    ArrayList<BeanProperty> list = new ArrayList();
    Iterator<BeanProperty> it = this.propertyMap.values().iterator();
    while (it.hasNext())
    {
      BeanProperty property = (BeanProperty)it.next();
      if (property.hasValidationRules(recurse)) {
        list.add(property);
      }
    }
    return (BeanProperty[])list.toArray(new BeanProperty[list.size()]);
  }
  
  public Validator[] getBeanValidators()
  {
    return new Validator[0];
  }
  
  public LinkedHashMap<String, BeanProperty> getPropertyMap()
  {
    return this.propertyMap;
  }
  
  public TableJoin[] getTableJoin()
  {
    return this.tableJoins;
  }
  
  public BeanProperty[] getBaseScalar()
  {
    return (BeanProperty[])this.baseScalar.toArray(new BeanProperty[this.baseScalar.size()]);
  }
  
  public BeanPropertyCompound[] getBaseCompound()
  {
    return (BeanPropertyCompound[])this.baseCompound.toArray(new BeanPropertyCompound[this.baseCompound.size()]);
  }
  
  public BeanProperty getNaturalKey()
  {
    String naturalKey = this.desc.getCacheOptions().getNaturalKey();
    if (naturalKey != null) {
      return (BeanProperty)this.propertyMap.get(naturalKey);
    }
    return null;
  }
  
  public BeanProperty[] getId()
  {
    return (BeanProperty[])this.ids.toArray(new BeanProperty[this.ids.size()]);
  }
  
  public BeanProperty[] getNonTransients()
  {
    return (BeanProperty[])this.nonTransients.toArray(new BeanProperty[this.nonTransients.size()]);
  }
  
  public BeanProperty[] getTransients()
  {
    return (BeanProperty[])this.transients.toArray(new BeanProperty[this.transients.size()]);
  }
  
  public BeanProperty[] getVersion()
  {
    return (BeanProperty[])this.version.toArray(new BeanProperty[this.version.size()]);
  }
  
  public BeanProperty[] getLocal()
  {
    return (BeanProperty[])this.local.toArray(new BeanProperty[this.local.size()]);
  }
  
  public BeanPropertyAssocOne<?>[] getEmbedded()
  {
    return (BeanPropertyAssocOne[])this.embedded.toArray(new BeanPropertyAssocOne[this.embedded.size()]);
  }
  
  public BeanPropertyAssocOne<?>[] getOneExported()
  {
    return (BeanPropertyAssocOne[])this.onesExported.toArray(new BeanPropertyAssocOne[this.onesExported.size()]);
  }
  
  public BeanPropertyAssocOne<?>[] getOneImported()
  {
    return (BeanPropertyAssocOne[])this.onesImported.toArray(new BeanPropertyAssocOne[this.onesImported.size()]);
  }
  
  public BeanPropertyAssocOne<?>[] getOnes()
  {
    return (BeanPropertyAssocOne[])this.ones.toArray(new BeanPropertyAssocOne[this.ones.size()]);
  }
  
  public BeanPropertyAssocOne<?>[] getOneExportedSave()
  {
    return getOne(false, Mode.Save);
  }
  
  public BeanPropertyAssocOne<?>[] getOneExportedDelete()
  {
    return getOne(false, Mode.Delete);
  }
  
  public BeanPropertyAssocOne<?>[] getOneImportedSave()
  {
    return getOne(true, Mode.Save);
  }
  
  public BeanPropertyAssocOne<?>[] getOneImportedDelete()
  {
    return getOne(true, Mode.Delete);
  }
  
  public BeanProperty[] getNonMany()
  {
    return (BeanProperty[])this.nonManys.toArray(new BeanProperty[this.nonManys.size()]);
  }
  
  public BeanPropertyAssocMany<?>[] getMany()
  {
    return (BeanPropertyAssocMany[])this.manys.toArray(new BeanPropertyAssocMany[this.manys.size()]);
  }
  
  public BeanPropertyAssocMany<?>[] getManySave()
  {
    return getMany(Mode.Save);
  }
  
  public BeanPropertyAssocMany<?>[] getManyDelete()
  {
    return getMany(Mode.Delete);
  }
  
  public BeanPropertyAssocMany<?>[] getManyToMany()
  {
    return getMany2Many();
  }
  
  private static enum Mode
  {
    Save,  Delete,  Validate;
    
    private Mode() {}
  }
  
  private BeanPropertyAssocOne<?>[] getOne(boolean imported, Mode mode)
  {
    ArrayList<BeanPropertyAssocOne<?>> list = new ArrayList();
    for (int i = 0; i < this.ones.size(); i++)
    {
      BeanPropertyAssocOne<?> prop = (BeanPropertyAssocOne)this.ones.get(i);
      if (imported != prop.isOneToOneExported()) {
        switch (mode)
        {
        case Save: 
          if (prop.getCascadeInfo().isSave()) {
            list.add(prop);
          }
          break;
        case Delete: 
          if (prop.getCascadeInfo().isDelete()) {
            list.add(prop);
          }
          break;
        case Validate: 
          if (prop.getCascadeInfo().isValidate()) {
            list.add(prop);
          }
          break;
        }
      }
    }
    return (BeanPropertyAssocOne[])list.toArray(new BeanPropertyAssocOne[list.size()]);
  }
  
  private BeanPropertyAssocMany<?>[] getMany2Many()
  {
    ArrayList<BeanPropertyAssocMany<?>> list = new ArrayList();
    for (int i = 0; i < this.manys.size(); i++)
    {
      BeanPropertyAssocMany<?> prop = (BeanPropertyAssocMany)this.manys.get(i);
      if (prop.isManyToMany()) {
        list.add(prop);
      }
    }
    return (BeanPropertyAssocMany[])list.toArray(new BeanPropertyAssocMany[list.size()]);
  }
  
  private BeanPropertyAssocMany<?>[] getMany(Mode mode)
  {
    ArrayList<BeanPropertyAssocMany<?>> list = new ArrayList();
    for (int i = 0; i < this.manys.size(); i++)
    {
      BeanPropertyAssocMany<?> prop = (BeanPropertyAssocMany)this.manys.get(i);
      switch (mode)
      {
      case Save: 
        if ((prop.getCascadeInfo().isSave()) || (prop.isManyToMany()) || (BeanCollection.ModifyListenMode.REMOVALS.equals(prop.getModifyListenMode()))) {
          list.add(prop);
        }
        break;
      case Delete: 
        if ((prop.getCascadeInfo().isDelete()) || (BeanCollection.ModifyListenMode.REMOVALS.equals(prop.getModifyListenMode()))) {
          list.add(prop);
        }
        break;
      case Validate: 
        if (prop.getCascadeInfo().isValidate()) {
          list.add(prop);
        }
        break;
      }
    }
    return (BeanPropertyAssocMany[])list.toArray(new BeanPropertyAssocMany[list.size()]);
  }
  
  private BeanProperty createBeanProperty(BeanDescriptorMap owner, DeployBeanProperty deployProp)
  {
    if ((deployProp instanceof DeployBeanPropertyAssocOne)) {
      return new BeanPropertyAssocOne(owner, this.desc, (DeployBeanPropertyAssocOne)deployProp);
    }
    if ((deployProp instanceof DeployBeanPropertySimpleCollection)) {
      return new BeanPropertySimpleCollection(owner, this.desc, (DeployBeanPropertySimpleCollection)deployProp);
    }
    if ((deployProp instanceof DeployBeanPropertyAssocMany)) {
      return new BeanPropertyAssocMany(owner, this.desc, (DeployBeanPropertyAssocMany)deployProp);
    }
    if ((deployProp instanceof DeployBeanPropertyCompound)) {
      return new BeanPropertyCompound(owner, this.desc, (DeployBeanPropertyCompound)deployProp);
    }
    return new BeanProperty(owner, this.desc, deployProp);
  }
}
