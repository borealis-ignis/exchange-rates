package com.borealis.erates.util;

/**
 * @author Kastalski Sergey
 */
public class Pair<K, V> {
	
	private K key;
	private V value;
	
	public Pair(final K key, final V value) {
		this.key = key;
		this.value = value;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return key + "=" + value;
	}
	
	@Override
	public int hashCode() {
		return ((key == null) ? 0 : key.hashCode()) * 13 + ((value == null) ? 0 : value.hashCode());
	}
	
	@Override
    public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (o instanceof Pair) {
			@SuppressWarnings("unchecked")
			final Pair<K, V> pair = (Pair<K, V>) o;
			if ((key != null) ? !key.equals(pair.key) : pair.key != null) {
				return false;
			}
			if ((value != null) ? !value.equals(pair.value) : pair.value != null) {
				return false;
			}
			
			return true;
		}
		
		return false;
    }
	
}
