package filetransfer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class FileTransfer {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    private enum TransferType 
    {
        SEND, 
        RECEIVE
    }

    private static TransferType type;
    private static int port;
    private static String address;
    private static String fileName;
    private static String password;

    public static void main(String[] args) throws UnknownHostException, IOException, ArgumentParsingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        parseArgs(args);

        runTransfer();

        System.out.println("Program terminated with no error. Ensure that the file sent/received is authentic.");
    }

    public static byte[] encrypt(byte[] bytes, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        SecretKeySpec key = new SecretKeySpec(hashedPassword(password), 0, KEY_SIZE/8, ALGORITHM);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(bytes);
    }

    public static byte[] decrypt(byte[] bytes, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        SecretKeySpec key = new SecretKeySpec(hashedPassword(password), 0, KEY_SIZE/8, ALGORITHM);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(bytes);
    }

    private static byte[] hashedPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        byte[] result = Arrays.copyOf(hash, KEY_SIZE / 8);
        return result;
    }

    private static void parseArgs(String[] args) throws ArgumentParsingException
    {
        // Default password value
        password = "default";

        for (int i = 0; i < args.length; i++)
        {
            String arg = args[i];
            if (isHelp(arg))
            {
                printHelp();
                System.exit(0);
            }
            else if (isAddress(arg))
            {
                String [] addressComponents = args[++i].split(":");
                
                if (addressComponents.length == 1)
                {
                    port = Integer.parseInt(addressComponents[0]);
                }
                else if (addressComponents.length == 2)
                {
                    address = addressComponents[0];
                    port = Integer.parseInt(addressComponents[1]);
                }
                else
                {
                    throw new ArgumentParsingException("Improper address argument " + arg + ", must be of the form ADDRESS:PORTNUM or PORTNUM");
                }
            }
            else if (isFileName(arg))
            {
                fileName = args[++i];
            }
            else if (isPassword(arg))
            {
                password = args[++i];
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
                throw new ArgumentParsingException("Unexpected argument " + arg + " at location " + i);
            }
        }
    }

    private static void runTransfer() throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        switch(type)
        {
            case SEND:
            FileSender sender = new FileSender(address, port, password);
            sender.sendFile(new File(fileName));
            break;

            case RECEIVE:
            FileReceiver receiver = new FileReceiver(port, password);
            receiver.receive(new File(fileName));
            break;

            default:
            System.err.println("Send or receive not specified");
            System.exit(2);
        }
    }

    private static void printHelp()
    {
        System.out.println("Usage: filetransfer [options] [arguments]");
        System.out.println("Options:");
        System.out.println("    -h, --help                          Display this help message");
        System.out.println("    -a, --address [address:port|port]   Specify address:port combination, or port only for sending");
        System.out.println("    -f, --filename                      Specify sent/received file name");
        System.out.println("    -p, --password                      Set the password for encryption/decryption");
        System.out.println("Arguments:");
        System.out.println("    [send|receive]                      Run the program in send or receive mode for the given options");
    }

    private static boolean isHelp(String s)
    {
        return s.equals("--help") || s.equals("-h");
    }

    private static boolean isAddress(String s)
    {
        return s.equals("--address") || s.equals ("-a");
    }

    private static boolean isFileName(String s)
    {
        return s.equals("--filename") || s.equals("-f");
    }

    private static boolean isPassword(String s)
    {
        return s.equals("--password") || s.equals("-p");
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
