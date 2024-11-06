# Инструкция по запуску
1) Скачать файл
2) Подключить к любому main классу
##Пример:
1)Path rootDirectory = Paths.get("path/to/rootDirect");
2)Path outputFile = Paths.get("path/to/output_file.txt");
3)FileConcatenator.concatenateFilesInDependencyOrder(rootDirectory, outputFile);
