package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;

public class SimpleExpression
  extends AbstractExpression
  implements LuceneAwareExpression
{
  private static final long serialVersionUID = -382881395755603790L;
  private final Op type;
  private final Object value;
  
  static enum Op
  {
    EQ(" = ? ", " = "),  NOT_EQ(" <> ? ", " <> "),  LT(" < ? ", " < "),  LT_EQ(" <= ? ", " <= "),  GT(" > ? ", " > "),  GT_EQ(" >= ? ", " >= ");
    
    String exp;
    String shortDesc;
    
    private Op(String exp, String shortDesc)
    {
      this.exp = exp;
      this.shortDesc = shortDesc;
    }
    
    public String bind()
    {
      return this.exp;
    }
    
    public String shortDesc()
    {
      return this.shortDesc;
    }
  }
  
  public SimpleExpression(FilterExprPath pathPrefix, String propertyName, Op type, Object value)
  {
    super(pathPrefix, propertyName);
    this.type = type;
    this.value = value;
  }
  
  public boolean isOpEquals()
  {
    return Op.EQ.equals(this.type);
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    ElPropertyValue prop = getElProp(request);
    if (prop != null)
    {
      if (prop.isAssocId())
      {
        Object[] ids = prop.getAssocOneIdValues(this.value);
        if (ids != null) {
          for (int i = 0; i < ids.length; i++) {
            request.addBindValue(ids[i]);
          }
        }
        return;
      }
      if (prop.isDbEncrypted())
      {
        String encryptKey = prop.getBeanProperty().getEncryptKey().getStringValue();
        request.addBindValue(encryptKey);
      }
      else if (!prop.isLocalEncrypted()) {}
    }
    request.addBindValue(this.value);
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    String propertyName = getPropertyName();
    
    ElPropertyValue prop = getElProp(request);
    if (prop != null)
    {
      if (prop.isAssocId())
      {
        request.append(prop.getAssocOneIdExpr(propertyName, this.type.bind()));
        return;
      }
      if (prop.isDbEncrypted())
      {
        String dsql = prop.getBeanProperty().getDecryptSql();
        request.append(dsql).append(this.type.bind());
        return;
      }
    }
    request.append(propertyName).append(this.type.bind());
  }
  
  public int queryAutoFetchHash()
  {
    int hc = SimpleExpression.class.getName().hashCode();
    hc = hc * 31 + this.propName.hashCode();
    hc = hc * 31 + this.type.name().hashCode();
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    return queryAutoFetchHash();
  }
  
  public int queryBindHash()
  {
    return this.value.hashCode();
  }
  
  public Object getValue()
  {
    return this.value;
  }
}
