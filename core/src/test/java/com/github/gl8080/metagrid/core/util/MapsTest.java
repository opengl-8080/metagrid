package com.github.gl8080.metagrid.core.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class MapsTest {

    @Test
    public void test() {
        Map<String, Integer> map = 
            Maps
                .hashMap("a", 100)
                .entry("b", 200)
                .entry("c", 300);
        
        assertThat(map).contains(
            entry("a", 100),
            entry("b", 200),
            entry("c", 300)
        );
    }
    public static <K, V> Entry<K, V> entry(K key, V value) {
        return new DummyEntry<>(key, value);
    }
    
    private static class DummyEntry<K, V> implements Entry<K, V> {
        
        private K key;
        private V value;
        
        public DummyEntry(K key, V value) {
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
            throw new UnsupportedOperationException();
        }
    }

}
