package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;

public final class GameProfileSerializer
{
  public static GameProfile deserialize(NBTTagCompound ☃)
  {
    String ☃ = null;
    String ☃ = null;
    if (☃.hasKeyOfType("Name", 8)) {
      ☃ = ☃.getString("Name");
    }
    if (☃.hasKeyOfType("Id", 8)) {
      ☃ = ☃.getString("Id");
    }
    if ((!UtilColor.b(☃)) || (!UtilColor.b(☃)))
    {
      UUID ☃;
      try
      {
        ☃ = UUID.fromString(☃);
      }
      catch (Throwable ☃)
      {
        ☃ = null;
      }
      GameProfile ☃ = new GameProfile(☃, ☃);
      NBTTagCompound ☃;
      if (☃.hasKeyOfType("Properties", 10))
      {
        ☃ = ☃.getCompound("Properties");
        for (String ☃ : ☃.c())
        {
          NBTTagList ☃ = ☃.getList(☃, 10);
          for (int ☃ = 0; ☃ < ☃.size(); ☃++)
          {
            NBTTagCompound ☃ = ☃.get(☃);
            String ☃ = ☃.getString("Value");
            if (☃.hasKeyOfType("Signature", 8)) {
              ☃.getProperties().put(☃, new Property(☃, ☃, ☃.getString("Signature")));
            } else {
              ☃.getProperties().put(☃, new Property(☃, ☃));
            }
          }
        }
      }
      return ☃;
    }
    return null;
  }
  
  public static NBTTagCompound serialize(NBTTagCompound ☃, GameProfile ☃)
  {
    if (!UtilColor.b(☃.getName())) {
      ☃.setString("Name", ☃.getName());
    }
    if (☃.getId() != null) {
      ☃.setString("Id", ☃.getId().toString());
    }
    if (!☃.getProperties().isEmpty())
    {
      NBTTagCompound ☃ = new NBTTagCompound();
      for (String ☃ : ☃.getProperties().keySet())
      {
        NBTTagList ☃ = new NBTTagList();
        for (Property ☃ : ☃.getProperties().get(☃))
        {
          NBTTagCompound ☃ = new NBTTagCompound();
          ☃.setString("Value", ☃.getValue());
          if (☃.hasSignature()) {
            ☃.setString("Signature", ☃.getSignature());
          }
          ☃.add(☃);
        }
        ☃.set(☃, ☃);
      }
      ☃.set("Properties", ☃);
    }
    return ☃;
  }
  
  public static boolean a(NBTBase ☃, NBTBase ☃, boolean ☃)
  {
    if (☃ == ☃) {
      return true;
    }
    if (☃ == null) {
      return true;
    }
    if (☃ == null) {
      return false;
    }
    if (!☃.getClass().equals(☃.getClass())) {
      return false;
    }
    if ((☃ instanceof NBTTagCompound))
    {
      NBTTagCompound ☃ = (NBTTagCompound)☃;
      NBTTagCompound ☃ = (NBTTagCompound)☃;
      for (String ☃ : ☃.c())
      {
        NBTBase ☃ = ☃.get(☃);
        if (!a(☃, ☃.get(☃), ☃)) {
          return false;
        }
      }
      return true;
    }
    if (((☃ instanceof NBTTagList)) && (☃))
    {
      NBTTagList ☃ = (NBTTagList)☃;
      NBTTagList ☃ = (NBTTagList)☃;
      if (☃.size() == 0) {
        return ☃.size() == 0;
      }
      for (int ☃ = 0; ☃ < ☃.size(); ☃++)
      {
        NBTBase ☃ = ☃.g(☃);
        boolean ☃ = false;
        for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
          if (a(☃, ☃.g(☃), ☃))
          {
            ☃ = true;
            break;
          }
        }
        if (!☃) {
          return false;
        }
      }
      return true;
    }
    return ☃.equals(☃);
  }
}
