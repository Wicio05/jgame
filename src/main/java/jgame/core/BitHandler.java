package jgame.core;

public class BitHandler 
{
    private BitHandler()
    {

    }

    public static int getBit(int value, int bit)
    {
        if(bit >= 32)
        {
            throw new IndexOutOfBoundsException("Failed to set bit at " + bit);
        }

        return value & (1 << bit);
    }

    public static int setBit(int value, int bit)
    {
        if(bit >= 32)
        {
            throw new IndexOutOfBoundsException("Failed to set bit at " + bit);
        }

        return value | (1 << bit);
    }

    public static int removeBit(int value, int bit)
    {
        if(bit >= 32)
        {
            throw new IndexOutOfBoundsException("Failed to remove bit at " + bit);
        }

        return value & ~(1 << bit);
    }
}
