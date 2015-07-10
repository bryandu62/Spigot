package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectMessage
  implements Message
{
  private static final long serialVersionUID = -5903272448334166185L;
  private transient Object obj;
  
  public ObjectMessage(Object obj)
  {
    if (obj == null) {
      obj = "null";
    }
    this.obj = obj;
  }
  
  public String getFormattedMessage()
  {
    return this.obj.toString();
  }
  
  public String getFormat()
  {
    return this.obj.toString();
  }
  
  public Object[] getParameters()
  {
    return new Object[] { this.obj };
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    ObjectMessage that = (ObjectMessage)o;
    
    return this.obj != null ? this.obj.equals(that.obj) : that.obj == null;
  }
  
  public int hashCode()
  {
    return this.obj != null ? this.obj.hashCode() : 0;
  }
  
  public String toString()
  {
    return "ObjectMessage[obj=" + this.obj.toString() + "]";
  }
  
  private void writeObject(ObjectOutputStream out)
    throws IOException
  {
    out.defaultWriteObject();
    if ((this.obj instanceof Serializable)) {
      out.writeObject(this.obj);
    } else {
      out.writeObject(this.obj.toString());
    }
  }
  
  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    this.obj = in.readObject();
  }
  
  public Throwable getThrowable()
  {
    return (this.obj instanceof Throwable) ? (Throwable)this.obj : null;
  }
}
