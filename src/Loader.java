import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Loader
{
    static Queue<String> queue = null; // куча для номеров
    static volatile boolean cycle = true;// флаг для остановки Writer на диск

    public static void main(String[] args)
    {
        //===================== MultiThread Generator ========================================================

        queue = new ConcurrentLinkedQueue<>();// потокобезопасная куча для записи сгенерированных регионов
        ArrayList<Thread> pool = new ArrayList<>();// куча для потоков генерации
        long startTime = System.currentTimeMillis();
        System.out.println("------------------------- MultiThread generation ---------------------------\n");

        try
        {
            for (int i = 0; i < 4; i++)
                pool.add(new Thread(new RegionCreator(i, System.currentTimeMillis())));// создаем генераторы регионов

            Thread writer = new Thread(new Writer("res\\num_multi.txt"));
            writer.start();// стартуем Writer номеров на диск

            for (Thread thread : pool)
                thread.start();// стартуем генераторы
            for (Thread thread : pool)
                thread.join();// ставим главный поток на ожидание выполнения параллельных потоков

            cycle = false;// тормозим Writer
            writer.join();// ждем пока запишет все
            System.out.println("------------------------- Process finished ---------------------------");
            System.out.println((System.currentTimeMillis() - startTime) + " ms\n");

        //===================== SingleThread Generator ===============================================

        // То же самое но теперь все потоки друг за другом в режиме однопоточного исполнения

            pool.clear();
            queue.clear();
            startTime = System.currentTimeMillis();
            cycle = true;
            System.out.println("------------------------- SingleThread generation ---------------------------\n");

            for (int i = 0; i < 4; i++)
                pool.add(new Thread(new RegionCreator(i, System.currentTimeMillis())));

            Thread writer1 = new Thread(new Writer("res\\num_single.txt"));
            writer1.start();

            for (Thread thread : pool) {
                thread.start();
                thread.join();
            }
            cycle = false;
            writer.join();
            System.out.println("------------------------- Process finished ---------------------------");
            System.out.println((System.currentTimeMillis() - startTime) + " ms\n");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
