package com.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Разбор на уровне примера
 * PAYPALISHIRING с numRows 3 превращается в PAHNAPLSIIGYIR (перезапись слева направо)
 * по буквам
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * <p>
 * <p>
 * PAYPALISHIRING c numRows 4 превращается в PINALSIGYAHRPI
 * <p>
 * P     I    N
 * A   L S  I G
 * Y A   H R
 * P     I
 * <p>
 * "A", numRows = 1 = "A"
 */
public class ZigzagConversionProblem {
    public static void main(String[] args) {
        System.out.println(new VisitByRowSolution().convert("PAYPALISHIRING", 4));
    }

    /**
     * PAYPALISHIRING numRows = 4 = PINALSIGYAHRPI
     * <p>
     * Символы row 0 расположены по индексам, расчитанные по формуле k (2 * numRows - 2)
     * Смотрим первый символ P - 0, второй 6 позиция I, третий 12 - N
     * PIN
     * <p>
     * Символы внутренних row k(2 * numRows - 2) + i и (k + 1)(2 * numRows - 2) - i (чередование) - где i номер внутренней строки, k номер цикла от 0 до 3
     * <p>
     * i = 1
     * 0 * (2 * numRows - 2) + 1 - для первого символа = 0 + 1 = A
     * (0 + 1) * (2 * numRows - 2) - 1 = 5 = L
     * 1 * (2 * numRows - 2) + 1 = 7 = S
     * (1 + 1) * (2 * numRows - 2) - 1 = 2 * 6 - 1 = 11 = I
     * 2 * (2 * numRows - 2) + 1 = 2 * 6 + 1 = 13 = G
     * (1 + 2) * 6 - 1 = 3 * 6 - 1 = 17 SKIP
     * 3 * 6 + 1 = 19 SKIP
     * ALSIG
     * <p>
     * i = 2
     * 0 * (2 * numRows - 2) + 2 - для первого символа = 0 + 2 = Y
     * (0 + 1) * (2 * numRows - 2) - 2 = 4 = A
     * 1 * (2 * numRows - 2) + 2 = 8 = H
     * (1 + 1) * (2 * numRows - 2) - 2 = 12 - 2 = 10 = R
     * 2 * (2 * numRows - 2) + 2 = 14 SKIP
     * <p>
     * YAHR
     * <p>
     * last row k * (2 * numRows - 2) + numRows - 1
     * 0 * 6 + 4 - 1 = 3 = P
     * 1 * 6 + 4 - 1 = 9 = I
     * 2 * 6 + 4 - 1 = 15 SKIP
     * PI
     * <p>
     * складываем PIN + ALSIG + YAHR + PI = PINALSIGYAHRPI
     */
    static class VisitByRowSolution {
        public String convert(String str, int numRows) {
            if (numRows <= 1) {
                return str;
            }


            var lastRow = numRows - 1;
            int cycle = 2 * numRows - 2;
            var result = new StringBuilder();
            var arr = str.toCharArray();

            // проход по row
            for (int i = 0; i < numRows; i++) {
                if (i == 0) {
                    fillFirstRow(cycle, result, arr);
                } else if (i == lastRow) {
                    fillLastRow(numRows, cycle, result, arr);
                } else {
                    fillInnerRow(cycle, result, arr, i);
                }
            }

            return result.toString();
        }

        private static void fillLastRow(int numRows, int cycle, StringBuilder result, char[] arr) {
            int k = 0;
            int length = arr.length;

            while (true) {
                int index = k * cycle + numRows - 1;
                if (index >= length) {
                    break;
                }

                result.append(arr[index]);
                k++;
            }
        }

        private static void fillInnerRow(int cycle, StringBuilder result, char[] arr, int i) {
            int k = 0;
            int length = arr.length;

            while (true) {
                int firstIndex = k * cycle + i;
                if (firstIndex >= length) {
                    break;
                }

                result.append(arr[firstIndex]);
                int secondIndex = (k + 1) * cycle - i;
                if (secondIndex >= length) {
                    break;
                }

                result.append(arr[secondIndex]);
                k++;
            }
        }

        private static void fillFirstRow(int cycle, StringBuilder result, char[] arr) {
            int k = 0;
            int length = arr.length;

            while (true) {
                int index = k * cycle;
                if (index >= length) {
                    break;
                }
                result.append(arr[index]);
                k++;
            }
        }
    }

