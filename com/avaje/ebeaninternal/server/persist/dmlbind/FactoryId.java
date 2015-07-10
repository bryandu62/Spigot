package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;

public class FactoryId
{
  public BindableId createId(BeanDescriptor<?> desc)
  {
    BeanProperty[] uids = desc.propertiesId();
    if (uids.length == 0) {
      return new BindableIdEmpty();
    }
    if (uids.length == 1)
    {
      if (!uids[0].isEmbedded()) {
        return new BindableIdScalar(uids[0]);
      }
      BeanPropertyAssocOne<?> embId = (BeanPropertyAssocOne)uids[0];
      return new BindableIdEmbedded(embId, desc);
    }
    return new BindableIdMap(uids, desc);
  }
}
