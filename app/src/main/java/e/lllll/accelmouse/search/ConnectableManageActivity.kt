package e.lllll.accelmouse.search

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.koushikdutta.async.AsyncServer
import com.squareup.moshi.Moshi
import e.lllll.accelmouse.R
import kotlinx.android.synthetic.main.connection_setting_list.*
import java.net.InetSocketAddress
import java.util.*
import kotlin.concurrent.thread

class ConnectableManageActivity : Activity() {
    private val tag: String = ConnectableManageActivity::class.java.simpleName
    /**
     * ListView内のアイテムを管理する
     */
    private lateinit var adapter: HostAdapter
    /**
     * メインスレッド以外でUIを更新するために必要
     */
    private lateinit var handler: Handler
    /**
     * ソケットの取得
     */
    private val socket by lazy {
        AsyncServer.getDefault().openDatagram(InetSocketAddress(10001), true)
    }

    /**
     * JSONパーサの用意
     */
    private val jsonParser by lazy {
        Moshi.Builder().build().adapter(Host::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connection_setting_list)
        handler = Handler()
        adapter = HostAdapter(this)
        connectionListView.adapter = adapter
        connectionListView.setOnItemClickListener { parent, view, position, id ->
            Log.d(tag, "position: ${position}")
        }
    }

    override fun onResume() {
        super.onResume()
        searchServer()
        searchUnconnectableServer()
    }

    /**
     * 接続可能なサーバの調査開始
     */
    private fun searchServer() {
        Log.d(tag, "searchServer")
        if (!socket.isOpen) {
            Log.w(tag, "socket was closed")
        }
        // データを受信した際に本コールバックが呼ばれる(別スレッドです)
        socket.setDataCallback { _, bb ->
            Log.d(tag, "setDataCallback")
            val data = bb.allByteArray
            val host = jsonParser.fromJson(String(data))
            if (host != null) {
                // 別スレッドで、UIを操作する
                handler.post {
                    adapter.setItem(host)
                }
            }
        }
    }

    /**
     * 一定時間受信しなかったサーバ情報の削除を行う
     */
    private fun searchUnconnectableServer() {
        thread {
            while (true) {
                Thread.sleep(10000L)
                handler.post {
                    adapter.deleteItem()
                }
            }
        }
    }

    fun close() {
        socket.disconnect()
        AsyncServer.getDefault().stop()
    }
}