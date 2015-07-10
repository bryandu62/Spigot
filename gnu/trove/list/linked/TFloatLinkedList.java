package gnu.trove.list.linked;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.list.TFloatList;
import gnu.trove.procedure.TFloatProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

public class TFloatLinkedList
  implements TFloatList, Externalizable
{
  float no_entry_value;
  int size;
  TFloatLink head = null;
  TFloatLink tail = this.head;
  
  public TFloatLinkedList() {}
  
  public TFloatLinkedList(float no_entry_value)
  {
    this.no_entry_value = no_entry_value;
  }
  
  public TFloatLinkedList(TFloatList list)
  {
    this.no_entry_value = list.getNoEntryValue();
    for (TFloatIterator iterator = list.iterator(); iterator.hasNext();)
    {
      float next = iterator.next();
      add(next);
    }
  }
  
  public float getNoEntryValue()
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
  
  public boolean add(float val)
  {
    TFloatLink l = new TFloatLink(val);
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
  
  public void add(float[] vals)
  {
    for (float val : vals) {
      add(val);
    }
  }
  
  public void add(float[] vals, int offset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      float val = vals[(offset + i)];
      add(val);
    }
  }
  
  public void insert(int offset, float value)
  {
    TFloatLinkedList tmp = new TFloatLinkedList();
    tmp.add(value);
    insert(offset, tmp);
  }
  
  public void insert(int offset, float[] values)
  {
    insert(offset, link(values, 0, values.length));
  }
  
  public void insert(int offset, float[] values, int valOffset, int len)
  {
    insert(offset, link(values, valOffset, len));
  }
  
  void insert(int offset, TFloatLinkedList tmp)
  {
    TFloatLink l = getLinkAt(offset);
    
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
      TFloatLink prev = l.getPrevious();
      l.getPrevious().setNext(tmp.head);
      
      tmp.tail.setNext(l);
      l.setPrevious(tmp.tail);
      
      tmp.head.setPrevious(prev);
    }
  }
  
  static TFloatLinkedList link(float[] values, int valOffset, int len)
  {
    TFloatLinkedList ret = new TFloatLinkedList();
    for (int i = 0; i < len; i++) {
      ret.add(values[(valOffset + i)]);
    }
    return ret;
  }
  
  public float get(int offset)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TFloatLink l = getLinkAt(offset);
    if (no(l)) {
      return this.no_entry_value;
    }
    return l.getValue();
  }
  
  public TFloatLink getLinkAt(int offset)
  {
    if (offset >= size()) {
      return null;
    }
    if (offset <= size() >>> 1) {
      return getLink(this.head, 0, offset, true);
    }
    return getLink(this.tail, size() - 1, offset, false);
  }
  
  private static TFloatLink getLink(TFloatLink l, int idx, int offset)
  {
    return getLink(l, idx, offset, true);
  }
  
  private static TFloatLink getLink(TFloatLink l, int idx, int offset, boolean next)
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
  
  public float set(int offset, float val)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TFloatLink l = getLinkAt(offset);
    if (no(l)) {
      throw new IndexOutOfBoundsException("at offset " + offset);
    }
    float prev = l.getValue();
    l.setValue(val);
    return prev;
  }
  
  public void set(int offset, float[] values)
  {
    set(offset, values, 0, values.length);
  }
  
  public void set(int offset, float[] values, int valOffset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      float value = values[(valOffset + i)];
      set(offset + i, value);
    }
  }
  
  public float replace(int offset, float val)
  {
    return set(offset, val);
  }
  
  public void clear()
  {
    this.size = 0;
    
    this.head = null;
    this.tail = null;
  }
  
  public boolean remove(float value)
  {
    boolean changed = false;
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value)
      {
        changed = true;
        
        removeLink(l);
      }
    }
    return changed;
  }
  
  private void removeLink(TFloatLink l)
  {
    if (no(l)) {
      return;
    }
    this.size -= 1;
    
    TFloatLink prev = l.getPrevious();
    TFloatLink next = l.getNext();
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
      if ((o instanceof Float))
      {
        Float i = (Float)o;
        if (!contains(i.floatValue())) {
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
  
  public boolean containsAll(TFloatCollection collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (TFloatIterator it = collection.iterator(); it.hasNext();)
    {
      float i = it.next();
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(float[] array)
  {
    if (isEmpty()) {
      return false;
    }
    for (float i : array) {
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean addAll(Collection<? extends Float> collection)
  {
    boolean ret = false;
    for (Float v : collection) {
      if (add(v.floatValue())) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(TFloatCollection collection)
  {
    boolean ret = false;
    for (TFloatIterator it = collection.iterator(); it.hasNext();)
    {
      float i = it.next();
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(float[] array)
  {
    boolean ret = false;
    for (float i : array) {
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean retainAll(Collection<?> collection)
  {
    boolean modified = false;
    TFloatIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(Float.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(TFloatCollection collection)
  {
    boolean modified = false;
    TFloatIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(float[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TFloatIterator iter = iterator();
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
    TFloatIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(Float.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(TFloatCollection collection)
  {
    boolean modified = false;
    TFloatIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(float[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TFloatIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) >= 0)
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public float removeAt(int offset)
  {
    TFloatLink l = getLinkAt(offset);
    if (no(l)) {
      throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
    }
    float prev = l.getValue();
    removeLink(l);
    return prev;
  }
  
  public void remove(int offset, int length)
  {
    for (int i = 0; i < length; i++) {
      removeAt(offset);
    }
  }
  
  public void transformValues(TFloatFunction function)
  {
    for (TFloatLink l = this.head; got(l);)
    {
      l.setValue(function.execute(l.getValue()));
      
      l = l.getNext();
    }
  }
  
  public void reverse()
  {
    TFloatLink h = this.head;
    TFloatLink t = this.tail;
    
    TFloatLink l = this.head;
    while (got(l))
    {
      TFloatLink next = l.getNext();
      TFloatLink prev = l.getPrevious();
      
      TFloatLink tmp = l;
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
    TFloatLink start = getLinkAt(from);
    TFloatLink stop = getLinkAt(to);
    
    TFloatLink tmp = null;
    
    TFloatLink tmpHead = start.getPrevious();
    
    TFloatLink l = start;
    while (l != stop)
    {
      TFloatLink next = l.getNext();
      TFloatLink prev = l.getPrevious();
      
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
      TFloatLink l = getLinkAt(rand.nextInt(size()));
      removeLink(l);
      add(l.getValue());
    }
  }
  
  public TFloatList subList(int begin, int end)
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
    TFloatLinkedList ret = new TFloatLinkedList();
    TFloatLink tmp = getLinkAt(begin);
    for (int i = begin; i < end; i++)
    {
      ret.add(tmp.getValue());
      tmp = tmp.getNext();
    }
    return ret;
  }
  
  public float[] toArray()
  {
    return toArray(new float[this.size], 0, this.size);
  }
  
  public float[] toArray(int offset, int len)
  {
    return toArray(new float[len], offset, 0, len);
  }
  
  public float[] toArray(float[] dest)
  {
    return toArray(dest, 0, this.size);
  }
  
  public float[] toArray(float[] dest, int offset, int len)
  {
    return toArray(dest, offset, 0, len);
  }
  
  public float[] toArray(float[] dest, int source_pos, int dest_pos, int len)
  {
    if (len == 0) {
      return dest;
    }
    if ((source_pos < 0) || (source_pos >= size())) {
      throw new ArrayIndexOutOfBoundsException(source_pos);
    }
    TFloatLink tmp = getLinkAt(source_pos);
    for (int i = 0; i < len; i++)
    {
      dest[(dest_pos + i)] = tmp.getValue();
      tmp = tmp.getNext();
    }
    return dest;
  }
  
  public boolean forEach(TFloatProcedure procedure)
  {
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      if (!procedure.execute(l.getValue())) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachDescending(TFloatProcedure procedure)
  {
    for (TFloatLink l = this.tail; got(l); l = l.getPrevious()) {
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
    TFloatList tmp = subList(fromIndex, toIndex);
    float[] vals = tmp.toArray();
    Arrays.sort(vals);
    set(fromIndex, vals);
  }
  
  public void fill(float val)
  {
    fill(0, this.size, val);
  }
  
  public void fill(int fromIndex, int toIndex, float val)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    TFloatLink l = getLinkAt(fromIndex);
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
  
  public int binarySearch(float value)
  {
    return binarySearch(value, 0, size());
  }
  
  public int binarySearch(float value, int fromIndex, int toIndex)
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
    TFloatLink fromLink = getLinkAt(fromIndex);
    int to = toIndex;
    while (from < to)
    {
      int mid = from + to >>> 1;
      TFloatLink middle = getLink(fromLink, from, mid);
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
  
  public int indexOf(float value)
  {
    return indexOf(0, value);
  }
  
  public int indexOf(int offset, float value)
  {
    int count = offset;
    for (TFloatLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        return count;
      }
      count++;
    }
    return -1;
  }
  
  public int lastIndexOf(float value)
  {
    return lastIndexOf(0, value);
  }
  
  public int lastIndexOf(int offset, float value)
  {
    if (isEmpty()) {
      return -1;
    }
    int last = -1;
    int count = offset;
    for (TFloatLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        last = count;
      }
      count++;
    }
    return last;
  }
  
  public boolean contains(float value)
  {
    if (isEmpty()) {
      return false;
    }
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value) {
        return true;
      }
    }
    return false;
  }
  
  public TFloatIterator iterator()
  {
    new TFloatIterator()
    {
      TFloatLinkedList.TFloatLink l = TFloatLinkedList.this.head;
      TFloatLinkedList.TFloatLink current;
      
      public float next()
      {
        if (TFloatLinkedList.no(this.l)) {
          throw new NoSuchElementException();
        }
        float ret = this.l.getValue();
        this.current = this.l;
        this.l = this.l.getNext();
        
        return ret;
      }
      
      public boolean hasNext()
      {
        return TFloatLinkedList.got(this.l);
      }
      
      public void remove()
      {
        if (this.current == null) {
          throw new IllegalStateException();
        }
        TFloatLinkedList.this.removeLink(this.current);
        this.current = null;
      }
    };
  }
  
  public TFloatList grep(TFloatProcedure condition)
  {
    TFloatList ret = new TFloatLinkedList();
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      if (condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public TFloatList inverseGrep(TFloatProcedure condition)
  {
    TFloatList ret = new TFloatLinkedList();
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      if (!condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public float max()
  {
    float ret = Float.NEGATIVE_INFINITY;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      if (ret < l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public float min()
  {
    float ret = Float.POSITIVE_INFINITY;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      if (ret > l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public float sum()
  {
    float sum = 0.0F;
    for (TFloatLink l = this.head; got(l); l = l.getNext()) {
      sum += l.getValue();
    }
    return sum;
  }
  
  static class TFloatLink
  {
    float value;
    TFloatLink previous;
    TFloatLink next;
    
    TFloatLink(float value)
    {
      this.value = value;
    }
    
    public float getValue()
    {
      return this.value;
    }
    
    public void setValue(float value)
    {
      this.value = value;
    }
    
    public TFloatLink getPrevious()
    {
      return this.previous;
    }
    
    public void setPrevious(TFloatLink previous)
    {
      this.previous = previous;
    }
    
    public TFloatLink getNext()
    {
      return this.next;
    }
    
    public void setNext(TFloatLink next)
    {
      this.next = next;
    }
  }
  
  class RemoveProcedure
    implements TFloatProcedure
  {
    boolean changed = false;
    
    RemoveProcedure() {}
    
    public boolean execute(float value)
    {
      if (TFloatLinkedList.this.remove(value)) {
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
    
    out.writeFloat(this.no_entry_value);
    
    out.writeInt(this.size);
    for (TFloatIterator iterator = iterator(); iterator.hasNext();)
    {
      float next = iterator.next();
      out.writeFloat(next);
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    this.no_entry_value = in.readFloat();
    
    int len = in.readInt();
    for (int i = 0; i < len; i++) {
      add(in.readFloat());
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
    TFloatLinkedList that = (TFloatLinkedList)o;
    if (this.no_entry_value != that.no_entry_value) {
      return false;
    }
    if (this.size != that.size) {
      return false;
    }
    TFloatIterator iterator = iterator();
    TFloatIterator thatIterator = that.iterator();
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
    for (TFloatIterator iterator = iterator(); iterator.hasNext();) {
      result = 31 * result + HashFunctions.hash(iterator.next());
    }
    return result;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    TFloatIterator it = iterator();
    while (it.hasNext())
    {
      float next = it.next();
      buf.append(next);
      if (it.hasNext()) {
        buf.append(", ");
      }
    }
    buf.append("}");
    return buf.toString();
  }
}
