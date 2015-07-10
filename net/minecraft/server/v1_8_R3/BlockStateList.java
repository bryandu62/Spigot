package net.minecraft.server.v1_8_R3;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BlockStateList
{
  private static final Joiner a = Joiner.on(", ");
  private static final Function<IBlockState, String> b = new Function()
  {
    public String a(IBlockState ☃)
    {
      return ☃ == null ? "<NULL>" : ☃.a();
    }
  };
  private final Block c;
  private final ImmutableList<IBlockState> d;
  private final ImmutableList<IBlockData> e;
  
  public BlockStateList(Block ☃, IBlockState... ☃)
  {
    this.c = ☃;
    
    Arrays.sort(☃, new Comparator()
    {
      public int a(IBlockState ☃, IBlockState ☃)
      {
        return ☃.a().compareTo(☃.a());
      }
    });
    this.d = ImmutableList.copyOf(☃);
    
    Map<Map<IBlockState, Comparable>, BlockData> ☃ = Maps.newLinkedHashMap();
    List<BlockData> ☃ = Lists.newArrayList();
    
    Iterable<List<Comparable>> ☃ = IteratorUtils.a(e());
    for (List<Comparable> ☃ : ☃)
    {
      Map<IBlockState, Comparable> ☃ = MapGeneratorUtils.b(this.d, ☃);
      BlockData ☃ = new BlockData(☃, ImmutableMap.copyOf(☃), null);
      
      ☃.put(☃, ☃);
      ☃.add(☃);
    }
    for (BlockData ☃ : ☃) {
      ☃.a(☃);
    }
    this.e = ImmutableList.copyOf(☃);
  }
  
  public ImmutableList<IBlockData> a()
  {
    return this.e;
  }
  
  private List<Iterable<Comparable>> e()
  {
    List<Iterable<Comparable>> ☃ = Lists.newArrayList();
    for (int ☃ = 0; ☃ < this.d.size(); ☃++) {
      ☃.add(((IBlockState)this.d.get(☃)).c());
    }
    return ☃;
  }
  
  public IBlockData getBlockData()
  {
    return (IBlockData)this.e.get(0);
  }
  
  public Block getBlock()
  {
    return this.c;
  }
  
  public Collection<IBlockState> d()
  {
    return this.d;
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("block", Block.REGISTRY.c(this.c)).add("properties", Iterables.transform(this.d, b)).toString();
  }
  
  static class BlockData
    extends BlockDataAbstract
  {
    private final Block a;
    private final ImmutableMap<IBlockState, Comparable> b;
    private ImmutableTable<IBlockState, Comparable, IBlockData> c;
    
    private BlockData(Block ☃, ImmutableMap<IBlockState, Comparable> ☃)
    {
      this.a = ☃;
      this.b = ☃;
    }
    
    public Collection<IBlockState> a()
    {
      return Collections.unmodifiableCollection(this.b.keySet());
    }
    
    public <T extends Comparable<T>> T get(IBlockState<T> ☃)
    {
      if (!this.b.containsKey(☃)) {
        throw new IllegalArgumentException("Cannot get property " + ☃ + " as it does not exist in " + this.a.P());
      }
      return (Comparable)☃.b().cast(this.b.get(☃));
    }
    
    public <T extends Comparable<T>, V extends T> IBlockData set(IBlockState<T> ☃, V ☃)
    {
      if (!this.b.containsKey(☃)) {
        throw new IllegalArgumentException("Cannot set property " + ☃ + " as it does not exist in " + this.a.P());
      }
      if (!☃.c().contains(☃)) {
        throw new IllegalArgumentException("Cannot set property " + ☃ + " to " + ☃ + " on block " + Block.REGISTRY.c(this.a) + ", it is not an allowed value");
      }
      if (this.b.get(☃) == ☃) {
        return this;
      }
      return (IBlockData)this.c.get(☃, ☃);
    }
    
    public ImmutableMap<IBlockState, Comparable> b()
    {
      return this.b;
    }
    
    public Block getBlock()
    {
      return this.a;
    }
    
    public boolean equals(Object ☃)
    {
      return this == ☃;
    }
    
    public int hashCode()
    {
      return this.b.hashCode();
    }
    
    public void a(Map<Map<IBlockState, Comparable>, BlockData> ☃)
    {
      if (this.c != null) {
        throw new IllegalStateException();
      }
      Table<IBlockState, Comparable, IBlockData> ☃ = HashBasedTable.create();
      for (Iterator ☃ = this.b.keySet().iterator(); ☃.hasNext();)
      {
        ☃ = (IBlockState)☃.next();
        for (Comparable ☃ : ☃.c()) {
          if (☃ != this.b.get(☃)) {
            ☃.put(☃, ☃, ☃.get(b(☃, ☃)));
          }
        }
      }
      IBlockState<? extends Comparable> ☃;
      this.c = ImmutableTable.copyOf(☃);
    }
    
    private Map<IBlockState, Comparable> b(IBlockState ☃, Comparable ☃)
    {
      Map<IBlockState, Comparable> ☃ = Maps.newHashMap(this.b);
      ☃.put(☃, ☃);
      return ☃;
    }
  }
}
