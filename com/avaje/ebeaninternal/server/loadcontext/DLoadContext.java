package com.avaje.ebeaninternal.server.loadcontext;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.ObjectGraphOrigin;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.LoadContext;
import com.avaje.ebeaninternal.api.LoadSecondaryQuery;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DLoadContext
  implements LoadContext
{
  private final SpiEbeanServer ebeanServer;
  private final BeanDescriptor<?> rootDescriptor;
  private final Map<String, DLoadBeanContext> beanMap = new HashMap();
  private final Map<String, DLoadManyContext> manyMap = new HashMap();
  private final DLoadBeanContext rootBeanContext;
  private final Boolean readOnly;
  private final boolean excludeBeanCache;
  private final int defaultBatchSize;
  private final String relativePath;
  private final ObjectGraphOrigin origin;
  private final boolean useAutofetchManager;
  private final boolean hardRefs;
  private final Map<String, ObjectGraphNode> nodePathMap = new HashMap();
  private PersistenceContext persistenceContext;
  private List<OrmQueryProperties> secQuery;
  
  public DLoadContext(SpiEbeanServer ebeanServer, BeanDescriptor<?> rootDescriptor, Boolean readOnly, SpiQuery<?> query)
  {
    this(ebeanServer, rootDescriptor, readOnly, Boolean.FALSE.equals(query.isUseBeanCache()), query.getParentNode(), query.getAutoFetchManager() != null);
  }
  
  public DLoadContext(SpiEbeanServer ebeanServer, BeanDescriptor<?> rootDescriptor, Boolean readOnly, boolean excludeBeanCache, ObjectGraphNode parentNode, boolean useAutofetchManager)
  {
    this.ebeanServer = ebeanServer;
    this.hardRefs = GlobalProperties.getBoolean("ebean.hardrefs", false);
    this.defaultBatchSize = ebeanServer.getLazyLoadBatchSize();
    this.rootDescriptor = rootDescriptor;
    this.rootBeanContext = new DLoadBeanContext(this, rootDescriptor, null, this.defaultBatchSize, null, createBeanLoadList());
    this.readOnly = readOnly;
    this.excludeBeanCache = excludeBeanCache;
    this.useAutofetchManager = useAutofetchManager;
    if (parentNode != null)
    {
      this.origin = parentNode.getOriginQueryPoint();
      this.relativePath = parentNode.getPath();
    }
    else
    {
      this.origin = null;
      this.relativePath = null;
    }
  }
  
  protected boolean isExcludeBeanCache()
  {
    return this.excludeBeanCache;
  }
  
  public int getSecondaryQueriesMinBatchSize(OrmQueryRequest<?> parentRequest, int defaultQueryBatch)
  {
    if (this.secQuery == null) {
      return -1;
    }
    int maxBatch = 0;
    for (int i = 0; i < this.secQuery.size(); i++)
    {
      int batchSize = ((OrmQueryProperties)this.secQuery.get(i)).getQueryFetchBatch();
      if (batchSize == 0) {
        batchSize = defaultQueryBatch;
      }
      maxBatch = Math.max(maxBatch, batchSize);
    }
    return maxBatch;
  }
  
  public void executeSecondaryQueries(OrmQueryRequest<?> parentRequest, int defaultQueryBatch)
  {
    if (this.secQuery != null) {
      for (int i = 0; i < this.secQuery.size(); i++)
      {
        OrmQueryProperties properties = (OrmQueryProperties)this.secQuery.get(i);
        
        int batchSize = properties.getQueryFetchBatch();
        if (batchSize == 0) {
          batchSize = defaultQueryBatch;
        }
        LoadSecondaryQuery load = getLoadSecondaryQuery(properties.getPath());
        load.loadSecondaryQuery(parentRequest, batchSize, properties.isQueryFetchAll());
      }
    }
  }
  
  private LoadSecondaryQuery getLoadSecondaryQuery(String path)
  {
    LoadSecondaryQuery beanLoad = (LoadSecondaryQuery)this.beanMap.get(path);
    if (beanLoad == null) {
      beanLoad = (LoadSecondaryQuery)this.manyMap.get(path);
    }
    return beanLoad;
  }
  
  public void registerSecondaryQueries(SpiQuery<?> query)
  {
    this.secQuery = query.removeQueryJoins();
    if (this.secQuery != null) {
      for (int i = 0; i < this.secQuery.size(); i++)
      {
        OrmQueryProperties props = (OrmQueryProperties)this.secQuery.get(i);
        registerSecondaryQuery(props);
      }
    }
    List<OrmQueryProperties> lazyQueries = query.removeLazyJoins();
    if (lazyQueries != null) {
      for (int i = 0; i < lazyQueries.size(); i++)
      {
        OrmQueryProperties lazyProps = (OrmQueryProperties)lazyQueries.get(i);
        registerSecondaryQuery(lazyProps);
      }
    }
  }
  
  private void registerSecondaryQuery(OrmQueryProperties props)
  {
    String propName = props.getPath();
    ElPropertyValue elGetValue = this.rootDescriptor.getElGetValue(propName);
    
    boolean many = elGetValue.getBeanProperty().containsMany();
    registerSecondaryNode(many, props);
  }
  
  public ObjectGraphNode getObjectGraphNode(String path)
  {
    ObjectGraphNode node = (ObjectGraphNode)this.nodePathMap.get(path);
    if (node == null)
    {
      node = createObjectGraphNode(path);
      this.nodePathMap.put(path, node);
    }
    return node;
  }
  
  private ObjectGraphNode createObjectGraphNode(String path)
  {
    if (this.relativePath != null) {
      if (path == null) {
        path = this.relativePath;
      } else {
        path = this.relativePath + "." + path;
      }
    }
    return new ObjectGraphNode(this.origin, path);
  }
  
  public boolean isUseAutofetchManager()
  {
    return this.useAutofetchManager;
  }
  
  public String getRelativePath()
  {
    return this.relativePath;
  }
  
  protected SpiEbeanServer getEbeanServer()
  {
    return this.ebeanServer;
  }
  
  protected Boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.persistenceContext;
  }
  
  public void setPersistenceContext(PersistenceContext persistenceContext)
  {
    this.persistenceContext = persistenceContext;
  }
  
  public void register(String path, EntityBeanIntercept ebi)
  {
    getBeanContext(path).register(ebi);
  }
  
  public void register(String path, BeanCollection<?> bc)
  {
    getManyContext(path).register(bc);
  }
  
  private DLoadBeanContext getBeanContext(String path)
  {
    if (path == null) {
      return this.rootBeanContext;
    }
    DLoadBeanContext beanContext = (DLoadBeanContext)this.beanMap.get(path);
    if (beanContext == null)
    {
      beanContext = createBeanContext(path, this.defaultBatchSize, null);
      this.beanMap.put(path, beanContext);
    }
    return beanContext;
  }
  
  private void registerSecondaryNode(boolean many, OrmQueryProperties props)
  {
    String path = props.getPath();
    int lazyJoinBatch = props.getLazyFetchBatch();
    int batchSize = lazyJoinBatch > 0 ? lazyJoinBatch : this.defaultBatchSize;
    if (many)
    {
      DLoadManyContext manyContext = createManyContext(path, batchSize, props);
      this.manyMap.put(path, manyContext);
    }
    else
    {
      DLoadBeanContext beanContext = createBeanContext(path, batchSize, props);
      this.beanMap.put(path, beanContext);
    }
  }
  
  private DLoadManyContext getManyContext(String path)
  {
    if (path == null) {
      throw new RuntimeException("path is null?");
    }
    DLoadManyContext ctx = (DLoadManyContext)this.manyMap.get(path);
    if (ctx == null)
    {
      ctx = createManyContext(path, this.defaultBatchSize, null);
      this.manyMap.put(path, ctx);
    }
    return ctx;
  }
  
  private DLoadManyContext createManyContext(String path, int batchSize, OrmQueryProperties queryProps)
  {
    BeanPropertyAssocMany<?> p = (BeanPropertyAssocMany)getBeanProperty(this.rootDescriptor, path);
    
    return new DLoadManyContext(this, p, path, batchSize, queryProps, createBeanCollectionLoadList());
  }
  
  private DLoadList<EntityBeanIntercept> createBeanLoadList()
  {
    if (this.hardRefs) {
      return new DLoadHardList();
    }
    return new DLoadWeakList();
  }
  
  private DLoadList<BeanCollection<?>> createBeanCollectionLoadList()
  {
    if (this.hardRefs) {
      return new DLoadHardList();
    }
    return new DLoadWeakList();
  }
  
  private DLoadBeanContext createBeanContext(String path, int batchSize, OrmQueryProperties queryProps)
  {
    BeanPropertyAssoc<?> p = (BeanPropertyAssoc)getBeanProperty(this.rootDescriptor, path);
    BeanDescriptor<?> targetDescriptor = p.getTargetDescriptor();
    
    return new DLoadBeanContext(this, targetDescriptor, path, batchSize, queryProps, createBeanLoadList());
  }
  
  private BeanProperty getBeanProperty(BeanDescriptor<?> desc, String path)
  {
    return desc.getBeanPropertyFromPath(path);
  }
}
