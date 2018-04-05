package org.opensaga.plugin.util;

import java.util.Collection;
import java.util.Iterator;

public class Iterate
{
    
    public static Range from(int from)
    {
        return new Range(from, Integer.MAX_VALUE);
    }
    
    public static Range to(int to)
    {
        return new Range(0, to);
    }
    
    public static <T> Iterable<Index<T>> over(final Collection<T> collection)
    {
        return new Iterable<Index<T>>() {
            @Override
            public Iterator<Index<T>> iterator() {
                return new Iterator<Index<T>>() {
                    private int index = 0;

                    private Iterator<T> iterator = collection.iterator();
                    
                    @Override
                    public boolean hasNext() { return iterator.hasNext();  }
                    @Override
                    public Index<T> next() { return new Index<T>(iterator.next(), index++); }
                    @Override
                    public void remove() { }
                };
            }
        };
    }
    
    public static <T> Iterable<Index<T>> over(final T[] array)
    {
        return new Iterable<Index<T>>() {
            @Override
            public Iterator<Index<T>> iterator() {
                return new Iterator<Index<T>>() {
                    int index = 0;
                    
                    @Override
                    public boolean hasNext() { return index < array.length; }
                    @Override
                    public Index<T> next() { return new Index<T>(array[index], index++); }
                    @Override
                    public void remove() { }
                };
            }
        };
    }
        
    public static class Range {
        
        private final class RangeIterable
            implements Iterable<Index<Void>>
        {
            private final int from;
            
            private final int to;

            private RangeIterable(int from, int to)
            {
                if(from > to) { from--; to--; }
                
                this.from = from;
                this.to = to;
            }


            @Override
            public Iterator<Index<Void>> iterator() {
                return new Iterator<Index<Void>>() {
                    int index = from;
                    
                    @Override
                    public boolean hasNext()
                    {
                        if(from > to)
                            return index > to;
                        else
                            return index < to;
                    }


                    @Override
                    public Index<Void> next()
                    {
                        Index<Void> iterator = new Index<Void>(null, index);
                        if(from > to) index--; else index++;
                        
                        return iterator;
                    }
                    @Override
                    public void remove() { }
                };
            }
        }

        private int from;
        
        private int to;

        public Range(int from, int to)
        {
            this.from = from;
            this.to = to;
        }
        
        public Iterable<Index<Void>> from(final int from) {
            return new RangeIterable(from, to);
        }
        
        public Iterable<Index<Void>> to(final int to)
        {
            return new RangeIterable(from, to);
        }
        
    }
    
    public static class Index<T> {
        
        public final T value;
        
        public final int index;

        public Index(T value, int index)
        {
            this.value = value;
            this.index = index;
        }
    }
}
