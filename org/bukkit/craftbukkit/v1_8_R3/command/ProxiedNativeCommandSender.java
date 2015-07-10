package org.bukkit.craftbukkit.v1_8_R3.command;

import java.util.Set;
import net.minecraft.server.v1_8_R3.ICommandListener;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class ProxiedNativeCommandSender
  implements ProxiedCommandSender
{
  private final ICommandListener orig;
  private final CommandSender caller;
  private final CommandSender callee;
  
  public ProxiedNativeCommandSender(ICommandListener orig, CommandSender caller, CommandSender callee)
  {
    this.orig = orig;
    this.caller = caller;
    this.callee = callee;
  }
  
  public ICommandListener getHandle()
  {
    return this.orig;
  }
  
  public CommandSender getCaller()
  {
    return this.caller;
  }
  
  public CommandSender getCallee()
  {
    return this.callee;
  }
  
  public void sendMessage(String message)
  {
    getCaller().sendMessage(message);
  }
  
  public void sendMessage(String[] messages)
  {
    getCaller().sendMessage(messages);
  }
  
  public Server getServer()
  {
    return getCallee().getServer();
  }
  
  public String getName()
  {
    return getCallee().getName();
  }
  
  public boolean isPermissionSet(String name)
  {
    return getCaller().isPermissionSet(name);
  }
  
  public boolean isPermissionSet(Permission perm)
  {
    return getCaller().isPermissionSet(perm);
  }
  
  public boolean hasPermission(String name)
  {
    return getCaller().hasPermission(name);
  }
  
  public boolean hasPermission(Permission perm)
  {
    return getCaller().hasPermission(perm);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value)
  {
    return getCaller().addAttachment(plugin, name, value);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin)
  {
    return getCaller().addAttachment(plugin);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks)
  {
    return getCaller().addAttachment(plugin, name, value, ticks);
  }
  
  public PermissionAttachment addAttachment(Plugin plugin, int ticks)
  {
    return getCaller().addAttachment(plugin, ticks);
  }
  
  public void removeAttachment(PermissionAttachment attachment)
  {
    getCaller().removeAttachment(attachment);
  }
  
  public void recalculatePermissions()
  {
    getCaller().recalculatePermissions();
  }
  
  public Set<PermissionAttachmentInfo> getEffectivePermissions()
  {
    return getCaller().getEffectivePermissions();
  }
  
  public boolean isOp()
  {
    return getCaller().isOp();
  }
  
  public void setOp(boolean value)
  {
    getCaller().setOp(value);
  }
}
