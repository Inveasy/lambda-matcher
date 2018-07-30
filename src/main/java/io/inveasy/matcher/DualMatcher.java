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
