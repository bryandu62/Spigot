package com.avaje.ebeaninternal.server.autofetch;

import com.avaje.ebean.bean.NodeUsageCollector;
import com.avaje.ebean.meta.MetaAutoFetchStatistic.NodeUsageStats;
import com.avaje.ebean.text.PathProperties;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.query.SplitName;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

public class StatisticsNodeUsage
  implements Serializable
{
  private static final long serialVersionUID = -1663951463963779547L;
  private static final Logger logger = Logger.getLogger(StatisticsNodeUsage.class.getName());
  private final String monitor = new String();
  private final String path;
  private final boolean queryTuningAddVersion;
  private int profileCount;
  private int profileUsedCount;
  private boolean modified;
  private Set<String> aggregateUsed = new LinkedHashSet();
  
  public StatisticsNodeUsage(String path, boolean queryTuningAddVersion)
  {
    this.path = path;
    this.queryTuningAddVersion = queryTuningAddVersion;
  }
  
  public MetaAutoFetchStatistic.NodeUsageStats createPublicMeta()
  {
    synchronized (this.monitor)
    {
      String[] usedProps = (String[])this.aggregateUsed.toArray(new String[this.aggregateUsed.size()]);
      return new MetaAutoFetchStatistic.NodeUsageStats(this.path, this.profileCount, this.profileUsedCount, usedProps);
    }
  }
  
  public void buildTunedFetch(PathProperties pathProps, BeanDescriptor<?> rootDesc)
  {
    synchronized (this.monitor)
    {
      BeanDescriptor<?> desc = rootDesc;
      if (this.path != null)
      {
        ElPropertyValue elGetValue = rootDesc.getElGetValue(this.path);
        if (elGetValue == null)
        {
          desc = null;
          logger.warning("Autofetch: Can't find join for path[" + this.path + "] for " + rootDesc.getName());
        }
        else
        {
          BeanProperty beanProperty = elGetValue.getBeanProperty();
          if ((beanProperty instanceof BeanPropertyAssoc)) {
            desc = ((BeanPropertyAssoc)beanProperty).getTargetDescriptor();
          }
        }
      }
      for (String propName : this.aggregateUsed)
      {
        BeanProperty beanProp = desc.getBeanPropertyFromPath(propName);
        if (beanProp == null)
        {
          logger.warning("Autofetch: Can't find property[" + propName + "] for " + desc.getName());
        }
        else if ((beanProp instanceof BeanPropertyAssoc))
        {
          BeanPropertyAssoc<?> assocProp = (BeanPropertyAssoc)beanProp;
          String targetIdProp = assocProp.getTargetIdProperty();
          String manyPath = SplitName.add(this.path, assocProp.getName());
          pathProps.addToPath(manyPath, targetIdProp);
        }
        else if ((!beanProp.isLob()) || (beanProp.isFetchEager()))
        {
          pathProps.addToPath(this.path, beanProp.getName());
        }
      }
      if (((this.modified) || (this.queryTuningAddVersion)) && (desc != null))
      {
        BeanProperty[] versionProps = desc.propertiesVersion();
        if (versionProps.length > 0) {
          pathProps.addToPath(this.path, versionProps[0].getName());
        }
      }
    }
  }
  
  public void publish(NodeUsageCollector profile)
  {
    synchronized (this.monitor)
    {
      HashSet<String> used = profile.getUsed();
      
      this.profileCount += 1;
      if (!used.isEmpty())
      {
        this.profileUsedCount += 1;
        this.aggregateUsed.addAll(used);
      }
      if (profile.isModified()) {
        this.modified = true;
      }
    }
  }
  
  public String toString()
  {
    return "path[" + this.path + "] profileCount[" + this.profileCount + "] used[" + this.profileUsedCount + "] props" + this.aggregateUsed;
  }
}
