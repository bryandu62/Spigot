package gnu.trove.list.linked;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.list.TDoubleList;
import gnu.trove.procedure.TDoubleProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

public class TDoubleLinkedList
  implements TDoubleList, Externalizable
{
  double no_entry_value;
  int size;
  TDoubleLink head = null;
  TDoubleLink tail = this.head;
  
  public TDoubleLinkedList() {}
  
  public TDoubleLinkedList(double no_entry_value)
  {
    this.no_entry_value = no_entry_value;
  }
  
  public TDoubleLinkedList(TDoubleList list)
  {
    this.no_entry_value = list.getNoEntryValue();
    for (TDoubleIterator iterator = list.iterator(); iterator.hasNext();)
    {
      double next = iterator.next();
      add(next);
    }
  }
  
  public double getNoEntryValue()
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
  
  public boolean add(double val)
  {
    TDoubleLink l = new TDoubleLink(val);
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
  
  public void add(double[] vals)
  {
    for (double val : vals) {
      add(val);
    }
  }
  
  public void add(double[] vals, int offset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      double val = vals[(offset + i)];
      add(val);
    }
  }
  
  public void insert(int offset, double value)
  {
    TDoubleLinkedList tmp = new TDoubleLinkedList();
    tmp.add(value);
    insert(offset, tmp);
  }
  
  public void insert(int offset, double[] values)
  {
    insert(offset, link(values, 0, values.length));
  }
  
  public void insert(int offset, double[] values, int valOffset, int len)
  {
    insert(offset, link(values, valOffset, len));
  }
  
  void insert(int offset, TDoubleLinkedList tmp)
  {
    TDoubleLink l = getLinkAt(offset);
    
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
      TDoubleLink prev = l.getPrevious();
      l.getPrevious().setNext(tmp.head);
      
      tmp.tail.setNext(l);
      l.setPrevious(tmp.tail);
      
      tmp.head.setPrevious(prev);
    }
  }
  
  static TDoubleLinkedList link(double[] values, int valOffset, int len)
  {
    TDoubleLinkedList ret = new TDoubleLinkedList();
    for (int i = 0; i < len; i++) {
      ret.add(values[(valOffset + i)]);
    }
    return ret;
  }
  
  public double get(int offset)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TDoubleLink l = getLinkAt(offset);
    if (no(l)) {
      return this.no_entry_value;
    }
    return l.getValue();
  }
  
  public TDoubleLink getLinkAt(int offset)
  {
    if (offset >= size()) {
      return null;
    }
    if (offset <= size() >>> 1) {
      return getLink(this.head, 0, offset, true);
    }
    return getLink(this.tail, size() - 1, offset, false);
  }
  
  private static TDoubleLink getLink(TDoubleLink l, int idx, int offset)
  {
    return getLink(l, idx, offset, true);
  }
  
  private static TDoubleLink getLink(TDoubleLink l, int idx, int offset, boolean next)
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
  
  public double set(int offset, double val)
  {
    if (offset > this.size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
    }
    TDoubleLink l = getLinkAt(offset);
    if (no(l)) {
      throw new IndexOutOfBoundsException("at offset " + offset);
    }
    double prev = l.getValue();
    l.setValue(val);
    return prev;
  }
  
  public void set(int offset, double[] values)
  {
    set(offset, values, 0, values.length);
  }
  
  public void set(int offset, double[] values, int valOffset, int length)
  {
    for (int i = 0; i < length; i++)
    {
      double value = values[(valOffset + i)];
      set(offset + i, value);
    }
  }
  
  public double replace(int offset, double val)
  {
    return set(offset, val);
  }
  
  public void clear()
  {
    this.size = 0;
    
    this.head = null;
    this.tail = null;
  }
  
  public boolean remove(double value)
  {
    boolean changed = false;
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value)
      {
        changed = true;
        
        removeLink(l);
      }
    }
    return changed;
  }
  
  private void removeLink(TDoubleLink l)
  {
    if (no(l)) {
      return;
    }
    this.size -= 1;
    
    TDoubleLink prev = l.getPrevious();
    TDoubleLink next = l.getNext();
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
      if ((o instanceof Double))
      {
        Double i = (Double)o;
        if (!contains(i.doubleValue())) {
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
  
  public boolean containsAll(TDoubleCollection collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (TDoubleIterator it = collection.iterator(); it.hasNext();)
    {
      double i = it.next();
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(double[] array)
  {
    if (isEmpty()) {
      return false;
    }
    for (double i : array) {
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean addAll(Collection<? extends Double> collection)
  {
    boolean ret = false;
    for (Double v : collection) {
      if (add(v.doubleValue())) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(TDoubleCollection collection)
  {
    boolean ret = false;
    for (TDoubleIterator it = collection.iterator(); it.hasNext();)
    {
      double i = it.next();
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(double[] array)
  {
    boolean ret = false;
    for (double i : array) {
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean retainAll(Collection<?> collection)
  {
    boolean modified = false;
    TDoubleIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(Double.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(TDoubleCollection collection)
  {
    boolean modified = false;
    TDoubleIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(double[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TDoubleIterator iter = iterator();
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
    TDoubleIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(Double.valueOf(iter.next())))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(TDoubleCollection collection)
  {
    boolean modified = false;
    TDoubleIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(iter.next()))
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(double[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TDoubleIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) >= 0)
      {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public double removeAt(int offset)
  {
    TDoubleLink l = getLinkAt(offset);
    if (no(l)) {
      throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
    }
    double prev = l.getValue();
    removeLink(l);
    return prev;
  }
  
  public void remove(int offset, int length)
  {
    for (int i = 0; i < length; i++) {
      removeAt(offset);
    }
  }
  
  public void transformValues(TDoubleFunction function)
  {
    for (TDoubleLink l = this.head; got(l);)
    {
      l.setValue(function.execute(l.getValue()));
      
      l = l.getNext();
    }
  }
  
  public void reverse()
  {
    TDoubleLink h = this.head;
    TDoubleLink t = this.tail;
    
    TDoubleLink l = this.head;
    while (got(l))
    {
      TDoubleLink next = l.getNext();
      TDoubleLink prev = l.getPrevious();
      
      TDoubleLink tmp = l;
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
    TDoubleLink start = getLinkAt(from);
    TDoubleLink stop = getLinkAt(to);
    
    TDoubleLink tmp = null;
    
    TDoubleLink tmpHead = start.getPrevious();
    
    TDoubleLink l = start;
    while (l != stop)
    {
      TDoubleLink next = l.getNext();
      TDoubleLink prev = l.getPrevious();
      
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
      TDoubleLink l = getLinkAt(rand.nextInt(size()));
      removeLink(l);
      add(l.getValue());
    }
  }
  
  public TDoubleList subList(int begin, int end)
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
    TDoubleLinkedList ret = new TDoubleLinkedList();
    TDoubleLink tmp = getLinkAt(begin);
    for (int i = begin; i < end; i++)
    {
      ret.add(tmp.getValue());
      tmp = tmp.getNext();
    }
    return ret;
  }
  
  public double[] toArray()
  {
    return toArray(new double[this.size], 0, this.size);
  }
  
  public double[] toArray(int offset, int len)
  {
    return toArray(new double[len], offset, 0, len);
  }
  
  public double[] toArray(double[] dest)
  {
    return toArray(dest, 0, this.size);
  }
  
  public double[] toArray(double[] dest, int offset, int len)
  {
    return toArray(dest, offset, 0, len);
  }
  
  public double[] toArray(double[] dest, int source_pos, int dest_pos, int len)
  {
    if (len == 0) {
      return dest;
    }
    if ((source_pos < 0) || (source_pos >= size())) {
      throw new ArrayIndexOutOfBoundsException(source_pos);
    }
    TDoubleLink tmp = getLinkAt(source_pos);
    for (int i = 0; i < len; i++)
    {
      dest[(dest_pos + i)] = tmp.getValue();
      tmp = tmp.getNext();
    }
    return dest;
  }
  
  public boolean forEach(TDoubleProcedure procedure)
  {
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      if (!procedure.execute(l.getValue())) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachDescending(TDoubleProcedure procedure)
  {
    for (TDoubleLink l = this.tail; got(l); l = l.getPrevious()) {
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
    TDoubleList tmp = subList(fromIndex, toIndex);
    double[] vals = tmp.toArray();
    Arrays.sort(vals);
    set(fromIndex, vals);
  }
  
  public void fill(double val)
  {
    fill(0, this.size, val);
  }
  
  public void fill(int fromIndex, int toIndex, double val)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    TDoubleLink l = getLinkAt(fromIndex);
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
  
  public int binarySearch(double value)
  {
    return binarySearch(value, 0, size());
  }
  
  public int binarySearch(double value, int fromIndex, int toIndex)
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
    TDoubleLink fromLink = getLinkAt(fromIndex);
    int to = toIndex;
    while (from < to)
    {
      int mid = from + to >>> 1;
      TDoubleLink middle = getLink(fromLink, from, mid);
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
  
  public int indexOf(double value)
  {
    return indexOf(0, value);
  }
  
  public int indexOf(int offset, double value)
  {
    int count = offset;
    for (TDoubleLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        return count;
      }
      count++;
    }
    return -1;
  }
  
  public int lastIndexOf(double value)
  {
    return lastIndexOf(0, value);
  }
  
  public int lastIndexOf(int offset, double value)
  {
    if (isEmpty()) {
      return -1;
    }
    int last = -1;
    int count = offset;
    for (TDoubleLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext())
    {
      if (l.getValue() == value) {
        last = count;
      }
      count++;
    }
    return last;
  }
  
  public boolean contains(double value)
  {
    if (isEmpty()) {
      return false;
    }
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      if (l.getValue() == value) {
        return true;
      }
    }
    return false;
  }
  
  public TDoubleIterator iterator()
  {
    new TDoubleIterator()
    {
      TDoubleLinkedList.TDoubleLink l = TDoubleLinkedList.this.head;
      TDoubleLinkedList.TDoubleLink current;
      
      public double next()
      {
        if (TDoubleLinkedList.no(this.l)) {
          throw new NoSuchElementException();
        }
        double ret = this.l.getValue();
        this.current = this.l;
        this.l = this.l.getNext();
        
        return ret;
      }
      
      public boolean hasNext()
      {
        return TDoubleLinkedList.got(this.l);
      }
      
      public void remove()
      {
        if (this.current == null) {
          throw new IllegalStateException();
        }
        TDoubleLinkedList.this.removeLink(this.current);
        this.current = null;
      }
    };
  }
  
  public TDoubleList grep(TDoubleProcedure condition)
  {
    TDoubleList ret = new TDoubleLinkedList();
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      if (condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public TDoubleList inverseGrep(TDoubleProcedure condition)
  {
    TDoubleList ret = new TDoubleLinkedList();
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      if (!condition.execute(l.getValue())) {
        ret.add(l.getValue());
      }
    }
    return ret;
  }
  
  public double max()
  {
    double ret = Double.NEGATIVE_INFINITY;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      if (ret < l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public double min()
  {
    double ret = Double.POSITIVE_INFINITY;
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      if (ret > l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public double sum()
  {
    double sum = 0.0D;
    for (TDoubleLink l = this.head; got(l); l = l.getNext()) {
      sum += l.getValue();
    }
    return sum;
  }
  
  static class TDoubleLink
  {
    double value;
    TDoubleLink previous;
    TDoubleLink next;
    
    TDoubleLink(double value)
    {
      this.value = value;
    }
    
    public double getValue()
    {
      return this.value;
    }
    
    public void setValue(double value)
    {
      this.value = value;
    }
    
    public TDoubleLink getPrevious()
    {
      return this.previous;
    }
    
    public void setPrevious(TDoubleLink previous)
    {
      this.previous = previous;
    }
    
    public TDoubleLink getNext()
    {
      return this.next;
    }
    
    public void setNext(TDoubleLink next)
    {
      this.next = next;
    }
  }
  
  class RemoveProcedure
    implements TDoubleProcedure
  {
    boolean changed = false;
    
    RemoveProcedure() {}
    
    public boolean execute(double value)
    {
      if (TDoubleLinkedList.this.remove(value)) {
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
    
    out.writeDouble(this.no_entry_value);
    
    out.writeInt(this.size);
    for (TDoubleIterator iterator = iterator(); iterator.hasNext();)
    {
      double next = iterator.next();
      out.writeDouble(next);
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    this.no_entry_value = in.readDouble();
    
    int len = in.readInt();
    for (int i = 0; i < len; i++) {
      add(in.readDouble());
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
    TDoubleLinkedList that = (TDoubleLinkedList)o;
    if (this.no_entry_value != that.no_entry_value) {
      return false;
    }
    if (this.size != that.size) {
      return false;
    }
    TDoubleIterator iterator = iterator();
    TDoubleIterator thatIterator = that.iterator();
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
    for (TDoubleIterator iterator = iterator(); iterator.hasNext();) {
      result = 31 * result + HashFunctions.hash(iterator.next());
    }
    return result;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    TDoubleIterator it = iterator();
    while (it.hasNext())
    {
      double next = it.next();
      buf.append(next);
      if (it.hasNext()) {
        buf.append(", ");
      }
    }
    buf.append("}");
    return buf.toString();
  }
}
