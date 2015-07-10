package gnu.trove.list.linked;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.list.TByteList;
import gnu.trove.procedure.TByteProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

public class TByteLinkedList
  implements TByteList, Externalizable
{
  byte no_entry_value;
  int size;
  TByteLink head = null;
  TByteLink tail = this.head;
  
  public TByteLinkedList() {}
  
  public TByteLinkedList(byte no_entry_value)
  {
    this.no_entry_value = no_entry_value;
  }
  
  public TByteLinkedList(TByteList list)
  {
    this.no_entry_value = list.getNoEntryValue();
    for (TByteIterator iterator = list.iterator(); iterator.hasNext();)
    {
      byte next = iterator.next();
      add(next);
    }
  }
  
  public byte getNoEntryValue()
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
  
  public boolean add(byte val)
  {
    TByteLink l = new TByteLink(val);
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
  
  public void add(byte[] vals)
  {
    for (byte val : vals) {
      add(val);
    }
  }
  
  public void add(byte[] vals, int offset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      byte val = vals[(offset + i)];
      add(val);
    }
  }
  
  public void insert(int offset, byte value)
  {
    TByteLinkedList tmp = new TByteLinkedList();
    tmp.add(value);
    insert(offset, tmp);
  }
  
  public void insert(int offset, byte[] values)
  {
    insert(offset, link(values, 0, values.length));
  }
  
  public void insert(int offset, byte[] values, int valOffset, int len)
  {
    insert(offset, link(values, valOffset, len));
  }
  
  void insert(int offset, TByteLinkedList tmp)
  {
    TByteLink l = getLinkAt(offset);
    
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
      TByteLink prev = l.getPrevious();
      l.getPrevious().setNext(tmp.head);
      
      tmp.tail.setNext(l);
      l.setPrevious(tmp.tail);
      
      tmp.head.setPrevious(prev);
    }
  }
  
  static TByteLinkedList link(byte[] values, int valOffset, int len)
  {
    TByteLinkedList ret = new TByteLinkedList();
    for (int i = 0; i < len; i++) {
      ret.add(values[(valOffset + i)]);
    }
    return ret;
  }
  
  public byte get(int offset)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TByteLink l = getLinkAt(offset);
    if (no(l)) {
      return this.no_entry_value;
    }
    return l.getValue();
  }
  
  public TByteLink getLinkAt(int offset)
  {
    if (offset >= size()) {
      return null;
    }
    if (offset <= size() >>> 1) {
      return getLink(this.head, 0, offset, true);
    }
    return getLink(this.tail, size() - 1, offset, false);
  }
  
  private static TByteLink getLink(TByteLink l, int idx, int offset)
  {
    return getLink(l, idx, offset, true);
  }
  
  private static TByteLink getLink(TByteLink l, int idx, int offset, boolean next)
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
  
  public byte set(int offset, byte val)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TByteLink l = getLinkAt(offset);
    if (no(l)) {
      throw new IndexOutOfBoundsException("at offset " + offset);
    }
    byte prev = l.getValue();
    l.setValue(val);
    return prev;
  }
  
  public void set(int offset, byte[] values)
  {
    set(offset, values, 0, values.length);
  }
  
  public void set(int offset, byte[] values, int valOffset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      byte value = values[(valOffset + i)];
      set(offset + i, value);
    }
  }
  
  public byte replace(int offset, byte val)
  {
    return set(offset, val);
  }
  
  public void clear()
  {
    this.size = 0;
    
    this.head = null;
    this.tail = null;
  }
  
  public boolean remove(byte value)
  {
    boolean changed = false;
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value)
      {
        changed = true;
        
        removeLink(l);
      }
    }
    return changed;
  }
  
  private void removeLink(TByteLink l)
  {
    if (no(l)) {
      return;
    }
    this.size -= 1;
    
    TByteLink prev = l.getPrevious();
    TByteLink next = l.getNext();
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
      if ((o instanceof Byte))
      {
        Byte i = (Byte)o;
        if (!contains(i.byteValue())) {
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
  
  public boolean containsAll(TByteCollection collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (TByteIterator it = collection.iterator(); it.hasNext();)
    {
      byte i = it.next();
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(byte[] array)
  {
    if (isEmpty()) {
      return false;
    }
    for (byte i : array) {
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean addAll(Collection<? extends Byte> collection)
  {
    boolean ret = false;
    for (Byte v : collection) {
      if (add(v.byteValue())) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(TByteCollection collection)
  {
    boolean ret = false;
    for (TByteIterator it = collection.iterator(); it.hasNext();)
    {
      byte i = it.next();
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(byte[] array)
  {
    boolean ret = false;
    for (byte i : array) {
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean retainAll(Collection<?> collection)
  {
    boolean modified = false;
    TByteIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(Byte.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(TByteCollection collection)
  {
    boolean modified = false;
    TByteIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(byte[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TByteIterator iter = iterator();
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
    TByteIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(Byte.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(TByteCollection collection)
  {
    boolean modified = false;
    TByteIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(byte[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TByteIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) >= 0)
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public byte removeAt(int offset)
  {
    TByteLink l = getLinkAt(offset);
    if (no(l)) {
      throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
    }
    byte prev = l.getValue();
    removeLink(l);
    return prev;
  }
  
  public void remove(int offset, int length)
  {
    for (int i = 0; i < length; i++) {
      removeAt(offset);
    }
  }
  
  public void transformValues(TByteFunction function)
  {
    for (TByteLink l = this.head; got(l);)
    {
      l.setValue(function.execute(l.getValue()));
      
      l = l.getNext();
    }
  }
  
  public void reverse()
  {
    TByteLink h = this.head;
    TByteLink t = this.tail;
    
    TByteLink l = this.head;
    while (got(l))
    {
      TByteLink next = l.getNext();
      TByteLink prev = l.getPrevious();
      
      TByteLink tmp = l;
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
    TByteLink start = getLinkAt(from);
    TByteLink stop = getLinkAt(to);
    
    TByteLink tmp = null;
    
    TByteLink tmpHead = start.getPrevious();
    
    TByteLink l = start;
    while (l != stop)
    {
      TByteLink next = l.getNext();
      TByteLink prev = l.getPrevious();
      
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
      TByteLink l = getLinkAt(rand.nextInt(size()));
      removeLink(l);
      add(l.getValue());
    }
  }
  
  public TByteList subList(int begin, int end)
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
    TByteLinkedList ret = new TByteLinkedList();
    TByteLink tmp = getLinkAt(begin);
    for (int i = begin; i < end; i++)
    {
      ret.add(tmp.getValue());
      tmp = tmp.getNext();
    }
    return ret;
  }
  
  public byte[] toArray()
  {
    return toArray(new byte[this.size], 0, this.size);
  }
  
  public byte[] toArray(int offset, int len)
  {
    return toArray(new byte[len], offset, 0, len);
  }
  
  public byte[] toArray(byte[] dest)
  {
    return toArray(dest, 0, this.size);
  }
  
  public byte[] toArray(byte[] dest, int offset, int len)
  {
    return toArray(dest, offset, 0, len);
  }
  
  public byte[] toArray(byte[] dest, int source_pos, int dest_pos, int len)
  {
    if (len == 0) {
      return dest;
    }
    if ((source_pos < 0) || (source_pos >= size())) {
      throw new ArrayIndexOutOfBoundsException(source_pos);
    }
    TByteLink tmp = getLinkAt(source_pos);
    for (int i = 0; i < len; i++)
    {
      dest[(dest_pos + i)] = tmp.getValue();
      tmp = tmp.getNext();
    }
    return dest;
  }
  
  public boolean forEach(TByteProcedure procedure)
  {
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      if (!procedure.execute(l.getValue())) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachDescending(TByteProcedure procedure)
  {
    for (TByteLink l = this.tail; got(l); l = l.getPrevious()) {
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
    TByteList tmp = subList(fromIndex, toIndex);
    byte[] vals = tmp.toArray();
    Arrays.sort(vals);
    set(fromIndex, vals);
  }
  
  public void fill(byte val)
  {
    fill(0, this.size, val);
  }
  
  public void fill(int fromIndex, int toIndex, byte val)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    TByteLink l = getLinkAt(fromIndex);
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
  
  public int binarySearch(byte value)
  {
    return binarySearch(value, 0, size());
  }
  
  public int binarySearch(byte value, int fromIndex, int toIndex)
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
    TByteLink fromLink = getLinkAt(fromIndex);
    int to = toIndex;
    while (from < to)
    {
      int mid = from + to >>> 1;
      TByteLink middle = getLink(fromLink, from, mid);
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
  
  public int indexOf(byte value)
  {
    return indexOf(0, value);
  }
  
  public int indexOf(int offset, byte value)
  {
    int count = offset;
    for (TByteLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        return count;
      }
      count++;
    }
    return -1;
  }
  
  public int lastIndexOf(byte value)
  {
    return lastIndexOf(0, value);
  }
  
  public int lastIndexOf(int offset, byte value)
  {
    if (isEmpty()) {
      return -1;
    }
    int last = -1;
    int count = offset;
    for (TByteLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        last = count;
      }
      count++;
    }
    return last;
  }
  
  public boolean contains(byte value)
  {
    if (isEmpty()) {
      return false;
    }
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value) {
        return true;
      }
    }
    return false;
  }
  
  public TByteIterator iterator()
  {
    new TByteIterator()
    {
      TByteLinkedList.TByteLink l = TByteLinkedList.this.head;
      TByteLinkedList.TByteLink current;
      
      public byte next()
      {
        if (TByteLinkedList.no(this.l)) {
          throw new NoSuchElementException();
        }
        byte ret = this.l.getValue();
        this.current = this.l;
        this.l = this.l.getNext();
        
        return ret;
      }
      
      public boolean hasNext()
      {
        return TByteLinkedList.got(this.l);
      }
      
      public void remove()
      {
        if (this.current == null) {
          throw new IllegalStateException();
        }
        TByteLinkedList.this.removeLink(this.current);
        this.current = null;
      }
    };
  }
  
  public TByteList grep(TByteProcedure condition)
  {
    TByteList ret = new TByteLinkedList();
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      if (condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public TByteList inverseGrep(TByteProcedure condition)
  {
    TByteList ret = new TByteLinkedList();
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      if (!condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public byte max()
  {
    byte ret = Byte.MIN_VALUE;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      if (ret < l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public byte min()
  {
    byte ret = Byte.MAX_VALUE;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      if (ret > l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public byte sum()
  {
    byte sum = 0;
    for (TByteLink l = this.head; got(l); l = l.getNext()) {
      sum = (byte)(sum + l.getValue());
    }
    return sum;
  }
  
  static class TByteLink
  {
    byte value;
    TByteLink previous;
    TByteLink next;
    
    TByteLink(byte value)
    {
      this.value = value;
    }
    
    public byte getValue()
    {
      return this.value;
    }
    
    public void setValue(byte value)
    {
      this.value = value;
    }
    
    public TByteLink getPrevious()
    {
      return this.previous;
    }
    
    public void setPrevious(TByteLink previous)
    {
      this.previous = previous;
    }
    
    public TByteLink getNext()
    {
      return this.next;
    }
    
    public void setNext(TByteLink next)
    {
      this.next = next;
    }
  }
  
  class RemoveProcedure
    implements TByteProcedure
  {
    boolean changed = false;
    
    RemoveProcedure() {}
    
    public boolean execute(byte value)
    {
      if (TByteLinkedList.this.remove(value)) {
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
    
    out.writeByte(this.no_entry_value);
    
    out.writeInt(this.size);
    for (TByteIterator iterator = iterator(); iterator.hasNext();)
    {
      byte next = iterator.next();
      out.writeByte(next);
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    this.no_entry_value = in.readByte();
    
    int len = in.readInt();
    for (int i = 0; i < len; i++) {
      add(in.readByte());
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
    TByteLinkedList that = (TByteLinkedList)o;
    if (this.no_entry_value != that.no_entry_value) {
      return false;
    }
    if (this.size != that.size) {
      return false;
    }
    TByteIterator iterator = iterator();
    TByteIterator thatIterator = that.iterator();
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
    for (TByteIterator iterator = iterator(); iterator.hasNext();) {
      result = 31 * result + HashFunctions.hash(iterator.next());
    }
    return result;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    TByteIterator it = iterator();
    while (it.hasNext())
    {
      byte next = it.next();
      buf.append(next);
      if (it.hasNext()) {
        buf.append(", ");
      }
    }
    buf.append("}");
    return buf.toString();
  }
}
