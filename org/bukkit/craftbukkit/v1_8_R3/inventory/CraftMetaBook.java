package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.inventory.meta.BookMeta;
import org.spigotmc.ValidateUtils;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBook
  extends CraftMetaItem
  implements BookMeta
{
  static final CraftMetaItem.ItemMetaKey BOOK_TITLE = new CraftMetaItem.ItemMetaKey("title");
  static final CraftMetaItem.ItemMetaKey BOOK_AUTHOR = new CraftMetaItem.ItemMetaKey("author");
  static final CraftMetaItem.ItemMetaKey BOOK_PAGES = new CraftMetaItem.ItemMetaKey("pages");
  static final CraftMetaItem.ItemMetaKey RESOLVED = new CraftMetaItem.ItemMetaKey("resolved");
  static final CraftMetaItem.ItemMetaKey GENERATION = new CraftMetaItem.ItemMetaKey("generation");
  static final int MAX_PAGE_LENGTH = 32767;
  static final int MAX_TITLE_LENGTH = 65535;
  protected String title;
  protected String author;
  public List<IChatBaseComponent> pages = new ArrayList();
  protected Integer generation;
  
  CraftMetaBook(CraftMetaItem meta)
  {
    super(meta);
    if ((meta instanceof CraftMetaBook))
    {
      CraftMetaBook bookMeta = (CraftMetaBook)meta;
      this.title = bookMeta.title;
      this.author = bookMeta.author;
      this.pages.addAll(bookMeta.pages);
      this.generation = bookMeta.generation;
    }
  }
  
  CraftMetaBook(NBTTagCompound tag)
  {
    this(tag, true);
  }
  
  CraftMetaBook(NBTTagCompound tag, boolean handlePages)
  {
    super(tag);
    if (tag.hasKey(BOOK_TITLE.NBT)) {
      this.title = ValidateUtils.limit(tag.getString(BOOK_TITLE.NBT), 1024);
    }
    if (tag.hasKey(BOOK_AUTHOR.NBT)) {
      this.author = ValidateUtils.limit(tag.getString(BOOK_AUTHOR.NBT), 1024);
    }
    boolean resolved = false;
    if (tag.hasKey(RESOLVED.NBT)) {
      resolved = tag.getBoolean(RESOLVED.NBT);
    }
    if (tag.hasKey(GENERATION.NBT)) {
      this.generation = Integer.valueOf(tag.getInt(GENERATION.NBT));
    }
    if ((tag.hasKey(BOOK_PAGES.NBT)) && (handlePages))
    {
      NBTTagList pages = tag.getList(BOOK_PAGES.NBT, 8);
      for (int i = 0; i < pages.size(); i++)
      {
        String page = pages.getString(i);
        if (resolved) {
          try
          {
            this.pages.add(IChatBaseComponent.ChatSerializer.a(page));
          }
          catch (Exception localException) {}
        } else {
          addPage(new String[] { ValidateUtils.limit(page, 2048) });
        }
      }
    }
  }
  
  CraftMetaBook(Map<String, Object> map)
  {
    super(map);
    
    setAuthor(CraftMetaItem.SerializableMeta.getString(map, BOOK_AUTHOR.BUKKIT, true));
    
    setTitle(CraftMetaItem.SerializableMeta.getString(map, BOOK_TITLE.BUKKIT, true));
    
    Iterable<?> pages = (Iterable)CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, BOOK_PAGES.BUKKIT, true);
    if (pages != null) {
      for (Object page : pages) {
        if ((page instanceof String)) {
          addPage(new String[] { (String)page });
        }
      }
    }
    this.generation = ((Integer)CraftMetaItem.SerializableMeta.getObject(Integer.class, map, GENERATION.BUKKIT, true));
  }
  
  void applyToItem(NBTTagCompound itemData)
  {
    applyToItem(itemData, true);
  }
  
  void applyToItem(NBTTagCompound itemData, boolean handlePages)
  {
    super.applyToItem(itemData);
    if (hasTitle()) {
      itemData.setString(BOOK_TITLE.NBT, this.title);
    }
    if (hasAuthor()) {
      itemData.setString(BOOK_AUTHOR.NBT, this.author);
    }
    if (handlePages)
    {
      if (hasPages())
      {
        NBTTagList list = new NBTTagList();
        for (IChatBaseComponent page : this.pages) {
          list.add(new NBTTagString(CraftChatMessage.fromComponent(page)));
        }
        itemData.set(BOOK_PAGES.NBT, list);
      }
      itemData.remove(RESOLVED.NBT);
    }
    if (this.generation != null) {
      itemData.setInt(GENERATION.NBT, this.generation.intValue());
    }
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (isBookEmpty());
  }
  
  boolean isBookEmpty()
  {
    return (!hasPages()) && (!hasAuthor()) && (!hasTitle());
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case SPRUCE_WOOD_STAIRS: 
    case STAINED_CLAY: 
      return true;
    }
    return false;
  }
  
  public boolean hasAuthor()
  {
    return !Strings.isNullOrEmpty(this.author);
  }
  
  public boolean hasTitle()
  {
    return !Strings.isNullOrEmpty(this.title);
  }
  
  public boolean hasPages()
  {
    return !this.pages.isEmpty();
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public boolean setTitle(String title)
  {
    if (title == null)
    {
      this.title = null;
      return true;
    }
    if (title.length() > 65535) {
      return false;
    }
    this.title = title;
    return true;
  }
  
  public String getAuthor()
  {
    return this.author;
  }
  
  public void setAuthor(String author)
  {
    this.author = author;
  }
  
  public String getPage(int page)
  {
    Validate.isTrue(isValidPage(page), "Invalid page number");
    return CraftChatMessage.fromComponent((IChatBaseComponent)this.pages.get(page - 1));
  }
  
  public void setPage(int page, String text)
  {
    if (!isValidPage(page)) {
      throw new IllegalArgumentException("Invalid page number " + page + "/" + this.pages.size());
    }
    String newText = text.length() > 32767 ? text.substring(0, 32767) : text == null ? "" : text;
    this.pages.set(page - 1, CraftChatMessage.fromString(newText, true)[0]);
  }
  
  public void setPages(String... pages)
  {
    this.pages.clear();
    
    addPage(pages);
  }
  
  public void addPage(String... pages)
  {
    String[] arrayOfString;
    int i = (arrayOfString = pages).length;
    for (int j = 0; j < i; j++)
    {
      String page = arrayOfString[j];
      if (page == null) {
        page = "";
      } else if (page.length() > 32767) {
        page = page.substring(0, 32767);
      }
      this.pages.add(CraftChatMessage.fromString(page, true)[0]);
    }
  }
  
  public int getPageCount()
  {
    return this.pages.size();
  }
  
  public List<String> getPages()
  {
    final List<IChatBaseComponent> copy = ImmutableList.copyOf(this.pages);
    new AbstractList()
    {
      public String get(int index)
      {
        return CraftChatMessage.fromComponent((IChatBaseComponent)copy.get(index));
      }
      
      public int size()
      {
        return copy.size();
      }
    };
  }
  
  public void setPages(List<String> pages)
  {
    this.pages.clear();
    for (String page : pages) {
      addPage(new String[] { page });
    }
  }
  
  private boolean isValidPage(int page)
  {
    return (page > 0) && (page <= this.pages.size());
  }
  
  public CraftMetaBook clone()
  {
    CraftMetaBook meta = (CraftMetaBook)super.clone();
    meta.pages = new ArrayList(this.pages);
    return meta;
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasTitle()) {
      hash = 61 * hash + this.title.hashCode();
    }
    if (hasAuthor()) {
      hash = 61 * hash + 13 * this.author.hashCode();
    }
    if (hasPages()) {
      hash = 61 * hash + 17 * this.pages.hashCode();
    }
    return original != hash ? CraftMetaBook.class.hashCode() ^ hash : hash;
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaBook))
    {
      CraftMetaBook that = (CraftMetaBook)meta;
      
      return (hasTitle() ? (that.hasTitle()) && (this.title.equals(that.title)) : !that.hasTitle()) && 
        (hasAuthor() ? (that.hasAuthor()) && (this.author.equals(that.author)) : !that.hasAuthor()) && 
        (hasPages() ? (that.hasPages()) && (this.pages.equals(that.pages)) : !that.hasPages());
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaBook)) || (isBookEmpty()));
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (hasTitle()) {
      builder.put(BOOK_TITLE.BUKKIT, this.title);
    }
    if (hasAuthor()) {
      builder.put(BOOK_AUTHOR.BUKKIT, this.author);
    }
    if (hasPages())
    {
      List<String> pagesString = new ArrayList();
      for (IChatBaseComponent comp : this.pages) {
        pagesString.add(CraftChatMessage.fromComponent(comp));
      }
      builder.put(BOOK_PAGES.BUKKIT, pagesString);
    }
    if (this.generation != null) {
      builder.put(GENERATION.BUKKIT, this.generation);
    }
    return builder;
  }
}
