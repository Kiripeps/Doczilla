package tst.task;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args){
        //Получение пути до основной директории.
        Path rootDirectory = Paths.get("C:/Users/Kiripeps/Desktop/DesktopDirectory");
        //Получение пути до файла с выводом.
        Path outputFile = Paths.get("C:/Users/Kiripeps/Desktop/output_file.txt");

        try {
            FileConcatenator.concatenateFilesInDependencyOrder(rootDirectory, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}