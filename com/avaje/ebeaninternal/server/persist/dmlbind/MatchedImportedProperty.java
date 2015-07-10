package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.id.ImportedId;

class MatchedImportedProperty
{
  private final BeanPropertyAssocOne<?> assocOne;
  private final BeanProperty foreignProp;
  private final BeanProperty localProp;
  
  protected MatchedImportedProperty(BeanPropertyAssocOne<?> assocOne, BeanProperty foreignProp, BeanProperty localProp)
  {
    this.assocOne = assocOne;
    this.foreignProp = foreignProp;
    this.localProp = localProp;
  }
  
  protected void populate(Object sourceBean, Object destBean)
  {
    Object assocBean = this.assocOne.getValue(sourceBean);
    if (assocBean == null)
    {
      String msg = "The assoc bean for " + this.assocOne + " is null?";
      throw new NullPointerException(msg);
    }
    Object value = this.foreignProp.getValue(assocBean);
    this.localProp.setValue(destBean, value);
  }
  
  protected static MatchedImportedProperty[] build(BeanProperty[] props, BeanDescriptor<?> desc)
  {
    MatchedImportedProperty[] matches = new MatchedImportedProperty[props.length];
    for (int i = 0; i < props.length; i++)
    {
      matches[i] = findMatch(props[i], desc);
      if (matches[i] == null) {
        return null;
      }
    }
    return matches;
  }
  
  private static MatchedImportedProperty findMatch(BeanProperty prop, BeanDescriptor<?> desc)
  {
    String dbColumn = prop.getDbColumn();
    
    BeanPropertyAssocOne<?>[] assocOnes = desc.propertiesOne();
    for (int i = 0; i < assocOnes.length; i++) {
      if (assocOnes[i].isImportedPrimaryKey())
      {
        BeanProperty foreignMatch = assocOnes[i].getImportedId().findMatchImport(dbColumn);
        if (foreignMatch != null) {
          return new MatchedImportedProperty(assocOnes[i], foreignMatch, prop);
        }
      }
    }
    return null;
  }
}
