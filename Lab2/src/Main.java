/**
 * Главный класс для демонстрации работы дерева
 * Есть меню и тестовые данные
 */

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        initializeTestData(trie);

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean continueRunning = true;

        System.out.println("ПРЕФИКСНОЕ ДЕРЕВО");
        System.out.println("Тестовые данные уже загружены!");

        while (continueRunning) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        handleInsertWord(scanner, trie);
                        break;
                    case "2":
                        handleSearchWord(scanner, trie);
                        break;
                    case "3":
                        handleCheckPrefix(scanner, trie);
                        break;
                    case "4":
                        handleGetByPrefix(scanner, trie);
                        break;
                    case "5":
                        handleRemoveWord(scanner, trie);
                        break;
                    case "6":
                        displayAllWords(trie);
                        break;
                    case "7":
                        displayWordCount(trie);
                        break;
                    case "8":
                        handleGraphOperations(scanner, trie);
                        break;
                    case "0":
                        continueRunning = false;
                        System.out.println("Программа завершена.");
                        break;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }

        scanner.close();
    }

    private static void initializeTestData(Trie trie) {
        String[] testWords = {
                "apple", "app", "application", "apricot", "apply", "applicant",
                "banana", "band", "bandage", "bandit", "banish", "banner",
                "cat", "car", "card", "care", "carpet", "cart", "carrot",
                "dog", "door", "doll", "dollar", "dolphin", "domain"
        };

        for (String word : testWords) {
            try {
                trie.insertWord(word);
            } catch (Trie.WordAlreadyExistsException e) {
                // Игнорируем для тестовых данных
            }
        }

        try {
            trie.addGraphEdge("app", "apple", 1, false);
            trie.addGraphEdge("app", "application", 1, false);
            trie.addGraphEdge("application", "apply", 1, false);
            trie.addGraphEdge("application", "applicant", 1, false);

            trie.addGraphEdge("banana", "band", 1, false);
            trie.addGraphEdge("band", "bandage", 1, false);

            trie.addGraphEdge("car", "card", 1, false);
            trie.addGraphEdge("card", "care", 1, false);

        } catch (Exception e) {
            System.out.println("Ошибка при добавлении тестовых графовых связей: " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Добавить слово");
        System.out.println("2. Проверить наличие слова");
        System.out.println("3. Проверить префикс");
        System.out.println("4. Получить слова по префиксу");
        System.out.println("5. Удалить слово");
        System.out.println("6. Показать все слова");
        System.out.println("7. Показать количество слов");
        System.out.println("8. Операции с графами");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void handleInsertWord(java.util.Scanner scanner, Trie trie) {
        System.out.print("Введите слово для добавления: ");
        String word = scanner.nextLine().trim();
        trie.insertWord(word);
        System.out.println("Слово '" + word + "' успешно добавлено");
    }

    private static void handleSearchWord(java.util.Scanner scanner, Trie trie) {
        System.out.print("Введите слово для поиска: ");
        String word = scanner.nextLine().trim();
        boolean exists = trie.containsWord(word);
        System.out.println("Слово '" + word + "' " + (exists ? "найдено" : "не найдено"));
    }

    private static void handleCheckPrefix(java.util.Scanner scanner, Trie trie) {
        System.out.print("Введите префикс для проверки: ");
        String prefix = scanner.nextLine().trim();
        boolean hasPrefix = trie.hasPrefix(prefix);
        System.out.println("Префикс '" + prefix + "' " +
                (hasPrefix ? "существует в словаре" : "не существует в словаре"));
    }

    private static void handleGetByPrefix(java.util.Scanner scanner, Trie trie) {
        System.out.print("Введите префикс для поиска слов: ");
        String prefix = scanner.nextLine().trim();
        Trie.CustomCollection<String> words = trie.getWordsByPrefix(prefix);
        if (words.isEmpty()) {
            System.out.println("Слова с префиксом '" + prefix + "' не найдены");
        } else {
            System.out.print("Слова с префиксом '" + prefix + "': ");
            for (int i = 0; i < words.getSize(); i++) {
                System.out.print(words.getElement(i) + " ");
            }
            System.out.println();
            System.out.println("Найдено слов: " + words.getSize());
        }
    }

    private static void handleRemoveWord(java.util.Scanner scanner, Trie trie) {
        System.out.print("Введите слово для удаления: ");
        String word = scanner.nextLine().trim();
        trie.removeWord(word);
        System.out.println("Слово '" + word + "' успешно удалено");
    }

    private static void displayAllWords(Trie trie) {
        Trie.CustomCollection<String> allWords = trie.getWordsByPrefix("");
        if (allWords.isEmpty()) {
            System.out.println("Словарь пуст");
        } else {
            System.out.print("Все слова в словаре: ");
            for (int i = 0; i < allWords.getSize(); i++) {
                System.out.print(allWords.getElement(i) + " ");
            }
            System.out.println();
            System.out.println("Всего слов: " + allWords.getSize());
        }
    }

    private static void displayWordCount(Trie trie) {
        System.out.println("Общее количество слов в словаре: " + trie.getTotalWordCount());
    }

    private static void handleGraphOperations(java.util.Scanner scanner, Trie trie) {
        System.out.println("\n=== ОПЕРАЦИИ С ГРАФАМИ ===");
        System.out.println("1. Добавить ребро в граф");
        System.out.println("2. Найти кратчайший путь");
        System.out.println("3. Обход в глубину (DFS)");
        System.out.println("4. Показать информацию о графе");
        System.out.println("5. Удалить ребро");
        System.out.println("6. Подсчитать слова с префиксом");
        System.out.print("Выберите операцию: ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    System.out.print("Введите начальную вершину: ");
                    String from = scanner.nextLine().trim();
                    System.out.print("Введите конечную вершину: ");
                    String to = scanner.nextLine().trim();
                    System.out.print("Введите вес ребра: ");
                    int weight = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Ориентированное ребро? (true/false): ");
                    boolean directed = Boolean.parseBoolean(scanner.nextLine().trim());

                    trie.addGraphEdge(from, to, weight, directed);
                    System.out.println("Ребро добавлено успешно");
                    break;

                case "2":
                    System.out.print("Введите начальную вершину: ");
                    String start = scanner.nextLine().trim();
                    System.out.print("Введите конечную вершину: ");
                    String end = scanner.nextLine().trim();

                    Trie.CustomCollection<String> path = trie.findShortestPath(start, end);
                    if (path.isEmpty()) {
                        System.out.println("Путь не найден");
                    } else {
                        System.out.print("Кратчайший путь: ");
                        for (int i = 0; i < path.getSize(); i++) {
                            System.out.print(path.getElement(i) + " ");
                        }
                        System.out.println();
                    }
                    break;

                case "3":
                    System.out.print("Введите начальную вершину: ");
                    String startNode = scanner.nextLine().trim();

                    Trie.CustomCollection<String> dfsResult = trie.performDepthFirstSearch(startNode);
                    System.out.print("DFS обход: ");
                    for (int i = 0; i < dfsResult.getSize(); i++) {
                        System.out.print(dfsResult.getElement(i) + " ");
                    }
                    System.out.println();
                    break;

                case "4":
                    trie.displayGraphStructure();
                    break;

                case "5":
                    System.out.print("Введите начальную вершину: ");
                    String fromEdge = scanner.nextLine().trim();
                    System.out.print("Введите конечную вершину: ");
                    String toEdge = scanner.nextLine().trim();
                    System.out.print("Ориентированное ребро? (true/false): ");
                    boolean directedEdge = Boolean.parseBoolean(scanner.nextLine().trim());

                    trie.removeGraphEdge(fromEdge, toEdge, directedEdge);
                    System.out.println("Ребро успешно удалено");
                    break;

                case "6":
                    System.out.print("Введите префикс: ");
                    String countPrefix = scanner.nextLine().trim();
                    int count = trie.countWordsWithPrefix(countPrefix);
                    System.out.println("Слов с префиксом '" + countPrefix + "': " + count);
                    break;

                default:
                    System.out.println("Неверный выбор");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}