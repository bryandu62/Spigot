package net.minecraft.server.v1_8_R3;

import io.netty.buffer.ByteBufInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import org.spigotmc.LimitStream;

public class NBTCompressedStreamTools
{
  /* Error */
  public static NBTTagCompound a(InputStream inputstream)
    throws IOException
  {
    // Byte code:
    //   0: new 17	java/io/DataInputStream
    //   3: dup
    //   4: new 19	java/io/BufferedInputStream
    //   7: dup
    //   8: new 21	java/util/zip/GZIPInputStream
    //   11: dup
    //   12: aload_0
    //   13: invokespecial 24	java/util/zip/GZIPInputStream:<init>	(Ljava/io/InputStream;)V
    //   16: invokespecial 25	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   19: invokespecial 26	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   22: astore_1
    //   23: aload_1
    //   24: getstatic 31	net/minecraft/server/v1_8_R3/NBTReadLimiter:a	Lnet/minecraft/server/v1_8_R3/NBTReadLimiter;
    //   27: invokestatic 34	net/minecraft/server/v1_8_R3/NBTCompressedStreamTools:a	(Ljava/io/DataInput;Lnet/minecraft/server/v1_8_R3/NBTReadLimiter;)Lnet/minecraft/server/v1_8_R3/NBTTagCompound;
    //   30: astore_2
    //   31: goto +10 -> 41
    //   34: astore_3
    //   35: aload_1
    //   36: invokevirtual 41	java/io/DataInputStream:close	()V
    //   39: aload_3
    //   40: athrow
    //   41: aload_1
    //   42: invokevirtual 41	java/io/DataInputStream:close	()V
    //   45: aload_2
    //   46: areturn
    // Line number table:
    //   Java source line #18	-> byte code offset #0
    //   Java source line #23	-> byte code offset #23
    //   Java source line #24	-> byte code offset #31
    //   Java source line #25	-> byte code offset #35
    //   Java source line #26	-> byte code offset #39
    //   Java source line #25	-> byte code offset #41
    //   Java source line #28	-> byte code offset #45
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	47	0	inputstream	InputStream
    //   22	20	1	datainputstream	DataInputStream
    //   30	2	2	nbttagcompound	NBTTagCompound
    //   41	5	2	nbttagcompound	NBTTagCompound
    //   34	6	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   23	34	34	finally
  }
  
  /* Error */
  public static void a(NBTTagCompound nbttagcompound, java.io.OutputStream outputstream)
    throws IOException
  {
    // Byte code:
    //   0: new 52	java/io/DataOutputStream
    //   3: dup
    //   4: new 54	java/io/BufferedOutputStream
    //   7: dup
    //   8: new 56	java/util/zip/GZIPOutputStream
    //   11: dup
    //   12: aload_1
    //   13: invokespecial 59	java/util/zip/GZIPOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   16: invokespecial 60	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   19: invokespecial 61	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   22: astore_2
    //   23: aload_0
    //   24: aload_2
    //   25: invokestatic 64	net/minecraft/server/v1_8_R3/NBTCompressedStreamTools:a	(Lnet/minecraft/server/v1_8_R3/NBTTagCompound;Ljava/io/DataOutput;)V
    //   28: goto +10 -> 38
    //   31: astore_3
    //   32: aload_2
    //   33: invokevirtual 67	java/io/DataOutputStream:close	()V
    //   36: aload_3
    //   37: athrow
    //   38: aload_2
    //   39: invokevirtual 67	java/io/DataOutputStream:close	()V
    //   42: return
    // Line number table:
    //   Java source line #32	-> byte code offset #0
    //   Java source line #35	-> byte code offset #23
    //   Java source line #36	-> byte code offset #28
    //   Java source line #37	-> byte code offset #32
    //   Java source line #38	-> byte code offset #36
    //   Java source line #37	-> byte code offset #38
    //   Java source line #40	-> byte code offset #42
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	43	0	nbttagcompound	NBTTagCompound
    //   0	43	1	outputstream	java.io.OutputStream
    //   22	17	2	dataoutputstream	java.io.DataOutputStream
    //   31	6	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   23	31	31	finally
  }
  
  public static NBTTagCompound a(DataInputStream datainputstream)
    throws IOException
  {
    return a(datainputstream, NBTReadLimiter.a);
  }
  
  public static NBTTagCompound a(DataInput datainput, NBTReadLimiter nbtreadlimiter)
    throws IOException
  {
    if ((datainput instanceof ByteBufInputStream)) {
      datainput = new DataInputStream(new LimitStream((InputStream)datainput, nbtreadlimiter));
    }
    NBTBase nbtbase = a(datainput, 0, nbtreadlimiter);
    if ((nbtbase instanceof NBTTagCompound)) {
      return (NBTTagCompound)nbtbase;
    }
    throw new IOException("Root tag must be a named compound tag");
  }
  
  public static void a(NBTTagCompound nbttagcompound, DataOutput dataoutput)
    throws IOException
  {
    a(nbttagcompound, dataoutput);
  }
  
  private static void a(NBTBase nbtbase, DataOutput dataoutput)
    throws IOException
  {
    dataoutput.writeByte(nbtbase.getTypeId());
    if (nbtbase.getTypeId() != 0)
    {
      dataoutput.writeUTF("");
      nbtbase.write(dataoutput);
    }
  }
  
  private static NBTBase a(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter)
    throws IOException
  {
    byte b0 = datainput.readByte();
    if (b0 == 0) {
      return new NBTTagEnd();
    }
    datainput.readUTF();
    NBTBase nbtbase = NBTBase.createTag(b0);
    try
    {
      nbtbase.load(datainput, i, nbtreadlimiter);
      return nbtbase;
    }
    catch (IOException ioexception)
    {
      CrashReport crashreport = CrashReport.a(ioexception, "Loading NBT data");
      CrashReportSystemDetails crashreportsystemdetails = crashreport.a("NBT Tag");
      
      crashreportsystemdetails.a("Tag name", "[UNNAMED TAG]");
      crashreportsystemdetails.a("Tag type", Byte.valueOf(b0));
      throw new ReportedException(crashreport);
    }
  }
}
