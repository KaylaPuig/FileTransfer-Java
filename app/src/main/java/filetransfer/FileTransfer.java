package filetransfer;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class FileTransfer {
    private enum TransferType {
        SEND, 
        RECEIVE
    }

    private static TransferType type;
    private static int port;
    private static String address;
    private static String fileName;

    public static void main(String[] args) throws UnknownHostException, IOException {
        parseArgs(args);

        switch(type)
        {
            case SEND:
            FileSender sender = new FileSender(address, port);
            sender.sendFile(new File(fileName));
            break;

            case RECEIVE:
            FileReceiver receiver = new FileReceiver(port);
            receiver.receive(new File(fileName));
            break;

            default:
            System.err.println("Send or receive not specified");
            System.exit(2);
        }

        System.out.println("Program terminated with no error. Ensure that the file sent/received is authentic.");
    }

    private static void parseArgs(String[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            String arg = args[i];
            if (isPort(arg))
            {
                port = Integer.parseInt(args[++i]);
            }
            else if (isAddress(arg))
            {
                address = args[++i];
            }
            else if (isFileName(arg))
            {
                fileName = args[++i];
            }
            else if (isReceiver(arg))
            {
                type = TransferType.RECEIVE;
            }
            else if (isSender(arg))
            {
                type = TransferType.SEND;
            }
            else
            {
                System.err.println("Unexpected argument " + arg);
                System.exit(1);
            }
        }
    }

    private static boolean isPort(String s)
    {
        return s.equals("--port") || s.equals("-p");
    }

    private static boolean isAddress(String s)
    {
        return s.equals("--address") || s.equals ("-a");
    }

    private static boolean isFileName(String s)
    {
        return s.equals("--filename") || s.equals("-f");
    }

    private static boolean isReceiver(String s)
    {
        return s.equals("receive");
    }

    private static boolean isSender(String s)
    {
        return s.equals("send");
    }
}
