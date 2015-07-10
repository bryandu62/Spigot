package com.avaje.ebeaninternal.server.cluster.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class SocketConnection
{
  ObjectOutputStream oos;
  ObjectInputStream ois;
  InputStream is;
  OutputStream os;
  Socket socket;
  
  public SocketConnection(Socket socket)
    throws IOException
  {
    this.is = socket.getInputStream();
    this.os = socket.getOutputStream();
    this.socket = socket;
  }
  
  public void disconnect()
    throws IOException
  {
    this.os.flush();
    this.socket.close();
  }
  
  public void flush()
    throws IOException
  {
    this.os.flush();
  }
  
  public Object readObject()
    throws IOException, ClassNotFoundException
  {
    return getObjectInputStream().readObject();
  }
  
  public ObjectOutputStream writeObject(Object object)
    throws IOException
  {
    ObjectOutputStream oos = getObjectOutputStream();
    oos.writeObject(object);
    return oos;
  }
  
  public ObjectOutputStream getObjectOutputStream()
    throws IOException
  {
    if (this.oos == null) {
      this.oos = new ObjectOutputStream(this.os);
    }
    return this.oos;
  }
  
  public ObjectInputStream getObjectInputStream()
    throws IOException
  {
    if (this.ois == null) {
      this.ois = new ObjectInputStream(this.is);
    }
    return this.ois;
  }
  
  public void setObjectInputStream(ObjectInputStream ois)
  {
    this.ois = ois;
  }
  
  public void setObjectOutputStream(ObjectOutputStream oos)
  {
    this.oos = oos;
  }
  
  public InputStream getInputStream()
    throws IOException
  {
    return this.is;
  }
  
  public OutputStream getOutputStream()
    throws IOException
  {
    return this.os;
  }
}
