package io.inveasy.matcher;

@FunctionalInterface
public interface QuadFunction<K, V, X, Y, U>
{
	U apply(K val1, V val2, X val3, Y val4);
}
