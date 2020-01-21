package io.github.stefanji.playground.vpn;

/**
 * Create by jy on 2020-01-01
 * <pre>
 *    0                   1                   2                   3
 *    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |          Source Port          |       Destination Port        |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                        Sequence Number                        |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                    Acknowledgment Number                      |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |  Data |           |U|A|P|R|S|F|                               |
 *    | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
 *    |       |           |G|K|H|T|N|N|                               |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |           Checksum            |         Urgent Pointer        |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                    Options                    |    Padding    |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *    |                             data                              |
 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </pre>
 */
public class TcpHeader {
    short sourcePort;
    short destPort;
    int seqNumber;
    int ackNumber;
    byte dataOffset;
    byte reserved;
    /* -----flags----- */
    byte ns;
    byte cwr;
    byte ece;
    byte urg;
    byte ack;
    byte psh;
    byte rst;
    byte syn;
    byte fin;
    /* -----end flags---- */
    short windowSize;
    short checksum;
    short urgentPointer;

    public TcpHeader(final byte[] packet, final int offset) {
        sourcePort = ProtocolUtils.readShort(packet, offset);
        destPort = ProtocolUtils.readShort(packet, offset + 2);
        seqNumber = ProtocolUtils.readInt(packet, offset + 4);
        ackNumber = ProtocolUtils.readInt(packet, offset + 8);
        dataOffset = (byte) ((packet[offset + 12] >> 4) & 0xF);
        reserved = (byte) ((packet[offset + 12] >> 1) & 0b0111);

        ns = (byte) (packet[offset + 12] & 0x1);
        cwr = (byte) ((packet[13] >> 7) & 0xF);
        ece = (byte) ((packet[13] >> 6) & 0xF);
        urg = (byte) ((packet[13] >> 5) & 0xF);
        ack = (byte) ((packet[13] >> 4) & 0xF);
        psh = (byte) ((packet[13] >> 3) & 0xF);
        rst = (byte) ((packet[13] >> 2) & 0xF);
        syn = (byte) ((packet[13] >> 1) & 0xF);
        fin = (byte) ((packet[13]) & 0xF);

        windowSize = ProtocolUtils.readShort(packet, offset + 14);
        checksum = ProtocolUtils.readShort(packet, offset + 16);
        urgentPointer = ProtocolUtils.readShort(packet, offset + 18);
    }

    @Override
    public String toString() {
        return "TcpHeader{" +
                "sourcePort=" + (sourcePort & 0xFFFF) +
                ", destPort=" + (destPort & 0xFFFF) +
                ", seqNumber=" + seqNumber +
                ", ackNumber=" + ackNumber +
                ", dataOffset=" + dataOffset +
                ", reserved=" + reserved +
                ", ns=" + ns +
                ", cwr=" + cwr +
                ", ece=" + ece +
                ", urg=" + urg +
                ", ack=" + ack +
                ", psh=" + psh +
                ", rst=" + rst +
                ", syn=" + syn +
                ", fin=" + fin +
                ", windowSize=" + (windowSize & 0xFFFF) +
                ", checksum=" + (checksum & 0xFFFF) +
                ", urgentPointer=" + (urgentPointer & 0xFFFF) +
                '}';
    }
}
