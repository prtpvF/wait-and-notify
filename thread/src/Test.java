import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;


public class Test {
    public static void main(String[] args) throws InterruptedException {
        waitAndNotify waitAndNotify = new waitAndNotify();


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    waitAndNotify.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    waitAndNotify.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

    }
}
class waitAndNotify{
    private Object lock = new Object();
    public void produce() throws InterruptedException {
        synchronized (lock){
            System.out.println("producer thread started...");
            lock.wait(); /* вызов метода привязывается к объекту явно (по умолчанию this)
                    * методы wait и notify вызываются только в синхронизированных блоках
                    * при вызове wait происходит 1-отдаем монитор на котором шла синхронизация(другие потоки могут забрать монитор)
                    * 2-ждем, пока будет вызван notify именно на этом объекте

                    */
            System.out.println("producer thread resumed...");
        }
    }
    public void consume() throws InterruptedException{
        Thread.sleep(1000);
        Scanner scanner = new Scanner(System.in);
            synchronized (lock){
                System.out.println("waiting for key pressed");
                scanner.nextLine();
                lock.notify(); // будит  поток который ждет (вызвал метод wait())
            }             // монитор возвращается только после того, как весь код в потоке завершился
    }


}

