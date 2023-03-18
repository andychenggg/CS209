package class2;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static <T> void get(List<? super T> Ae){

    }
    public static void main(String[] args) {
        ArrayList<A1> al = new ArrayList<>();
        get(al);
    }
}
class A1{
    int a;
}
class B1 extends A1{
    int b;
}
class C1 extends B1{

}