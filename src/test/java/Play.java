import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;

/**
 * @Author: hesen
 * @Date: 2023-06-25-18:40
 * @Description:
 */
public class Play {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(9);
        list.add(10);
        list.add(13);
        list.add(19);
        IntSummaryStatistics intSummaryStatistics = list.stream().mapToInt(x -> x).summaryStatistics();
        double average = intSummaryStatistics.getAverage();
        long count = intSummaryStatistics.getCount();
        int max = intSummaryStatistics.getMax();
        int min = intSummaryStatistics.getMin();
        long sum = intSummaryStatistics.getSum();
        System.out.println(sum);

        List<Person> personList = new ArrayList<>();

        Person person = new Person("hs", 123);



        for (int i = 0; i < 10; i++) {
            personList.add(person);
        }
        personList.add(new Person("zs", 12));

        IntSummaryStatistics intSummaryStatistics1 = personList.stream().mapToInt(p -> p.age).summaryStatistics();

        System.out.println(intSummaryStatistics1.getMin());


        String a = "123";
        String haha = Optional.ofNullable(a).orElse("321");
        System.out.println(haha);
//        Integer sum = list.stream().reduce(0,(x, y) -> x + y);
//        System.out.println(sum);
    }

    static class Person {
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
