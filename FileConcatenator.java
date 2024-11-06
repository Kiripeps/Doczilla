package tst.task;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * @author Брянцев Кирилл
 */
public class FileConcatenator {

    //Поиск всех текстовых файлов.
    public static List<Path> findAllTextFiles(Path rootDirectory) throws IOException {
        List<Path> textFiles = new ArrayList<>();
        Files.walk(rootDirectory)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".txt"))
                .forEach(textFiles::add);
        textFiles.sort(Comparator.comparing(Path::getFileName)); // Сортировка по имени
        return textFiles;
    }

    //Парсинг всех зависимостей в текстовых файлах
    public static List<Path> parseRequirements(Path filePath, Path rootDirectory) throws IOException {
        List<Path> dependencies = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\*require\\s+'(.+?)'");
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    Path dependencyPath = rootDirectory.resolve(matcher.group(1)).normalize();
                    dependencies.add(dependencyPath);
                }
            }
        }
        return dependencies;
    }

    // Построение графа зависимостей
    public static Map<Path, List<Path>> buildDependencyGraph(List<Path> files, Path rootDirectory) throws IOException {
        Map<Path, List<Path>> dependencyGraph = new HashMap<>();
        Set<Path> allFilesSet = new HashSet<>(files);

        for (Path file : files) {
            List<Path> dependencies = parseRequirements(file, rootDirectory);
            dependencyGraph.put(file, new ArrayList<>());
            for (Path dependency : dependencies) {
                if (allFilesSet.contains(dependency)) {
                    dependencyGraph.get(file).add(dependency);
                } else {
                    System.out.println("Warning: файл " + dependency + " не найден, но указан в " + file);
                }
            }
        }
        return dependencyGraph;
    }

    //Сортировка с проверкой на циклы
    public static List<Path> topologicalSort(Map<Path, List<Path>> dependencyGraph) {
        Map<Path, Integer> inDegree = new HashMap<>();
        for (Path node : dependencyGraph.keySet()) {
            inDegree.put(node, 0);
        }
        for (List<Path> dependencies : dependencyGraph.values()) {
            for (Path dep : dependencies) {
                inDegree.put(dep, inDegree.get(dep) + 1);
            }
        }

        Queue<Path> queue = new LinkedList<>();
        for (Path node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }

        List<Path> sortedFiles = new ArrayList<>();
        int visitedCount = 0;

        while (!queue.isEmpty()) {
            Path node = queue.poll();
            sortedFiles.add(node);
            visitedCount++;

            for (Path neighbor : dependencyGraph.getOrDefault(node, Collections.emptyList())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (visitedCount != dependencyGraph.size()) {
            System.out.println("Циклическая зависимость обнаружена!");
            return null;
        }

        Collections.reverse(sortedFiles); // Разворачиваем порядок
        return sortedFiles;
    }

    // Объединение файлов по зависимостям
    public static void concatenateFilesInDependencyOrder(Path rootDirectory, Path outputFile) throws IOException {
        List<Path> textFiles = findAllTextFiles(rootDirectory);
        Map<Path, List<Path>> dependencyGraph = buildDependencyGraph(textFiles, rootDirectory);
        List<Path> sortedFiles = topologicalSort(dependencyGraph);

        if (sortedFiles == null) {
            System.out.println("Циклическая зависимость. Построение правильной очередности невозможно.");
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            for (Path filePath : sortedFiles) {
                Files.lines(filePath).forEach(line -> {
                    try {
                        writer.write(line);
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                writer.newLine(); // Добавляем пустую строку между файлами
            }
        }
    }
}

