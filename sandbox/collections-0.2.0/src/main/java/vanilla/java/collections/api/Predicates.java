/*
 * Copyright (c) 2011 Peter Lawrey
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

package vanilla.java.collections.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public enum Predicates implements Predicate {
  NONE {
    @Override
    public boolean test(Object o) {
      return false;
    }
  },
  ALL {
    @Override
    public boolean test(Object o) {
      return true;
    }
  };

  public static <T> Predicate<T> equ(Class<T> tClass, String fieldName, final Object value) {
    return equals(tClass, fieldName, value, true);
  }

  public static <T> Predicate<T> neq(Class<T> tClass, String fieldName, final Object value) {
    return equals(tClass, fieldName, value, false);
  }

  private static <T> Predicate<T> equals(Class<T> tClass, String fieldName, final Object value, final boolean expected) {
    final Method method = getterFor(tClass, fieldName);
    return new Predicate<T>() {
      @Override
      public boolean test(T t) {
        try {
          return method.invoke(t).equals(value) == expected;
        } catch (IllegalAccessException e) {
          throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
          throw new IllegalArgumentException(e.getCause());
        }
      }
    };
  }

  public static <T> Predicate<T> gt(Class<T> tClass, String fieldName, final Comparable value) {
    return compare(tClass, fieldName, value, false, false, true);
  }

  public static <T> Predicate<T> gte(Class<T> tClass, String fieldName, final Comparable value) {
    return compare(tClass, fieldName, value, false, true, true);
  }

  public static <T> Predicate<T> lt(Class<T> tClass, String fieldName, final Comparable value) {
    return compare(tClass, fieldName, value, true, false, false);
  }

  public static <T> Predicate<T> lte(Class<T> tClass, String fieldName, final Comparable value) {
    return compare(tClass, fieldName, value, true, true, false);
  }

  private static <T> Predicate<T> compare(Class<T> tClass, String fieldName, final Comparable value, final boolean lt, final boolean equ, final boolean gt) {
    final Method method = getterFor(tClass, fieldName);
    return new Predicate<T>() {
      @Override
      public boolean test(T t) {
        try {
          final int cmp = ((Comparable) method.invoke(t)).compareTo(value);
          return cmp < 0 ? lt : cmp > 0 ? gt : equ;
        } catch (IllegalAccessException e) {
          throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
          throw new IllegalArgumentException(e.getCause());
        }
      }
    };
  }

  public static Method setterFor(Class tClass, String fieldName, Class valueClass) {
    try {
      return tClass.getDeclaredMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), valueClass);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static Method getterFor(Class tClass, String fieldName) {
    try {
      return tClass.getDeclaredMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> Predicate<T> or(Predicate<T>... predicates) {
    Set<Predicate<T>> predicateSet = new LinkedHashSet<Predicate<T>>(Arrays.asList(predicates));
    predicateSet.remove(NONE);
    if (predicateSet.isEmpty())
      return NONE;
    if (predicateSet.contains(ALL))
      return ALL;
    if (predicateSet.size() == 1)
      return predicateSet.iterator().next();
    return new OrPredicate(predicateSet.toArray(new Predicate[predicateSet.size()]));
  }

  public static <T> Predicate<T> and(Predicate<T>... predicates) {
    Set<Predicate<T>> predicateSet = new LinkedHashSet<Predicate<T>>(Arrays.asList(predicates));
    predicateSet.remove(ALL);
    if (predicateSet.isEmpty())
      return ALL;
    if (predicateSet.contains(NONE))
      return NONE;
    if (predicateSet.size() == 1)
      return predicateSet.iterator().next();
    return new AndPredicate(predicateSet.toArray(new Predicate[predicateSet.size()]));
  }

  public static <T> Predicate<T> not(Predicate<T> p) {
    if (p == NONE) return ALL;
    if (p == ALL) return NONE;
    if (p instanceof NotPredicate)
      return ((NotPredicate<T>) p).p;
    return new NotPredicate<T>(p);
  }

  public static class OrPredicate<T> implements Predicate<T> {
    private final Predicate<T>[] predicates;

    public OrPredicate(Predicate<T>[] predicates) {
      this.predicates = predicates;
    }

    @Override
    public boolean test(T t) {
      for (Predicate<T> predicate : predicates) {
        if (predicate.test(t)) return true;
      }
      return false;
    }
  }

  public static class AndPredicate<T> implements Predicate<T> {
    private final Predicate<T>[] predicates;

    public AndPredicate(Predicate<T>[] predicates) {
      this.predicates = predicates;
    }

    @Override
    public boolean test(T t) {
      for (Predicate<T> predicate : predicates) {
        if (!predicate.test(t)) return false;
      }
      return true;
    }
  }

  public static class NotPredicate<T> implements Predicate<T> {
    private final Predicate<T> p;

    public NotPredicate(Predicate<T> p) {
      this.p = p;
    }

    @Override
    public boolean test(T t) {
      return !p.test(t);
    }
  }
}
