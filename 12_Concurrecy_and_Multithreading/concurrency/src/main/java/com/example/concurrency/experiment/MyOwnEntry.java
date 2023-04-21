package com.example.concurrency.experiment;

import java.util.Map;

public class MyOwnEntry<K, V> implements Map.Entry<K, V> {

    K key;
    V value;

    public MyOwnEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        V temp = this.value;
        this.value = value;
        return temp;
    }
}

