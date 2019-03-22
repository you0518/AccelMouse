package e.lllll.accelmouse.search

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.koushikdutta.async.AsyncServer
import com.squareup.moshi.Moshi
import e.lllll.accelmouse.R
import kotlinx.android.synthetic.main.connection_setting_list.*
import java.net.InetSocketAddress

class ConnectableManageActivity : Activity() {
    private val tag: String = ConnectableServerManager::class.java.simpleName
    private lateinit var adapter: HostAdapter
    /**
     * ソケットの取得
     */
    private val socket by lazy {
        AsyncServer.getDefault().openDatagram(InetSocketAddress(10000), true)
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
        adapter = HostAdapter(this)
        connectionListView.adapter = adapter
        connectionListView.setOnItemClickListener { parent, view, position, id ->
            Log.d(tag, "position: ${position}")
        }
    }

    /**
     * 接続可能なサーバの調査開始
     */
    private fun searchServer() {
        if (!socket.isOpen) {
            Log.w(tag, "socket was closed")
        }

        socket.setDataCallback { _, bb ->
            val data = bb.allByteArray.toString()
            val host = jsonParser.fromJson(data)
            if (host != null) {
                    adapter.setItem(host)
            }
            Log.d(tag, data)
        }
    }

    private fun setListItem() {

    }

    fun close() {
        socket.disconnect()
        AsyncServer.getDefault().stop()
    }
}