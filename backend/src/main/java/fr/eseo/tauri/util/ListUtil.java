package fr.eseo.tauri.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtil {

	private ListUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		var filteredList = new ArrayList<T>();
		for (var element : list) {
			if (predicate.test(element)) {
				filteredList.add(element);
			}
		}
		return filteredList;
	}

	public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
		var mappedList = new ArrayList<R>();
		for (var element : list) {
			mappedList.add(function.apply(element));
		}
		return mappedList;
	}

	public static <T> boolean contains(List<T> list, T value) {
		for (var element : list) {
			if (element.equals(value)) {
				return true;
			}
		}
		return false;
	}

}
