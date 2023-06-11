package filetransfer;

import java.util.Arrays;

/*
 * A class with static methods for handling scrambling and descrambling of byte arrays
 * This class is not a valid substitute for genuine encryption methods and should not be treated as such
 * By that same token, neither should this file transfer program be treated as secure
 */
public class Scramble
{
    public static byte[] scramble(byte[] data, long seed)
    {
        byte[] scrambled = Arrays.copyOf(data, data.length);

        for (int i = 0; i < scrambled.length; i++)
        {
            int randIndex = Math.abs(((int) rand(seed, i)) % scrambled.length);

            byte temp = scrambled[i];
            scrambled[i] = scrambled[randIndex];
            scrambled[randIndex] = temp;
        }

        return scrambled;
    }

    public static byte[] unscramble(byte[] data, long seed)
    {
        byte[] unscrambled = Arrays.copyOf(data, data.length);

        for (int i = unscrambled.length - 1; i >= 0; i--)
        {
            int randIndex = Math.abs(((int) rand(seed, i)) % unscrambled.length);

            byte temp = unscrambled[i];
            unscrambled[i] = unscrambled[randIndex];
            unscrambled[randIndex] = temp;
        }

        return unscrambled;
    }

    private static final long MULTIPLIER = 25214903917L;
    private static final long INCREMENT = 11L;
    private static final long MODULUS = (1L << 48);
    private static long rand(long seed, long current)
    {
        return (MULTIPLIER * (seed + current) + INCREMENT) % MODULUS;
    }

    //private static void swap
}
