package io.github.stefanji.playground.vpn;

/**
 * Create by jy on 2020-01-01
 */
public enum Protocol {
    TCP(6),
    UDP(17),
    ICMP(1),
    UNKOWN(-1);

    final int number;

    Protocol(int number) {
        this.number = number;
    }

    static Protocol parse(byte number) {
        if (number == Protocol.TCP.number) {
            return Protocol.TCP;
        }
        if (number == Protocol.UDP.number) {
            return Protocol.UDP;
        }
        if (number == Protocol.ICMP.number) {
            return Protocol.ICMP;
        }
        return Protocol.UNKOWN;
    }
}

