package Lab6;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class practice1 {
    public static void main(String[] args) throws IOException {
        readZip();
        readJar();

    }
    public static void readZip() throws IOException {
        ZipInputStream zFile = new ZipInputStream(new BufferedInputStream(new FileInputStream("src.zip")));
        ZipEntry entry;
        int count = 0;
        while((entry = zFile.getNextEntry()) != null){
            if(entry.getName().matches("java/n?io/[^ ]+.java")){
                count++;
                System.out.println(entry.getName());
            }
        }
        System.out.println(count);
    }
    public static void readJar() throws IOException {
        JarFile jf = new JarFile("rt.jar");
        Stream<JarEntry> str = Collections.list(jf.entries()).stream();
        List<JarEntry> lj = str.filter(e -> e.getName().matches("java/n?io/[^ ]+.class")).toList();
        System.out.println(lj.size());
        lj.forEach(System.out::println);
    }
}
