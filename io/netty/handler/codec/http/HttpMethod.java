package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.util.HashMap;
import java.util.Map;

public class HttpMethod
  implements Comparable<HttpMethod>
{
  public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS", true);
  public static final HttpMethod GET = new HttpMethod("GET", true);
  public static final HttpMethod HEAD = new HttpMethod("HEAD", true);
  public static final HttpMethod POST = new HttpMethod("POST", true);
  public static final HttpMethod PUT = new HttpMethod("PUT", true);
  public static final HttpMethod PATCH = new HttpMethod("PATCH", true);
  public static final HttpMethod DELETE = new HttpMethod("DELETE", true);
  public static final HttpMethod TRACE = new HttpMethod("TRACE", true);
  public static final HttpMethod CONNECT = new HttpMethod("CONNECT", true);
  private static final Map<String, HttpMethod> methodMap = new HashMap();
  private final String name;
  private final byte[] bytes;
  
  static
  {
    methodMap.put(OPTIONS.toString(), OPTIONS);
    methodMap.put(GET.toString(), GET);
    methodMap.put(HEAD.toString(), HEAD);
    methodMap.put(POST.toString(), POST);
    methodMap.put(PUT.toString(), PUT);
    methodMap.put(PATCH.toString(), PATCH);
    methodMap.put(DELETE.toString(), DELETE);
    methodMap.put(TRACE.toString(), TRACE);
    methodMap.put(CONNECT.toString(), CONNECT);
  }
  
  public static HttpMethod valueOf(String name)
  {
    if (name == null) {
      throw new NullPointerException("name");
    }
    name = name.trim();
    if (name.isEmpty()) {
      throw new IllegalArgumentException("empty name");
    }
    HttpMethod result = (HttpMethod)methodMap.get(name);
    if (result != null) {
      return result;
    }
    return new HttpMethod(name);
  }
  
  public HttpMethod(String name)
  {
    this(name, false);
  }
  
  private HttpMethod(String name, boolean bytes)
  {
    if (name == null) {
      throw new NullPointerException("name");
    }
    name = name.trim();
    if (name.isEmpty()) {
      throw new IllegalArgumentException("empty name");
    }
    for (int i = 0; i < name.length(); i++) {
      if ((Character.isISOControl(name.charAt(i))) || (Character.isWhitespace(name.charAt(i)))) {
        throw new IllegalArgumentException("invalid character in name");
      }
    }
    this.name = name;
    if (bytes) {
      this.bytes = name.getBytes(CharsetUtil.US_ASCII);
    } else {
      this.bytes = null;
    }
  }
  
  public String name()
  {
    return this.name;
  }
  
  public int hashCode()
  {
    return name().hashCode();
  }
  
  public boolean equals(Object o)
  {
    if (!(o instanceof HttpMethod)) {
      return false;
    }
    HttpMethod that = (HttpMethod)o;
    return name().equals(that.name());
  }
  
  public String toString()
  {
    return name();
  }
  
  public int compareTo(HttpMethod o)
  {
    return name().compareTo(o.name());
  }
  
  void encode(ByteBuf buf)
  {
    if (this.bytes == null) {
      HttpHeaders.encodeAscii0(this.name, buf);
    } else {
      buf.writeBytes(this.bytes);
    }
  }
}