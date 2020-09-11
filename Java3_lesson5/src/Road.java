import java.util.concurrent.atomic.AtomicInteger;

public class Road extends Stage {
public static AtomicInteger ai = new AtomicInteger();
    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);
            System.out.println(c.getName() + " закончил этап: " + description);
            ai.getAndIncrement();
            if(ai.get() == (Main.CARS_COUNT*2)-3 ){
                System.out.println(c.getName() + " WINNER");
            }
            Main.finishLine.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}