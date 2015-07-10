package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.LikeType;
import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;

class LikeExpression
  extends AbstractExpression
  implements LuceneAwareExpression
{
  private static final long serialVersionUID = -5398151809111172380L;
  private final String val;
  private final boolean caseInsensitive;
  private final LikeType type;
  
  LikeExpression(FilterExprPath pathPrefix, String propertyName, String value, boolean caseInsensitive, LikeType type)
  {
    super(pathPrefix, propertyName);
    this.caseInsensitive = caseInsensitive;
    this.type = type;
    this.val = value;
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    ElPropertyValue prop = getElProp(request);
    if ((prop != null) && (prop.isDbEncrypted()))
    {
      String encryptKey = prop.getBeanProperty().getEncryptKey().getStringValue();
      request.addBindValue(encryptKey);
    }
    String bindValue = getValue(this.val, this.caseInsensitive, this.type);
    request.addBindValue(bindValue);
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    String propertyName = getPropertyName();
    String pname = propertyName;
    
    ElPropertyValue prop = getElProp(request);
    if ((prop != null) && (prop.isDbEncrypted())) {
      pname = prop.getBeanProperty().getDecryptProperty(propertyName);
    }
    if (this.caseInsensitive) {
      request.append("lower(").append(pname).append(")");
    } else {
      request.append(pname);
    }
    if (this.type.equals(LikeType.EQUAL_TO)) {
      request.append(" = ? ");
    } else {
      request.append(" like ? ");
    }
  }
  
  public int queryAutoFetchHash()
  {
    int hc = LikeExpression.class.getName().hashCode();
    hc = hc * 31 + (this.caseInsensitive ? 0 : 1);
    hc = hc * 31 + this.propName.hashCode();
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    return queryAutoFetchHash();
  }
  
  public int queryBindHash()
  {
    return this.val.hashCode();
  }
  
  private static String getValue(String value, boolean caseInsensitive, LikeType type)
  {
    if (caseInsensitive) {
      value = value.toLowerCase();
    }
    switch (type)
    {
    case RAW: 
      return value;
    case STARTS_WITH: 
      return value + "%";
    case ENDS_WITH: 
      return "%" + value;
    case CONTAINS: 
      return "%" + value + "%";
    case EQUAL_TO: 
      return value;
    }
    throw new RuntimeException("LikeType " + type + " missed?");
  }
}
