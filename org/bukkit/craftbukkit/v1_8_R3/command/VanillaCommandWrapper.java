package org.bukkit.craftbukkit.v1_8_R3.command;

import java.util.List;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.CommandAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartCommandBlock;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.PlayerSelector;
import net.minecraft.server.v1_8_R3.RemoteControlCommandListener;
import org.apache.commons.lang.Validate;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper
  extends VanillaCommand
{
  protected final CommandAbstract vanillaCommand;
  
  public VanillaCommandWrapper(CommandAbstract vanillaCommand)
  {
    super(vanillaCommand.getCommand());
    this.vanillaCommand = vanillaCommand;
  }
  
  public VanillaCommandWrapper(CommandAbstract vanillaCommand, String usage)
  {
    super(vanillaCommand.getCommand());
    this.vanillaCommand = vanillaCommand;
    this.description = "A Mojang provided command.";
    this.usageMessage = usage;
    setPermission("minecraft.command." + vanillaCommand.getCommand());
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    ICommandListener icommandlistener = getListener(sender);
    dispatchVanillaCommand(sender, icommandlistener, args);
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws IllegalArgumentException
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    return this.vanillaCommand.tabComplete(getListener(sender), args, new BlockPosition(0, 0, 0));
  }
  
  public static CommandSender lastSender = null;
  
  /* Error */
  public final int dispatchVanillaCommand(CommandSender bSender, ICommandListener icommandlistener, String[] as)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_3
    //   2: invokespecial 114	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:getPlayerListSize	([Ljava/lang/String;)I
    //   5: istore 4
    //   7: iconst_0
    //   8: istore 5
    //   10: invokestatic 120	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   13: getfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   16: astore 6
    //   18: invokestatic 120	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   21: astore 7
    //   23: aload 7
    //   25: aload 7
    //   27: getfield 128	net/minecraft/server/v1_8_R3/MinecraftServer:worlds	Ljava/util/List;
    //   30: invokeinterface 134 1 0
    //   35: anewarray 136	net/minecraft/server/v1_8_R3/WorldServer
    //   38: putfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   41: aload 7
    //   43: getfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   46: iconst_0
    //   47: aload_2
    //   48: invokeinterface 142 1 0
    //   53: checkcast 136	net/minecraft/server/v1_8_R3/WorldServer
    //   56: aastore
    //   57: iconst_0
    //   58: istore 8
    //   60: iconst_1
    //   61: istore 9
    //   63: goto +54 -> 117
    //   66: aload 7
    //   68: getfield 128	net/minecraft/server/v1_8_R3/MinecraftServer:worlds	Ljava/util/List;
    //   71: iload 8
    //   73: iinc 8 1
    //   76: invokeinterface 150 2 0
    //   81: checkcast 136	net/minecraft/server/v1_8_R3/WorldServer
    //   84: astore 10
    //   86: aload 7
    //   88: getfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   91: iconst_0
    //   92: aaload
    //   93: aload 10
    //   95: if_acmpne +9 -> 104
    //   98: iinc 9 -1
    //   101: goto +13 -> 114
    //   104: aload 7
    //   106: getfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   109: iload 9
    //   111: aload 10
    //   113: aastore
    //   114: iinc 9 1
    //   117: iload 9
    //   119: aload 7
    //   121: getfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   124: arraylength
    //   125: if_icmplt -59 -> 66
    //   128: aload_0
    //   129: getfield 31	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:vanillaCommand	Lnet/minecraft/server/v1_8_R3/CommandAbstract;
    //   132: aload_2
    //   133: invokevirtual 154	net/minecraft/server/v1_8_R3/CommandAbstract:canUse	(Lnet/minecraft/server/v1_8_R3/ICommandListener;)Z
    //   136: ifeq +276 -> 412
    //   139: iload 4
    //   141: iconst_m1
    //   142: if_icmple +245 -> 387
    //   145: aload_2
    //   146: aload_3
    //   147: iload 4
    //   149: aaload
    //   150: ldc -100
    //   152: invokestatic 162	net/minecraft/server/v1_8_R3/PlayerSelector:getPlayers	(Lnet/minecraft/server/v1_8_R3/ICommandListener;Ljava/lang/String;Ljava/lang/Class;)Ljava/util/List;
    //   155: astore 9
    //   157: aload_3
    //   158: iload 4
    //   160: aaload
    //   161: astore 10
    //   163: aload_2
    //   164: getstatic 166	net/minecraft/server/v1_8_R3/CommandObjectiveExecutor$EnumCommandResult:AFFECTED_ENTITIES	Lnet/minecraft/server/v1_8_R3/CommandObjectiveExecutor$EnumCommandResult;
    //   167: aload 9
    //   169: invokeinterface 134 1 0
    //   174: invokeinterface 170 3 0
    //   179: aload 9
    //   181: invokeinterface 174 1 0
    //   186: astore 11
    //   188: goto +180 -> 368
    //   191: aload 11
    //   193: invokeinterface 182 1 0
    //   198: checkcast 156	net/minecraft/server/v1_8_R3/Entity
    //   201: astore 12
    //   203: getstatic 18	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:lastSender	Lorg/bukkit/command/CommandSender;
    //   206: astore 13
    //   208: aload_1
    //   209: putstatic 18	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:lastSender	Lorg/bukkit/command/CommandSender;
    //   212: aload_3
    //   213: iload 4
    //   215: aload 12
    //   217: invokevirtual 186	net/minecraft/server/v1_8_R3/Entity:getUniqueID	()Ljava/util/UUID;
    //   220: invokevirtual 189	java/util/UUID:toString	()Ljava/lang/String;
    //   223: aastore
    //   224: aload_0
    //   225: getfield 31	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:vanillaCommand	Lnet/minecraft/server/v1_8_R3/CommandAbstract;
    //   228: aload_2
    //   229: aload_3
    //   230: invokevirtual 192	net/minecraft/server/v1_8_R3/CommandAbstract:execute	(Lnet/minecraft/server/v1_8_R3/ICommandListener;[Ljava/lang/String;)V
    //   233: iinc 5 1
    //   236: goto +127 -> 363
    //   239: astore 14
    //   241: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   244: dup
    //   245: ldc -60
    //   247: iconst_1
    //   248: anewarray 198	java/lang/Object
    //   251: dup
    //   252: iconst_0
    //   253: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   256: dup
    //   257: aload 14
    //   259: invokevirtual 201	net/minecraft/server/v1_8_R3/ExceptionUsage:getMessage	()Ljava/lang/String;
    //   262: aload 14
    //   264: invokevirtual 205	net/minecraft/server/v1_8_R3/ExceptionUsage:getArgs	()[Ljava/lang/Object;
    //   267: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   270: aastore
    //   271: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   274: astore 15
    //   276: aload 15
    //   278: invokevirtual 212	net/minecraft/server/v1_8_R3/ChatMessage:getChatModifier	()Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   281: getstatic 218	net/minecraft/server/v1_8_R3/EnumChatFormat:RED	Lnet/minecraft/server/v1_8_R3/EnumChatFormat;
    //   284: invokevirtual 224	net/minecraft/server/v1_8_R3/ChatModifier:setColor	(Lnet/minecraft/server/v1_8_R3/EnumChatFormat;)Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   287: pop
    //   288: aload_2
    //   289: aload 15
    //   291: invokeinterface 228 2 0
    //   296: aload 13
    //   298: putstatic 18	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:lastSender	Lorg/bukkit/command/CommandSender;
    //   301: goto +67 -> 368
    //   304: astore 14
    //   306: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   309: dup
    //   310: aload 14
    //   312: invokevirtual 229	net/minecraft/server/v1_8_R3/CommandException:getMessage	()Ljava/lang/String;
    //   315: aload 14
    //   317: invokevirtual 230	net/minecraft/server/v1_8_R3/CommandException:getArgs	()[Ljava/lang/Object;
    //   320: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   323: astore 15
    //   325: aload 15
    //   327: invokevirtual 212	net/minecraft/server/v1_8_R3/ChatMessage:getChatModifier	()Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   330: getstatic 218	net/minecraft/server/v1_8_R3/EnumChatFormat:RED	Lnet/minecraft/server/v1_8_R3/EnumChatFormat;
    //   333: invokevirtual 224	net/minecraft/server/v1_8_R3/ChatModifier:setColor	(Lnet/minecraft/server/v1_8_R3/EnumChatFormat;)Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   336: pop
    //   337: aload_2
    //   338: aload 15
    //   340: invokeinterface 228 2 0
    //   345: aload 13
    //   347: putstatic 18	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:lastSender	Lorg/bukkit/command/CommandSender;
    //   350: goto +18 -> 368
    //   353: astore 16
    //   355: aload 13
    //   357: putstatic 18	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:lastSender	Lorg/bukkit/command/CommandSender;
    //   360: aload 16
    //   362: athrow
    //   363: aload 13
    //   365: putstatic 18	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:lastSender	Lorg/bukkit/command/CommandSender;
    //   368: aload 11
    //   370: invokeinterface 234 1 0
    //   375: ifne -184 -> 191
    //   378: aload_3
    //   379: iload 4
    //   381: aload 10
    //   383: aastore
    //   384: goto +434 -> 818
    //   387: aload_2
    //   388: getstatic 166	net/minecraft/server/v1_8_R3/CommandObjectiveExecutor$EnumCommandResult:AFFECTED_ENTITIES	Lnet/minecraft/server/v1_8_R3/CommandObjectiveExecutor$EnumCommandResult;
    //   391: iconst_1
    //   392: invokeinterface 170 3 0
    //   397: aload_0
    //   398: getfield 31	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:vanillaCommand	Lnet/minecraft/server/v1_8_R3/CommandAbstract;
    //   401: aload_2
    //   402: aload_3
    //   403: invokevirtual 192	net/minecraft/server/v1_8_R3/CommandAbstract:execute	(Lnet/minecraft/server/v1_8_R3/ICommandListener;[Ljava/lang/String;)V
    //   406: iinc 5 1
    //   409: goto +409 -> 818
    //   412: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   415: dup
    //   416: ldc -20
    //   418: iconst_0
    //   419: anewarray 198	java/lang/Object
    //   422: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   425: astore 9
    //   427: aload 9
    //   429: invokevirtual 212	net/minecraft/server/v1_8_R3/ChatMessage:getChatModifier	()Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   432: getstatic 218	net/minecraft/server/v1_8_R3/EnumChatFormat:RED	Lnet/minecraft/server/v1_8_R3/EnumChatFormat;
    //   435: invokevirtual 224	net/minecraft/server/v1_8_R3/ChatModifier:setColor	(Lnet/minecraft/server/v1_8_R3/EnumChatFormat;)Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   438: pop
    //   439: aload_2
    //   440: aload 9
    //   442: invokeinterface 228 2 0
    //   447: goto +371 -> 818
    //   450: astore 9
    //   452: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   455: dup
    //   456: ldc -60
    //   458: iconst_1
    //   459: anewarray 198	java/lang/Object
    //   462: dup
    //   463: iconst_0
    //   464: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   467: dup
    //   468: aload 9
    //   470: invokevirtual 201	net/minecraft/server/v1_8_R3/ExceptionUsage:getMessage	()Ljava/lang/String;
    //   473: aload 9
    //   475: invokevirtual 205	net/minecraft/server/v1_8_R3/ExceptionUsage:getArgs	()[Ljava/lang/Object;
    //   478: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   481: aastore
    //   482: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   485: astore 10
    //   487: aload 10
    //   489: invokevirtual 212	net/minecraft/server/v1_8_R3/ChatMessage:getChatModifier	()Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   492: getstatic 218	net/minecraft/server/v1_8_R3/EnumChatFormat:RED	Lnet/minecraft/server/v1_8_R3/EnumChatFormat;
    //   495: invokevirtual 224	net/minecraft/server/v1_8_R3/ChatModifier:setColor	(Lnet/minecraft/server/v1_8_R3/EnumChatFormat;)Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   498: pop
    //   499: aload_2
    //   500: aload 10
    //   502: invokeinterface 228 2 0
    //   507: invokestatic 120	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   510: aload 6
    //   512: putfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   515: goto +311 -> 826
    //   518: astore 9
    //   520: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   523: dup
    //   524: aload 9
    //   526: invokevirtual 229	net/minecraft/server/v1_8_R3/CommandException:getMessage	()Ljava/lang/String;
    //   529: aload 9
    //   531: invokevirtual 230	net/minecraft/server/v1_8_R3/CommandException:getArgs	()[Ljava/lang/Object;
    //   534: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   537: astore 10
    //   539: aload 10
    //   541: invokevirtual 212	net/minecraft/server/v1_8_R3/ChatMessage:getChatModifier	()Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   544: getstatic 218	net/minecraft/server/v1_8_R3/EnumChatFormat:RED	Lnet/minecraft/server/v1_8_R3/EnumChatFormat;
    //   547: invokevirtual 224	net/minecraft/server/v1_8_R3/ChatModifier:setColor	(Lnet/minecraft/server/v1_8_R3/EnumChatFormat;)Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   550: pop
    //   551: aload_2
    //   552: aload 10
    //   554: invokeinterface 228 2 0
    //   559: invokestatic 120	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   562: aload 6
    //   564: putfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   567: goto +259 -> 826
    //   570: astore 9
    //   572: new 194	net/minecraft/server/v1_8_R3/ChatMessage
    //   575: dup
    //   576: ldc -18
    //   578: iconst_0
    //   579: anewarray 198	java/lang/Object
    //   582: invokespecial 208	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   585: astore 10
    //   587: aload 10
    //   589: invokevirtual 212	net/minecraft/server/v1_8_R3/ChatMessage:getChatModifier	()Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   592: getstatic 218	net/minecraft/server/v1_8_R3/EnumChatFormat:RED	Lnet/minecraft/server/v1_8_R3/EnumChatFormat;
    //   595: invokevirtual 224	net/minecraft/server/v1_8_R3/ChatModifier:setColor	(Lnet/minecraft/server/v1_8_R3/EnumChatFormat;)Lnet/minecraft/server/v1_8_R3/ChatModifier;
    //   598: pop
    //   599: aload_2
    //   600: aload 10
    //   602: invokeinterface 228 2 0
    //   607: aload_2
    //   608: invokeinterface 242 1 0
    //   613: instanceof 244
    //   616: ifeq +74 -> 690
    //   619: getstatic 248	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   622: getstatic 254	org/apache/logging/log4j/Level:WARN	Lorg/apache/logging/log4j/Level;
    //   625: ldc_w 256
    //   628: iconst_3
    //   629: anewarray 198	java/lang/Object
    //   632: dup
    //   633: iconst_0
    //   634: aload_2
    //   635: invokeinterface 260 1 0
    //   640: invokevirtual 263	net/minecraft/server/v1_8_R3/BlockPosition:getX	()I
    //   643: invokestatic 269	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   646: aastore
    //   647: dup
    //   648: iconst_1
    //   649: aload_2
    //   650: invokeinterface 260 1 0
    //   655: invokevirtual 272	net/minecraft/server/v1_8_R3/BlockPosition:getY	()I
    //   658: invokestatic 269	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   661: aastore
    //   662: dup
    //   663: iconst_2
    //   664: aload_2
    //   665: invokeinterface 260 1 0
    //   670: invokevirtual 275	net/minecraft/server/v1_8_R3/BlockPosition:getZ	()I
    //   673: invokestatic 269	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   676: aastore
    //   677: invokestatic 279	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   680: aload 9
    //   682: invokeinterface 285 4 0
    //   687: goto +107 -> 794
    //   690: aload_2
    //   691: instanceof 287
    //   694: ifeq +77 -> 771
    //   697: aload_2
    //   698: checkcast 287	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract
    //   701: astore 11
    //   703: getstatic 248	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   706: getstatic 254	org/apache/logging/log4j/Level:WARN	Lorg/apache/logging/log4j/Level;
    //   709: ldc_w 289
    //   712: iconst_3
    //   713: anewarray 198	java/lang/Object
    //   716: dup
    //   717: iconst_0
    //   718: aload 11
    //   720: invokevirtual 290	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:getChunkCoordinates	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   723: invokevirtual 263	net/minecraft/server/v1_8_R3/BlockPosition:getX	()I
    //   726: invokestatic 269	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   729: aastore
    //   730: dup
    //   731: iconst_1
    //   732: aload 11
    //   734: invokevirtual 290	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:getChunkCoordinates	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   737: invokevirtual 272	net/minecraft/server/v1_8_R3/BlockPosition:getY	()I
    //   740: invokestatic 269	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   743: aastore
    //   744: dup
    //   745: iconst_2
    //   746: aload 11
    //   748: invokevirtual 290	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:getChunkCoordinates	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   751: invokevirtual 275	net/minecraft/server/v1_8_R3/BlockPosition:getZ	()I
    //   754: invokestatic 269	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   757: aastore
    //   758: invokestatic 279	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   761: aload 9
    //   763: invokeinterface 285 4 0
    //   768: goto +26 -> 794
    //   771: getstatic 248	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   774: getstatic 254	org/apache/logging/log4j/Level:WARN	Lorg/apache/logging/log4j/Level;
    //   777: ldc_w 292
    //   780: iconst_0
    //   781: anewarray 198	java/lang/Object
    //   784: invokestatic 279	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   787: aload 9
    //   789: invokeinterface 285 4 0
    //   794: invokestatic 120	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   797: aload 6
    //   799: putfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   802: goto +24 -> 826
    //   805: astore 17
    //   807: invokestatic 120	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   810: aload 6
    //   812: putfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   815: aload 17
    //   817: athrow
    //   818: invokestatic 120	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   821: aload 6
    //   823: putfield 124	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   826: aload_2
    //   827: getstatic 295	net/minecraft/server/v1_8_R3/CommandObjectiveExecutor$EnumCommandResult:SUCCESS_COUNT	Lnet/minecraft/server/v1_8_R3/CommandObjectiveExecutor$EnumCommandResult;
    //   830: iload 5
    //   832: invokeinterface 170 3 0
    //   837: iload 5
    //   839: ireturn
    // Line number table:
    //   Java source line #59	-> byte code offset #0
    //   Java source line #60	-> byte code offset #7
    //   Java source line #63	-> byte code offset #10
    //   Java source line #64	-> byte code offset #18
    //   Java source line #65	-> byte code offset #23
    //   Java source line #66	-> byte code offset #41
    //   Java source line #67	-> byte code offset #57
    //   Java source line #68	-> byte code offset #60
    //   Java source line #69	-> byte code offset #66
    //   Java source line #70	-> byte code offset #86
    //   Java source line #71	-> byte code offset #98
    //   Java source line #72	-> byte code offset #101
    //   Java source line #74	-> byte code offset #104
    //   Java source line #68	-> byte code offset #114
    //   Java source line #78	-> byte code offset #128
    //   Java source line #79	-> byte code offset #139
    //   Java source line #80	-> byte code offset #145
    //   Java source line #81	-> byte code offset #157
    //   Java source line #83	-> byte code offset #163
    //   Java source line #84	-> byte code offset #179
    //   Java source line #86	-> byte code offset #188
    //   Java source line #87	-> byte code offset #191
    //   Java source line #89	-> byte code offset #203
    //   Java source line #90	-> byte code offset #208
    //   Java source line #92	-> byte code offset #212
    //   Java source line #93	-> byte code offset #224
    //   Java source line #94	-> byte code offset #233
    //   Java source line #95	-> byte code offset #236
    //   Java source line #96	-> byte code offset #241
    //   Java source line #97	-> byte code offset #276
    //   Java source line #98	-> byte code offset #288
    //   Java source line #104	-> byte code offset #296
    //   Java source line #99	-> byte code offset #304
    //   Java source line #100	-> byte code offset #306
    //   Java source line #101	-> byte code offset #325
    //   Java source line #102	-> byte code offset #337
    //   Java source line #104	-> byte code offset #345
    //   Java source line #103	-> byte code offset #353
    //   Java source line #104	-> byte code offset #355
    //   Java source line #105	-> byte code offset #360
    //   Java source line #104	-> byte code offset #363
    //   Java source line #86	-> byte code offset #368
    //   Java source line #107	-> byte code offset #378
    //   Java source line #108	-> byte code offset #384
    //   Java source line #109	-> byte code offset #387
    //   Java source line #110	-> byte code offset #397
    //   Java source line #111	-> byte code offset #406
    //   Java source line #113	-> byte code offset #409
    //   Java source line #114	-> byte code offset #412
    //   Java source line #115	-> byte code offset #427
    //   Java source line #116	-> byte code offset #439
    //   Java source line #118	-> byte code offset #447
    //   Java source line #119	-> byte code offset #452
    //   Java source line #120	-> byte code offset #487
    //   Java source line #121	-> byte code offset #499
    //   Java source line #139	-> byte code offset #507
    //   Java source line #122	-> byte code offset #518
    //   Java source line #123	-> byte code offset #520
    //   Java source line #124	-> byte code offset #539
    //   Java source line #125	-> byte code offset #551
    //   Java source line #139	-> byte code offset #559
    //   Java source line #126	-> byte code offset #570
    //   Java source line #127	-> byte code offset #572
    //   Java source line #128	-> byte code offset #587
    //   Java source line #129	-> byte code offset #599
    //   Java source line #130	-> byte code offset #607
    //   Java source line #131	-> byte code offset #619
    //   Java source line #132	-> byte code offset #687
    //   Java source line #133	-> byte code offset #697
    //   Java source line #134	-> byte code offset #703
    //   Java source line #135	-> byte code offset #768
    //   Java source line #136	-> byte code offset #771
    //   Java source line #139	-> byte code offset #794
    //   Java source line #138	-> byte code offset #805
    //   Java source line #139	-> byte code offset #807
    //   Java source line #140	-> byte code offset #815
    //   Java source line #139	-> byte code offset #818
    //   Java source line #141	-> byte code offset #826
    //   Java source line #142	-> byte code offset #837
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	840	0	this	VanillaCommandWrapper
    //   0	840	1	bSender	CommandSender
    //   0	840	2	icommandlistener	ICommandListener
    //   0	840	3	as	String[]
    //   5	375	4	i	int
    //   8	830	5	j	int
    //   16	806	6	prev	net.minecraft.server.v1_8_R3.WorldServer[]
    //   21	99	7	server	net.minecraft.server.v1_8_R3.MinecraftServer
    //   58	14	8	bpos	int
    //   61	57	9	pos	int
    //   155	25	9	list	List<net.minecraft.server.v1_8_R3.Entity>
    //   425	16	9	chatmessage	net.minecraft.server.v1_8_R3.ChatMessage
    //   450	24	9	exceptionusage	net.minecraft.server.v1_8_R3.ExceptionUsage
    //   518	12	9	commandexception	net.minecraft.server.v1_8_R3.CommandException
    //   570	218	9	throwable	Throwable
    //   84	28	10	world	net.minecraft.server.v1_8_R3.WorldServer
    //   161	221	10	s2	String
    //   485	16	10	chatmessage1	net.minecraft.server.v1_8_R3.ChatMessage
    //   537	16	10	chatmessage2	net.minecraft.server.v1_8_R3.ChatMessage
    //   585	16	10	chatmessage3	net.minecraft.server.v1_8_R3.ChatMessage
    //   186	183	11	iterator	java.util.Iterator<net.minecraft.server.v1_8_R3.Entity>
    //   701	46	11	listener	net.minecraft.server.v1_8_R3.CommandBlockListenerAbstract
    //   201	15	12	entity	net.minecraft.server.v1_8_R3.Entity
    //   206	158	13	oldSender	CommandSender
    //   239	24	14	exceptionusage	net.minecraft.server.v1_8_R3.ExceptionUsage
    //   304	12	14	commandexception	net.minecraft.server.v1_8_R3.CommandException
    //   274	16	15	chatmessage	net.minecraft.server.v1_8_R3.ChatMessage
    //   323	16	15	chatmessage	net.minecraft.server.v1_8_R3.ChatMessage
    //   353	8	16	localObject1	Object
    //   805	11	17	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   212	236	239	net/minecraft/server/v1_8_R3/ExceptionUsage
    //   212	236	304	net/minecraft/server/v1_8_R3/CommandException
    //   212	296	353	finally
    //   304	345	353	finally
    //   128	447	450	net/minecraft/server/v1_8_R3/ExceptionUsage
    //   128	447	518	net/minecraft/server/v1_8_R3/CommandException
    //   128	447	570	java/lang/Throwable
    //   128	507	805	finally
    //   518	559	805	finally
    //   570	794	805	finally
  }
  
  private ICommandListener getListener(CommandSender sender)
  {
    if ((sender instanceof Player)) {
      return ((CraftPlayer)sender).getHandle();
    }
    if ((sender instanceof BlockCommandSender)) {
      return ((CraftBlockCommandSender)sender).getTileEntity();
    }
    if ((sender instanceof CommandMinecart)) {
      return ((EntityMinecartCommandBlock)((CraftMinecartCommand)sender).getHandle()).getCommandBlock();
    }
    if ((sender instanceof RemoteConsoleCommandSender)) {
      return RemoteControlCommandListener.getInstance();
    }
    if ((sender instanceof ConsoleCommandSender)) {
      return ((CraftServer)sender.getServer()).getServer();
    }
    if ((sender instanceof ProxiedCommandSender)) {
      return ((ProxiedNativeCommandSender)sender).getHandle();
    }
    throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
  }
  
  private int getPlayerListSize(String[] as)
  {
    for (int i = 0; i < as.length; i++) {
      if ((this.vanillaCommand.isListStart(as, i)) && (PlayerSelector.isList(as[i]))) {
        return i;
      }
    }
    return -1;
  }
  
  public static String[] dropFirstArgument(String[] as)
  {
    String[] as1 = new String[as.length - 1];
    for (int i = 1; i < as.length; i++) {
      as1[(i - 1)] = as[i];
    }
    return as1;
  }
}
