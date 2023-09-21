import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class LastCreatedFileSearch {

            public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please input the extension");
                String extension = scanner.nextLine();
                System.out.println("Please input directory path");
                String directoryPath = scanner.nextLine();
                System.out.println("Please input max quantity of files");
                int quantityOfFiles = scanner.nextInt();

                File directory = new File(directoryPath);

                List<File> foundFiles = new ArrayList<>();
                searchForFiles(directory, extension, foundFiles);

                if (foundFiles.isEmpty()) {
                    System.out.println("Files with extension not found");
                    return;
                }

                foundFiles.sort(Comparator.comparingLong(File::lastModified).reversed());

                File newestFile = foundFiles.get(0);
                long newestFileCreationTime = newestFile.lastModified();

                List<File> matchingFiles = new ArrayList<>();
                matchingFiles.add(newestFile);

                for (int i = 1; i < foundFiles.size(); i++) {
                    File file = foundFiles.get(i);
                    long fileCreationTime = file.lastModified();
                    if (Math.abs(newestFileCreationTime - fileCreationTime) <= 10000 && quantityOfFiles >= matchingFiles.size() ) {
                        matchingFiles.add(file);
                    } else {
                        break;
                    }
                }

                System.out.println("List of files with the extension is:");
                for (File file : matchingFiles) {
                    System.out.println(file.getName());
                }
            }

            private static void searchForFiles(File directory, String extension, List<File> foundFiles) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(extension)) {
                            foundFiles.add(file);
                        } else if (file.isDirectory()) {
                            searchForFiles(file, extension, foundFiles);
                        }
                    }
                }
            }
        }
