package gnu.trove.list.linked;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.procedure.TIntProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

public class TIntLinkedList
  implements TIntList, Externalizable
{
  int no_entry_value;
  int size;
  TIntLink head = null;
  TIntLink tail = this.head;
  
  public TIntLinkedList() {}
  
  public TIntLinkedList(int no_entry_value)
  {
    this.no_entry_value = no_entry_value;
  }
  
  public TIntLinkedList(TIntList list)
  {
    this.no_entry_value = list.getNoEntryValue();
    for (TIntIterator iterator = list.iterator(); iterator.hasNext();)
    {
      int next = iterator.next();
      add(next);
    }
  }
  
  public int getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  public int size()
  {
    return this.size;
  }
  
  public boolean isEmpty()
  {
    return size() == 0;
  }
  
  public boolean add(int val)
  {
    TIntLink l = new TIntLink(val);
    if (no(this.head))
    {
      this.head = l;
      this.tail = l;
    }
    else
    {
      l.setPrevious(this.tail);
      this.tail.setNext(l);
      
      this.tail = l;
    }
    this.size += 1;
    return true;
  }
  
  public void add(int[] vals)
  {
    for (int val : vals) {
      add(val);
    }
  }
  
  public void add(int[] vals, int offset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      int val = vals[(offset + i)];
      add(val);
    }
  }
  
  public void insert(int offset, int value)
  {
    TIntLinkedList tmp = new TIntLinkedList();
    tmp.add(value);
    insert(offset, tmp);
  }
  
  public void insert(int offset, int[] values)
  {
    insert(offset, link(values, 0, values.length));
  }
  
  public void insert(int offset, int[] values, int valOffset, int len)
  {
    insert(offset, link(values, valOffset, len));
  }
  
  void insert(int offset, TIntLinkedList tmp)
  {
    TIntLink l = getLinkAt(offset);
    
    this.size += tmp.size;
    if (l == this.head)
    {
      tmp.tail.setNext(this.head);
      this.head.setPrevious(tmp.tail);
      this.head = tmp.head;
      
      return;
    }
    if (no(l))
    {
      if (this.size == 0)
      {
        this.head = tmp.head;
        this.tail = tmp.tail;
      }
      else
      {
        this.tail.setNext(tmp.head);
        tmp.head.setPrevious(this.tail);
        this.tail = tmp.tail;
      }
    }
    else
    {
      TIntLink prev = l.getPrevious();
      l.getPrevious().setNext(tmp.head);
      
      tmp.tail.setNext(l);
      l.setPrevious(tmp.tail);
      
      tmp.head.setPrevious(prev);
    }
  }
  
  static TIntLinkedList link(int[] values, int valOffset, int len)
  {
    TIntLinkedList ret = new TIntLinkedList();
    for (int i = 0; i < len; i++) {
      ret.add(values[(valOffset + i)]);
    }
    return ret;
  }
  
  public int get(int offset)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TIntLink l = getLinkAt(offset);
    if (no(l)) {
      return this.no_entry_value;
    }
    return l.getValue();
  }
  
  public TIntLink getLinkAt(int offset)
  {
    if (offset >= size()) {
      return null;
    }
    if (offset <= size() >>> 1) {
      return getLink(this.head, 0, offset, true);
    }
    return getLink(this.tail, size() - 1, offset, false);
  }
  
  private static TIntLink getLink(TIntLink l, int idx, int offset)
  {
    return getLink(l, idx, offset, true);
  }
  
  private static TIntLink getLink(TIntLink l, int idx, int offset, boolean next)
  {
    int i = idx;
    while (got(l))
    {
      if (i == offset) {
        return l;
      }
      i += (next ? 1 : -1);
      l = next ? l.getNext() : l.getPrevious();
    }
    return null;
  }
  
  public int set(int offset, int val)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TIntLink l = getLinkAt(offset);
    if (no(l)) {
      throw new IndexOutOfBoundsException("at offset " + offset);
    }
    int prev = l.getValue();
    l.setValue(val);
    return prev;
  }
  
  public void set(int offset, int[] values)
  {
    set(offset, values, 0, values.length);
  }
  
  public void set(int offset, int[] values, int valOffset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      int value = values[(valOffset + i)];
      set(offset + i, value);
    }
  }
  
  public int replace(int offset, int val)
  {
    return set(offset, val);
  }
  
  public void clear()
  {
    this.size = 0;
    
    this.head = null;
    this.tail = null;
  }
  
  public boolean remove(int value)
  {
    boolean changed = false;
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value)
      {
        changed = true;
        
        removeLink(l);
      }
    }
    return changed;
  }
  
  private void removeLink(TIntLink l)
  {
    if (no(l)) {
      return;
    }
    this.size -= 1;
    
    TIntLink prev = l.getPrevious();
    TIntLink next = l.getNext();
    if (got(prev)) {
      prev.setNext(next);
    } else {
      this.head = next;
    }
    if (got(next)) {
      next.setPrevious(prev);
    } else {
      this.tail = prev;
    }
    l.setNext(null);
    l.setPrevious(null);
  }
  
  public boolean containsAll(Collection<?> collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (Object o : collection) {
      if ((o instanceof Integer))
      {
        Integer i = (Integer)o;
        if (!contains(i.intValue())) {
          return false;
        }
      }
      else
      {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(TIntCollection collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (TIntIterator it = collection.iterator(); it.hasNext();)
    {
      int i = it.next();
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(int[] array)
  {
    if (isEmpty()) {
      return false;
    }
    for (int i : array) {
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean addAll(Collection<? extends Integer> collection)
  {
    boolean ret = false;
    for (Integer v : collection) {
      if (add(v.intValue())) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(TIntCollection collection)
  {
    boolean ret = false;
    for (TIntIterator it = collection.iterator(); it.hasNext();)
    {
      int i = it.next();
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(int[] array)
  {
    boolean ret = false;
    for (int i : array) {
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean retainAll(Collection<?> collection)
  {
    boolean modified = false;
    TIntIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(Integer.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(TIntCollection collection)
  {
    boolean modified = false;
    TIntIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(int[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TIntIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) < 0)
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(Collection<?> collection)
  {
    boolean modified = false;
    TIntIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(Integer.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(TIntCollection collection)
  {
    boolean modified = false;
    TIntIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(int[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TIntIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) >= 0)
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public int removeAt(int offset)
  {
    TIntLink l = getLinkAt(offset);
    if (no(l)) {
      throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
    }
    int prev = l.getValue();
    removeLink(l);
    return prev;
  }
  
  public void remove(int offset, int length)
  {
    for (int i = 0; i < length; i++) {
      removeAt(offset);
    }
  }
  
  public void transformValues(TIntFunction function)
  {
    for (TIntLink l = this.head; got(l);)
    {
      l.setValue(function.execute(l.getValue()));
      
      l = l.getNext();
    }
  }
  
  public void reverse()
  {
    TIntLink h = this.head;
    TIntLink t = this.tail;
    
    TIntLink l = this.head;
    while (got(l))
    {
      TIntLink next = l.getNext();
      TIntLink prev = l.getPrevious();
      
      TIntLink tmp = l;
      l = l.getNext();
      
      tmp.setNext(prev);
      tmp.setPrevious(next);
    }
    this.head = t;
    this.tail = h;
  }
  
  public void reverse(int from, int to)
  {
    if (from > to) {
      throw new IllegalArgumentException("from > to : " + from + ">" + to);
    }
    TIntLink start = getLinkAt(from);
    TIntLink stop = getLinkAt(to);
    
    TIntLink tmp = null;
    
    TIntLink tmpHead = start.getPrevious();
    
    TIntLink l = start;
    while (l != stop)
    {
      TIntLink next = l.getNext();
      TIntLink prev = l.getPrevious();
      
      tmp = l;
      l = l.getNext();
      
      tmp.setNext(prev);
      tmp.setPrevious(next);
    }
    if (got(tmp))
    {
      tmpHead.setNext(tmp);
      stop.setPrevious(tmpHead);
    }
    start.setNext(stop);
    stop.setPrevious(start);
  }
  
  public void shuffle(Random rand)
  {
    for (int i = 0; i < this.size; i++)
    {
      TIntLink l = getLinkAt(rand.nextInt(size()));
      removeLink(l);
      add(l.getValue());
    }
  }
  
  public TIntList subList(int begin, int end)
  {
    if (end < begin) {
      throw new IllegalArgumentException("begin index " + begin + " greater than end index " + end);
    }
    if (this.size < begin) {
      throw new IllegalArgumentException("begin index " + begin + " greater than last index " + this.size);
    }
    if (begin < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    if (end > this.size) {
      throw new IndexOutOfBoundsException("end index < " + this.size);
    }
    TIntLinkedList ret = new TIntLinkedList();
    TIntLink tmp = getLinkAt(begin);
    for (int i = begin; i < end; i++)
    {
      ret.add(tmp.getValue());
      tmp = tmp.getNext();
    }
    return ret;
  }
  
  public int[] toArray()
  {
    return toArray(new int[this.size], 0, this.size);
  }
  
  public int[] toArray(int offset, int len)
  {
    return toArray(new int[len], offset, 0, len);
  }
  
  public int[] toArray(int[] dest)
  {
    return toArray(dest, 0, this.size);
  }
  
  public int[] toArray(int[] dest, int offset, int len)
  {
    return toArray(dest, offset, 0, len);
  }
  
  public int[] toArray(int[] dest, int source_pos, int dest_pos, int len)
  {
    if (len == 0) {
      return dest;
    }
    if ((source_pos < 0) || (source_pos >= size())) {
      throw new ArrayIndexOutOfBoundsException(source_pos);
    }
    TIntLink tmp = getLinkAt(source_pos);
    for (int i = 0; i < len; i++)
    {
      dest[(dest_pos + i)] = tmp.getValue();
      tmp = tmp.getNext();
    }
    return dest;
  }
  
  public boolean forEach(TIntProcedure procedure)
  {
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      if (!procedure.execute(l.getValue())) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachDescending(TIntProcedure procedure)
  {
    for (TIntLink l = this.tail; got(l); l = l.getPrevious()) {
      if (!procedure.execute(l.getValue())) {
        return false;
      }
    }
    return true;
  }
  
  public void sort()
  {
    sort(0, this.size);
  }
  
  public void sort(int fromIndex, int toIndex)
  {
    TIntList tmp = subList(fromIndex, toIndex);
    int[] vals = tmp.toArray();
    Arrays.sort(vals);
    set(fromIndex, vals);
  }
  
  public void fill(int val)
  {
    fill(0, this.size, val);
  }
  
  public void fill(int fromIndex, int toIndex, int val)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    TIntLink l = getLinkAt(fromIndex);
    if (toIndex > this.size)
    {
      for (int i = fromIndex; i < this.size; i++)
      {
        l.setValue(val);
        l = l.getNext();
      }
      for (int i = this.size; i < toIndex; i++) {
        add(val);
      }
    }
    else
    {
      for (int i = fromIndex; i < toIndex; i++)
      {
        l.setValue(val);
        l = l.getNext();
      }
    }
  }
  
  public int binarySearch(int value)
  {
    return binarySearch(value, 0, size());
  }
  
  public int binarySearch(int value, int fromIndex, int toIndex)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    if (toIndex > this.size) {
      throw new IndexOutOfBoundsException("end index > size: " + toIndex + " > " + this.size);
    }
    if (toIndex < fromIndex) {
      return -(fromIndex + 1);
    }
    int from = fromIndex;
    TIntLink fromLink = getLinkAt(fromIndex);
    int to = toIndex;
    while (from < to)
    {
      int mid = from + to >>> 1;
      TIntLink middle = getLink(fromLink, from, mid);
      if (middle.getValue() == value) {
        return mid;
      }
      if (middle.getValue() < value)
      {
        from = mid + 1;
        fromLink = middle.next;
      }
      else
      {
        to = mid - 1;
      }
    }
    return -(from + 1);
  }
  
  public int indexOf(int value)
  {
    return indexOf(0, value);
  }
  
  public int indexOf(int offset, int value)
  {
    int count = offset;
    for (TIntLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        return count;
      }
      count++;
    }
    return -1;
  }
  
  public int lastIndexOf(int value)
  {
    return lastIndexOf(0, value);
  }
  
  public int lastIndexOf(int offset, int value)
  {
    if (isEmpty()) {
      return -1;
    }
    int last = -1;
    int count = offset;
    for (TIntLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        last = count;
      }
      count++;
    }
    return last;
  }
  
  public boolean contains(int value)
  {
    if (isEmpty()) {
      return false;
    }
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value) {
        return true;
      }
    }
    return false;
  }
  
  public TIntIterator iterator()
  {
    new TIntIterator()
    {
      TIntLinkedList.TIntLink l = TIntLinkedList.this.head;
      TIntLinkedList.TIntLink current;
      
      public int next()
      {
        if (TIntLinkedList.no(this.l)) {
          throw new NoSuchElementException();
        }
        int ret = this.l.getValue();
        this.current = this.l;
        this.l = this.l.getNext();
        
        return ret;
      }
      
      public boolean hasNext()
      {
        return TIntLinkedList.got(this.l);
      }
      
      public void remove()
      {
        if (this.current == null) {
          throw new IllegalStateException();
        }
        TIntLinkedList.this.removeLink(this.current);
        this.current = null;
      }
    };
  }
  
  public TIntList grep(TIntProcedure condition)
  {
    TIntList ret = new TIntLinkedList();
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      if (condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public TIntList inverseGrep(TIntProcedure condition)
  {
    TIntList ret = new TIntLinkedList();
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      if (!condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public int max()
  {
    int ret = Integer.MIN_VALUE;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      if (ret < l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public int min()
  {
    int ret = Integer.MAX_VALUE;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      if (ret > l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public int sum()
  {
    int sum = 0;
    for (TIntLink l = this.head; got(l); l = l.getNext()) {
      sum += l.getValue();
    }
    return sum;
  }
  
  static class TIntLink
  {
    int value;
    TIntLink previous;
    TIntLink next;
    
    TIntLink(int value)
    {
      this.value = value;
    }
    
    public int getValue()
    {
      return this.value;
    }
    
    public void setValue(int value)
    {
      this.value = value;
    }
    
    public TIntLink getPrevious()
    {
      return this.previous;
    }
    
    public void setPrevious(TIntLink previous)
    {
      this.previous = previous;
    }
    
    public TIntLink getNext()
    {
      return this.next;
    }
    
    public void setNext(TIntLink next)
    {
      this.next = next;
    }
  }
  
  class RemoveProcedure
    implements TIntProcedure
  {
    boolean changed = false;
    
    RemoveProcedure() {}
    
    public boolean execute(int value)
    {
      if (TIntLinkedList.this.remove(value)) {
        this.changed = true;
      }
      return true;
    }
    
    public boolean isChanged()
    {
      return this.changed;
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    
    out.writeInt(this.no_entry_value);
    
    out.writeInt(this.size);
    for (TIntIterator iterator = iterator(); iterator.hasNext();)
    {
      int next = iterator.next();
      out.writeInt(next);
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    this.no_entry_value = in.readInt();
    
    int len = in.readInt();
    for (int i = 0; i < len; i++) {
      add(in.readInt());
    }
  }
  
  static boolean got(Object ref)
  {
    return ref != null;
  }
  
  static boolean no(Object ref)
  {
    return ref == null;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    TIntLinkedList that = (TIntLinkedList)o;
    if (this.no_entry_value != that.no_entry_value) {
      return false;
    }
    if (this.size != that.size) {
      return false;
    }
    TIntIterator iterator = iterator();
    TIntIterator thatIterator = that.iterator();
    while (iterator.hasNext())
    {
      if (!thatIterator.hasNext()) {
        return false;
      }
      if (iterator.next() != thatIterator.next()) {
        return false;
      }
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = HashFunctions.hash(this.no_entry_value);
    result = 31 * result + this.size;
    for (TIntIterator iterator = iterator(); iterator.hasNext();) {
      result = 31 * result + HashFunctions.hash(iterator.next());
    }
    return result;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    TIntIterator it = iterator();
    while (it.hasNext())
    {
      int next = it.next();
      buf.append(next);
      if (it.hasNext()) {
        buf.append(", ");
      }
    }
    buf.append("}");
    return buf.toString();
  }
}
