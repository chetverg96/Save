import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        // cоздаю три экземпляра класса
        GameProgress gameProgress1 = new GameProgress(54, 52, 63, 23);
        GameProgress gameProgress2 = new GameProgress(45, 55, 78, 45);
        GameProgress gameProgress3 = new GameProgress(123, 45, 62, 77);

        String saveGamesDirPath = "C:/Games/savegames"; // Полный путь к директории с сохранениями
        String zipFilePath = "C:/Games/savegames/savegames.zip"; // Полный путь к файлу архива

        // Сохраняю сериализованные объекты GameProgress в папку savegames
        saveGame(saveGamesDirPath + "/gameProgress1.dat", gameProgress1);
        saveGame(saveGamesDirPath + "/gameProgress2.dat", gameProgress2);
        saveGame(saveGamesDirPath + "/gameProgress3.dat", gameProgress3);

        // создаю список файлов сохранений, которые необходимо упаковать в архив
        List<String> filesToZip = new ArrayList<>();
        filesToZip.add(saveGamesDirPath + "/gameProgress1.dat");
        filesToZip.add(saveGamesDirPath + "/gameProgress2.dat");
        filesToZip.add(saveGamesDirPath + "/gameProgress3.dat");

        // запаковал файлы сохранений в архив
        zipFiles(zipFilePath, filesToZip);

        // удалил файлы сохранений, которые не лежат в архиве
        deleteFiles(saveGamesDirPath, filesToZip);
    }

    public static void saveGame(String filePath, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            System.out.println("Игра сохранена в: " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении игры: " + e.getMessage());
        }
    }

    public static void zipFiles(String zipFilePath, List<String> filesToZip) {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String file : filesToZip) {
                File currentFile = new File(file);
                FileInputStream fis = new FileInputStream(currentFile);

                // создал новый элемент архива
                ZipEntry entry = new ZipEntry(currentFile.getName());
                zos.putNextEntry(entry);

                // считываю и записываю файловый поток
                byte[] buffer = new byte[1500];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            System.out.println("Файл в архив записан " + zipFilePath);
        } catch (IOException e) {
            System.out.println("Ошибка при записи файлов " + e.getMessage());
        }
    }

    public static void deleteFiles(String dirPath, List<String> filesToKeep) {
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (!filesToKeep.contains(file.getAbsolutePath())) {
                    if (file.delete()) {
                        System.out.println("Удаление файла: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Ошибка при удалении файла: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
