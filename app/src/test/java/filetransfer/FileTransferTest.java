package filetransfer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileTransferTest 
{
    private class UniqueFileMaker
    {
        File file;

        public UniqueFileMaker()
        {
            String name = "";
            for (int i = 0; i < 10; i++)
            {
                name += 'a' + (char) (Math.random() * 26);
            }

            int suffix = 1;
            file = new File(name);
            while (file.exists())
            {
                file = new File(name + (suffix++));
            }
        }

        public File getFile()
        {
            return file;
        }

        public void createFile(String text) throws IOException
        {
            file.createNewFile();
            Files.write(file.toPath(), text.getBytes());
        }

        public void deleteFile()
        {
            file.delete();
        }
    }

    private static int port = 15808;
    private static String password = "PineappleBananaSocks42";

    @Test void fileTransferWorks() throws IOException
    {
        UniqueFileMaker fileR = new UniqueFileMaker();
        UniqueFileMaker fileS = new UniqueFileMaker();

        fileR.createFile("");
        fileS.createFile("Hello, world!");

        FileReceiver receiver = new FileReceiver(port, password);
        FileSender sender = new FileSender("localhost", port, password);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> receiver.receive(fileR.getFile()));
        executor.submit(() -> sender.send(fileS.getFile()));

        executor.shutdown();
        while (!executor.isTerminated())
        {
            try 
            {
                Thread.sleep(1);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }

        byte[] received = Files.readAllBytes(fileR.getFile().toPath());
        byte[] sent = Files.readAllBytes(fileS.getFile().toPath());

        fileR.deleteFile();
        fileS.deleteFile();

        assertArrayEquals(received, sent);
    }
}
