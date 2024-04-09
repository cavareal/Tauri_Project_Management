package fr.eseo.tauri.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListUtil {

	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		var filteredList = new ArrayList<T>();
		for (var element : list) {
			if (predicate.test(element)) {
				filteredList.add(element);
			}
		}
		return filteredList;
	}

}
