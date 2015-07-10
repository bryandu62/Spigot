package net.minecraft.server.v1_8_R3;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtilities
{
  public static final ListeningExecutorService a = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build()));
  private static final AtomicInteger b = new AtomicInteger(0);
  private static final Logger c = LogManager.getLogger();
  
  public static String a(Map<String, Object> ☃)
  {
    StringBuilder ☃ = new StringBuilder();
    for (Map.Entry<String, Object> ☃ : ☃.entrySet())
    {
      if (☃.length() > 0) {
        ☃.append('&');
      }
      try
      {
        ☃.append(URLEncoder.encode((String)☃.getKey(), "UTF-8"));
      }
      catch (UnsupportedEncodingException ☃)
      {
        ☃.printStackTrace();
      }
      if (☃.getValue() != null)
      {
        ☃.append('=');
        try
        {
          ☃.append(URLEncoder.encode(☃.getValue().toString(), "UTF-8"));
        }
        catch (UnsupportedEncodingException ☃)
        {
          ☃.printStackTrace();
        }
      }
    }
    return ☃.toString();
  }
  
  public static String a(URL ☃, Map<String, Object> ☃, boolean ☃)
  {
    return a(☃, a(☃), ☃);
  }
  
  private static String a(URL ☃, String ☃, boolean ☃)
  {
    try
    {
      Proxy ☃ = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().ay();
      if (☃ == null) {
        ☃ = Proxy.NO_PROXY;
      }
      HttpURLConnection ☃ = (HttpURLConnection)☃.openConnection(☃);
      ☃.setRequestMethod("POST");
      ☃.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      
      ☃.setRequestProperty("Content-Length", "" + ☃.getBytes().length);
      ☃.setRequestProperty("Content-Language", "en-US");
      
      ☃.setUseCaches(false);
      ☃.setDoInput(true);
      ☃.setDoOutput(true);
      
      DataOutputStream ☃ = new DataOutputStream(☃.getOutputStream());
      ☃.writeBytes(☃);
      ☃.flush();
      ☃.close();
      
      BufferedReader ☃ = new BufferedReader(new InputStreamReader(☃.getInputStream()));
      
      StringBuffer ☃ = new StringBuffer();
      String ☃;
      while ((☃ = ☃.readLine()) != null)
      {
        ☃.append(☃);
        ☃.append('\r');
      }
      ☃.close();
      return ☃.toString();
    }
    catch (Exception ☃)
    {
      if (!☃) {
        c.error("Could not post to " + ☃, ☃);
      }
    }
    return "";
  }
}
