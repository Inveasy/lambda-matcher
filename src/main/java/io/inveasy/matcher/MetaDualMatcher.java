package io.inveasy.matcher;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

// StackOverflow inspired : https://stackoverflow.com/a/41550809
public class MetaDualMatcher<T, K>
{
	public <X, Y, U> Case<X, Y, U> match(BiPredicate<T, K> cond, BiFunction<X, Y, U> map)
	{
		return this.new Case<>(cond, map, Optional.empty());
	}
	
	public class Case<X, Y, U>
	{
		private BiPredicate<T, K> cond;
		private BiFunction<X, Y, U> map;
		private Optional<Case<X, Y, U>> previous;
		
		Case(BiPredicate<T, K> cond, BiFunction<X, Y, U> map, Optional<Case<X, Y, U>> previous)
		{
			this.cond = cond;
			this.map = map;
			this.previous = previous;
		}
		
		public Case<X, Y, U> match(BiPredicate<T, K> cond, BiFunction<X, Y, U> map)
		{
			return new Case<>(cond, map, Optional.of(this));
		}
		
		public QuadFunction<T, K, BiFunction<T, K, X>, BiFunction<T, K, Y>, U> otherwise(BiFunction<X, Y, U> map)
		{
			return (val1, val2, val3, val4) -> this.apply(val1, val2, val3, val4)
					.orElseGet(() -> map.apply(val3.apply(val1, val2), val4.apply(val1, val2)));
		}
		
		public Optional<U> apply(T value1, K value2, BiFunction<T, K, X> meta1, BiFunction<T, K, Y> meta2)
		{
			return cond.test(value1, value2) ?
					Optional.of(map.apply(meta1.apply(value1, value2), meta2.apply(value1, value2))) :
					previous.flatMap(p -> p.apply(value1, value2, meta1, meta2));
		}
	}
}
