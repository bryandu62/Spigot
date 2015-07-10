package org.bukkit.craftbukkit.v1_8_R3.command;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.CommandBlockListenerAbstract;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;

public class CraftBlockCommandSender
  extends ServerCommandSender
  implements BlockCommandSender
{
  private final CommandBlockListenerAbstract commandBlock;
  
  public CraftBlockCommandSender(CommandBlockListenerAbstract commandBlockListenerAbstract)
  {
    this.commandBlock = commandBlockListenerAbstract;
  }
  
  public Block getBlock()
  {
    return this.commandBlock.getWorld().getWorld().getBlockAt(this.commandBlock.getChunkCoordinates().getX(), this.commandBlock.getChunkCoordinates().getY(), this.commandBlock.getChunkCoordinates().getZ());
  }
  
  public void sendMessage(String message)
  {
    IChatBaseComponent[] arrayOfIChatBaseComponent;
    int i = (arrayOfIChatBaseComponent = CraftChatMessage.fromString(message)).length;
    for (int j = 0; j < i; j++)
    {
      IChatBaseComponent component = arrayOfIChatBaseComponent[j];
      this.commandBlock.sendMessage(component);
    }
  }
  
  public void sendMessage(String[] messages)
  {
    String[] arrayOfString;
    int i = (arrayOfString = messages).length;
    for (int j = 0; j < i; j++)
    {
      String message = arrayOfString[j];
      sendMessage(message);
    }
  }
  
  public String getName()
  {
    return this.commandBlock.getName();
  }
  
  public boolean isOp()
  {
    return true;
  }
  
  public void setOp(boolean value)
  {
    throw new UnsupportedOperationException("Cannot change operator status of a block");
  }
  
  public ICommandListener getTileEntity()
  {
    return this.commandBlock;
  }
}
