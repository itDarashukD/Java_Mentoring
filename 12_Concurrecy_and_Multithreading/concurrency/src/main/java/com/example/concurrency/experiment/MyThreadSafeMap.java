package com.example.concurrency.experiment;

import static java.util.Objects.requireNonNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


public class MyThreadSafeMap<K, V> implements Map<K, V> {


    private final int defaultSize = 10;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private ArrayList<MyOwnEntry<K, V>>[] table;
    private int size = 0;

    public MyThreadSafeMap() {
        table = new ArrayList[defaultSize];
        for (int i = 0; i < defaultSize; i++) {
	   table[i] = new ArrayList<>();
        }
    }

    private int indexFor(Object key) {
        return key.hashCode() % table.length;
    }

    @Override
    public int size() {
        lock.readLock().lock();

        try {
	   return size;

        } finally {
	   lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        lock.readLock().lock();

        try {
	   requireNonNull(key);

	   int index = indexFor(key);

	   return table[index].stream().anyMatch((e) -> e.getKey().equals(key));

        } finally {
	   lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        lock.readLock().lock();

        try {
	   requireNonNull(value);

	   return Arrays.stream(table)
		  .anyMatch((l) -> l.stream().anyMatch((e) -> e.getValue().equals(value)));
        } finally {
	   lock.readLock().unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.readLock().lock();

        try {
	   requireNonNull(key);

	   int index = indexFor(key);

	   for (MyOwnEntry<K, V> entry : table[index]) {
	       if (entry.getKey().equals(key)) {
		  return entry.getValue();
	       }
	   }
	   return null;
        } finally {
	   lock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.writeLock().lock();

        try {
	   requireNonNull(key);
	   requireNonNull(value);

	   int index = indexFor(key);

	   for (MyOwnEntry<K, V> entry : table[index]) {
	       if (entry.getKey().equals(key)) {
		  V oldValue = entry.getValue();
		  entry.setValue(value);
		  return oldValue;
	       }
	   }
	   table[index].add(new MyOwnEntry<K, V>(key, value));
	   size++;
	   return null;

        } finally {
	   lock.writeLock().unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.writeLock().lock();

        try {
	   requireNonNull(key);

	   int index = indexFor(key);

	   for (MyOwnEntry<K, V> entry : table[index]) {
	       if (entry.getKey().equals(key)) {
		  V oldValue = entry.getValue();
		  table[index].remove(entry);
		  return oldValue;
	       }
	   }
	   return null;

        } finally {
	   lock.writeLock().unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.writeLock().lock();

        try {
	   requireNonNull(m);

	   if (!m.isEmpty()) {
	       m.keySet().stream().forEach((k) -> put(k, m.get(k)));
	   }
        } finally {
	   lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();

        try {
	   Arrays.stream(table).forEach((l) -> l.clear());
	   size = 0;
        } finally {
	   lock.writeLock().unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        lock.readLock().lock();

        try {
	   Set<K> set = Arrays.stream(table)
		  .flatMap((l) -> l.stream().map((e) -> e.getKey()))
		  .collect(Collectors.toCollection(LinkedHashSet::new));
	   return set;
        } finally {
	   lock.readLock().unlock();
        }
    }

    @Override
    public Collection<V> values() {
        lock.readLock().lock();

        try {
	   Collection<V> collection = Arrays.stream(table)
		  .flatMap((l) -> l.stream().map((e) -> e.getValue()))
		  .collect(Collectors.toCollection(ArrayList::new));
	   return collection;
        } finally {
	   lock.readLock().unlock();
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        lock.readLock().lock();

        try {
	   Set<Entry<K, V>> set = Arrays.stream(table)
		  .flatMap((l) -> l.stream()
			 .map((e) -> new AbstractMap.SimpleEntry<K, V>(e.getKey(),
				e.getValue())))
		  .collect(Collectors.toCollection(LinkedHashSet::new));
	   return set;
        } finally {
	   lock.readLock().unlock();
        }
    }

    @Override
    public String toString() {
        Set<Map.Entry<K, V>> set = entrySet();

        String result = "{";
        result += set.stream()
	       .map((e) -> e.getKey() + "=" + e.getValue())
	       .collect(Collectors.joining(", "));
        result += "}";

        return result;
    }


}

