package e.lllll.accelmouse


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import e.lllll.accelmouse.mouse.MouseManager
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log


class MainActivity : AppCompatActivity(), SensorEventListener {

    private val mouseManager = MouseManager("192.168.2.1", 10000)
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
        mouseManager.displayWidth = display.x
        mouseManager.displayHeight = display.y

        registerSensors()
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

    /**
     * Androidのセンサを利用
     */
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
                mouseManager.setClick()
            }
            MotionEvent.ACTION_UP -> {
                mouseManager.releaseClick()
            }
            MotionEvent.ACTION_MOVE -> {
                mouseManager.setWheelDistance(y)
            }
        }
        mouseManager.setTouchCoordinates(x, y)
        mouseManager.sendData()
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
                mouseManager.setMoveCoordinates(event.values[0], event.values[2])
                mouseManager.sendData()
            }
        }
    }
    /**
     * センサーが変化したときのイベントを処理する。
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}
