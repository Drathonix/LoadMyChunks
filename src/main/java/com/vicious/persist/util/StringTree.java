package com.vicious.persist.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple StringTree Map. Uses '/' as a deliminator.
 * @param <T> the value type.
 */
public class StringTree<T> implements Map<String,T> {
    private final Node<T> root = new Node<>(null,false);
    private int size = 0;
    private int depth = 0;

    @Override
    public int size() {
        return size;
    }

    public int depth(){
        return depth;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if(!(key instanceof String)) return false;
        String[] path = ((String)key).split("/");
        Node<T> current = root;
        for (String s : path) {
            current = current.get(s);
            if(current == null) return false;
        }
        return current.isSet;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root,value);
    }

    private boolean containsValue(Node<T> current, Object value) {
        for (Node<T> node : current.values()) {
            if(node.value == value || containsValue(node,value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(Object key) {
        if(!(key instanceof String)) return null;
        String[] path = ((String)key).split("/");
        Node<T> current = root;
        for (String s : path) {
            current = current.get(s);
            if(current == null) return null;
        }
        return current.value;
    }

    @Nullable
    @Override
    public T put(String key, T value) {
        String[] path = key.split("/");
        Node<T> current = root;
        for (String s : path) {
            current = current.computeIfAbsent(s, k -> new Node<>(null,false));
        }
        T pre = current.value;
        current.value = value;
        current.isSet=true;
        recalc();
        return pre;
    }

    private void recalc(){
        int[] v = recalc(root);
        size = v[0];
        depth = v[1];
    }

    private int[] recalc(Node<T> current) {
        int k = current.isSet ? 1 : 0;
        int v = 0;
        for (Node<T> node : current.values()) {
            int[] x = recalc(node);
            k+=x[0];
            v=Math.max(v,x[1]);
        }
        return new int[]{k,v+(current.isEmpty() ? 0 : 1)};
    }

    @Override
    public T remove(Object key) {
        if(!(key instanceof String)) return null;
        String[] path = ((String)key).split("/");
        Node<T> current = root;
        for (String s : path) {
            current = current.get(s);
            if(current == null) return null;
        }
        T pre = current.value;
        current.value = null;
        current.isSet = false;
        prune(root);
        recalc();
        return pre;
    }

    private boolean prune(Node<T> current) {
        Iterator<Entry<String, Node<T>>> iterator = current.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, Node<T>> node = iterator.next();
            if(prune(node.getValue())) {
                iterator.remove();
            }
        }
        return current.isEmpty() && !current.isSet;
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends T> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        root.clear();
        size=0;
        depth=0;
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        Set<String> keys = new HashSet<>();
        keySet(keys,root,"");
        return keys;
    }

    private void keySet(Set<String> set, Node<T> current, String str) {
        for (String s : current.keySet()) {
            Node<T> node = current.get(s);
            String path = str + s;
            if (node.isSet) {
                set.add(path);
            }
            keySet(set, node, path+"/");
        }
    }

    @NotNull
    @Override
    public Collection<T> values() {
        List<T> values = new ArrayList<>();
        values(values,root);
        return values;
    }

    private void values(Collection<T> collection, Node<T> current) {
        if(current.isSet) {
            collection.add(current.value);
        }
        for (Node<T> node : current.values()) {
            values(collection,node);
        }
    }

    @NotNull
    @Override
    public Set<Entry<String, T>> entrySet() {
        Set<Entry<String, T>> set = new HashSet<>();
        entrySet(set,root, "");
        return set;
    }

    private void entrySet(Set<Entry<String,T>> set, Node<T> current, String str) {
        for (String s : current.keySet()) {
            Node<T> node = current.get(s);
            String path = str+s;
            if(node.isSet) {
                set.add(new AbstractMap.SimpleImmutableEntry<>(path, node.value));
            }
            entrySet(set,node,path+"/");
        }
    }

    public boolean containsNode(String key) {
        if(!(key instanceof String)) return false;
        String[] path = ((String)key).split("/");
        Node<T> current = root;
        for (String s : path) {
            current = current.get(s);
            if(current == null) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        Iterator<Entry<String,T>> i = entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Entry<String,T> e = i.next();
            String key = e.getKey();
            T value = e.getValue();
            sb.append(key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }

    private static class Node<T> extends HashMap<String,Node<T>> {
        private boolean isSet = false;
        private @Nullable T value;

        private Node(T value, boolean set){
            this.value = value;
            this.isSet = set;
        }

        @Override
        public void clear() {
            for (Node<T> tNode : values()) {
                tNode.clear();
            }
            super.clear();
        }
    }
}
