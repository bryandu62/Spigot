package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.constructor.SafeConstructor.ConstructUndefined;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public final class PluginDescriptionFile
{
  private static final ThreadLocal<Yaml> YAML = new ThreadLocal()
  {
    protected Yaml initialValue()
    {
      new Yaml(new SafeConstructor() {});
    }
  };
  String rawName = null;
  private String name = null;
  private String main = null;
  private String classLoaderOf = null;
  private List<String> depend = ImmutableList.of();
  private List<String> softDepend = ImmutableList.of();
  private List<String> loadBefore = ImmutableList.of();
  private String version = null;
  private Map<String, Map<String, Object>> commands = null;
  private String description = null;
  private List<String> authors = null;
  private String website = null;
  private String prefix = null;
  private boolean database = false;
  private PluginLoadOrder order = PluginLoadOrder.POSTWORLD;
  private List<Permission> permissions = null;
  private Map<?, ?> lazyPermissions = null;
  private PermissionDefault defaultPerm = PermissionDefault.OP;
  private Set<PluginAwareness> awareness = ImmutableSet.of();
  
  public PluginDescriptionFile(InputStream stream)
    throws InvalidDescriptionException
  {
    loadMap(asMap(((Yaml)YAML.get()).load(stream)));
  }
  
  public PluginDescriptionFile(Reader reader)
    throws InvalidDescriptionException
  {
    loadMap(asMap(((Yaml)YAML.get()).load(reader)));
  }
  
  public PluginDescriptionFile(String pluginName, String pluginVersion, String mainClass)
  {
    this.name = pluginName.replace(' ', '_');
    this.version = pluginVersion;
    this.main = mainClass;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getVersion()
  {
    return this.version;
  }
  
  public String getMain()
  {
    return this.main;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public PluginLoadOrder getLoad()
  {
    return this.order;
  }
  
  public List<String> getAuthors()
  {
    return this.authors;
  }
  
  public String getWebsite()
  {
    return this.website;
  }
  
  public boolean isDatabaseEnabled()
  {
    return this.database;
  }
  
  public List<String> getDepend()
  {
    return this.depend;
  }
  
  public List<String> getSoftDepend()
  {
    return this.softDepend;
  }
  
  public List<String> getLoadBefore()
  {
    return this.loadBefore;
  }
  
  public String getPrefix()
  {
    return this.prefix;
  }
  
  public Map<String, Map<String, Object>> getCommands()
  {
    return this.commands;
  }
  
  public List<Permission> getPermissions()
  {
    if (this.permissions == null) {
      if (this.lazyPermissions == null)
      {
        this.permissions = ImmutableList.of();
      }
      else
      {
        this.permissions = ImmutableList.copyOf(Permission.loadPermissions(this.lazyPermissions, "Permission node '%s' in plugin description file for " + getFullName() + " is invalid", this.defaultPerm));
        this.lazyPermissions = null;
      }
    }
    return this.permissions;
  }
  
  public PermissionDefault getPermissionDefault()
  {
    return this.defaultPerm;
  }
  
  public Set<PluginAwareness> getAwareness()
  {
    return this.awareness;
  }
  
  public String getFullName()
  {
    return this.name + " v" + this.version;
  }
  
  @Deprecated
  public String getClassLoaderOf()
  {
    return this.classLoaderOf;
  }
  
  public void setDatabaseEnabled(boolean database)
  {
    this.database = database;
  }
  
  public void save(Writer writer)
  {
    ((Yaml)YAML.get()).dump(saveMap(), writer);
  }
  
  private void loadMap(Map<?, ?> map)
    throws InvalidDescriptionException
  {
    try
    {
      this.name = (this.rawName = map.get("name").toString());
      if (!this.name.matches("^[A-Za-z0-9 _.-]+$")) {
        throw new InvalidDescriptionException("name '" + this.name + "' contains invalid characters.");
      }
      this.name = this.name.replace(' ', '_');
    }
    catch (NullPointerException ex)
    {
      throw new InvalidDescriptionException(ex, "name is not defined");
    }
    catch (ClassCastException ex)
    {
      throw new InvalidDescriptionException(ex, "name is of wrong type");
    }
    try
    {
      this.version = map.get("version").toString();
    }
    catch (NullPointerException ex)
    {
      throw new InvalidDescriptionException(ex, "version is not defined");
    }
    catch (ClassCastException ex)
    {
      throw new InvalidDescriptionException(ex, "version is of wrong type");
    }
    try
    {
      this.main = map.get("main").toString();
      if (this.main.startsWith("org.bukkit.")) {
        throw new InvalidDescriptionException("main may not be within the org.bukkit namespace");
      }
    }
    catch (NullPointerException ex)
    {
      throw new InvalidDescriptionException(ex, "main is not defined");
    }
    catch (ClassCastException ex)
    {
      throw new InvalidDescriptionException(ex, "main is of wrong type");
    }
    if (map.get("commands") != null)
    {
      ImmutableMap.Builder<String, Map<String, Object>> commandsBuilder = ImmutableMap.builder();
      try
      {
        for (Map.Entry<?, ?> command : ((Map)map.get("commands")).entrySet())
        {
          ImmutableMap.Builder<String, Object> commandBuilder = ImmutableMap.builder();
          if (command.getValue() != null) {
            for (Map.Entry<?, ?> commandEntry : ((Map)command.getValue()).entrySet()) {
              if ((commandEntry.getValue() instanceof Iterable))
              {
                ImmutableList.Builder<Object> commandSubList = ImmutableList.builder();
                for (Object commandSubListItem : (Iterable)commandEntry.getValue()) {
                  if (commandSubListItem != null) {
                    commandSubList.add(commandSubListItem);
                  }
                }
                commandBuilder.put(commandEntry.getKey().toString(), commandSubList.build());
              }
              else if (commandEntry.getValue() != null)
              {
                commandBuilder.put(commandEntry.getKey().toString(), commandEntry.getValue());
              }
            }
          }
          commandsBuilder.put(command.getKey().toString(), commandBuilder.build());
        }
      }
      catch (ClassCastException ex)
      {
        throw new InvalidDescriptionException(ex, "commands are of wrong type");
      }
      this.commands = commandsBuilder.build();
    }
    if (map.get("class-loader-of") != null) {
      this.classLoaderOf = map.get("class-loader-of").toString();
    }
    this.depend = makePluginNameList(map, "depend");
    this.softDepend = makePluginNameList(map, "softdepend");
    this.loadBefore = makePluginNameList(map, "loadbefore");
    if (map.get("database") != null) {
      try
      {
        this.database = ((Boolean)map.get("database")).booleanValue();
      }
      catch (ClassCastException ex)
      {
        throw new InvalidDescriptionException(ex, "database is of wrong type");
      }
    }
    if (map.get("website") != null) {
      this.website = map.get("website").toString();
    }
    if (map.get("description") != null) {
      this.description = map.get("description").toString();
    }
    if (map.get("load") != null) {
      try
      {
        this.order = PluginLoadOrder.valueOf(((String)map.get("load")).toUpperCase().replaceAll("\\W", ""));
      }
      catch (ClassCastException ex)
      {
        throw new InvalidDescriptionException(ex, "load is of wrong type");
      }
      catch (IllegalArgumentException ex)
      {
        throw new InvalidDescriptionException(ex, "load is not a valid choice");
      }
    }
    if (map.get("authors") != null)
    {
      ImmutableList.Builder<String> authorsBuilder = ImmutableList.builder();
      if (map.get("author") != null) {
        authorsBuilder.add(map.get("author").toString());
      }
      try
      {
        for (Object o : (Iterable)map.get("authors")) {
          authorsBuilder.add(o.toString());
        }
      }
      catch (ClassCastException ex)
      {
        throw new InvalidDescriptionException(ex, "authors are of wrong type");
      }
      catch (NullPointerException ex)
      {
        throw new InvalidDescriptionException(ex, "authors are improperly defined");
      }
      this.authors = authorsBuilder.build();
    }
    else if (map.get("author") != null)
    {
      this.authors = ImmutableList.of(map.get("author").toString());
    }
    else
    {
      this.authors = ImmutableList.of();
    }
    if (map.get("default-permission") != null) {
      try
      {
        this.defaultPerm = PermissionDefault.getByName(map.get("default-permission").toString());
      }
      catch (ClassCastException ex)
      {
        throw new InvalidDescriptionException(ex, "default-permission is of wrong type");
      }
      catch (IllegalArgumentException ex)
      {
        throw new InvalidDescriptionException(ex, "default-permission is not a valid choice");
      }
    }
    if ((map.get("awareness") instanceof Iterable))
    {
      Set<PluginAwareness> awareness = new HashSet();
      try
      {
        for (Object o : (Iterable)map.get("awareness")) {
          awareness.add((PluginAwareness)o);
        }
      }
      catch (ClassCastException ex)
      {
        throw new InvalidDescriptionException(ex, "awareness has wrong type");
      }
      this.awareness = ImmutableSet.copyOf(awareness);
    }
    try
    {
      this.lazyPermissions = ((Map)map.get("permissions"));
    }
    catch (ClassCastException ex)
    {
      throw new InvalidDescriptionException(ex, "permissions are of the wrong type");
    }
    if (map.get("prefix") != null) {
      this.prefix = map.get("prefix").toString();
    }
  }
  
  private static List<String> makePluginNameList(Map<?, ?> map, String key)
    throws InvalidDescriptionException
  {
    Object value = map.get(key);
    if (value == null) {
      return ImmutableList.of();
    }
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    try
    {
      for (Object entry : (Iterable)value) {
        builder.add(entry.toString().replace(' ', '_'));
      }
    }
    catch (ClassCastException ex)
    {
      throw new InvalidDescriptionException(ex, key + " is of wrong type");
    }
    catch (NullPointerException ex)
    {
      throw new InvalidDescriptionException(ex, "invalid " + key + " format");
    }
    return builder.build();
  }
  
  private Map<String, Object> saveMap()
  {
    Map<String, Object> map = new HashMap();
    
    map.put("name", this.name);
    map.put("main", this.main);
    map.put("version", this.version);
    map.put("database", Boolean.valueOf(this.database));
    map.put("order", this.order.toString());
    map.put("default-permission", this.defaultPerm.toString());
    if (this.commands != null) {
      map.put("command", this.commands);
    }
    if (this.depend != null) {
      map.put("depend", this.depend);
    }
    if (this.softDepend != null) {
      map.put("softdepend", this.softDepend);
    }
    if (this.website != null) {
      map.put("website", this.website);
    }
    if (this.description != null) {
      map.put("description", this.description);
    }
    if (this.authors.size() == 1) {
      map.put("author", this.authors.get(0));
    } else if (this.authors.size() > 1) {
      map.put("authors", this.authors);
    }
    if (this.classLoaderOf != null) {
      map.put("class-loader-of", this.classLoaderOf);
    }
    if (this.prefix != null) {
      map.put("prefix", this.prefix);
    }
    return map;
  }
  
  private Map<?, ?> asMap(Object object)
    throws InvalidDescriptionException
  {
    if ((object instanceof Map)) {
      return (Map)object;
    }
    throw new InvalidDescriptionException(object + " is not properly structured.");
  }
  
  @Deprecated
  public String getRawName()
  {
    return this.rawName;
  }
}
