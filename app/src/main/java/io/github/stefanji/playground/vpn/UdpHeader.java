package io.github.stefanji.playground.vpn;

/**
 * Create by jy on 2020-01-01
 * <pre>
 * 0      7 8     15 16    23 24    31
 * +--------+--------+--------+--------+
 * |     Source      |   Destination   |
 * |      Port       |      Port       |
 * +--------+--------+--------+--------+
 * |                 |                 |
 * |     Length      |    Checksum     |
 * +--------+--------+--------+--------+
 * |
 * |          data octets ...
 * +---------------- ...
 *
 *     User Datagram Header Format
 * </pre>
 */
public class UdpHeader {
    short sorcePort;
    short destPort;
    short len;
    short checksum;

    public UdpHeader(final byte[] packet, final int offset) {
        sorcePort = ProtocolUtils.readShort(packet, offset);
        destPort = ProtocolUtils.readShort(packet, offset + 2);
        len = ProtocolUtils.readShort(packet, offset + 4);
        checksum = ProtocolUtils.readShort(packet, offset + 6);
    }

    @Override
    public String toString() {
        return "UdpHeader{" +
                "sorcePort=" + sorcePort +
                ", destPort=" + destPort +
                ", len=" + len +
                ", checksum=" + checksum +
                '}';
    }
}
