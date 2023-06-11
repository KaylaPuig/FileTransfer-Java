package filetransfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver 
{
    private static final int TIMEOUT_MILLIS = 30 * 1000;

    private ServerSocket receiver;

    public FileReceiver(int port) throws IOException
    {
        this.receiver = new ServerSocket(port);
        receiver.setSoTimeout(TIMEOUT_MILLIS);
    }

    public boolean receive(File file) throws IOException
    {
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);

        Socket sender = receiver.accept();
        System.out.println("Connection received on " + sender.getInetAddress().getHostName());

        byte[] buffer = new byte[512];
        InputStream senderInput = sender.getInputStream();
        int rc = senderInput.read(buffer);

        while (rc != -1)
        {
            byte[] adjustedBuffer = buffer;
            if (rc < buffer.length)
            {
                adjustedBuffer = new byte[rc];
                for (int i = 0; i < rc; i++)
                {
                    adjustedBuffer[i] = buffer[i];
                }
            }

            out.write(adjustedBuffer);

            rc = senderInput.read(buffer);
        }

        receiver.close();
        out.close();

        return true;
    }
}
