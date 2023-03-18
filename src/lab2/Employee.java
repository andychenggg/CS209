package lab2;

public class Employee {
    private Long id;
    private String name;
    public Employee(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
