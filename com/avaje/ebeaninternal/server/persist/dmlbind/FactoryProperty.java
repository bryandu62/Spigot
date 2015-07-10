package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.generatedproperty.GeneratedProperty;
import com.avaje.ebeaninternal.server.persist.dml.DmlMode;

public class FactoryProperty
{
  private final boolean bindEncryptDataFirst;
  
  public FactoryProperty(boolean bindEncryptDataFirst)
  {
    this.bindEncryptDataFirst = bindEncryptDataFirst;
  }
  
  public Bindable create(BeanProperty prop, DmlMode mode, boolean withLobs)
  {
    if ((DmlMode.INSERT.equals(mode)) && (!prop.isDbInsertable())) {
      return null;
    }
    if ((DmlMode.UPDATE.equals(mode)) && (!prop.isDbUpdatable())) {
      return null;
    }
    if (prop.isLob())
    {
      if ((DmlMode.WHERE.equals(mode)) || (!withLobs)) {
        return null;
      }
      return prop.isDbEncrypted() ? new BindableEncryptedProperty(prop, this.bindEncryptDataFirst) : new BindableProperty(prop);
    }
    GeneratedProperty gen = prop.getGeneratedProperty();
    if (gen != null)
    {
      if (DmlMode.INSERT.equals(mode))
      {
        if (gen.includeInInsert()) {
          return new BindablePropertyInsertGenerated(prop, gen);
        }
        return null;
      }
      if (DmlMode.UPDATE.equals(mode))
      {
        if (gen.includeInUpdate()) {
          return new BindablePropertyUpdateGenerated(prop, gen);
        }
        return null;
      }
    }
    return prop.isDbEncrypted() ? new BindableEncryptedProperty(prop, this.bindEncryptDataFirst) : new BindableProperty(prop);
  }
}
