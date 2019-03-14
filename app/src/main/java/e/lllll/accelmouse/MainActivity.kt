package e.lllll.accelmouse


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import e.lllll.accelmouse.data.Mouse
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log


class MainActivity : AppCompatActivity(), SensorEventListener {

    private val mouse = Mouse()

    /**
     * Androidセンサを使用するために必要。
     * 本プロパティにアクセスされたときに初めて処理が実行される
     */
    private val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val gyroSensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

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

    private fun registerSensors() {
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    /**
     * タッチイベントを処理する
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            Log.d("onTouchEvent", "event is null")
            return super.onTouchEvent(event)
        }
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mouse.setClick()
            }
            MotionEvent.ACTION_UP -> {
                mouse.releaseClick()
            }
            MotionEvent.ACTION_MOVE -> {
                mouse.setWheelDistance(y)
            }
        }
        mouse.touchX = x
        mouse.touchY = y
        return super.onTouchEvent(event)
    }

    /**
     * センサーのイベントを処理する
     */
    override fun onSensorChanged(event: SensorEvent?) {

        if (event == null) {
            Log.d("onSensorChanged", "sensor is null")
            return
        }
        when (event.sensor.type) {
            Sensor.TYPE_GYROSCOPE -> {
                // 画面のX軸はジャイロスコープのX軸回転を利用
                // 画面のY軸はジャイロスコープのZ軸回転を利用
                mouse.x = event.values[0]
                mouse.y = event.values[2]
            }
        }
    }
    /**
     * センサーが変化したときのイベントを処理する。
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}
