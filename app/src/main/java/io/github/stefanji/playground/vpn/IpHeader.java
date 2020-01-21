package io.github.stefanji.playground.vpn;

import static io.github.stefanji.playground.vpn.ProtocolUtils.parseIpV4;
import static io.github.stefanji.playground.vpn.ProtocolUtils.readInt;
import static io.github.stefanji.playground.vpn.ProtocolUtils.readShort;

/**
 * Create by jy on 2020-01-01
 * <pre>
 * https://tools.ietf.org/html/rfc791#section-3.1
 *
 *     0                   1                   2                   3
 *     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |Version|  IHL  |Type of Service|          Total Length         |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |         Identification        |Flags|      Fragment Offset    |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |  Time to Live |    Protocol   |         Header Checksum       |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                       Source Address                          |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                    Destination Address                        |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                    Options                    |    Padding    |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </pre>
 */
public class IpHeader {
    private byte version;
    private byte ihl;
    private byte typeOfService;
    private short totalLength;
    private short identification;
    private byte flags;
    private short fragmentOffset;
    private byte ttl;
    Protocol protocol;
    short checksum;
    int sourceIp;
    int destIp;

    public IpHeader(byte[] packet) {
        version = (byte) (packet[0] >> 4);
        ihl = (byte) (packet[0] & 0xF);
        typeOfService = packet[1];
        totalLength = readShort(packet, 2);
        identification = readShort(packet, 4);
        flags = (byte) ((packet[6] & 0xF) >> 5);
        fragmentOffset = (short) (((packet[6] & 0x1F) << 8) | (packet[7] & 0xFF));
        ttl = packet[8];
        protocol = Protocol.parse(packet[9]);
        checksum = readShort(packet, 10);
        sourceIp = readInt(packet, 12);
        destIp = readInt(packet, 16);
    }

    public int getHeaderLength() {
        return ihl * 4;
    }

    @Override
    public String toString() {
        return "IpHeader{" +
                "version=" + version +
                ", ihl=" + ihl +
                ", typeOfService=" + typeOfService +
                ", totalLength=" + totalLength +
                ", ide=" + identification +
                ", flags=" + flags +
                ", fragmentOffset=" + fragmentOffset +
                ", ttl=" + ttl +
                ", protocol=" + protocol +
                ", checksum=" + checksum +
                ", sourceIp=" + parseIpV4(sourceIp) +
                ", destIp=" + parseIpV4(destIp) +
                ", length=" + getHeaderLength() +
                '}';
    }
}

