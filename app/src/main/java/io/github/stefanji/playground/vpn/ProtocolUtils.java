package io.github.stefanji.playground.vpn;

/**
 * Create by jy on 2020-01-01
 */
public class ProtocolUtils {
    // 我们知道 IPv4 地址用4个字节，32位表示，所以会占 ip header 中4个byte位置，而每个位置就是 c/c++ 中的 unsigned char，但是 Java 中的 byte 是 signed 的，范围就只有 [-128,127] 了，
    // 所以每个位置到 Java 层来表示就有可能超过 127 , 溢出变为负数了, 比如 192 -> -64
    // 于是可以将 signed byte 转为 unsigned int 来解决在 Java 上显示为负数的问题, 于是出现了 (int)packet[offset] & 0xFF :先将 byte 转为 int, 然后去掉高 24 位, 保留低 8 位的操作.
    // 原理:
    // java 中数字都是以补码的形式存储的(其实操作系统计算数字时也都是采用的补码方式)
    // byte -64 的原码: 11000000 (第一位为符号位, 1 表示是负数, 0表示正数)
    // => byte -64 的补码:  原码的除符号位外各位取反 -> 10111111 ,然后最后一位加1 -> 11000000
    // byte -64 转为 int 之后的补码: 11111111 11111111 11111111 11000000 (java 中 byte 数据转化为int数据时会自动补位，如果最高位（符号位）是0，则高24位全部补0，若是1，则高24位全部补1)
    // 然后与 0xFF 做与运算只保留低8位, 就能得到 unsigned int => 00000000 00000000 00000000 11000000
    public static int readInt(byte[] packet, int offset) {
        return (((int) packet[offset] & 0xFF) << 24) | (((int) packet[offset + 1] & 0xFF) << 16) | (((int) packet[offset + 2] & 0xFF) << 8) | ((int) packet[offset + 3] & 0xFF);
    }

    public static short readShort(byte[] packet, int offset) {
        // packet[offset] 会被隐式转型为 int, 同时由于 java 的 byte 为 [-128,127]
        int a = packet[offset] & 0xFF;
        int b = (int) packet[offset + 1] & 0xFF;
        return (short) ((a << 8) | b);
    }

    public static String parseIpV4(int ipV4) {
        return String.format("%s.%s.%s.%s", ipV4 >> 24 & 0xFF, ipV4 >> 16 & 0xFF, ipV4 >> 8 & 0xFF, ipV4 & 0xFF);
    }
}
