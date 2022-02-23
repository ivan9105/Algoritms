package com.leetcode;

import java.util.ArrayList;
import java.util.List;

public class WordBreakProblem {
    public static void main(String[] args) {
        System.out.println(new SimpleSolution().wordBreak("leetcode", new ArrayList<>() {{
            add("leet");
            add("code");
        }}));
    }

    /**
     * TODO не правильно понял задачу переделать
     *
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
