package io.github.stefanji.playground.vpn

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.FrameLayout
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.ServerSocket
import java.nio.ByteBuffer

/**
 * Create by jy on 2019-12-31
 */

private const val TAG = "Vpn"
private const val PORT = 10170
private const val HOST = "192.168.31.77"

class VpnActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val f = FrameLayout(this)
        setContentView(f, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        f.addView(Button(this).apply {
            text = "Start"
            setOnClickListener {
                start()
            }
        })
    }

    private fun start() {
        val prepareIntent = VpnService.prepare(this)
        if (prepareIntent != null) {
            startActivityForResult(prepareIntent, 10)
        } else {
            onActivityResult(10, RESULT_OK, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == 10) {
            startService(Intent(this, VpnS::class.java))
        }
    }
}

class VpnS : VpnService() {

    companion object {
        private const val CHANNEL_ID = "JYVP"
    }

    private var thread: Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // transform service to foreground
        val ntfMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT
                )
            ntfMgr.createNotificationChannel(channel)
            startForeground(1, Notification.Builder(this, CHANNEL_ID).build())
        }

        // start new thread to hook
        thread?.interrupt()
        thread = Thread(TunnelConnector(this), "TunnelConnector")
        thread?.start()

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        thread?.interrupt()
    }
}

/*
* Copy from https://segmentfault.com/a/1190000009249039
+----------------------------------------------------------------+
|                                                                |
|  +--------------------+      +--------------------+            |
|  | User Application A |      | User Application B |<-----+     |
|  +--------------------+      +--------------------+      |     |
|               | 1                    | 5                 |     |
|...............|......................|...................|.....|
|               ↓                      ↓                   |     |
|         +----------+           +----------+              |     |
|         | socket A |           | socket B |              |     |
|         +----------+           +----------+              |     |
|                 | 2               | 6                    |     |
|.................|.................|......................|.....|
|                 ↓                 ↓                      |     |
|             +------------------------+                 4 |     |
|             | Network Protocol Stack |                   |     |
|             +------------------------+                   |     |
|                | 7                 | 3                   |     |
|................|...................|.....................|.....|
|                ↓                   ↓                     |     |
|        +----------------+    +----------------+          |     |
|        |      eth0      |    |      tun0      |          |     |
|        +----------------+    +----------------+          |     |
|    10.32.0.11  |                   |   192.168.0.1       |     |
|                | 8                 +---------------------+     |
|                |                                               |
+----------------|-----------------------------------------------+
                 ↓
         Physical Network
 * App 们发出的包经过操作系统的网络协议栈处理之后会被传到 tun 设备驱动
 * 我们实现的 VpnService 则会从 tun 设备中读取到
 */
class TunnelConnector(private val service: VpnService) : Runnable {
    override fun run() {
        try {
            val localTunnel = service.Builder()
                // set virtual source ip
                .addAddress("192.168.0.1", 24)
                // 0.0.0.0 means: all packets will forward to this tun interface
                .addRoute("0.0.0.0", 0)
                .setSession("JYVpn")
                .establish()

            val ios = FileInputStream(localTunnel.fileDescriptor)
            val ops = FileOutputStream(localTunnel.fileDescriptor)

            val packet = ByteBuffer.allocate(4096)

            while (!Thread.currentThread().isInterrupted) {
                // read apps sent packet
                var len = ios.read(packet.array())
                if (len > 0) {
                    val bytes = packet.array()
                    val ihd = IpHeader(bytes)
                    Log.d(TAG, "Ip header: $ihd")
                    if (ihd.protocol == Protocol.TCP) {
                        val tcpHeader = TcpHeader(bytes, ihd.headerLength)
                        Log.d(TAG, "Tcp header: $tcpHeader")
                    }
                    if (ihd.protocol == Protocol.UDP) {
                        val udp = UdpHeader(bytes, ihd.headerLength);
                        Log.d(TAG, "Udp header: $udp")
                    }
                    // TODO 将包通过我们模拟的 VpnGateway 收发, 拿到结果, 然后写入到 TUN Interface 中
                }

                // write processed packet to apps
                if (len > 0) {
                    ops.write(packet.array(), 0, len)
                    packet.clear()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "$e")
            e.printStackTrace()
        }
    }
}

class MockVpnGatewayServer : Runnable {

    private var thread: Thread? = null

    fun start() {
        thread?.interrupt()
        thread = Thread(this, "mock server")
        thread?.start()
    }

    override fun run() {
        val server = ServerSocket(PORT)
        Log.d(TAG, "server bound: ${server.isBound}")
        Log.d(TAG, "${server.inetAddress}")

        while (true) {
            server.accept().let { socket ->
                //TODO 暂时每次开一个新的线程来 handle
                Log.d(TAG, "accept new connect")
                object : Thread() {
                    override fun run() {
                        val input = socket.inputStream
                        val out = socket.outputStream

                        val packet = ByteBuffer.allocate(4096)
                        val len = input.read(packet.array())
                        Log.d(TAG, "read $len bytes")
                        if (len > 0) {
                            out.write(packet.array())
                        }
                    }
                }.start()
            }
        }
    }
}