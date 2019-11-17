import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Writer implements Runnable
{
    private String path;
    Writer (String path)
    {
        this.path = path;
    }

    @Override
    public void run() {
        String str;
        while (Loader.cycle || Loader.queue.size() > 0) // ждем пока данные прилетят в кучу или выход из цикла
        {
            if ((str = Loader.queue.poll()) != null) // выдираем из кучи данные для записи и пишем их на диск
            {
                System.out.println("  Writer received data: " + str.length() + " bytes");// олдскульный логгер
                try {
                    Files.write(Paths.get(path), str.getBytes(),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Writer stopped....");
    }

}
