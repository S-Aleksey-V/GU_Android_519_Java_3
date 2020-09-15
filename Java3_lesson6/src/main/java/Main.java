import java.util.ArrayList;

public class Main {

    public  int[] arrayAnyOneFour(int[] arr) {
        ArrayList<Integer> arrayList = new ArrayList();
        for (int i : arr) {
            arrayList.add(i);
        }
        //тут проверяем есть ли хотя бы одна 4ка
        if (!arrayList.contains(4))
            throw new RuntimeException();
        ArrayList<Integer> endList = new ArrayList<Integer>();
        for (int i = 0; i < arrayList.size(); i++) {
            endList.add(arrayList.get(i));
            if (arrayList.get(i) == 4) {
                endList.clear();
            }
        }
        //что бы вернуть правильный масив преобразовал вот так .
        int[] intArray = new int[endList.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = endList.get(i);
        }
        return intArray;
    }

    public  boolean oneOrFour(int[] arrIn) {
        boolean one = false;
        boolean four = false;
        for (int i : arrIn) {
            if (i != 1 && i != 4) return false;
            if (i == 1) one = true;
            if (i == 4) four = true;
        }
        return one && four;
    }




}

