package net.md_5.bungee.chat;

import com.google.common.base.Preconditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Arrays;
import java.util.HashSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class BaseComponentSerializer
{
  protected void deserialize(JsonObject object, BaseComponent component, JsonDeserializationContext context)
  {
    if (object.has("color")) {
      component.setColor(ChatColor.valueOf(object.get("color").getAsString().toUpperCase()));
    }
    if (object.has("bold")) {
      component.setBold(Boolean.valueOf(object.get("bold").getAsBoolean()));
    }
    if (object.has("italic")) {
      component.setItalic(Boolean.valueOf(object.get("italic").getAsBoolean()));
    }
    if (object.has("underlined")) {
      component.setUnderlined(Boolean.valueOf(object.get("underlined").getAsBoolean()));
    }
    if (object.has("strikethrough")) {
      component.setStrikethrough(Boolean.valueOf(object.get("strikethrough").getAsBoolean()));
    }
    if (object.has("obfuscated")) {
      component.setObfuscated(Boolean.valueOf(object.get("obfuscated").getAsBoolean()));
    }
    if (object.has("extra")) {
      component.setExtra(Arrays.asList((Object[])context.deserialize(object.get("extra"), BaseComponent[].class)));
    }
    if (object.has("clickEvent"))
    {
      JsonObject event = object.getAsJsonObject("clickEvent");
      component.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(event.get("action").getAsString().toUpperCase()), event.get("value").getAsString()));
    }
    if (object.has("hoverEvent"))
    {
      JsonObject event = object.getAsJsonObject("hoverEvent");
      BaseComponent[] res;
      BaseComponent[] res;
      if (event.get("value").isJsonArray()) {
        res = (BaseComponent[])context.deserialize(event.get("value"), BaseComponent[].class);
      } else {
        res = new BaseComponent[] { (BaseComponent)context.deserialize(event.get("value"), BaseComponent.class) };
      }
      component.setHoverEvent(new HoverEvent(HoverEvent.Action.valueOf(event.get("action").getAsString().toUpperCase()), res));
    }
  }
  
  protected void serialize(JsonObject object, BaseComponent component, JsonSerializationContext context)
  {
    boolean first = false;
    if (ComponentSerializer.serializedComponents.get() == null)
    {
      first = true;
      ComponentSerializer.serializedComponents.set(new HashSet());
    }
    try
    {
      Preconditions.checkArgument(!((HashSet)ComponentSerializer.serializedComponents.get()).contains(component), "Component loop");
      ((HashSet)ComponentSerializer.serializedComponents.get()).add(component);
      if (component.getColorRaw() != null) {
        object.addProperty("color", component.getColorRaw().getName());
      }
      if (component.isBoldRaw() != null) {
        object.addProperty("bold", component.isBoldRaw());
      }
      if (component.isItalicRaw() != null) {
        object.addProperty("italic", component.isItalicRaw());
      }
      if (component.isUnderlinedRaw() != null) {
        object.addProperty("underlined", component.isUnderlinedRaw());
      }
      if (component.isStrikethroughRaw() != null) {
        object.addProperty("strikethrough", component.isStrikethroughRaw());
      }
      if (component.isObfuscatedRaw() != null) {
        object.addProperty("obfuscated", component.isObfuscatedRaw());
      }
      if (component.getExtra() != null) {
        object.add("extra", context.serialize(component.getExtra()));
      }
      if (component.getClickEvent() != null)
      {
        JsonObject clickEvent = new JsonObject();
        clickEvent.addProperty("action", component.getClickEvent().getAction().toString().toLowerCase());
        clickEvent.addProperty("value", component.getClickEvent().getValue());
        object.add("clickEvent", clickEvent);
      }
      if (component.getHoverEvent() != null)
      {
        JsonObject hoverEvent = new JsonObject();
        hoverEvent.addProperty("action", component.getHoverEvent().getAction().toString().toLowerCase());
        hoverEvent.add("value", context.serialize(component.getHoverEvent().getValue()));
        object.add("hoverEvent", hoverEvent);
      }
    }
    finally
    {
      ((HashSet)ComponentSerializer.serializedComponents.get()).remove(component);
      if (first) {
        ComponentSerializer.serializedComponents.set(null);
      }
    }
  }
}
