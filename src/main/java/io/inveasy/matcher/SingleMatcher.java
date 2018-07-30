package io.inveasy.matcher;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

// StackOverflow inspired : https://stackoverflow.com/a/41550809
public class SingleMatcher<T>
{
	public <U> Case<U> match(Predicate<T> cond, Function<T, U> map)
	{
		return this.new Case<>(cond, map, Optional.empty());
	}
	
	public class Case<U> implements Function<T, Optional<U>>
	{
		private Predicate<T> cond;
		private Function<T, U> map;
		private Optional<Case<U>> previous;
		
		Case(Predicate<T> cond, Function<T, U> map, Optional<Case<U>> previous)
		{
			this.cond = cond;
			this.map = map;
			this.previous = previous;
		}
		
		public Case<U> match(Predicate<T> cond, Function<T, U> map)
		{
			return new Case<>(cond, map, Optional.of(this));
		}
		
		@Override
		public Optional<U> apply(T value)
		{
			return cond.test(value) ? Optional.of(map.apply(value)) : previous.flatMap(p -> p.apply(value));
		}
	}
}
