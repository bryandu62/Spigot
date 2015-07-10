package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.OrderBy;
import com.avaje.ebean.OrderBy.Property;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import java.util.List;

public class CQueryOrderBy
{
  private final BeanDescriptor<?> desc;
  private final SpiQuery<?> query;
  
  public static String parse(BeanDescriptor<?> desc, SpiQuery<?> query)
  {
    return new CQueryOrderBy(desc, query).parseInternal();
  }
  
  private CQueryOrderBy(BeanDescriptor<?> desc, SpiQuery<?> query)
  {
    this.desc = desc;
    this.query = query;
  }
  
  private String parseInternal()
  {
    OrderBy<?> orderBy = this.query.getOrderBy();
    if (orderBy == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    
    List<OrderBy.Property> properties = orderBy.getProperties();
    if (properties.isEmpty()) {
      return null;
    }
    for (int i = 0; i < properties.size(); i++)
    {
      if (i > 0) {
        sb.append(", ");
      }
      OrderBy.Property p = (OrderBy.Property)properties.get(i);
      String expression = parseProperty(p);
      sb.append(expression);
    }
    return sb.toString();
  }
  
  private String parseProperty(OrderBy.Property p)
  {
    String propName = p.getProperty();
    ElPropertyValue el = this.desc.getElGetValue(propName);
    if (el == null) {
      return p.toStringFormat();
    }
    BeanProperty beanProperty = el.getBeanProperty();
    if ((beanProperty instanceof BeanPropertyAssoc))
    {
      BeanPropertyAssoc<?> ap = (BeanPropertyAssoc)beanProperty;
      IdBinder idBinder = ap.getTargetDescriptor().getIdBinder();
      return idBinder.getOrderBy(el.getElName(), p.isAscending());
    }
    return p.toStringFormat();
  }
}
