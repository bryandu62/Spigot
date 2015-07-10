package net.minecraft.server.v1_8_R3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;

public abstract interface IChatBaseComponent
  extends Iterable<IChatBaseComponent>
{
  public abstract IChatBaseComponent setChatModifier(ChatModifier paramChatModifier);
  
  public abstract ChatModifier getChatModifier();
  
  public abstract IChatBaseComponent a(String paramString);
  
  public abstract IChatBaseComponent addSibling(IChatBaseComponent paramIChatBaseComponent);
  
  public abstract String getText();
  
  public abstract String c();
  
  public abstract List<IChatBaseComponent> a();
  
  public abstract IChatBaseComponent f();
  
  public static class ChatSerializer
    implements JsonDeserializer<IChatBaseComponent>, JsonSerializer<IChatBaseComponent>
  {
    private static final Gson a;
    
    static
    {
      GsonBuilder ☃ = new GsonBuilder();
      ☃.registerTypeHierarchyAdapter(IChatBaseComponent.class, new ChatSerializer());
      ☃.registerTypeHierarchyAdapter(ChatModifier.class, new ChatModifier.ChatModifierSerializer());
      ☃.registerTypeAdapterFactory(new ChatTypeAdapterFactory());
      a = ☃.create();
    }
    
    public IChatBaseComponent a(JsonElement ☃, Type ☃, JsonDeserializationContext ☃)
      throws JsonParseException
    {
      if (☃.isJsonPrimitive()) {
        return new ChatComponentText(☃.getAsString());
      }
      if (☃.isJsonObject())
      {
        JsonObject ☃ = ☃.getAsJsonObject();
        IChatBaseComponent ☃;
        if (☃.has("text"))
        {
          ☃ = new ChatComponentText(☃.get("text").getAsString());
        }
        else
        {
          IChatBaseComponent ☃;
          if (☃.has("translate"))
          {
            String ☃ = ☃.get("translate").getAsString();
            IChatBaseComponent ☃;
            if (☃.has("with"))
            {
              JsonArray ☃ = ☃.getAsJsonArray("with");
              Object[] ☃ = new Object[☃.size()];
              for (int ☃ = 0; ☃ < ☃.length; ☃++)
              {
                ☃[☃] = a(☃.get(☃), ☃, ☃);
                if ((☃[☃] instanceof ChatComponentText))
                {
                  ChatComponentText ☃ = (ChatComponentText)☃[☃];
                  if ((☃.getChatModifier().g()) && (☃.a().isEmpty())) {
                    ☃[☃] = ☃.g();
                  }
                }
              }
              ☃ = new ChatMessage(☃, ☃);
            }
            else
            {
              ☃ = new ChatMessage(☃, new Object[0]);
            }
          }
          else
          {
            IChatBaseComponent ☃;
            if (☃.has("score"))
            {
              JsonObject ☃ = ☃.getAsJsonObject("score");
              if ((☃.has("name")) && (☃.has("objective")))
              {
                IChatBaseComponent ☃ = new ChatComponentScore(ChatDeserializer.h(☃, "name"), ChatDeserializer.h(☃, "objective"));
                if (☃.has("value")) {
                  ((ChatComponentScore)☃).b(ChatDeserializer.h(☃, "value"));
                }
              }
              else
              {
                throw new JsonParseException("A score component needs a least a name and an objective");
              }
            }
            else
            {
              IChatBaseComponent ☃;
              if (☃.has("selector")) {
                ☃ = new ChatComponentSelector(ChatDeserializer.h(☃, "selector"));
              } else {
                throw new JsonParseException("Don't know how to turn " + ☃.toString() + " into a Component");
              }
            }
          }
        }
        IChatBaseComponent ☃;
        if (☃.has("extra"))
        {
          JsonArray ☃ = ☃.getAsJsonArray("extra");
          if (☃.size() > 0) {
            for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
              ☃.addSibling(a(☃.get(☃), ☃, ☃));
            }
          } else {
            throw new JsonParseException("Unexpected empty array of components");
          }
        }
        ☃.setChatModifier((ChatModifier)☃.deserialize(☃, ChatModifier.class));
        
        return ☃;
      }
      if (☃.isJsonArray())
      {
        JsonArray ☃ = ☃.getAsJsonArray();
        IChatBaseComponent ☃ = null;
        for (JsonElement ☃ : ☃)
        {
          IChatBaseComponent ☃ = a(☃, ☃.getClass(), ☃);
          if (☃ == null) {
            ☃ = ☃;
          } else {
            ☃.addSibling(☃);
          }
        }
        return ☃;
      }
      throw new JsonParseException("Don't know how to turn " + ☃.toString() + " into a Component");
    }
    
    private void a(ChatModifier ☃, JsonObject ☃, JsonSerializationContext ☃)
    {
      JsonElement ☃ = ☃.serialize(☃);
      if (☃.isJsonObject())
      {
        JsonObject ☃ = (JsonObject)☃;
        for (Map.Entry<String, JsonElement> ☃ : ☃.entrySet()) {
          ☃.add((String)☃.getKey(), (JsonElement)☃.getValue());
        }
      }
    }
    
    public JsonElement a(IChatBaseComponent ☃, Type ☃, JsonSerializationContext ☃)
    {
      if (((☃ instanceof ChatComponentText)) && (☃.getChatModifier().g()) && (☃.a().isEmpty())) {
        return new JsonPrimitive(((ChatComponentText)☃).g());
      }
      JsonObject ☃ = new JsonObject();
      if (!☃.getChatModifier().g()) {
        a(☃.getChatModifier(), ☃, ☃);
      }
      if (!☃.a().isEmpty())
      {
        JsonArray ☃ = new JsonArray();
        for (IChatBaseComponent ☃ : ☃.a()) {
          ☃.add(a(☃, ☃.getClass(), ☃));
        }
        ☃.add("extra", ☃);
      }
      if ((☃ instanceof ChatComponentText))
      {
        ☃.addProperty("text", ((ChatComponentText)☃).g());
      }
      else if ((☃ instanceof ChatMessage))
      {
        ChatMessage ☃ = (ChatMessage)☃;
        ☃.addProperty("translate", ☃.i());
        if ((☃.j() != null) && (☃.j().length > 0))
        {
          JsonArray ☃ = new JsonArray();
          for (Object ☃ : ☃.j()) {
            if ((☃ instanceof IChatBaseComponent)) {
              ☃.add(a((IChatBaseComponent)☃, ☃.getClass(), ☃));
            } else {
              ☃.add(new JsonPrimitive(String.valueOf(☃)));
            }
          }
          ☃.add("with", ☃);
        }
      }
      else if ((☃ instanceof ChatComponentScore))
      {
        ChatComponentScore ☃ = (ChatComponentScore)☃;
        JsonObject ☃ = new JsonObject();
        ☃.addProperty("name", ☃.g());
        ☃.addProperty("objective", ☃.h());
        ☃.addProperty("value", ☃.getText());
        ☃.add("score", ☃);
      }
      else if ((☃ instanceof ChatComponentSelector))
      {
        ChatComponentSelector ☃ = (ChatComponentSelector)☃;
        ☃.addProperty("selector", ☃.g());
      }
      else
      {
        throw new IllegalArgumentException("Don't know how to serialize " + ☃ + " as a Component");
      }
      return ☃;
    }
    
    public static String a(IChatBaseComponent ☃)
    {
      return a.toJson(☃);
    }
    
    public static IChatBaseComponent a(String ☃)
    {
      return (IChatBaseComponent)a.fromJson(☃, IChatBaseComponent.class);
    }
  }
}
