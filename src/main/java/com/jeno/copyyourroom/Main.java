package com.jeno.copyyourroom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        try {
            copyDirectoryWithZeroLengthFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDirectoryWithZeroLengthFiles() throws IOException {
        File source = new File("/E:/Download/");
        Path target = Files.createTempDirectory("COPY_YOUR_ROOM_");

        FileUtils.copyDirectory(source, target.toFile(), DirectoryFileFilter.DIRECTORY);

        Stream<Path> fileTreeStream = Files.walk(Paths.get(source.toURI()));
        fileTreeStream
                .map(Path::toFile)
                .filter(File::isFile)
                .forEach(f -> {
                    File fileToCreate = new File(target.toString() + f.getParentFile().getAbsolutePath().replace(source.getAbsolutePath(), "") + "/" + f.getName());
                    if (!fileToCreate.exists()) {
                        try {
                            fileToCreate.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        System.err.println(target);
//        FileUtils.listFilesAndDirs(file, FileFilterUtils.trueFileFilter(), FileFilterUtils.directoryFileFilter())
//                .forEach(f -> System.err.println(f.getName() + " " + f.getAbsolutePath()));
    }
}
