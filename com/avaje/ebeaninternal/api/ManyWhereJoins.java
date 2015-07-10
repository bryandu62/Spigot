package com.avaje.ebeaninternal.api;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.el.ElPropertyDeploy;
import com.avaje.ebeaninternal.server.query.SplitName;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class ManyWhereJoins
  implements Serializable
{
  private static final long serialVersionUID = -6490181101871795417L;
  private final TreeSet<String> joins = new TreeSet();
  
  public void add(ElPropertyDeploy elProp)
  {
    String join = elProp.getElPrefix();
    BeanProperty p = elProp.getBeanProperty();
    if ((p instanceof BeanPropertyAssocMany)) {
      join = addManyToJoin(join, p.getName());
    }
    if (join != null)
    {
      this.joins.add(join);
      String secondaryTableJoinPrefix = p.getSecondaryTableJoinPrefix();
      if (secondaryTableJoinPrefix != null) {
        this.joins.add(join + "." + secondaryTableJoinPrefix);
      }
      addParentJoins(join);
    }
  }
  
  private String addManyToJoin(String join, String manyPropName)
  {
    if (join == null) {
      return manyPropName;
    }
    return join + "." + manyPropName;
  }
  
  private void addParentJoins(String join)
  {
    String[] split = SplitName.split(join);
    if (split[0] != null)
    {
      this.joins.add(split[0]);
      addParentJoins(split[0]);
    }
  }
  
  public boolean isEmpty()
  {
    return this.joins.isEmpty();
  }
  
  public Set<String> getJoins()
  {
    return this.joins;
  }
}
