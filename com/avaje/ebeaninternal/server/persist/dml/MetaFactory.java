package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.DbEncrypt;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.persist.dmlbind.Bindable;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableId;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableList;
import com.avaje.ebeaninternal.server.persist.dmlbind.BindableUnidirectional;
import com.avaje.ebeaninternal.server.persist.dmlbind.FactoryAssocOnes;
import com.avaje.ebeaninternal.server.persist.dmlbind.FactoryBaseProperties;
import com.avaje.ebeaninternal.server.persist.dmlbind.FactoryEmbedded;
import com.avaje.ebeaninternal.server.persist.dmlbind.FactoryId;
import com.avaje.ebeaninternal.server.persist.dmlbind.FactoryVersion;
import java.util.ArrayList;
import java.util.List;

public class MetaFactory
{
  private final FactoryBaseProperties baseFact;
  private final FactoryEmbedded embeddedFact;
  private final FactoryVersion versionFact = new FactoryVersion();
  private final FactoryAssocOnes assocOneFact = new FactoryAssocOnes();
  private final FactoryId idFact = new FactoryId();
  private static final boolean includeLobs = true;
  private final DatabasePlatform dbPlatform;
  private final boolean emptyStringAsNull;
  
  public MetaFactory(DatabasePlatform dbPlatform)
  {
    this.dbPlatform = dbPlatform;
    this.emptyStringAsNull = dbPlatform.isTreatEmptyStringsAsNull();
    
    DbEncrypt dbEncrypt = dbPlatform.getDbEncrypt();
    boolean bindEncryptDataFirst = dbEncrypt == null ? true : dbEncrypt.isBindEncryptDataFirst();
    
    this.baseFact = new FactoryBaseProperties(bindEncryptDataFirst);
    this.embeddedFact = new FactoryEmbedded(bindEncryptDataFirst);
  }
  
  public UpdateMeta createUpdate(BeanDescriptor<?> desc)
  {
    List<Bindable> setList = new ArrayList();
    
    this.baseFact.create(setList, desc, DmlMode.UPDATE, true);
    this.embeddedFact.create(setList, desc, DmlMode.UPDATE, true);
    this.assocOneFact.create(setList, desc, DmlMode.UPDATE);
    
    BindableId id = this.idFact.createId(desc);
    
    Bindable ver = this.versionFact.create(desc);
    
    List<Bindable> allList = new ArrayList();
    
    this.baseFact.create(allList, desc, DmlMode.WHERE, false);
    this.embeddedFact.create(allList, desc, DmlMode.WHERE, false);
    this.assocOneFact.create(allList, desc, DmlMode.WHERE);
    
    Bindable setBindable = new BindableList(setList);
    Bindable allBindable = new BindableList(allList);
    
    return new UpdateMeta(this.emptyStringAsNull, desc, setBindable, id, ver, allBindable);
  }
  
  public DeleteMeta createDelete(BeanDescriptor<?> desc)
  {
    BindableId id = this.idFact.createId(desc);
    
    Bindable ver = this.versionFact.create(desc);
    
    List<Bindable> allList = new ArrayList();
    
    this.baseFact.create(allList, desc, DmlMode.WHERE, false);
    this.embeddedFact.create(allList, desc, DmlMode.WHERE, false);
    this.assocOneFact.create(allList, desc, DmlMode.WHERE);
    
    Bindable allBindable = new BindableList(allList);
    
    return new DeleteMeta(this.emptyStringAsNull, desc, id, ver, allBindable);
  }
  
  public InsertMeta createInsert(BeanDescriptor<?> desc)
  {
    BindableId id = this.idFact.createId(desc);
    
    List<Bindable> allList = new ArrayList();
    
    this.baseFact.create(allList, desc, DmlMode.INSERT, true);
    this.embeddedFact.create(allList, desc, DmlMode.INSERT, true);
    this.assocOneFact.create(allList, desc, DmlMode.INSERT);
    
    Bindable allBindable = new BindableList(allList);
    
    BeanPropertyAssocOne<?> unidirectional = desc.getUnidirectional();
    Bindable shadowFkey;
    Bindable shadowFkey;
    if (unidirectional == null) {
      shadowFkey = null;
    } else {
      shadowFkey = new BindableUnidirectional(desc, unidirectional);
    }
    return new InsertMeta(this.dbPlatform, desc, shadowFkey, id, allBindable);
  }
}
