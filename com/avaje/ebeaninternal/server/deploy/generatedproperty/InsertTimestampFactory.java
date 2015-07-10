package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.PersistenceException;

public class InsertTimestampFactory
{
  final GeneratedInsertTimestamp timestamp = new GeneratedInsertTimestamp();
  final GeneratedInsertDate utilDate = new GeneratedInsertDate();
  final GeneratedInsertLong longTime = new GeneratedInsertLong();
  
  public void setInsertTimestamp(DeployBeanProperty property)
  {
    property.setGeneratedProperty(createInsertTimestamp(property));
  }
  
  public GeneratedProperty createInsertTimestamp(DeployBeanProperty property)
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
    String msg = "Generated Insert Timestamp not supported on " + propType.getName();
    throw new PersistenceException(msg);
  }
}
