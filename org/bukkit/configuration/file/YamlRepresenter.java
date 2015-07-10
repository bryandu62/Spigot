package org.bukkit.configuration.file;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.representer.SafeRepresenter.RepresentMap;

public class YamlRepresenter
  extends Representer
{
  public YamlRepresenter()
  {
    this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection(null));
    this.multiRepresenters.put(ConfigurationSerializable.class, new RepresentConfigurationSerializable(null));
  }
  
  private class RepresentConfigurationSection
    extends SafeRepresenter.RepresentMap
  {
    private RepresentConfigurationSection()
    {
      super();
    }
    
    public Node representData(Object data)
    {
      return super.representData(((ConfigurationSection)data).getValues(false));
    }
  }
  
  private class RepresentConfigurationSerializable
    extends SafeRepresenter.RepresentMap
  {
    private RepresentConfigurationSerializable()
    {
      super();
    }
    
    public Node representData(Object data)
    {
      ConfigurationSerializable serializable = (ConfigurationSerializable)data;
      Map<String, Object> values = new LinkedHashMap();
      values.put("==", ConfigurationSerialization.getAlias(serializable.getClass()));
      values.putAll(serializable.serialize());
      
      return super.representData(values);
    }
  }
}
