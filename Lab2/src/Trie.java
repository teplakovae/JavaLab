/**
 * Класс Trie представляет префиксное дерево
 * Хранит слова и связи между ними в виде графа
 */

public class Trie {
    private static final int ALPHABET_SIZE = 26;

    // Исключения
    public static class WordAlreadyExistsException extends RuntimeException {
        public WordAlreadyExistsException(String message) { super(message); }
    }

    public static class WordNotFoundException extends RuntimeException {
        public WordNotFoundException(String message) { super(message); }
    }

    public static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) { super(message); }
    }

    public static class DuplicateEdgeException extends RuntimeException {
        public DuplicateEdgeException(String message) { super(message); }
    }

    private static class TrieNode {
        private final TrieNode[] children;
        private boolean isEndOfWord;
        private String wordValue;
        private int childrenCount;

        // Графовые связи
        private CustomCollection<GraphEdge> graphEdges;

        public TrieNode() {
            this.children = new TrieNode[ALPHABET_SIZE];
            this.isEndOfWord = false;
            this.wordValue = null;
            this.childrenCount = 0;
            this.graphEdges = new CustomCollection<>();
        }
    }

    private static class GraphEdge {
        TrieNode target;
        int weight;
        boolean isDirected;

        GraphEdge(TrieNode target, int weight, boolean isDirected) {
            this.target = target;
            this.weight = weight;
            this.isDirected = isDirected;
        }
    }

    // Вспомогательный класс для хранения расстояний в алгоритме Дейкстры
    private static class DistanceEntry {
        TrieNode node;
        int distance;
        TrieNode previous;

        DistanceEntry(TrieNode node, int distance, TrieNode previous) {
            this.node = node;
            this.distance = distance;
            this.previous = previous;
        }
    }

    private static class SimplePriorityQueue {
        private CustomCollection<DijkstraNode> elements;

        public SimplePriorityQueue() {
            this.elements = new CustomCollection<>();
        }

        public void enqueue(DijkstraNode node) {
            elements.addElement(node);
        }

        public DijkstraNode dequeue() {
            if (elements.isEmpty()) {
                return null;
            }
            // Линейный поиск элемента с минимальным расстоянием
            int minIndex = 0;
            DijkstraNode minNode = elements.getElement(0);

            for (int i = 1; i < elements.getSize(); i++) {
                DijkstraNode current = elements.getElement(i);
                if (current.distance < minNode.distance) {
                    minNode = current;
                    minIndex = i;
                }
            }

            elements.removeElement(minIndex);
            return minNode;
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }
    }

    // Параметризованная коллекция для хранения элементов
    public static class CustomCollection<T> {
        private T[] elements;
        private int currentSize;
        private int currentCapacity;

        @SuppressWarnings("unchecked")
        public CustomCollection() {
            this.currentCapacity = 10;
            this.currentSize = 0;
            this.elements = (T[]) new Object[currentCapacity];
        }

        public int getSize() {
            return currentSize;
        }

        public boolean isEmpty() {
            return currentSize == 0;
        }

        @SuppressWarnings("unchecked")
        private void resizeArray() {
            currentCapacity *= 2;
            T[] newElementsArray = (T[]) new Object[currentCapacity];
            for (int i = 0; i < currentSize; i++) {
                newElementsArray[i] = elements[i];
            }
            elements = newElementsArray;
        }

        public void addElement(T element) {
            if (element == null) {
                throw new InvalidInputException("Элемент не может быть null");
            }
            if (currentSize == currentCapacity) {
                resizeArray();
            }
            elements[currentSize++] = element;
        }

        public T getElement(int index) {
            if (index < 0 || index >= currentSize) {
                throw new IndexOutOfBoundsException("Неверный индекс: " + index + ", размер коллекции: " + currentSize);
            }
            return elements[index];
        }

        public void removeElement(int index) {
            if (index < 0 || index >= currentSize) {
                throw new IndexOutOfBoundsException("Неверный индекс: " + index + ", размер коллекции: " + currentSize);
            }
            for (int i = index; i < currentSize - 1; i++) {
                elements[i] = elements[i + 1];
            }
            elements[currentSize - 1] = null;
            currentSize--;
        }

        public boolean contains(T element) {
            for (int i = 0; i < currentSize; i++) {
                if (elements[i] != null && elements[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }

        public CustomCollection<T> copyCollection() {
            CustomCollection<T> copy = new CustomCollection<>();
            for (int i = 0; i < currentSize; i++) {
                copy.addElement(elements[i]);
            }
            return copy;
        }
    }

    private final TrieNode rootNode;
    private int totalWordCount;

    public Trie() {
        this.rootNode = new TrieNode();
        this.totalWordCount = 0;
    }

    // Основные методы Trie
    public void insertWord(String word) {
        validateWord(word);
        word = word.toLowerCase();

        if (containsWord(word)) {
            throw new WordAlreadyExistsException("Слово '" + word + "' уже существует в дереве");
        }

        TrieNode currentNode = rootNode;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            int charIndex = currentChar - 'a';

            if (charIndex < 0 || charIndex >= ALPHABET_SIZE) {
                throw new InvalidInputException("Неверный символ в слове: " + currentChar);
            }

            if (currentNode.children[charIndex] == null) {
                currentNode.children[charIndex] = new TrieNode();
                currentNode.childrenCount++;
            }
            currentNode = currentNode.children[charIndex];
        }

        currentNode.isEndOfWord = true;
        currentNode.wordValue = word;
        totalWordCount++;
    }

    public boolean containsWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        word = word.toLowerCase();
        TrieNode node = findNodeByPrefix(word);
        return node != null && node.isEndOfWord;
    }

    public boolean hasPrefix(String prefix) {
        if (prefix == null) {
            return false;
        }
        prefix = prefix.toLowerCase();
        if (prefix.isEmpty()) {
            return totalWordCount > 0;
        }
        return findNodeByPrefix(prefix) != null;
    }

    public CustomCollection<String> getWordsByPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        prefix = prefix.toLowerCase();

        CustomCollection<String> resultCollection = new CustomCollection<>();
        TrieNode prefixNode = findNodeByPrefix(prefix);
        if (prefixNode != null) {
            collectAllWordsFromNode(prefixNode, resultCollection);
        }
        return resultCollection;
    }

    public void removeWord(String word) {
        validateWord(word);
        word = word.toLowerCase();

        if (!containsWord(word)) {
            throw new WordNotFoundException("Слово '" + word + "' не найдено для удаления");
        }

        removeWordFromTrie(rootNode, word, 0);
        totalWordCount--;
    }

    // Графовые методы
    public void addGraphEdge(String fromWord, String toWord, int weight, boolean directed) {
        validateWord(fromWord);
        validateWord(toWord);
        fromWord = fromWord.toLowerCase();
        toWord = toWord.toLowerCase();

        TrieNode fromNode = findNodeByPrefix(fromWord);
        TrieNode toNode = findNodeByPrefix(toWord);

        if (fromNode == null || !fromNode.isEndOfWord) {
            throw new WordNotFoundException("Исходное слово '" + fromWord + "' не найдено");
        }
        if (toNode == null || !toNode.isEndOfWord) {
            throw new WordNotFoundException("Целевое слово '" + toWord + "' не найдено");
        }

        for (int i = 0; i < fromNode.graphEdges.getSize(); i++) {
            GraphEdge edge = fromNode.graphEdges.getElement(i);
            if (edge.target == toNode && edge.isDirected == directed) {
                throw new DuplicateEdgeException("Ребро уже существует между '" + fromWord + "' и '" + toWord + "'");
            }
        }

        fromNode.graphEdges.addElement(new GraphEdge(toNode, weight, directed));

        if (!directed) {
            toNode.graphEdges.addElement(new GraphEdge(fromNode, weight, directed));
        }
    }

    public CustomCollection<String> findShortestPath(String startWord, String endWord) {
        validateWord(startWord);
        validateWord(endWord);
        startWord = startWord.toLowerCase();
        endWord = endWord.toLowerCase();

        TrieNode startNode = findNodeByPrefix(startWord);
        TrieNode endNode = findNodeByPrefix(endWord);

        if (startNode == null || !startNode.isEndOfWord) {
            throw new WordNotFoundException("Стартовое слово '" + startWord + "' не найдено");
        }
        if (endNode == null || !endNode.isEndOfWord) {
            throw new WordNotFoundException("Конечное слово '" + endWord + "' не найдено");
        }

        return performDijkstraAlgorithm(startNode, endNode);
    }

    public CustomCollection<String> performDepthFirstSearch(String startWord) {
        validateWord(startWord);
        startWord = startWord.toLowerCase();

        TrieNode startNode = findNodeByPrefix(startWord);
        if (startNode == null || !startNode.isEndOfWord) {
            throw new WordNotFoundException("Стартовое слово '" + startWord + "' не найдено");
        }

        CustomCollection<String> result = new CustomCollection<>();
        CustomCollection<TrieNode> visited = new CustomCollection<>();
        performDFSTraversal(startNode, visited, result);
        return result;
    }

    public void displayGraphStructure() {
        System.out.println("ИНФОРМАЦИЯ О СТРУКТУРЕ ГРАФА");
        displayGraphStructure(rootNode, new CustomCollection<>());
    }

    // Доп методы
    public int countWordsWithPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        prefix = prefix.toLowerCase();

        CustomCollection<String> words = getWordsByPrefix(prefix);
        return words.getSize();
    }

    public void removeGraphEdge(String fromWord, String toWord, boolean directed) {
        validateWord(fromWord);
        validateWord(toWord);
        fromWord = fromWord.toLowerCase();
        toWord = toWord.toLowerCase();

        TrieNode fromNode = findNodeByPrefix(fromWord);
        TrieNode toNode = findNodeByPrefix(toWord);

        if (fromNode == null || !fromNode.isEndOfWord) {
            throw new WordNotFoundException("Исходное слово '" + fromWord + "' не найдено");
        }

        boolean removed = false;
        for (int i = 0; i < fromNode.graphEdges.getSize(); i++) {
            GraphEdge edge = fromNode.graphEdges.getElement(i);
            if (edge.target == toNode && edge.isDirected == directed) {
                fromNode.graphEdges.removeElement(i);
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new WordNotFoundException("Ребро не найдено между '" + fromWord + "' и '" + toWord + "'");
        }

        if (!directed && toNode != null && toNode.isEndOfWord) {
            for (int i = 0; i < toNode.graphEdges.getSize(); i++) {
                GraphEdge edge = toNode.graphEdges.getElement(i);
                if (edge.target == fromNode && !edge.isDirected) {
                    toNode.graphEdges.removeElement(i);
                    break;
                }
            }
        }
    }

    // Вспомогательные методы
    private void validateWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new InvalidInputException("Слово не может быть null или пустым");
        }
    }

    private TrieNode findNodeByPrefix(String prefix) {
        TrieNode currentNode = rootNode;
        for (int i = 0; i < prefix.length(); i++) {
            char currentChar = prefix.charAt(i);
            int charIndex = currentChar - 'a';

            if (charIndex < 0 || charIndex >= ALPHABET_SIZE || currentNode.children[charIndex] == null) {
                return null;
            }
            currentNode = currentNode.children[charIndex];
        }
        return currentNode;
    }

    private void collectAllWordsFromNode(TrieNode node, CustomCollection<String> collection) {
        if (node == null) {
            return;
        }

        if (node.isEndOfWord && node.wordValue != null) {
            collection.addElement(node.wordValue);
        }

        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                collectAllWordsFromNode(node.children[i], collection);
            }
        }
    }

    private boolean removeWordFromTrie(TrieNode currentNode, String word, int currentDepth) {
        if (currentDepth == word.length()) {
            if (!currentNode.isEndOfWord) {
                return false;
            }
            currentNode.isEndOfWord = false;
            currentNode.wordValue = null;
            currentNode.graphEdges = new CustomCollection<>();
            return currentNode.childrenCount == 0;
        }

        char currentChar = word.charAt(currentDepth);
        int charIndex = currentChar - 'a';
        TrieNode childNode = currentNode.children[charIndex];

        if (childNode == null) {
            return false;
        }

        boolean shouldDeleteChildNode = removeWordFromTrie(childNode, word, currentDepth + 1);

        if (shouldDeleteChildNode) {
            currentNode.children[charIndex] = null;
            currentNode.childrenCount--;
            return currentNode.childrenCount == 0 && !currentNode.isEndOfWord;
        }
        return false;
    }

    // Вспомогательный метод для поиска DistanceEntry по узлу
    private DistanceEntry findDistanceEntry(CustomCollection<DistanceEntry> entries, TrieNode node) {
        for (int i = 0; i < entries.getSize(); i++) {
            DistanceEntry entry = entries.getElement(i);
            if (entry.node == node) {
                return entry;
            }
        }
        return null;
    }

    // алгоритм Дейкстры
    private CustomCollection<String> performDijkstraAlgorithm(TrieNode start, TrieNode end) {
        CustomCollection<DistanceEntry> distances = new CustomCollection<>();
        CustomCollection<TrieNode> visited = new CustomCollection<>();
        SimplePriorityQueue queue = new SimplePriorityQueue();

        distances.addElement(new DistanceEntry(start, 0, null));
        queue.enqueue(new DijkstraNode(start, 0));

        while (!queue.isEmpty()) {
            DijkstraNode current = queue.dequeue();
            TrieNode currentNode = current.node;

            if (visited.contains(currentNode)) continue;
            visited.addElement(currentNode);

            if (currentNode == end) break;

            // Обработка соседей
            for (int i = 0; i < currentNode.graphEdges.getSize(); i++) {
                GraphEdge edge = currentNode.graphEdges.getElement(i);
                if (!visited.contains(edge.target)) {
                    DistanceEntry currentDist = findDistanceEntry(distances, currentNode);
                    DistanceEntry edgeDist = findDistanceEntry(distances, edge.target);

                    int currentDistance = currentDist != null ? currentDist.distance : Integer.MAX_VALUE;
                    int neighborDistance = edgeDist != null ? edgeDist.distance : Integer.MAX_VALUE;
                    int newDistance = currentDistance + edge.weight;

                    if (newDistance < neighborDistance) {
                        if (edgeDist != null) {
                            edgeDist.distance = newDistance;
                            edgeDist.previous = currentNode;
                        } else {
                            distances.addElement(new DistanceEntry(edge.target, newDistance, currentNode));
                        }
                        queue.enqueue(new DijkstraNode(edge.target, newDistance));
                    }
                }
            }
        }

        return reconstructPath(distances, start, end);
    }

    private CustomCollection<String> reconstructPath(CustomCollection<DistanceEntry> distances,
                                                     TrieNode start, TrieNode end) {
        CustomCollection<String> path = new CustomCollection<>();
        TrieNode current = end;

        // Собираем путь в обратном порядке
        while (current != null) {
            path.addElement(current.wordValue);
            DistanceEntry entry = findDistanceEntry(distances, current);
            current = (entry != null) ? entry.previous : null;
        }

        // Разворачиваем путь
        CustomCollection<String> reversedPath = new CustomCollection<>();
        for (int i = path.getSize() - 1; i >= 0; i--) {
            reversedPath.addElement(path.getElement(i));
        }

        // Проверка что путь начинается со start
        return reversedPath.getSize() > 0 && reversedPath.getElement(0).equals(start.wordValue) ?
                reversedPath : new CustomCollection<>();
    }

    private void performDFSTraversal(TrieNode node, CustomCollection<TrieNode> visited, CustomCollection<String> result) {
        if (node == null || visited.contains(node)) return;

        visited.addElement(node);
        if (node.isEndOfWord && node.wordValue != null) {
            result.addElement(node.wordValue);
        }

        for (int i = 0; i < node.graphEdges.getSize(); i++) {
            GraphEdge edge = node.graphEdges.getElement(i);
            if (!visited.contains(edge.target)) {
                performDFSTraversal(edge.target, visited, result);
            }
        }
    }

    private void displayGraphStructure(TrieNode node, CustomCollection<TrieNode> visited) {
        if (node == null || visited.contains(node)) return;

        visited.addElement(node);
        if (node.isEndOfWord && node.wordValue != null) {
            System.out.print(node.wordValue + " -> ");
            for (int i = 0; i < node.graphEdges.getSize(); i++) {
                GraphEdge edge = node.graphEdges.getElement(i);
                System.out.print(edge.target.wordValue + "(" + edge.weight +
                        (edge.isDirected ? " ориентированное" : " неориентированное") + ") ");
            }
            System.out.println();
        }

        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                displayGraphStructure(node.children[i], visited);
            }
        }
    }

    private static class DijkstraNode implements Comparable<DijkstraNode> {
        TrieNode node;
        int distance;

        DijkstraNode(TrieNode node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(DijkstraNode other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public int getTotalWordCount() {
        return totalWordCount;
    }

    public void clearTrie() {
        for (int i = 0; i < rootNode.children.length; i++) {
            rootNode.children[i] = null;
        }
        rootNode.childrenCount = 0;
        totalWordCount = 0;
        rootNode.graphEdges = new CustomCollection<>();
    }
}