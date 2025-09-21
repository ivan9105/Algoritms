package com.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WordBreakProblem {
    public static void main(String[] args) {
        System.out.println(new BFSSolution().wordBreak("applepenapple", new ArrayList<>() {{
            add("apple");
            add("pen");
        }}));
    }

    /**
     // начинаем считывать с 0, до тех пор пока не достигаем одного из сегментов
     // добавляем позицию в очередь для след. обработки
     // дочитываем слово
     // если очередь не пустая начинаем заново уже со след. позици
     // если достигли конца строки и получившаяся часть слова содержиться в dict, это означает что слово делиться на выделенные сегменты
     */
    static class BFSSolution {
        public boolean wordBreak(String word, List<String> wordDict) {
            var queue = new LinkedList<Integer>();
            queue.add(0);

            var len = word.length();
            var visited = new boolean[len];
            while (!queue.isEmpty()) {
                var start = queue.poll();
                if (!visited[start]) {
                    for (int end = start + 1; end <= len; end++) {
                        var segment = word.substring(start, end);
                        if (wordDict.contains(segment)) {
                            if (end == len)
                                return true;
                            else
                                queue.add(end);
                        }
                    }
                    visited[start] = true;
                }
            }
            return false;
        }
    }
}
