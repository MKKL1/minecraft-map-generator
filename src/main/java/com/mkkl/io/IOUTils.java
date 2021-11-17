package com.mkkl.io;

public class IOUTils {

    public static byte[] toByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static byte[] toByteArray3(int value) {
        return new byte[] {
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
}
