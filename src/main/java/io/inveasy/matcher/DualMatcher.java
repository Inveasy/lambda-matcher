package io.inveasy.matcher;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

// StackOverflow inspired : https://stackoverflow.com/a/41550809
public class DualMatcher<T, K>
{
	public <U> Case<U> match(BiPredicate<T, K> cond, BiFunction<T, K, U> map)
	{
		return this.new Case<>(cond, map, Optional.empty());
	}
	
	public class Case<U> implements BiFunction<T, K, Optional<U>>
	{
		private BiPredicate<T, K> cond;
		private BiFunction<T, K, U> map;
		private Optional<Case<U>> previous;
		
		Case(BiPredicate<T, K> cond, BiFunction<T, K, U> map, Optional<Case<U>> previous)
		{
			this.cond = cond;
			this.map = map;
			this.previous = previous;
		}
		
		public Case<U> match(BiPredicate<T, K> cond, BiFunction<T, K, U> map)
		{
			return new Case<>(cond, map, Optional.of(this));
		}
		
		@Override
		public Optional<U> apply(T value1, K value2)
		{
			return cond.test(value1, value2) ? Optional.of(map.apply(value1, value2)) : previous.flatMap(p -> p.apply(value1, value2));
		}
	}
}
