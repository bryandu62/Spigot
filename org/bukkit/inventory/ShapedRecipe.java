package org.bukkit.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class ShapedRecipe
  implements Recipe
{
  private ItemStack output;
  private String[] rows;
  private Map<Character, ItemStack> ingredients = new HashMap();
  
  public ShapedRecipe(ItemStack result)
  {
    this.output = new ItemStack(result);
  }
  
  public ShapedRecipe shape(String... shape)
  {
    Validate.notNull(shape, "Must provide a shape");
    Validate.isTrue((shape.length > 0) && (shape.length < 4), "Crafting recipes should be 1, 2, 3 rows, not ", shape.length);
    String[] arrayOfString1;
    int j = (arrayOfString1 = shape).length;
    for (int k = 0; k < j; k++)
    {
      String row = arrayOfString1[k];
      Validate.notNull(row, "Shape cannot have null rows");
      Validate.isTrue((row.length() > 0) && (row.length() < 4), "Crafting rows should be 1, 2, or 3 characters, not ", row.length());
    }
    this.rows = new String[shape.length];
    for (int i = 0; i < shape.length; i++) {
      this.rows[i] = shape[i];
    }
    HashMap<Character, ItemStack> newIngredients = new HashMap();
    String[] arrayOfString2;
    int i = (arrayOfString2 = shape).length;
    for (j = 0; j < i; j++)
    {
      String row = arrayOfString2[j];
      char[] arrayOfChar;
      int m = (arrayOfChar = row.toCharArray()).length;
      for (int n = 0; n < m; n++)
      {
        Character c = Character.valueOf(arrayOfChar[n]);
        newIngredients.put(c, (ItemStack)this.ingredients.get(c));
      }
    }
    this.ingredients = newIngredients;
    
    return this;
  }
  
  public ShapedRecipe setIngredient(char key, MaterialData ingredient)
  {
    return setIngredient(key, ingredient.getItemType(), ingredient.getData());
  }
  
  public ShapedRecipe setIngredient(char key, Material ingredient)
  {
    return setIngredient(key, ingredient, 0);
  }
  
  @Deprecated
  public ShapedRecipe setIngredient(char key, Material ingredient, int raw)
  {
    Validate.isTrue(this.ingredients.containsKey(Character.valueOf(key)), "Symbol does not appear in the shape:", key);
    if (raw == -1) {
      raw = 32767;
    }
    this.ingredients.put(Character.valueOf(key), new ItemStack(ingredient, 1, (short)raw));
    return this;
  }
  
  public Map<Character, ItemStack> getIngredientMap()
  {
    HashMap<Character, ItemStack> result = new HashMap();
    for (Map.Entry<Character, ItemStack> ingredient : this.ingredients.entrySet()) {
      if (ingredient.getValue() == null) {
        result.put((Character)ingredient.getKey(), null);
      } else {
        result.put((Character)ingredient.getKey(), ((ItemStack)ingredient.getValue()).clone());
      }
    }
    return result;
  }
  
  public String[] getShape()
  {
    return (String[])this.rows.clone();
  }
  
  public ItemStack getResult()
  {
    return this.output.clone();
  }
}
