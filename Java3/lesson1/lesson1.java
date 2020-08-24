package Java3.lesson1;




import java.util.ArrayList;
import java.util.Arrays;

public class lesson1 {
    public static void main(String[] args) {

        Integer[] array = new Integer[]{32, 25, 55, 85, 12, 10};

        // 1е задание  меняем местами элементы массива.
        System.out.println("Изначальный массив " + Arrays.toString(array));
        swapArray(array, 0, 5);
        System.out.println("Преобразованный массив " + Arrays.toString(array));

        // 2е задание преобразуем масив в ArrayList
        System.out.println(arrayToArrayList(array));

        //3е задание .
        Apple apple1 = new Apple();
        Apple apple2 = new Apple();
        Apple apple3 = new Apple();

        Orange orange1 = new Orange();
        Orange orange2 = new Orange();
        Orange orange3 = new Orange();
        Orange orange4 = new Orange();

        Box<Apple> box1 = new Box<>(apple1, apple2);
        Box<Orange> box2 = new Box<>(orange1, orange2, orange3);
        box1.add(apple3);
        box2.add(orange4);

        System.out.println(box1.compare(box2));
        if (box1.compare(box2)) {
            System.out.println("Коробки равны по весу");
        }
        System.out.println("Коробки не равны по весу");

        Box<Apple> box3 = new Box<>();
        box1.shift(box3);


    }

    private static void swapArray(Object[] arr, int num1, int num2) {
        Object t = arr[num1];
        arr[num1] = arr[num2];
        arr[num2] = t;
    }

    private static <T> ArrayList<T> arrayToArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

}
