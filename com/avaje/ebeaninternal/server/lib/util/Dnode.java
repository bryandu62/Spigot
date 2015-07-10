package com.avaje.ebeaninternal.server.lib.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Dnode
{
  int level;
  String nodeName;
  String nodeContent;
  ArrayList<Dnode> children;
  LinkedHashMap<String, String> attrList = new LinkedHashMap();
  
  public static Dnode parse(String s)
  {
    DnodeReader r = new DnodeReader();
    return r.parseXml(s);
  }
  
  public String toXml()
  {
    StringBuilder sb = new StringBuilder();
    generate(sb);
    return sb.toString();
  }
  
  public StringBuilder generate(StringBuilder sb)
  {
    if (sb == null) {
      sb = new StringBuilder();
    }
    sb.append("<").append(this.nodeName);
    Iterator<String> it = attributeNames();
    while (it.hasNext())
    {
      String attr = (String)it.next();
      Object attrValue = getAttribute(attr);
      sb.append(" ").append(attr).append("=\"");
      if (attrValue != null) {
        sb.append(attrValue);
      }
      sb.append("\"");
    }
    if ((this.nodeContent == null) && (!hasChildren()))
    {
      sb.append(" />");
    }
    else
    {
      sb.append(">");
      if ((this.children != null) && (this.children.size() > 0)) {
        for (int i = 0; i < this.children.size(); i++)
        {
          Dnode child = (Dnode)this.children.get(i);
          child.generate(sb);
        }
      }
      if (this.nodeContent != null) {
        sb.append(this.nodeContent);
      }
      sb.append("</").append(this.nodeName).append(">");
    }
    return sb;
  }
  
  public String getNodeName()
  {
    return this.nodeName;
  }
  
  public void setNodeName(String nodeName)
  {
    this.nodeName = nodeName;
  }
  
  public String getNodeContent()
  {
    return this.nodeContent;
  }
  
  public void setNodeContent(String nodeContent)
  {
    this.nodeContent = nodeContent;
  }
  
  public boolean hasChildren()
  {
    return getChildrenCount() > 0;
  }
  
  public int getChildrenCount()
  {
    if (this.children == null) {
      return 0;
    }
    return this.children.size();
  }
  
  public boolean remove(Dnode node)
  {
    if (this.children == null) {
      return false;
    }
    if (this.children.remove(node)) {
      return true;
    }
    Iterator<Dnode> it = this.children.iterator();
    while (it.hasNext())
    {
      Dnode child = (Dnode)it.next();
      if (child.remove(node)) {
        return true;
      }
    }
    return false;
  }
  
  public List<Dnode> children()
  {
    if (this.children == null) {
      return null;
    }
    return this.children;
  }
  
  public void addChild(Dnode child)
  {
    if (this.children == null) {
      this.children = new ArrayList();
    }
    this.children.add(child);
    child.setLevel(this.level + 1);
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  public void setLevel(int level)
  {
    this.level = level;
    if (this.children != null) {
      for (int i = 0; i < this.children.size(); i++)
      {
        Dnode child = (Dnode)this.children.get(i);
        child.setLevel(level + 1);
      }
    }
  }
  
  public Dnode find(String nodeName)
  {
    return find(nodeName, null, null);
  }
  
  public Dnode find(String nodeName, String attrName, Object value)
  {
    return find(nodeName, attrName, value, -1);
  }
  
  public Dnode find(String nodeName, String attrName, Object value, int maxLevel)
  {
    ArrayList<Dnode> list = new ArrayList();
    findByNode(list, nodeName, true, attrName, value, maxLevel);
    if (list.size() >= 1) {
      return (Dnode)list.get(0);
    }
    return null;
  }
  
  public List<Dnode> findAll(String nodeName, int maxLevel)
  {
    int level = -1;
    if (maxLevel > 0) {
      level = this.level + maxLevel;
    }
    return findAll(nodeName, null, null, level);
  }
  
  public List<Dnode> findAll(String nodeName, String attrName, Object value, int maxLevel)
  {
    if ((nodeName == null) && (attrName == null)) {
      throw new RuntimeException("You can not have both nodeName and attrName null");
    }
    ArrayList<Dnode> list = new ArrayList();
    findByNode(list, nodeName, false, attrName, value, maxLevel);
    return list;
  }
  
  private void findByNode(List<Dnode> list, String node, boolean findOne, String attrName, Object value, int maxLevel)
  {
    if ((findOne) && (list.size() == 1)) {
      return;
    }
    if (((node == null) || (node.equals(this.nodeName))) && (
      (attrName == null) || (value.equals(getAttribute(attrName)))))
    {
      list.add(this);
      if (findOne) {
        return;
      }
    }
    if ((maxLevel <= 0) || (this.level < maxLevel)) {
      if (this.children != null) {
        for (int i = 0; i < this.children.size(); i++)
        {
          Dnode child = (Dnode)this.children.get(i);
          child.findByNode(list, node, findOne, attrName, value, maxLevel);
        }
      }
    }
  }
  
  public Iterator<String> attributeNames()
  {
    return this.attrList.keySet().iterator();
  }
  
  public String getAttribute(String name)
  {
    return (String)this.attrList.get(name);
  }
  
  public String getStringAttr(String name, String defaultValue)
  {
    Object o = this.attrList.get(name);
    if (o == null) {
      return defaultValue;
    }
    return o.toString();
  }
  
  public void setAttribute(String name, String value)
  {
    this.attrList.put(name, value);
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[").append(getNodeName()).append(" ").append(this.attrList).append("]");
    return sb.toString();
  }
}
