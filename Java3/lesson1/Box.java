package Java3.lesson1;

import java.util.ArrayList;
import java.util.Arrays;

public class Box<T extends Fruit> {

    private ArrayList<T> fruits;

    public Box(T... fruits) {
        this.fruits = new ArrayList<>(Arrays.asList(fruits));
    }

    public void add(T... fruits) {
        this.fruits.addAll(Arrays.asList(fruits));
    }

    // удалить всё из корзины
    private void clear() {
        fruits.clear();
    }

    // метот который выщитывает вес коробки , зная количество фруктов и вес одного фрукта
    private float getWeight() {
        if (fruits.size() == 0) return 0;
        float weight = 0;
        for (T fruit : fruits) {
            weight = weight + fruit.getWeight();
        }
        return weight;
    }

    // метод, который позваляет сравнить текущую коробку с той , которую подадут в compare в качестве параметра
    boolean compare(Box box) {
        return Math.abs(this.getWeight() - box.getWeight()) < 0.000001;
    }

    // метод , который позволяет пересыпать фрукты из текущей коробки в другую
    void shift(Box<? super T> box) {
        box.fruits.addAll(this.fruits);
        clear(); // удаляем всё из коробки , из которой пересыпали
    }
}
