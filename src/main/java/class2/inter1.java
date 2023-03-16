package class2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface inter1 {
    int a = 1;
    private void foo(){
    }
    //void foo1();
}

class A implements inter1{

    public static void main(String[] args) {
        Comparable<A> c= new Comparable<A>() {
            @Override
            public int compareTo(A o) {
                return 0;
            }
        };
    }
}
class B{

}
