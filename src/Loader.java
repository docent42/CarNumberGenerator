import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Loader
{
    static Queue<String> queue = null; // куча для номеров
    private static AtomicInteger counter = new AtomicInteger(0);// счетчик-флаг для остановки Писателя

    public static void main(String[] args)
    {
        //===================== MultiThread Generator ========================================================

        queue = new ConcurrentLinkedQueue<>();// потокобезопасная куча для записи сгенерированных регионов
        int coreCount = Runtime.getRuntime().availableProcessors();
        int regionsAmount = 100;
        long startTime = System.currentTimeMillis();
        String str;
        Path targetPath = Paths.get("res\\num_multi.txt");
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        System.out.println("------------------------- MultiThread generation ---------------------------\n");

        try
        {
            for (int i = 1; i < regionsAmount; i++)
                executorService.submit(new Thread(new RegionCreator(i)));// создаем генераторы регионов

            while (counter.get() < regionsAmount-1 || Loader.queue.size() > 0)
            {
                if ((str = Loader.queue.poll()) != null) // выдираем из кучи данные для записи и пишем их на диск
                {
                    System.out.printf("   <----  Записано < %.1f %% > данных: %d bytes ---->%n",(double) counter.incrementAndGet()/99 * 100,str.length());// олдскульный логгер
                    try {
                        Files.write(targetPath, str.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            executorService.shutdown();
            System.out.println("Writer stopped....");
            System.out.println("------------------------- Process finished ---------------------------");
            System.out.println((System.currentTimeMillis() - startTime) + " ms\n");

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
