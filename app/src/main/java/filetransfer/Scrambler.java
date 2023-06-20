package filetransfer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/*
 * A class with static methods for handling scrambling and descrambling of byte arrays
 * This class is not a valid substitute for genuine encryption methods and should not be treated as such
 * By that same token, neither should this file transfer program be treated as secure
 */
public class Scrambler
{
    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long INCREMENT = 0xBL;
    private static final long MODULUS = (1L << 48);

    private long seed;

    public Scrambler(long seed)
    {
        this.seed = seed;
    }

    public byte[] scramble(byte[] data)
    {
        byte[] scrambled = Arrays.copyOf(data, data.length);

        for (int i = 0; i < scrambled.length; i++)
        {
            int randIndex = Math.abs(nextInt()) % scrambled.length;

            byte temp = scrambled[i];
            scrambled[i] = scrambled[randIndex];
            scrambled[randIndex] = temp;
        }

        return scrambled;
    }

    public byte[] unscramble(byte[] data)
    {
        int[] indices = new int[data.length];
        for (int i = 0; i < indices.length; i++)
        {
            indices[i] = Math.abs(nextInt()) % indices.length;
        }

        byte[] unscrambled = Arrays.copyOf(data, data.length);

        for (int i = unscrambled.length - 1; i >= 0; i--)
        {
            int randIndex = indices[i];

            byte temp = unscrambled[i];
            unscrambled[i] = unscrambled[randIndex];
            unscrambled[randIndex] = temp;
        }

        return unscrambled;
    }

    public int nextInt()
    {
        return (int) nextLong();
    }

    public long nextLong()
    {
        seed = (seed * MULTIPLIER + INCREMENT) & (MODULUS - 1);
        return seed;
    }

    /*
     * FNV-1 Hashing function, based on Wikipedia's description of the algorithm
     */
    private static final long FNV_OFFSET = 0xcbf29ce484222325L;
    private static final long FNV_PRIME = 0x100000001b3L;
    public static long seedFromString(String str)
    {
        long hash = FNV_OFFSET;

        for (byte b : str.getBytes(StandardCharsets.UTF_8))
        {
            hash *= FNV_PRIME;
            hash ^= b;
        }

        return hash;
    }
}
