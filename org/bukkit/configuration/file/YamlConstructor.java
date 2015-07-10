package org.bukkit.configuration.file;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.constructor.SafeConstructor.ConstructYamlMap;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlConstructor
  extends SafeConstructor
{
  public YamlConstructor()
  {
    this.yamlConstructors.put(Tag.MAP, new ConstructCustomObject(null));
  }
  
  private class ConstructCustomObject
    extends SafeConstructor.ConstructYamlMap
  {
    private ConstructCustomObject()
    {
      super();
    }
    
    public Object construct(Node node)
    {
      if (node.isTwoStepsConstruction()) {
        throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
      }
      Map<?, ?> raw = (Map)super.construct(node);
      if (raw.containsKey("=="))
      {
        Map<String, Object> typed = new LinkedHashMap(raw.size());
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
          typed.put(entry.getKey().toString(), entry.getValue());
        }
        try
        {
          return ConfigurationSerialization.deserializeObject(typed);
        }
        catch (IllegalArgumentException ex)
        {
          throw new YAMLException("Could not deserialize object", ex);
        }
      }
      return raw;
    }
    
    public void construct2ndStep(Node node, Object object)
    {
      throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
    }
  }
}
