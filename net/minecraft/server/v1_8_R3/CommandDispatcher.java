package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import org.spigotmc.SpigotConfig;

public class CommandDispatcher
  extends CommandHandler
  implements ICommandDispatcher
{
  public CommandDispatcher()
  {
    a(new CommandTime());
    a(new CommandGamemode());
    a(new CommandDifficulty());
    a(new CommandGamemodeDefault());
    a(new CommandKill());
    a(new CommandToggleDownfall());
    a(new CommandWeather());
    a(new CommandXp());
    a(new CommandTp());
    a(new CommandGive());
    a(new CommandReplaceItem());
    a(new CommandStats());
    a(new CommandEffect());
    a(new CommandEnchant());
    a(new CommandParticle());
    a(new CommandMe());
    a(new CommandSeed());
    a(new CommandHelp());
    a(new CommandDebug());
    a(new CommandTell());
    a(new CommandSay());
    a(new CommandSpawnpoint());
    a(new CommandSetWorldSpawn());
    a(new CommandGamerule());
    a(new CommandClear());
    a(new CommandTestFor());
    a(new CommandSpreadPlayers());
    a(new CommandPlaySound());
    a(new CommandScoreboard());
    a(new CommandExecute());
    a(new CommandTrigger());
    a(new CommandAchievement());
    a(new CommandSummon());
    a(new CommandSetBlock());
    a(new CommandFill());
    a(new CommandClone());
    a(new CommandTestForBlocks());
    a(new CommandBlockData());
    a(new CommandTestForBlock());
    a(new CommandTellRaw());
    a(new CommandWorldBorder());
    a(new CommandTitle());
    a(new CommandEntityData());
    if (MinecraftServer.getServer().ae())
    {
      a(new CommandOp());
      a(new CommandDeop());
      a(new CommandStop());
      a(new CommandSaveAll());
      a(new CommandSaveOff());
      a(new CommandSaveOn());
      a(new CommandBanIp());
      a(new CommandPardonIP());
      a(new CommandBan());
      a(new CommandBanList());
      a(new CommandPardon());
      a(new CommandKick());
      a(new CommandList());
      a(new CommandWhitelist());
      a(new CommandIdleTimeout());
    }
    else
    {
      a(new CommandPublish());
    }
    CommandAbstract.a(this);
  }
  
  public void a(ICommandListener icommandlistener, ICommand icommand, int i, String s, Object... aobject)
  {
    boolean flag = true;
    MinecraftServer minecraftserver = MinecraftServer.getServer();
    if (!icommandlistener.getSendCommandFeedback()) {
      flag = false;
    }
    ChatMessage chatmessage = new ChatMessage("chat.type.admin", new Object[] { icommandlistener.getName(), new ChatMessage(s, aobject) });
    
    chatmessage.getChatModifier().setColor(EnumChatFormat.GRAY);
    chatmessage.getChatModifier().setItalic(Boolean.valueOf(true));
    if (flag)
    {
      Iterator iterator = minecraftserver.getPlayerList().v().iterator();
      while (iterator.hasNext())
      {
        EntityHuman entityhuman = (EntityHuman)iterator.next();
        if ((entityhuman != icommandlistener) && (minecraftserver.getPlayerList().isOp(entityhuman.getProfile())) && (icommand.canUse(icommandlistener)))
        {
          boolean flag1 = ((icommandlistener instanceof MinecraftServer)) && (MinecraftServer.getServer().r());
          boolean flag2 = ((icommandlistener instanceof RemoteControlCommandListener)) && (MinecraftServer.getServer().q());
          if ((flag1) || (flag2) || ((!(icommandlistener instanceof RemoteControlCommandListener)) && (!(icommandlistener instanceof MinecraftServer)))) {
            entityhuman.sendMessage(chatmessage);
          }
        }
      }
    }
    if ((icommandlistener != minecraftserver) && (minecraftserver.worldServer[0].getGameRules().getBoolean("logAdminCommands")) && (!SpigotConfig.silentCommandBlocks)) {
      minecraftserver.sendMessage(chatmessage);
    }
    boolean flag3 = minecraftserver.worldServer[0].getGameRules().getBoolean("sendCommandFeedback");
    if ((icommandlistener instanceof CommandBlockListenerAbstract)) {
      flag3 = ((CommandBlockListenerAbstract)icommandlistener).m();
    }
    if ((((i & 0x1) != 1) && (flag3)) || ((icommandlistener instanceof MinecraftServer))) {
      icommandlistener.sendMessage(new ChatMessage(s, aobject));
    }
  }
}
