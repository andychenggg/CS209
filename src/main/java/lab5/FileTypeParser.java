package lab5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileTypeParser {

    public static void main(String[] args) throws URISyntaxException, IOException {
        String filePath1 = Paths.get(FileTypeParser.class.getClassLoader().getResource("1").toURI()).toString();
        String filePath2 = Paths.get(FileTypeParser.class.getClassLoader().getResource("2").toURI()).toString();
        String filePath3 = Paths.get(FileTypeParser.class.getClassLoader().getResource("3").toURI()).toString();

        byte [][] headers = new byte[3][4];
        FileInputStream f1 = new FileInputStream(filePath1);
        for(int i = 0; i < 4; i++){
            headers[0][i] = (byte)f1.read();
        }
        f1.close();
        FileInputStream f2 = new FileInputStream(filePath2);
        for(int i = 0; i < 4; i++){
            headers[1][i] = (byte)f2.read();
        }
        f2.close();
        FileInputStream f3 = new FileInputStream(filePath3);
        for(int i = 0; i < 4; i++){
            headers[2][i] = (byte)f3.read();
        }
        f3.close();

        for(int j = 0; j< 3; j++){
            switch (args[j]){
                case "1":
                    System.out.println("Filename: 1");
                    System.out.print("File Header(Hex): [");
                    for(int i = 0; i< 4; i++){
                        System.out.printf("%2X", headers[0][i]);
                        if (i != 3)
                            System.out.print(", ");
                        else
                            System.out.println("]");
                    }
                    break;
                case "2":
                    System.out.println("Filename: 2");
                    System.out.print("File Header(Hex): [");
                    for(int i = 0; i< 4; i++){
                        System.out.printf("%2X", headers[1][i]);
                        if (i != 3)
                            System.out.print(", ");
                        else
                            System.out.println("]");
                    }
                    break;
                default:
                    System.out.println("Filename: 3");
                    System.out.print("File Header(Hex): [");
                    for(int i = 0; i< 4; i++){
                        System.out.printf("%2X", headers[2][i]);
                        if (i != 3)
                            System.out.print(", ");
                        else
                            System.out.println("]");
                    }
                    break;
            }
            int type = Integer.parseInt(args[j]) - 1;
            String s = "unknown";
            if(headers[type][0] == (byte)0x89 && headers[type][1] == (byte)0x50 && headers[type][2] == (byte)0x4e && headers[type][3] == (byte)0x47)
                s = "png";
            if(headers[type][0] == (byte)0x50 && headers[type][1] == (byte)0x4b && headers[type][2] == (byte)0x03 && headers[type][3] == (byte)0x04)
                s = "zip or jar";
            if(headers[type][0] == (byte)0xca && headers[type][1] == (byte)0xfe && headers[type][2] == (byte)0xba && headers[type][3] == (byte)0xbe)
                s = "class";
            System.out.println("File Type: " + s);

        }



    }

}
