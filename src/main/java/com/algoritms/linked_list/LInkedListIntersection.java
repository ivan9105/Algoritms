package com.algoritms.linked_list;

import java.util.LinkedList;
import java.util.List;

public class LInkedListIntersection {
    public static void main(String[] args) {
        var first = new LinkedList<Integer>() {{
            addAll(List.of(3, 1, 5, 9, 7, 2, 1));
        }};

        var second = new LinkedList<Integer>() {{
            addAll(List.of(4, 6, 7, 2, 1));
        }};

        var intersection = find(first, second);
        System.out.printf("The intersection in tail with %s and %s: %s %n", first, second, intersection);
    }

    //пересечение учитывает только хвосты
    private static LinkedList<Integer> find(LinkedList<Integer> first, LinkedList<Integer> second) {
        if (!second.getLast().equals(first.getLast())) {
            return null;
        }

        var result = new LinkedList<Integer>();
        var longer = second.size() > first.size() ? second : first;
        var shorter = longer != second ? second : first;

        var longerIterator = longer.listIterator(longer.size());
        var shorterIterator = shorter.listIterator(shorter.size());

        var longerItem = longerIterator.previous();
        var shortIterm = shorterIterator.previous();
        while (longerItem.equals(shortIterm)) {
            result.addFirst(longerItem);
            longerItem = longerIterator.previous();
            shortIterm = shorterIterator.previous();
        }

        return result;
    }
}
