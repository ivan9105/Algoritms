package com.leetcode;

/**
 * class Trie {
 * <p>
 * public Trie() {
 * <p>
 * }
 * <p>
 * public void insert(String word) {
 * <p>
 * }
 * <p>
 * public boolean search(String word) {
 * <p>
 * }
 * <p>
 * public boolean startsWith(String prefix) {
 * <p>
 * }
 * }
 */
public class PrefixTrieProblem {
    public static void main(String[] args) {

    }

    class Trie {
        private TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        // проходимся по слову и делаем ветвь в дереве
        public void insert(String word) {
            //инициализируем локальную переменную для прохода по всему слову
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char currentChar = word.charAt(i);
                if (!current.containsKey(currentChar)) {
                    current.put(currentChar, new TrieNode());
                }
                current = current.get(currentChar);
            }
            current.setEnd();
        }

        // требуется пройтись по всему дереву начиная с root и найти ветвь с признаком isEnd,
        // тогда мы будем уверены что нашли полностью все слово
        public boolean search(String word) {
            TrieNode targetNode = searchPrefix(word);
            if (targetNode == null) {
                return false;
            }

            return targetNode.isEnd;
        }

        // требуется пройтись по всему дереву начиная с root и найти ветвь признак isEnd нам не особо важен,
        // тогда мы будем уверены что нашли часть слова
        public boolean startsWith(String prefix) {
            TrieNode targetNode = searchPrefix(prefix);
            return targetNode != null;
        }

        private TrieNode searchPrefix(String word) {
            //инициализируем локальную переменную для прохода по всему слову
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char currentChar = word.charAt(i);
                if (!current.containsKey(currentChar)) {
                    return null;
                }

                current = current.get(currentChar);
            }
            return current;
        }

    }

    /**
     * Слово будет храниться в виде дерева
     * при вставке одна node хранит одну букву дальше двигаясь в цикле по слову мы формируем связанный список
     * максимальная длина уровня соотвественно 26 символов, кол-во букв в английском алфавите
     */
    private class TrieNode {
        private TrieNode[] links;
        private boolean isEnd;

        private final int MAX_ALPHABET_SIZE = 26;

        public TrieNode() {
            links = new TrieNode[MAX_ALPHABET_SIZE];
        }

        /**
         * длина массива максимальная 26
         * <p>
         * соотвественно когда мы отнимаем char от char
         * java конвертрирует char в int
         * например 'A' в числовом диапазоне в таблице юникод == 65
         * и определяем позицию в массиве
         * пример
         * 'a' в int == 97
         * 'a' - 'a' == 0
         */
        public boolean containsKey(char ch) {
            return links[ch - 'a'] != null;
        }

        public TrieNode get(char ch) {
            return links[ch - 'a'];
        }

        public void put(char ch, TrieNode node) {
            links[ch - 'a'] = node;
        }

        public void setEnd() {
            isEnd = true;
        }

        public boolean isEnd() {
            return isEnd;
        }
    }
}