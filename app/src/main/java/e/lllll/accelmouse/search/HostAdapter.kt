package e.lllll.accelmouse.search

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import e.lllll.accelmouse.R
import java.text.SimpleDateFormat
import java.util.*


class HostAdapter(context: Context) : BaseAdapter() {
    private val tag: String = HostAdapter::class.java.simpleName
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    /**
     * サーバの接続情報を格納する。
     */
    private var hostDateList = mutableListOf<HostDate>()
    private val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val convertView = view ?: layoutInflater.inflate(R.layout.connection_setting_list_item, parent, false)

        (convertView.findViewById(R.id.host) as TextView).text = hostDateList[position].host.host
        (convertView.findViewById(R.id.pcName) as TextView).text = hostDateList[position].host.pcName
        (convertView.findViewById(R.id.addTime) as TextView).text = formatter.format(hostDateList[position].addedDate)

        return convertView
    }

    override fun getItem(position: Int): Any {
        return hostDateList[position]
    }

    override fun getItemId(position: Int): Long {
        return hostDateList[position].id
    }

    override fun getCount(): Int {
        return hostDateList.size
    }

    /**
     * 引数のアイテムをListViewに追加する
     */
    fun setItem(host: Host) {
        // すでに追加されているアイテムかどうかをチェックする
        val index = hostDateList.indexOfFirst {
            TextUtils.equals(it.host.host, host.host)
        }
        val hostDate = HostDate(host = host, addedDate = Date())
        if (index >= 0) {
            hostDateList[index] = hostDate
            Log.d(tag, "${host} has already been added.")
        } else {
            hostDateList.add(hostDate)
        }
        notifyDataSetChanged()
    }

    /**
     * 一定時刻以上更新が無いアイテムを削除する
     */
    fun deleteItem() {
        Log.d(tag, "delete Item")
        // アイテムが一つも無ければ削除する必要は内
        if (hostDateList.size == 0) {
            Log.d(tag, "no host")
            return
        }
        val now = Date()
        // 追加時刻が10秒以内のアイテムリストを抽出する
        hostDateList = hostDateList.filter {
            (now.time - it.addedDate.time) / 1000L < 10
        }.toMutableList()
        notifyDataSetChanged()
    }

}