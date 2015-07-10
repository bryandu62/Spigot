package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.Collections;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class MongoDBObject
  implements NoSQLObject<BasicDBObject>
{
  private final BasicDBObject mongoObject;
  
  public MongoDBObject()
  {
    this.mongoObject = new BasicDBObject();
  }
  
  public void set(String field, Object value)
  {
    this.mongoObject.append(field, value);
  }
  
  public void set(String field, NoSQLObject<BasicDBObject> value)
  {
    this.mongoObject.append(field, value.unwrap());
  }
  
  public void set(String field, Object[] values)
  {
    BasicDBList list = new BasicDBList();
    Collections.addAll(list, values);
    this.mongoObject.append(field, list);
  }
  
  public void set(String field, NoSQLObject<BasicDBObject>[] values)
  {
    BasicDBList list = new BasicDBList();
    for (NoSQLObject<BasicDBObject> value : values) {
      list.add(value.unwrap());
    }
    this.mongoObject.append(field, list);
  }
  
  public BasicDBObject unwrap()
  {
    return this.mongoObject;
  }
}
