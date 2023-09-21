import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

public class LastCreatedFileSearch {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the extension");
        String extension = scanner.nextLine();
        System.out.println("Please input directory path");
        String directoryPath = scanner.nextLine();
        System.out.println("Please input max quantity of files");
        int quantityOfFiles = scanner.nextInt();
        System.out.println("Please input quantity of directories");
        int max = scanner.nextInt();

        File directory = new File(directoryPath);

        List<File> foundFiles = new ArrayList<>();
        searchForFiles(directory, extension, foundFiles, 0, max);

        if (foundFiles.isEmpty()) {
            System.out.println("Files with extension not found");
            return;
        }
        foundFiles.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                try {
                    long a = Files.readAttributes(o1.toPath(), BasicFileAttributes.class).creationTime().toMillis();
                    long b = Files.readAttributes(o2.toPath(), BasicFileAttributes.class).creationTime().toMillis();
                    if (a > b) {
                        return -1;
                    } else if (a == b) {
                        return 0;
                    } else if (b > a) {
                        return 1;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return 0;
            }
        });

        File newestFile = foundFiles.get(0);
        long newestFileCreationTime = Files.readAttributes(newestFile.toPath(), BasicFileAttributes.class).creationTime().toMillis();

        List<File> matchingFiles = new ArrayList<>();
        matchingFiles.add(newestFile);

        List<File> recentFiles = foundFiles.stream().filter(file -> {
            try {
                long a = Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().toMillis();
                long num = newestFileCreationTime - a;
                return num<10000;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        if (quantityOfFiles < recentFiles.size()) {
            System.out.println("log");
            recentFiles = recentFiles.subList(0, quantityOfFiles);
        }

        System.out.println("List of files with the extension is:");
        for (File file : recentFiles) {
            System.out.println(file.getName());
        }
    }

    private static void searchForFiles(File directory, String extension, List<File> foundFiles, int i, int max) {
        File[] files = directory.listFiles();
        if (files != null && i <= max) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(extension)) {
                    foundFiles.add(file);

                } else if (file.isDirectory()) {
                    searchForFiles(file, extension, foundFiles, i + 1, max);
                }
            }
        }
    }
}
