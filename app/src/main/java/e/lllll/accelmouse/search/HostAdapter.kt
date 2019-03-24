package e.lllll.accelmouse.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import e.lllll.accelmouse.R


class HostAdapter(private val context: Context) : BaseAdapter() {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
    private val hostList = mutableListOf<Host>()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val convertView = view ?: layoutInflater.inflate(R.layout.connection_setting_list_item, parent, false)

        (convertView.findViewById(R.id.host) as TextView).text = hostList[position].host
        (convertView.findViewById(R.id.host) as TextView).text = hostList[position].host
        return convertView
    }

    override fun getItem(position: Int): Any {
        return hostList[position]
    }

    override fun getItemId(position: Int): Long {
        return hostList[position].id
    }

    override fun getCount(): Int {
        return hostList.size
    }

    fun setItem(host: Host) {
        hostList.add(host)
        notifyDataSetChanged()
    }

}