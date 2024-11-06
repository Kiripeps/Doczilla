# Инструкция по запуску
1) Скачать файл
2) Подключить к любому main классу
*//Получение пути до основной директории.
*Path rootDirectory = Paths.get("path/to/rootDirect");
*//Получение пути до файла с выводом.
*Path outputFile = Paths.get("path/to/output_file.txt");
*FileConcatenator.concatenateFilesInDependencyOrder(rootDirectory, outputFile);
