package com.avaje.ebeaninternal.server.subclass;

import com.avaje.ebean.enhance.agent.ClassMeta;
import com.avaje.ebean.enhance.agent.EnhanceConstants;
import com.avaje.ebean.enhance.agent.FieldMeta;
import com.avaje.ebean.enhance.asm.ClassVisitor;
import com.avaje.ebean.enhance.asm.Opcodes;
import java.util.List;

public class GetterSetterMethods
  implements Opcodes, EnhanceConstants
{
  public static void add(ClassVisitor cv, ClassMeta classMeta)
  {
    List<FieldMeta> localFields = classMeta.getLocalFields();
    for (int x = 0; x < localFields.size(); x++)
    {
      FieldMeta fieldMeta = (FieldMeta)localFields.get(x);
      fieldMeta.addPublicGetSetMethods(cv, classMeta, true);
    }
    List<FieldMeta> inheritedFields = classMeta.getInheritedFields();
    for (int i = 0; i < inheritedFields.size(); i++)
    {
      FieldMeta fieldMeta = (FieldMeta)inheritedFields.get(i);
      
      fieldMeta.addPublicGetSetMethods(cv, classMeta, false);
    }
  }
}
