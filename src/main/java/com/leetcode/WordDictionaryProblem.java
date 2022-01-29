package com.leetcode;

/**
 * class WordDictionary {
 * <p>
 * public WordDictionary() {
 * <p>
 * }
 * <p>
 * public void addWord(String word) {
 * <p>
 * }
 * <p>
 * public boolean search(String word) {
 * <p>
 * }
 * }
 */
public class WordDictionaryProblem {
    public static void main(String[] args) {

    }

    class WordDictionary {
        private WordDictionaryNode root;

        private static final char ANY = '.';

        public WordDictionary() {
            root = new WordDictionaryNode();
        }

        // проходимся по слову и делаем ветвь в дереве
        public void addWord(String word) {
            //инициализируем локальную переменную для прохода по всему слову
            WordDictionaryNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char currentChar = word.charAt(i);
                if (!current.containsKey(currentChar)) {
                    current.put(currentChar, new WordDictionaryNode());
                }
                current = current.get(currentChar);
            }
            current.setEnd();
        }

        /**
         * Алгоритм очень похож на PrefixTrieProblem
         * за исключением того что у нас добавляется тут специальный символ, который может означать любой символ '.'
         * <p>
         * Input
         * ["WordDictionary","addWord","addWord","addWord","search","search","search","search"]
         * [[],["bad"],["dad"],["mad"],["pad"],["bad"],[".ad"],["b.."]]
         * Output
         * [null,null,null,null,false,true,true,true]
         */
        public boolean search(String word) {
            return isMatch(word, 0, root);
        }

        //рекурсивно проходимся по всем child начиная с root, делая при этом проход по всем возможным вариантам
        private boolean isMatch(String word, int level, WordDictionaryNode current) {
            //выходная функция
            if (level == word.length()) {
                return current.isEnd();
            }

            char currentChar = word.charAt(level);
            //если символ не равняется любому символу отрабатывает стандартный алгоритм PrefixTrieProblem
            if (currentChar != ANY) {
                WordDictionaryNode secondNode = current.get(currentChar);
                return secondNode != null && isMatch(word, level + 1, secondNode);
            } else {
                //иначе пропускаем фильтрацию по символам по всем остальным child и переходим на обработку след node
                for (int i = 0; i < current.links.length; i++) {
                    WordDictionaryNode currentChild = current.links[i];
                    if (currentChild != null && isMatch(word, level + 1, currentChild)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Слово будет храниться в виде дерева
     * при вставке одна node хранит одну букву дальше двигаясь в цикле по слову мы формируем связанный список
     * максимальная длина уровня соотвественно 26 символов, кол-во букв в английском алфавите
     */
    private static class WordDictionaryNode {
        private WordDictionaryNode[] links;
        private boolean isEnd;

        private static final int MAX_ALPHABET_SIZE = 26;

        public WordDictionaryNode() {
            links = new WordDictionaryNode[MAX_ALPHABET_SIZE];
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

        public WordDictionaryNode get(char ch) {
            return links[ch - 'a'];
        }

        public void put(char ch, WordDictionaryNode node) {
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
