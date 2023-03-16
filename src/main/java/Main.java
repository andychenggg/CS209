import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
//    public static void main(String[] args) {
//        int x = 10; // final variable
//        final int y = 20; // final variable
//
//        IntUnaryOperator plusX = i -> {i += x; return i + x;}; // OK
//        IntUnaryOperator plusY = i -> i + y; // OK
//
//        //x = 30; // Error: Variable 'x' is accessed from within inner class, needs to be final or effectively final
//        //y = 40; // Error: cannot assign a value to final variable 'y'
//
//    }
public static void main(String[] args) {
    ArrayList<Integer> al = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
    System.out.println(al.stream().reduce(0, Integer::sum));


}
public static void foo(int ... values){}
}
interface A{
    int aMethod(String s);
}
class NatualSupplier implements Supplier<Integer> {
    int n = 0;
    public Integer get() {
        n++;
        return n;
    }
}
