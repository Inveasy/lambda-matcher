/*
 * Copyright 2018 Guillaume Gravetot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
