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
    private val tag: String = ConnectableManageActivity::class.java.simpleName
    private lateinit var adapter: HostAdapter
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
        Log.d(tag, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connection_setting_list)
        adapter = HostAdapter(this)
        connectionListView.adapter = adapter
        connectionListView.setOnItemClickListener { parent, view, position, id ->
            Log.d(tag, "position: ${position}")
        }
    }

    override fun onResume() {
        super.onResume()
        searchServer()
    }

    /**
     * 接続可能なサーバの調査開始
     */
    private fun searchServer() {
        Log.d(tag, "searchServer")
        if (!socket.isOpen) {
            Log.w(tag, "socket was closed")
        }
        socket.setDataCallback { emitter, bb ->
            Log.d(tag, "setDataCallback")
            val data = bb.allByteArray
            val host = jsonParser.fromJson(String(data))
            Log.d(tag, host.toString())
        }
    }

    private fun setListItem() {

    }

    fun close() {
        socket.disconnect()
        AsyncServer.getDefault().stop()
    }
}