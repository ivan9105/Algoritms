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
            add("bbb");
            add("bbbb");
        }}));
    }

    /**
     * Смысл задачи следующий
     * необходимо понять что строка изначально делиться на слова из словаря
     *
     * пример applepenapple, словарь apple pen
     * 0, 5 - apple
     * 5, 8 - pen
     * 8, 13 - apple
     *
     * пример applepen, словарь apples, pen
     * вернет false
     * так как по факту у нас никогда не будет вхождения apple
     */
    static class BFSSolution {
        public boolean wordBreak(String s, List<String> wordDict) {
            Queue<Integer> queue = new LinkedList<>();
            queue.add(0);

            int len = s.length();
            boolean[] visited = new boolean[len];
            while (!queue.isEmpty()) {
                System.out.printf("Queue: %s %n", queue);
                int start = queue.poll();
                System.out.printf("Start: %s %n", start);
                if (!visited[start]) {
                    for (int end = start + 1; end <= len; end++) {
                        String segment = s.substring(start, end);
                        System.out.printf("Start: %s, End: %s, Segment: %s, Queue: %s %n", start, end, segment, queue);
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

    /**
     * TODO не правильно понял задачу переделать
     * <p>
     * Input:
     * "bb"
     * ["a","b","bbb","bbbb"]
     * Output:
     * false
     * Expected:
     * true
     */
    static class SimpleSolution {
        public boolean wordBreak(String s, List<String> wordDict) {
            List<CalcResult> calcResults = new ArrayList<>();

            for (String word : wordDict) {
                if (!s.contains(word)) {
                    return false;
                }

                int start = s.indexOf(word);
                int end = start + word.length();
                var newCalcResult = new CalcResult(start, end);
                if (isIntersect(newCalcResult, calcResults)) {
                    return false;
                }
                calcResults.add(newCalcResult);
            }

            return true;
        }

        private static boolean isIntersect(CalcResult newCalcResult, List<CalcResult> existsCalcResults) {
            for (CalcResult oldCalcResult : existsCalcResults) {
                if (oldCalcResult.isIntersect(newCalcResult)) {
                    return true;
                }
            }
            return false;
        }

        private static class CalcResult {
            private final int start;
            private final int end;

            public CalcResult(int start, int end) {
                this.start = start;
                this.end = end;
            }

            public boolean isIntersect(CalcResult another) {
                if (another.start > start && another.start < end) {
                    return true;
                }

                if (another.end > start && another.end < end) {
                    return true;
                }

                if (another.start < start && another.end > start) {
                    return true;
                }

                if (another.end > end && another.start < end) {
                    return true;
                }

                return false;
            }
        }
    }
}
