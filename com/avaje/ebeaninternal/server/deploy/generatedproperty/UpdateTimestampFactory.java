package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.PersistenceException;

public class UpdateTimestampFactory
{
  final GeneratedUpdateTimestamp timestamp = new GeneratedUpdateTimestamp();
  final GeneratedUpdateDate utilDate = new GeneratedUpdateDate();
  final GeneratedUpdateLong longTime = new GeneratedUpdateLong();
  
  public void setUpdateTimestamp(DeployBeanProperty property)
  {
    property.setGeneratedProperty(createUpdateTimestamp(property));
  }
  
  private GeneratedProperty createUpdateTimestamp(DeployBeanProperty property)
  {
    Class<?> propType = property.getPropertyType();
    if (propType.equals(Timestamp.class)) {
      return this.timestamp;
    }
    if (propType.equals(Date.class)) {
      return this.utilDate;
    }
    if ((propType.equals(Long.class)) || (propType.equals(Long.TYPE))) {
      return this.longTime;
    }
    String msg = "Generated update Timestamp not supported on " + propType.getName();
    throw new PersistenceException(msg);
  }
}
