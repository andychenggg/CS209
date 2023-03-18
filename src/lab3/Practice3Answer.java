package lab3;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Practice3Answer {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        tab:
        while(true){
            List<Integer> list = new ArrayList<>();
            String s1 = "Please input the function No:\n" +
                "1 - Get even numbers\n" +
                "2 - Get odd numbers\n" +
                "3 - Get prime numbers\n" +
                "4 - Get prime numbers that are bigger than 5\n" +
                "0 - Quit";
            System.out.println(s1);
            int operation = in.nextInt();
            if(operation == 0)
                break tab;
            s1 = "Input size of the list:";
            System.out.println(s1);
            int size = in.nextInt();

            s1 = "Input elements of the list:";
            System.out.println(s1);
            for(int i = 0; i< size; i++){
                list.add(in.nextInt());
            }

            s1 = "Filter results:";
            System.out.println(s1);
            List<Integer> result = null;
            switch (operation){
                case 1: result = list.stream().filter(i1 ->  i1 % 2 == 0).toList(); break;
                case 2: result = list.stream().filter(i1 ->  i1 % 2 != 0).toList(); break;
                case 3: result = list.stream().filter( i1 -> {
                    for(int p = 2; p < i1; p++){
                        if(i1 % p == 0) return false;
                    }
                    return true;
                }).toList(); break;
                case 4: result = list.stream().filter( i1 -> {
                    if(i1 <= 5) return false;
                    for(int p = 2; p < i1; p++){
                        if(i1 % p == 0) return false;
                    }
                    return true;
                }).toList(); break;
            }
            System.out.println(result);
        }

    }

}
