package gnu.trove.list.linked;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.TLongList;
import gnu.trove.procedure.TLongProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

public class TLongLinkedList
  implements TLongList, Externalizable
{
  long no_entry_value;
  int size;
  TLongLink head = null;
  TLongLink tail = this.head;
  
  public TLongLinkedList() {}
  
  public TLongLinkedList(long no_entry_value)
  {
    this.no_entry_value = no_entry_value;
  }
  
  public TLongLinkedList(TLongList list)
  {
    this.no_entry_value = list.getNoEntryValue();
    for (TLongIterator iterator = list.iterator(); iterator.hasNext();)
    {
      long next = iterator.next();
      add(next);
    }
  }
  
  public long getNoEntryValue()
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
  
  public boolean add(long val)
  {
    TLongLink l = new TLongLink(val);
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
  
  public void add(long[] vals)
  {
    for (long val : vals) {
      add(val);
    }
  }
  
  public void add(long[] vals, int offset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      long val = vals[(offset + i)];
      add(val);
    }
  }
  
  public void insert(int offset, long value)
  {
    TLongLinkedList tmp = new TLongLinkedList();
    tmp.add(value);
    insert(offset, tmp);
  }
  
  public void insert(int offset, long[] values)
  {
    insert(offset, link(values, 0, values.length));
  }
  
  public void insert(int offset, long[] values, int valOffset, int len)
  {
    insert(offset, link(values, valOffset, len));
  }
  
  void insert(int offset, TLongLinkedList tmp)
  {
    TLongLink l = getLinkAt(offset);
    
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
      TLongLink prev = l.getPrevious();
      l.getPrevious().setNext(tmp.head);
      
      tmp.tail.setNext(l);
      l.setPrevious(tmp.tail);
      
      tmp.head.setPrevious(prev);
    }
  }
  
  static TLongLinkedList link(long[] values, int valOffset, int len)
  {
    TLongLinkedList ret = new TLongLinkedList();
    for (int i = 0; i < len; i++) {
      ret.add(values[(valOffset + i)]);
    }
    return ret;
  }
  
  public long get(int offset)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TLongLink l = getLinkAt(offset);
    if (no(l)) {
      return this.no_entry_value;
    }
    return l.getValue();
  }
  
  public TLongLink getLinkAt(int offset)
  {
    if (offset >= size()) {
      return null;
    }
    if (offset <= size() >>> 1) {
      return getLink(this.head, 0, offset, true);
    }
    return getLink(this.tail, size() - 1, offset, false);
  }
  
  private static TLongLink getLink(TLongLink l, int idx, int offset)
  {
    return getLink(l, idx, offset, true);
  }
  
  private static TLongLink getLink(TLongLink l, int idx, int offset, boolean next)
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
  
  public long set(int offset, long val)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TLongLink l = getLinkAt(offset);
    if (no(l)) {
      throw new IndexOutOfBoundsException("at offset " + offset);
    }
    long prev = l.getValue();
    l.setValue(val);
    return prev;
  }
  
  public void set(int offset, long[] values)
  {
    set(offset, values, 0, values.length);
  }
  
  public void set(int offset, long[] values, int valOffset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      long value = values[(valOffset + i)];
      set(offset + i, value);
    }
  }
  
  public long replace(int offset, long val)
  {
    return set(offset, val);
  }
  
  public void clear()
  {
    this.size = 0;
    
    this.head = null;
    this.tail = null;
  }
  
  public boolean remove(long value)
  {
    boolean changed = false;
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value)
      {
        changed = true;
        
        removeLink(l);
      }
    }
    return changed;
  }
  
  private void removeLink(TLongLink l)
  {
    if (no(l)) {
      return;
    }
    this.size -= 1;
    
    TLongLink prev = l.getPrevious();
    TLongLink next = l.getNext();
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
      if ((o instanceof Long))
      {
        Long i = (Long)o;
        if (!contains(i.longValue())) {
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
  
  public boolean containsAll(TLongCollection collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (TLongIterator it = collection.iterator(); it.hasNext();)
    {
      long i = it.next();
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(long[] array)
  {
    if (isEmpty()) {
      return false;
    }
    for (long i : array) {
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean addAll(Collection<? extends Long> collection)
  {
    boolean ret = false;
    for (Long v : collection) {
      if (add(v.longValue())) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(TLongCollection collection)
  {
    boolean ret = false;
    for (TLongIterator it = collection.iterator(); it.hasNext();)
    {
      long i = it.next();
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(long[] array)
  {
    boolean ret = false;
    for (long i : array) {
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean retainAll(Collection<?> collection)
  {
    boolean modified = false;
    TLongIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(Long.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(TLongCollection collection)
  {
    boolean modified = false;
    TLongIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(long[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TLongIterator iter = iterator();
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
    TLongIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(Long.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(TLongCollection collection)
  {
    boolean modified = false;
    TLongIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(long[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TLongIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) >= 0)
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public long removeAt(int offset)
  {
    TLongLink l = getLinkAt(offset);
    if (no(l)) {
      throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
    }
    long prev = l.getValue();
    removeLink(l);
    return prev;
  }
  
  public void remove(int offset, int length)
  {
    for (int i = 0; i < length; i++) {
      removeAt(offset);
    }
  }
  
  public void transformValues(TLongFunction function)
  {
    for (TLongLink l = this.head; got(l);)
    {
      l.setValue(function.execute(l.getValue()));
      
      l = l.getNext();
    }
  }
  
  public void reverse()
  {
    TLongLink h = this.head;
    TLongLink t = this.tail;
    
    TLongLink l = this.head;
    while (got(l))
    {
      TLongLink next = l.getNext();
      TLongLink prev = l.getPrevious();
      
      TLongLink tmp = l;
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
    TLongLink start = getLinkAt(from);
    TLongLink stop = getLinkAt(to);
    
    TLongLink tmp = null;
    
    TLongLink tmpHead = start.getPrevious();
    
    TLongLink l = start;
    while (l != stop)
    {
      TLongLink next = l.getNext();
      TLongLink prev = l.getPrevious();
      
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
      TLongLink l = getLinkAt(rand.nextInt(size()));
      removeLink(l);
      add(l.getValue());
    }
  }
  
  public TLongList subList(int begin, int end)
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
    TLongLinkedList ret = new TLongLinkedList();
    TLongLink tmp = getLinkAt(begin);
    for (int i = begin; i < end; i++)
    {
      ret.add(tmp.getValue());
      tmp = tmp.getNext();
    }
    return ret;
  }
  
  public long[] toArray()
  {
    return toArray(new long[this.size], 0, this.size);
  }
  
  public long[] toArray(int offset, int len)
  {
    return toArray(new long[len], offset, 0, len);
  }
  
  public long[] toArray(long[] dest)
  {
    return toArray(dest, 0, this.size);
  }
  
  public long[] toArray(long[] dest, int offset, int len)
  {
    return toArray(dest, offset, 0, len);
  }
  
  public long[] toArray(long[] dest, int source_pos, int dest_pos, int len)
  {
    if (len == 0) {
      return dest;
    }
    if ((source_pos < 0) || (source_pos >= size())) {
      throw new ArrayIndexOutOfBoundsException(source_pos);
    }
    TLongLink tmp = getLinkAt(source_pos);
    for (int i = 0; i < len; i++)
    {
      dest[(dest_pos + i)] = tmp.getValue();
      tmp = tmp.getNext();
    }
    return dest;
  }
  
  public boolean forEach(TLongProcedure procedure)
  {
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      if (!procedure.execute(l.getValue())) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachDescending(TLongProcedure procedure)
  {
    for (TLongLink l = this.tail; got(l); l = l.getPrevious()) {
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
    TLongList tmp = subList(fromIndex, toIndex);
    long[] vals = tmp.toArray();
    Arrays.sort(vals);
    set(fromIndex, vals);
  }
  
  public void fill(long val)
  {
    fill(0, this.size, val);
  }
  
  public void fill(int fromIndex, int toIndex, long val)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    TLongLink l = getLinkAt(fromIndex);
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
  
  public int binarySearch(long value)
  {
    return binarySearch(value, 0, size());
  }
  
  public int binarySearch(long value, int fromIndex, int toIndex)
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
    TLongLink fromLink = getLinkAt(fromIndex);
    int to = toIndex;
    while (from < to)
    {
      int mid = from + to >>> 1;
      TLongLink middle = getLink(fromLink, from, mid);
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
  
  public int indexOf(long value)
  {
    return indexOf(0, value);
  }
  
  public int indexOf(int offset, long value)
  {
    int count = offset;
    for (TLongLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        return count;
      }
      count++;
    }
    return -1;
  }
  
  public int lastIndexOf(long value)
  {
    return lastIndexOf(0, value);
  }
  
  public int lastIndexOf(int offset, long value)
  {
    if (isEmpty()) {
      return -1;
    }
    int last = -1;
    int count = offset;
    for (TLongLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        last = count;
      }
      count++;
    }
    return last;
  }
  
  public boolean contains(long value)
  {
    if (isEmpty()) {
      return false;
    }
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value) {
        return true;
      }
    }
    return false;
  }
  
  public TLongIterator iterator()
  {
    new TLongIterator()
    {
      TLongLinkedList.TLongLink l = TLongLinkedList.this.head;
      TLongLinkedList.TLongLink current;
      
      public long next()
      {
        if (TLongLinkedList.no(this.l)) {
          throw new NoSuchElementException();
        }
        long ret = this.l.getValue();
        this.current = this.l;
        this.l = this.l.getNext();
        
        return ret;
      }
      
      public boolean hasNext()
      {
        return TLongLinkedList.got(this.l);
      }
      
      public void remove()
      {
        if (this.current == null) {
          throw new IllegalStateException();
        }
        TLongLinkedList.this.removeLink(this.current);
        this.current = null;
      }
    };
  }
  
  public TLongList grep(TLongProcedure condition)
  {
    TLongList ret = new TLongLinkedList();
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      if (condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public TLongList inverseGrep(TLongProcedure condition)
  {
    TLongList ret = new TLongLinkedList();
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      if (!condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public long max()
  {
    long ret = Long.MIN_VALUE;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      if (ret < l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public long min()
  {
    long ret = Long.MAX_VALUE;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      if (ret > l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public long sum()
  {
    long sum = 0L;
    for (TLongLink l = this.head; got(l); l = l.getNext()) {
      sum += l.getValue();
    }
    return sum;
  }
  
  static class TLongLink
  {
    long value;
    TLongLink previous;
    TLongLink next;
    
    TLongLink(long value)
    {
      this.value = value;
    }
    
    public long getValue()
    {
      return this.value;
    }
    
    public void setValue(long value)
    {
      this.value = value;
    }
    
    public TLongLink getPrevious()
    {
      return this.previous;
    }
    
    public void setPrevious(TLongLink previous)
    {
      this.previous = previous;
    }
    
    public TLongLink getNext()
    {
      return this.next;
    }
    
    public void setNext(TLongLink next)
    {
      this.next = next;
    }
  }
  
  class RemoveProcedure
    implements TLongProcedure
  {
    boolean changed = false;
    
    RemoveProcedure() {}
    
    public boolean execute(long value)
    {
      if (TLongLinkedList.this.remove(value)) {
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
    
    out.writeLong(this.no_entry_value);
    
    out.writeInt(this.size);
    for (TLongIterator iterator = iterator(); iterator.hasNext();)
    {
      long next = iterator.next();
      out.writeLong(next);
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    this.no_entry_value = in.readLong();
    
    int len = in.readInt();
    for (int i = 0; i < len; i++) {
      add(in.readLong());
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
    TLongLinkedList that = (TLongLinkedList)o;
    if (this.no_entry_value != that.no_entry_value) {
      return false;
    }
    if (this.size != that.size) {
      return false;
    }
    TLongIterator iterator = iterator();
    TLongIterator thatIterator = that.iterator();
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
    for (TLongIterator iterator = iterator(); iterator.hasNext();) {
      result = 31 * result + HashFunctions.hash(iterator.next());
    }
    return result;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    TLongIterator it = iterator();
    while (it.hasNext())
    {
      long next = it.next();
      buf.append(next);
      if (it.hasNext()) {
        buf.append(", ");
      }
    }
    buf.append("}");
    return buf.toString();
  }
}
