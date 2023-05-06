package filetransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileSender 
{
    private Socket receiver;

    public FileSender(String address, int port) throws UnknownHostException, IOException
    {
        this.receiver = new Socket(address, port);
    }

    public boolean sendFile(File file) throws IOException
    {
        if (!file.exists() || !file.isFile())
            return false;
        
        byte[] buffer = new byte[512];
        FileInputStream in = new FileInputStream(file);
        int rc = in.read(buffer);

        OutputStream receiverStream = receiver.getOutputStream();

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

            receiverStream.write(adjustedBuffer);

            rc = in.read(buffer);
        }

        receiver.close();
        in.close();

        return true;
    }
}
