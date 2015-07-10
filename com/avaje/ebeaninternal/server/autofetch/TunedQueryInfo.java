package com.avaje.ebeaninternal.server.autofetch;

import com.avaje.ebean.bean.ObjectGraphOrigin;
import com.avaje.ebean.meta.MetaAutoFetchTunedQueryInfo;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import java.io.Serializable;

public class TunedQueryInfo
  implements Serializable
{
  private static final long serialVersionUID = 7381493228797997282L;
  private final ObjectGraphOrigin origin;
  private OrmQueryDetail tunedDetail;
  private int profileCount;
  private Long lastTuneTime = Long.valueOf(0L);
  private final String rateMonitor = new String();
  private transient int tunedCount;
  private transient int rateTotal;
  private transient int rateHits;
  private transient double lastRate;
  
  public TunedQueryInfo(ObjectGraphOrigin queryPoint, OrmQueryDetail tunedDetail, int profileCount)
  {
    this.origin = queryPoint;
    this.tunedDetail = tunedDetail;
    this.profileCount = profileCount;
  }
  
  public boolean isPercentageProfile(double rate)
  {
    synchronized (this.rateMonitor)
    {
      if (this.lastRate != rate)
      {
        this.lastRate = rate;
        this.rateTotal = 0;
        this.rateHits = 0;
      }
      this.rateTotal += 1;
      if (rate > this.rateHits / this.rateTotal)
      {
        this.rateHits += 1;
        return true;
      }
      return false;
    }
  }
  
  public MetaAutoFetchTunedQueryInfo createPublicMeta()
  {
    return new MetaAutoFetchTunedQueryInfo(this.origin, this.tunedDetail.toString(), this.profileCount, this.tunedCount, this.lastTuneTime.longValue());
  }
  
  public void setProfileCount(int profileCount)
  {
    this.profileCount = profileCount;
  }
  
  public void setTunedDetail(OrmQueryDetail tunedDetail)
  {
    this.tunedDetail = tunedDetail;
    this.lastTuneTime = Long.valueOf(System.currentTimeMillis());
  }
  
  public boolean isSame(OrmQueryDetail newQueryDetail)
  {
    if (this.tunedDetail == null) {
      return false;
    }
    return this.tunedDetail.isAutoFetchEqual(newQueryDetail);
  }
  
  public boolean autoFetchTune(SpiQuery<?> query)
  {
    if (this.tunedDetail == null) {
      return false;
    }
    boolean tuned = false;
    if (query.isDetailEmpty())
    {
      tuned = true;
      
      query.setDetail(this.tunedDetail.copy());
    }
    else
    {
      tuned = query.tuneFetchProperties(this.tunedDetail);
    }
    if (tuned)
    {
      query.setAutoFetchTuned(true);
      
      this.tunedCount += 1;
    }
    return tuned;
  }
  
  public Long getLastTuneTime()
  {
    return this.lastTuneTime;
  }
  
  public int getTunedCount()
  {
    return this.tunedCount;
  }
  
  public int getProfileCount()
  {
    return this.profileCount;
  }
  
  public OrmQueryDetail getTunedDetail()
  {
    return this.tunedDetail;
  }
  
  public ObjectGraphOrigin getOrigin()
  {
    return this.origin;
  }
  
  public String getLogOutput(OrmQueryDetail newQueryDetail)
  {
    boolean changed = newQueryDetail != null;
    
    StringBuilder sb = new StringBuilder(150);
    sb.append(changed ? "\"Changed\"," : "\"New\",");
    sb.append("\"").append(this.origin.getBeanType()).append("\",");
    sb.append("\"").append(this.origin.getKey()).append("\",");
    if (changed)
    {
      sb.append("\"to: ").append(newQueryDetail.toString()).append("\",");
      sb.append("\"from: ").append(this.tunedDetail.toString()).append("\",");
    }
    else
    {
      sb.append("\"to: ").append(this.tunedDetail.toString()).append("\",");
      sb.append("\"\",");
    }
    sb.append("\"").append(this.origin.getFirstStackElement()).append("\"");
    
    return sb.toString();
  }
  
  public String toString()
  {
    return this.origin.getBeanType() + " " + this.origin.getKey() + " " + this.tunedDetail;
  }
}
