package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.util.Locale;
import java.util.Map;

public class ChatTypeAdapterFactory
  implements TypeAdapterFactory
{
  public <T> TypeAdapter<T> create(Gson ☃, TypeToken<T> ☃)
  {
    Class<T> ☃ = ☃.getRawType();
    if (!☃.isEnum()) {
      return null;
    }
    Map<String, T> ☃ = Maps.newHashMap();
    for (T ☃ : ☃.getEnumConstants()) {
      ☃.put(a(☃), ☃);
    }
    return new ChatTypeAdapter(this, ☃);
  }
  
  private String a(Object ☃)
  {
    if ((☃ instanceof Enum)) {
      return ((Enum)☃).name().toLowerCase(Locale.US);
    }
    return ☃.toString().toLowerCase(Locale.US);
  }
}
