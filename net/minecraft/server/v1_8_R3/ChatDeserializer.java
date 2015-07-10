package net.minecraft.server.v1_8_R3;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

public class ChatDeserializer
{
  public static boolean d(JsonObject ☃, String ☃)
  {
    if (!g(☃, ☃)) {
      return false;
    }
    if (!☃.get(☃).isJsonArray()) {
      return false;
    }
    return true;
  }
  
  public static boolean g(JsonObject ☃, String ☃)
  {
    if (☃ == null) {
      return false;
    }
    if (☃.get(☃) == null) {
      return false;
    }
    return true;
  }
  
  public static String a(JsonElement ☃, String ☃)
  {
    if (☃.isJsonPrimitive()) {
      return ☃.getAsString();
    }
    throw new JsonSyntaxException("Expected " + ☃ + " to be a string, was " + d(☃));
  }
  
  public static String h(JsonObject ☃, String ☃)
  {
    if (☃.has(☃)) {
      return a(☃.get(☃), ☃);
    }
    throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a string");
  }
  
  public static boolean b(JsonElement ☃, String ☃)
  {
    if (☃.isJsonPrimitive()) {
      return ☃.getAsBoolean();
    }
    throw new JsonSyntaxException("Expected " + ☃ + " to be a Boolean, was " + d(☃));
  }
  
  public static boolean a(JsonObject ☃, String ☃, boolean ☃)
  {
    if (☃.has(☃)) {
      return b(☃.get(☃), ☃);
    }
    return ☃;
  }
  
  public static float d(JsonElement ☃, String ☃)
  {
    if ((☃.isJsonPrimitive()) && (☃.getAsJsonPrimitive().isNumber())) {
      return ☃.getAsFloat();
    }
    throw new JsonSyntaxException("Expected " + ☃ + " to be a Float, was " + d(☃));
  }
  
  public static float a(JsonObject ☃, String ☃, float ☃)
  {
    if (☃.has(☃)) {
      return d(☃.get(☃), ☃);
    }
    return ☃;
  }
  
  public static int f(JsonElement ☃, String ☃)
  {
    if ((☃.isJsonPrimitive()) && (☃.getAsJsonPrimitive().isNumber())) {
      return ☃.getAsInt();
    }
    throw new JsonSyntaxException("Expected " + ☃ + " to be a Int, was " + d(☃));
  }
  
  public static int m(JsonObject ☃, String ☃)
  {
    if (☃.has(☃)) {
      return f(☃.get(☃), ☃);
    }
    throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a Int");
  }
  
  public static int a(JsonObject ☃, String ☃, int ☃)
  {
    if (☃.has(☃)) {
      return f(☃.get(☃), ☃);
    }
    return ☃;
  }
  
  public static JsonObject l(JsonElement ☃, String ☃)
  {
    if (☃.isJsonObject()) {
      return ☃.getAsJsonObject();
    }
    throw new JsonSyntaxException("Expected " + ☃ + " to be a JsonObject, was " + d(☃));
  }
  
  public static JsonArray m(JsonElement ☃, String ☃)
  {
    if (☃.isJsonArray()) {
      return ☃.getAsJsonArray();
    }
    throw new JsonSyntaxException("Expected " + ☃ + " to be a JsonArray, was " + d(☃));
  }
  
  public static JsonArray t(JsonObject ☃, String ☃)
  {
    if (☃.has(☃)) {
      return m(☃.get(☃), ☃);
    }
    throw new JsonSyntaxException("Missing " + ☃ + ", expected to find a JsonArray");
  }
  
  public static String d(JsonElement ☃)
  {
    String ☃ = StringUtils.abbreviateMiddle(String.valueOf(☃), "...", 10);
    if (☃ == null) {
      return "null (missing)";
    }
    if (☃.isJsonNull()) {
      return "null (json)";
    }
    if (☃.isJsonArray()) {
      return "an array (" + ☃ + ")";
    }
    if (☃.isJsonObject()) {
      return "an object (" + ☃ + ")";
    }
    if (☃.isJsonPrimitive())
    {
      JsonPrimitive ☃ = ☃.getAsJsonPrimitive();
      if (☃.isNumber()) {
        return "a number (" + ☃ + ")";
      }
      if (☃.isBoolean()) {
        return "a boolean (" + ☃ + ")";
      }
    }
    return ☃;
  }
}