    /**
     * Сложность O(n)
     * Разбор алгоритма пример
     * PAHNAPLSIIGYIR numRows = 4
     * <p>
     * Первично формируем список string builder-ов для добавления элементов
     * <p>
     * Разбор PAYPALISHIRING
     * инициализация
     * curRow = 0, isUp = false, sb1 = "", sb2 = "", sb3 = "", sb4 = ""
     * <p>
     * проход по первому символу P
     * curRow = 1, isUp = true, sb1 = "P", sb2 = "", sb3 = "", sb4 = ""
     * <p>
     * проход по второму символу A
     * curRow = 2, isUp = true, sb1 = "P", sb2 = "A", sb3 = "", sb4 = ""
     * <p>
     * проход по Y
     * curRow = 3, isUp = true, sb1 = "P", sb2 = "A", sb3 = "Y", sb4 = ""
     * <p>
     * проход по P
     * curRow = 4, isUp = false, sb1 = "P", sb2 = "A", sb3 = "Y", sb4 = "P"
     * <p>
     * проход по A
     * curRow = 3, isUp = false, sb1 = "P", sb2 = "A", sb3 = "YA", sb4 = "P"
     * <p>
     * проход по L
     * curRow = 2, isUp = false, sb1 = "P", sb2 = "AL", sb3 = "YA", sb4 = "P"
     * <p>
     * ... etc как дойдем до curRow = 0, isUp = !isUp
     * <p>
     * в итоге складываем row с 0 до 3 и получаем PINALSIGYAHRPI
     */
    static class SortByRowSolution {
        public String convert(String str, int numRows) {
            if (numRows <= 1) {
                return str;
            }

            List<StringBuilder> rows = new ArrayList<>();
            for (int i = 0; i < Math.min(numRows, str.length()); i++)
                rows.add(new StringBuilder());

            int curRow = 0;
            boolean isUp = false;

            for (char c : str.toCharArray()) {
                rows.get(curRow).append(c);
                if (isFirstOrLastRow(numRows, curRow)) {
                    isUp = !isUp;
                }
                curRow += isUp ? 1 : -1;
            }

            StringBuilder res = new StringBuilder();
            for (StringBuilder row : rows) res.append(row);
            return res.toString();
        }

        private static boolean isFirstOrLastRow(int numRows, int curRow) {
            return curRow == 0 || curRow == numRows - 1;
        }
    }

    /**
     * Помоешный алгоритм
     * Runtime: 53 ms, faster than 12.46% of Java online submissions for Zigzag Conversion.
     * Memory Usage: 65.1 MB, less than 5.90% of Java online submissions for Zigzag Conversion.
     * <p>
     * с очень плохими показателями скорости и исп памяти
     * <p>
     * TODO как вариант можно избавиться от записи в массив а сразу писать в строку используя индексы и длину диагонали
     */
    static class SimpleSolution {
        public String convert(String str, int numRows) {
            if (str == null || str.length() <= 1) {
                return str;
            }

            if (str.length() <= numRows) {
                return str;
            }

            int length = str.length();
            int colRows = Math.max(calculateColumnsCount(length, numRows), numRows) + 1;

            /**
             * Матрица имеет одинаковую длину row / col
             * Для чего это сделано
             * чтобы можно было сделать обход по факту "наоборот" - слева направо а не сверху вниз как будем записывать
             * при записи результат
             *
             */
            char[][] arr = new char[colRows][colRows];
            char initCh = arr[0][0];

            boolean isRowFinished = false;
            int writeCount = 0;
            int diagonalCounter = numRows > 1 ? numRows - 2 : 0;

            //write arr
            for (int i = 0; i < colRows; i++) {
                char[] columnChars = arr[i];

                if (!isRowFinished) {
                    for (int j = 0; j < numRows; j++) {
                        if (writeCount >= length) {
                            break;
                        }

                        columnChars[j] = str.charAt(writeCount);
                        writeCount++;
                    }

                    if (numRows > 2) {
                        isRowFinished = true;
                    }
                } else {
                    if (writeCount >= length) {
                        break;
                    }

                    columnChars[diagonalCounter] = str.charAt(writeCount);
                    writeCount++;

                    if (diagonalCounter == 0 || diagonalCounter - 1 == 0) {
                        isRowFinished = false;
                        diagonalCounter = numRows > 1 ? numRows - 2 : 0;
                    } else {
                        diagonalCounter--;
                    }
                }
            }

            int readCount = 0;
            char[] res = new char[length];
            //read arr
            for (int i = 0; i < colRows; i++) {
                for (int j = 0; j < colRows; j++) {
                    char ch = arr[j][i];

                    if (ch != initCh) {
                        res[readCount] = ch;
                        readCount++;
                    }
                }
            }


            return String.valueOf(res);
        }

        private int calculateColumnsCount(int length, int numRows) {
            if (numRows >= length) {
                return 1;
            }

            int diagonal = numRows - 2;
            int count = 0;
            int res = 0;
            while (count <= length) {
                count += numRows;
                res += 1;

                if (count >= length) {
                    return res;
                }

                for (int i = 1; i <= diagonal; i++) {
                    count += 1;
                    res += 1;

                    if (count >= length) {
                        return res;
                    }
                }
            }

            return res;
        }
    }
}
