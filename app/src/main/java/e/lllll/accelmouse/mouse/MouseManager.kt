package e.lllll.accelmouse.mouse

import android.util.Log
import com.koushikdutta.async.AsyncServer
import com.squareup.moshi.Moshi
import java.nio.ByteBuffer


class MouseManager(
    /**
     * 送信先ホストとポート番号
     */
    private val host: String,
    private val port: Int
) {
    private val mouse = Mouse()
    /**
     * クリックポジション
     */
    private val noClick: Int = 0
    private val leftClick: Int = 1
    private val rightClick: Int = 2
    private val middleClick: Int = 3

    /**
     * ディスプレイタッチ座標
     */
    var touchX: Float = 0F
    var touchY: Float = 0F
    /**
     * 端末の画面サイズ
     */
    var displayWidth: Int = 0
    var displayHeight: Int = 0
    /**
     * UDPソケットの取得
     */
    private val socket by lazy {
        AsyncServer.getDefault().openDatagram()
    }

    /**
     * JSONパーサ
     */
    private val jsonParser by lazy {
        Moshi.Builder().build().adapter(Mouse::class.java)
    }

    /**
     * タッチ座標を設定する
     */
    fun setTouchCoordinates(x: Float, y: Float) {
        touchX = x
        touchY = y
    }

    fun setMoveCoordinates(x: Float, y: Float) {
        mouse.x = x
        mouse.y = y
    }

    /**
     * タッチ座標に応じてクリック値を設定する
     */
    fun setClick() {
        if (isOnLeftClickArea()) {
            mouse.clicked = leftClick
        }
        if (isOnRightClickArea()) {
            mouse.clicked = rightClick
        }
    }

    /**
     * クリック状態を解除する
     */
    fun releaseClick() {
        mouse.clicked = noClick
        mouse.wheel = 0F
    }

    /**
     * 現在と前回のタッチ座標を用いて、
     * マウスホイール距離を設定する
     */
    fun setWheelDistance(lastTouchY: Float) {
        if (touchX > displayWidth * 4 / 9 && touchX < displayWidth * 5 / 9) {
            mouse.wheel = touchY - lastTouchY
            return
        }
        mouse.wheel = 0F
    }

    /**
     * 左クリック領域をタッチしているかを判定する
     */
    private fun isOnLeftClickArea(): Boolean {
        return touchX < displayWidth * 4 / 9
    }

    /**
     * 右クリック領域をタッチしているかを判定する
     */
    private fun isOnRightClickArea(): Boolean {
        return touchX > displayWidth * 5 / 9
    }

    /**
     * 指定したホストとポートへマウス値を送信する

     */
    fun sendData() {
        if (socket.isOpen) {
            Log.d("sendData", "socket was closed")
            return
        }
        val json = jsonParser.toJson(mouse).toByteArray()
        val buffer = ByteBuffer.wrap(json)
        socket.send(host, port, buffer)
    }

    fun close() {
        socket.disconnect()
        AsyncServer.getDefault().stop()
    }
}