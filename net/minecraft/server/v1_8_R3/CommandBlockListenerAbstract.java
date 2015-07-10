package net.minecraft.server.v1_8_R3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import org.bukkit.command.CommandSender;

public abstract class CommandBlockListenerAbstract
  implements ICommandListener
{
  private static final SimpleDateFormat a = new SimpleDateFormat("HH:mm:ss");
  private int b;
  private boolean c = true;
  private IChatBaseComponent d = null;
  private String e = "";
  private String f = "@";
  private final CommandObjectiveExecutor g = new CommandObjectiveExecutor();
  protected CommandSender sender;
  
  public int j()
  {
    return this.b;
  }
  
  public IChatBaseComponent k()
  {
    return this.d;
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setString("Command", this.e);
    nbttagcompound.setInt("SuccessCount", this.b);
    nbttagcompound.setString("CustomName", this.f);
    nbttagcompound.setBoolean("TrackOutput", this.c);
    if ((this.d != null) && (this.c)) {
      nbttagcompound.setString("LastOutput", IChatBaseComponent.ChatSerializer.a(this.d));
    }
    this.g.b(nbttagcompound);
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    this.e = nbttagcompound.getString("Command");
    this.b = nbttagcompound.getInt("SuccessCount");
    if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
      this.f = nbttagcompound.getString("CustomName");
    }
    if (nbttagcompound.hasKeyOfType("TrackOutput", 1)) {
      this.c = nbttagcompound.getBoolean("TrackOutput");
    }
    if ((nbttagcompound.hasKeyOfType("LastOutput", 8)) && (this.c)) {
      this.d = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("LastOutput"));
    }
    this.g.a(nbttagcompound);
  }
  
  public boolean a(int i, String s)
  {
    return i <= 2;
  }
  
  public void setCommand(String s)
  {
    this.e = s;
    this.b = 0;
  }
  
  public String getCommand()
  {
    return this.e;
  }
  
  public void a(World world)
  {
    if (world.isClientSide) {
      this.b = 0;
    }
    MinecraftServer minecraftserver = MinecraftServer.getServer();
    if ((minecraftserver != null) && (minecraftserver.O()) && (minecraftserver.getEnableCommandBlock()))
    {
      minecraftserver.getCommandHandler();
      try
      {
        this.d = null;
        
        this.b = executeCommand(this, this.sender, this.e);
      }
      catch (Throwable throwable)
      {
        CrashReport crashreport = CrashReport.a(throwable, "Executing command block");
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Command to be executed");
        
        crashreportsystemdetails.a("Command", new Callable()
        {
          public String a()
            throws Exception
          {
            return CommandBlockListenerAbstract.this.getCommand();
          }
          
          public Object call()
            throws Exception
          {
            return a();
          }
        });
        crashreportsystemdetails.a("Name", new Callable()
        {
          public String a()
            throws Exception
          {
            return CommandBlockListenerAbstract.this.getName();
          }
          
          public Object call()
            throws Exception
          {
            return a();
          }
        });
        throw new ReportedException(crashreport);
      }
    }
    else
    {
      this.b = 0;
    }
  }
  
  /* Error */
  public static int executeCommand(ICommandListener sender, CommandSender bSender, String command)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokeinterface 205 1 0
    //   6: invokevirtual 208	net/minecraft/server/v1_8_R3/World:getServer	()Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   9: invokevirtual 214	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getCommandMap	()Lorg/bukkit/command/SimpleCommandMap;
    //   12: astore_3
    //   13: ldc -40
    //   15: invokestatic 222	com/google/common/base/Joiner:on	(Ljava/lang/String;)Lcom/google/common/base/Joiner;
    //   18: astore 4
    //   20: aload_2
    //   21: ldc -32
    //   23: invokevirtual 229	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   26: ifeq +9 -> 35
    //   29: aload_2
    //   30: iconst_1
    //   31: invokevirtual 233	java/lang/String:substring	(I)Ljava/lang/String;
    //   34: astore_2
    //   35: aload_2
    //   36: ldc -40
    //   38: invokevirtual 239	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   41: astore 5
    //   43: new 241	java/util/ArrayList
    //   46: dup
    //   47: invokespecial 242	java/util/ArrayList:<init>	()V
    //   50: astore 6
    //   52: aload 5
    //   54: iconst_0
    //   55: aaload
    //   56: astore 7
    //   58: aload 7
    //   60: ldc -12
    //   62: invokevirtual 229	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   65: ifeq +15 -> 80
    //   68: aload 7
    //   70: ldc -12
    //   72: invokevirtual 247	java/lang/String:length	()I
    //   75: invokevirtual 233	java/lang/String:substring	(I)Ljava/lang/String;
    //   78: astore 7
    //   80: aload 7
    //   82: ldc -5
    //   84: invokevirtual 229	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   87: ifeq +15 -> 102
    //   90: aload 7
    //   92: ldc -5
    //   94: invokevirtual 247	java/lang/String:length	()I
    //   97: invokevirtual 233	java/lang/String:substring	(I)Ljava/lang/String;
    //   100: astore 7
    //   102: aload 7
    //   104: ldc -3
    //   106: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   109: ifne +91 -> 200
    //   112: aload 7
    //   114: ldc_w 258
    //   117: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   120: ifne +80 -> 200
    //   123: aload 7
    //   125: ldc_w 260
    //   128: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   131: ifne +69 -> 200
    //   134: aload 7
    //   136: ldc_w 262
    //   139: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   142: ifne +58 -> 200
    //   145: aload 7
    //   147: ldc_w 264
    //   150: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   153: ifne +47 -> 200
    //   156: aload 7
    //   158: ldc_w 266
    //   161: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   164: ifne +36 -> 200
    //   167: aload 7
    //   169: ldc_w 268
    //   172: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   175: ifne +25 -> 200
    //   178: aload 7
    //   180: ldc_w 270
    //   183: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   186: ifne +14 -> 200
    //   189: aload 7
    //   191: ldc_w 272
    //   194: invokevirtual 256	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   197: ifeq +5 -> 202
    //   200: iconst_0
    //   201: ireturn
    //   202: aload_0
    //   203: invokeinterface 205 1 0
    //   208: getfield 276	net/minecraft/server/v1_8_R3/World:players	Ljava/util/List;
    //   211: invokeinterface 281 1 0
    //   216: ifeq +5 -> 221
    //   219: iconst_0
    //   220: ireturn
    //   221: aload_3
    //   222: aload 5
    //   224: iconst_0
    //   225: aaload
    //   226: invokevirtual 284	org/bukkit/command/SimpleCommandMap:getCommand	(Ljava/lang/String;)Lorg/bukkit/command/Command;
    //   229: astore 8
    //   231: aload_0
    //   232: invokeinterface 205 1 0
    //   237: invokevirtual 208	net/minecraft/server/v1_8_R3/World:getServer	()Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   240: aload 5
    //   242: iconst_0
    //   243: aaload
    //   244: invokevirtual 287	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getCommandBlockOverride	(Ljava/lang/String;)Z
    //   247: ifeq +28 -> 275
    //   250: aload_3
    //   251: new 289	java/lang/StringBuilder
    //   254: dup
    //   255: ldc -12
    //   257: invokespecial 290	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   260: aload 5
    //   262: iconst_0
    //   263: aaload
    //   264: invokevirtual 294	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   267: invokevirtual 297	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   270: invokevirtual 284	org/bukkit/command/SimpleCommandMap:getCommand	(Ljava/lang/String;)Lorg/bukkit/command/Command;
    //   273: astore 8
    //   275: aload 8
    //   277: instanceof 301
    //   280: ifeq +65 -> 345
    //   283: aload_2
    //   284: invokevirtual 304	java/lang/String:trim	()Ljava/lang/String;
    //   287: astore_2
    //   288: aload_2
    //   289: ldc -32
    //   291: invokevirtual 229	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   294: ifeq +9 -> 303
    //   297: aload_2
    //   298: iconst_1
    //   299: invokevirtual 233	java/lang/String:substring	(I)Ljava/lang/String;
    //   302: astore_2
    //   303: aload_2
    //   304: ldc -40
    //   306: invokevirtual 239	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   309: astore 9
    //   311: aload 9
    //   313: invokestatic 308	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:dropFirstArgument	([Ljava/lang/String;)[Ljava/lang/String;
    //   316: astore 9
    //   318: aload 8
    //   320: checkcast 301	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper
    //   323: aload_1
    //   324: invokevirtual 312	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:testPermission	(Lorg/bukkit/command/CommandSender;)Z
    //   327: ifne +5 -> 332
    //   330: iconst_0
    //   331: ireturn
    //   332: aload 8
    //   334: checkcast 301	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper
    //   337: aload_1
    //   338: aload_0
    //   339: aload 9
    //   341: invokevirtual 316	org/bukkit/craftbukkit/v1_8_R3/command/VanillaCommandWrapper:dispatchVanillaCommand	(Lorg/bukkit/command/CommandSender;Lnet/minecraft/server/v1_8_R3/ICommandListener;[Ljava/lang/String;)I
    //   344: ireturn
    //   345: aload_3
    //   346: aload 5
    //   348: iconst_0
    //   349: aaload
    //   350: invokevirtual 284	org/bukkit/command/SimpleCommandMap:getCommand	(Ljava/lang/String;)Lorg/bukkit/command/Command;
    //   353: ifnonnull +5 -> 358
    //   356: iconst_0
    //   357: ireturn
    //   358: aload 6
    //   360: aload 5
    //   362: invokevirtual 320	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   365: pop
    //   366: invokestatic 146	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   369: getfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   372: astore 9
    //   374: invokestatic 146	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   377: astore 10
    //   379: aload 10
    //   381: aload 10
    //   383: getfield 327	net/minecraft/server/v1_8_R3/MinecraftServer:worlds	Ljava/util/List;
    //   386: invokeinterface 330 1 0
    //   391: anewarray 332	net/minecraft/server/v1_8_R3/WorldServer
    //   394: putfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   397: aload 10
    //   399: getfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   402: iconst_0
    //   403: aload_0
    //   404: invokeinterface 205 1 0
    //   409: checkcast 332	net/minecraft/server/v1_8_R3/WorldServer
    //   412: aastore
    //   413: iconst_0
    //   414: istore 11
    //   416: iconst_1
    //   417: istore 12
    //   419: goto +54 -> 473
    //   422: aload 10
    //   424: getfield 327	net/minecraft/server/v1_8_R3/MinecraftServer:worlds	Ljava/util/List;
    //   427: iload 11
    //   429: iinc 11 1
    //   432: invokeinterface 339 2 0
    //   437: checkcast 332	net/minecraft/server/v1_8_R3/WorldServer
    //   440: astore 13
    //   442: aload 10
    //   444: getfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   447: iconst_0
    //   448: aaload
    //   449: aload 13
    //   451: if_acmpne +9 -> 460
    //   454: iinc 12 -1
    //   457: goto +13 -> 470
    //   460: aload 10
    //   462: getfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   465: iload 12
    //   467: aload 13
    //   469: aastore
    //   470: iinc 12 1
    //   473: iload 12
    //   475: aload 10
    //   477: getfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   480: arraylength
    //   481: if_icmplt -59 -> 422
    //   484: new 241	java/util/ArrayList
    //   487: dup
    //   488: invokespecial 242	java/util/ArrayList:<init>	()V
    //   491: astore 12
    //   493: iconst_0
    //   494: istore 13
    //   496: goto +75 -> 571
    //   499: aload 5
    //   501: iload 13
    //   503: aaload
    //   504: invokestatic 344	net/minecraft/server/v1_8_R3/PlayerSelector:isPattern	(Ljava/lang/String;)Z
    //   507: ifeq +61 -> 568
    //   510: iconst_0
    //   511: istore 14
    //   513: goto +28 -> 541
    //   516: aload 12
    //   518: aload_0
    //   519: aload 6
    //   521: iload 14
    //   523: invokevirtual 345	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   526: checkcast 249	[Ljava/lang/String;
    //   529: iload 13
    //   531: invokestatic 349	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:buildCommands	(Lnet/minecraft/server/v1_8_R3/ICommandListener;[Ljava/lang/String;I)Ljava/util/ArrayList;
    //   534: invokevirtual 353	java/util/ArrayList:addAll	(Ljava/util/Collection;)Z
    //   537: pop
    //   538: iinc 14 1
    //   541: iload 14
    //   543: aload 6
    //   545: invokevirtual 354	java/util/ArrayList:size	()I
    //   548: if_icmplt -32 -> 516
    //   551: aload 6
    //   553: astore 14
    //   555: aload 12
    //   557: astore 6
    //   559: aload 14
    //   561: astore 12
    //   563: aload 12
    //   565: invokevirtual 357	java/util/ArrayList:clear	()V
    //   568: iinc 13 1
    //   571: iload 13
    //   573: aload 5
    //   575: arraylength
    //   576: if_icmplt -77 -> 499
    //   579: goto +16 -> 595
    //   582: astore 15
    //   584: invokestatic 146	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   587: aload 9
    //   589: putfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   592: aload 15
    //   594: athrow
    //   595: invokestatic 146	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   598: aload 9
    //   600: putfield 324	net/minecraft/server/v1_8_R3/MinecraftServer:worldServer	[Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   603: iconst_0
    //   604: istore 12
    //   606: iconst_0
    //   607: istore 13
    //   609: goto +239 -> 848
    //   612: aload_3
    //   613: aload_1
    //   614: aload 4
    //   616: aload 6
    //   618: iload 13
    //   620: invokevirtual 345	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   623: checkcast 249	[Ljava/lang/String;
    //   626: invokestatic 363	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   629: invokevirtual 367	com/google/common/base/Joiner:join	(Ljava/lang/Iterable;)Ljava/lang/String;
    //   632: invokevirtual 371	org/bukkit/command/SimpleCommandMap:dispatch	(Lorg/bukkit/command/CommandSender;Ljava/lang/String;)Z
    //   635: ifeq +210 -> 845
    //   638: iinc 12 1
    //   641: goto +204 -> 845
    //   644: astore 14
    //   646: aload_0
    //   647: invokeinterface 374 1 0
    //   652: instanceof 376
    //   655: ifeq +78 -> 733
    //   658: invokestatic 146	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   661: getfield 380	net/minecraft/server/v1_8_R3/MinecraftServer:server	Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   664: invokevirtual 384	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getLogger	()Ljava/util/logging/Logger;
    //   667: getstatic 390	java/util/logging/Level:WARNING	Ljava/util/logging/Level;
    //   670: ldc_w 392
    //   673: iconst_3
    //   674: anewarray 4	java/lang/Object
    //   677: dup
    //   678: iconst_0
    //   679: aload_0
    //   680: invokeinterface 396 1 0
    //   685: invokevirtual 401	net/minecraft/server/v1_8_R3/BlockPosition:getX	()I
    //   688: invokestatic 407	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   691: aastore
    //   692: dup
    //   693: iconst_1
    //   694: aload_0
    //   695: invokeinterface 396 1 0
    //   700: invokevirtual 410	net/minecraft/server/v1_8_R3/BlockPosition:getY	()I
    //   703: invokestatic 407	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   706: aastore
    //   707: dup
    //   708: iconst_2
    //   709: aload_0
    //   710: invokeinterface 396 1 0
    //   715: invokevirtual 413	net/minecraft/server/v1_8_R3/BlockPosition:getZ	()I
    //   718: invokestatic 407	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   721: aastore
    //   722: invokestatic 417	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   725: aload 14
    //   727: invokevirtual 423	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   730: goto +115 -> 845
    //   733: aload_0
    //   734: instanceof 2
    //   737: ifeq +81 -> 818
    //   740: aload_0
    //   741: checkcast 2	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract
    //   744: astore 15
    //   746: invokestatic 146	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   749: getfield 380	net/minecraft/server/v1_8_R3/MinecraftServer:server	Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   752: invokevirtual 384	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getLogger	()Ljava/util/logging/Logger;
    //   755: getstatic 390	java/util/logging/Level:WARNING	Ljava/util/logging/Level;
    //   758: ldc_w 425
    //   761: iconst_3
    //   762: anewarray 4	java/lang/Object
    //   765: dup
    //   766: iconst_0
    //   767: aload 15
    //   769: invokevirtual 426	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:getChunkCoordinates	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   772: invokevirtual 401	net/minecraft/server/v1_8_R3/BlockPosition:getX	()I
    //   775: invokestatic 407	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   778: aastore
    //   779: dup
    //   780: iconst_1
    //   781: aload 15
    //   783: invokevirtual 426	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:getChunkCoordinates	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   786: invokevirtual 410	net/minecraft/server/v1_8_R3/BlockPosition:getY	()I
    //   789: invokestatic 407	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   792: aastore
    //   793: dup
    //   794: iconst_2
    //   795: aload 15
    //   797: invokevirtual 426	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:getChunkCoordinates	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   800: invokevirtual 413	net/minecraft/server/v1_8_R3/BlockPosition:getZ	()I
    //   803: invokestatic 407	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   806: aastore
    //   807: invokestatic 417	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   810: aload 14
    //   812: invokevirtual 423	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   815: goto +30 -> 845
    //   818: invokestatic 146	net/minecraft/server/v1_8_R3/MinecraftServer:getServer	()Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   821: getfield 380	net/minecraft/server/v1_8_R3/MinecraftServer:server	Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   824: invokevirtual 384	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getLogger	()Ljava/util/logging/Logger;
    //   827: getstatic 390	java/util/logging/Level:WARNING	Ljava/util/logging/Level;
    //   830: ldc_w 428
    //   833: iconst_0
    //   834: anewarray 4	java/lang/Object
    //   837: invokestatic 417	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   840: aload 14
    //   842: invokevirtual 423	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   845: iinc 13 1
    //   848: iload 13
    //   850: aload 6
    //   852: invokevirtual 354	java/util/ArrayList:size	()I
    //   855: if_icmplt -243 -> 612
    //   858: iload 12
    //   860: ireturn
    // Line number table:
    //   Java source line #126	-> byte code offset #0
    //   Java source line #127	-> byte code offset #13
    //   Java source line #128	-> byte code offset #20
    //   Java source line #129	-> byte code offset #29
    //   Java source line #131	-> byte code offset #35
    //   Java source line #132	-> byte code offset #43
    //   Java source line #134	-> byte code offset #52
    //   Java source line #135	-> byte code offset #58
    //   Java source line #136	-> byte code offset #80
    //   Java source line #139	-> byte code offset #102
    //   Java source line #140	-> byte code offset #134
    //   Java source line #141	-> byte code offset #167
    //   Java source line #142	-> byte code offset #200
    //   Java source line #146	-> byte code offset #202
    //   Java source line #147	-> byte code offset #219
    //   Java source line #151	-> byte code offset #221
    //   Java source line #152	-> byte code offset #231
    //   Java source line #153	-> byte code offset #250
    //   Java source line #155	-> byte code offset #275
    //   Java source line #156	-> byte code offset #283
    //   Java source line #157	-> byte code offset #288
    //   Java source line #158	-> byte code offset #297
    //   Java source line #160	-> byte code offset #303
    //   Java source line #161	-> byte code offset #311
    //   Java source line #162	-> byte code offset #318
    //   Java source line #163	-> byte code offset #330
    //   Java source line #165	-> byte code offset #332
    //   Java source line #169	-> byte code offset #345
    //   Java source line #170	-> byte code offset #356
    //   Java source line #173	-> byte code offset #358
    //   Java source line #176	-> byte code offset #366
    //   Java source line #177	-> byte code offset #374
    //   Java source line #178	-> byte code offset #379
    //   Java source line #179	-> byte code offset #397
    //   Java source line #180	-> byte code offset #413
    //   Java source line #181	-> byte code offset #416
    //   Java source line #182	-> byte code offset #422
    //   Java source line #183	-> byte code offset #442
    //   Java source line #184	-> byte code offset #454
    //   Java source line #185	-> byte code offset #457
    //   Java source line #187	-> byte code offset #460
    //   Java source line #181	-> byte code offset #470
    //   Java source line #190	-> byte code offset #484
    //   Java source line #191	-> byte code offset #493
    //   Java source line #192	-> byte code offset #499
    //   Java source line #193	-> byte code offset #510
    //   Java source line #194	-> byte code offset #516
    //   Java source line #193	-> byte code offset #538
    //   Java source line #196	-> byte code offset #551
    //   Java source line #197	-> byte code offset #555
    //   Java source line #198	-> byte code offset #559
    //   Java source line #199	-> byte code offset #563
    //   Java source line #191	-> byte code offset #568
    //   Java source line #202	-> byte code offset #579
    //   Java source line #203	-> byte code offset #584
    //   Java source line #204	-> byte code offset #592
    //   Java source line #203	-> byte code offset #595
    //   Java source line #206	-> byte code offset #603
    //   Java source line #209	-> byte code offset #606
    //   Java source line #211	-> byte code offset #612
    //   Java source line #212	-> byte code offset #638
    //   Java source line #214	-> byte code offset #641
    //   Java source line #215	-> byte code offset #646
    //   Java source line #216	-> byte code offset #658
    //   Java source line #217	-> byte code offset #730
    //   Java source line #218	-> byte code offset #740
    //   Java source line #219	-> byte code offset #746
    //   Java source line #220	-> byte code offset #815
    //   Java source line #221	-> byte code offset #818
    //   Java source line #209	-> byte code offset #845
    //   Java source line #226	-> byte code offset #858
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	861	0	sender	ICommandListener
    //   0	861	1	bSender	CommandSender
    //   0	861	2	command	String
    //   12	601	3	commandMap	org.bukkit.command.SimpleCommandMap
    //   18	597	4	joiner	com.google.common.base.Joiner
    //   41	533	5	args	String[]
    //   50	801	6	commands	ArrayList<String[]>
    //   56	134	7	cmd	String
    //   229	104	8	commandBlockCommand	org.bukkit.command.Command
    //   309	31	9	as	String[]
    //   372	227	9	prev	WorldServer[]
    //   377	99	10	server	MinecraftServer
    //   414	14	11	bpos	int
    //   417	57	12	pos	int
    //   491	73	12	newCommands	ArrayList<String[]>
    //   604	255	12	completed	int
    //   440	28	13	world	WorldServer
    //   494	78	13	i	int
    //   607	242	13	i	int
    //   511	31	14	j	int
    //   553	7	14	temp	ArrayList<String[]>
    //   644	197	14	exception	Throwable
    //   582	11	15	localObject	Object
    //   744	52	15	listener	CommandBlockListenerAbstract
    // Exception table:
    //   from	to	target	type
    //   484	582	582	finally
    //   612	641	644	java/lang/Throwable
  }
  
  private static ArrayList<String[]> buildCommands(ICommandListener sender, String[] args, int pos)
  {
    ArrayList<String[]> commands = new ArrayList();
    List<EntityPlayer> players = PlayerSelector.getPlayers(sender, args[pos], EntityPlayer.class);
    if (players != null) {
      for (EntityPlayer player : players) {
        if (player.world == sender.getWorld())
        {
          String[] command = (String[])args.clone();
          command[pos] = player.getName();
          commands.add(command);
        }
      }
    }
    return commands;
  }
  
  public String getName()
  {
    return this.f;
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    return new ChatComponentText(getName());
  }
  
  public void setName(String s)
  {
    this.f = s;
  }
  
  public void sendMessage(IChatBaseComponent ichatbasecomponent)
  {
    if ((this.c) && (getWorld() != null) && (!getWorld().isClientSide))
    {
      this.d = new ChatComponentText("[" + a.format(new Date()) + "] ").addSibling(ichatbasecomponent);
      h();
    }
  }
  
  public boolean getSendCommandFeedback()
  {
    MinecraftServer minecraftserver = MinecraftServer.getServer();
    
    return (minecraftserver == null) || (!minecraftserver.O()) || (minecraftserver.worldServer[0].getGameRules().getBoolean("commandBlockOutput"));
  }
  
  public void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i)
  {
    this.g.a(this, commandobjectiveexecutor_enumcommandresult, i);
  }
  
  public abstract void h();
  
  public void b(IChatBaseComponent ichatbasecomponent)
  {
    this.d = ichatbasecomponent;
  }
  
  public void a(boolean flag)
  {
    this.c = flag;
  }
  
  public boolean m()
  {
    return this.c;
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    if (!entityhuman.abilities.canInstantlyBuild) {
      return false;
    }
    if (entityhuman.getWorld().isClientSide) {
      entityhuman.a(this);
    }
    return true;
  }
  
  public CommandObjectiveExecutor n()
  {
    return this.g;
  }
}
