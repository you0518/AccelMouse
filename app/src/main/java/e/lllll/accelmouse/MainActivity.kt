package e.lllll.accelmouse


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import e.lllll.accelmouse.data.Mouse
import android.app.Activity
import android.graphics.Point


class MainActivity : AppCompatActivity() {
    private val mouse = Mouse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val display = getDisplaySize(this)
        mouse.displayWidth = display.x
        mouse.displayHeight = display.y
    }

    /**
     * ディスプレイサイズの取得
     */
    private fun getDisplaySize(activity: Activity): Point {
        val display = activity.windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        return point
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mouse.setClick()
            }
        }
        mouse.touchX = event.x
        mouse.touchY = event.y
        return super.onTouchEvent(event)
    }
}
