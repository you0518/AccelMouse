package e.lllll.accelmouse.data

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Mouse(
    // マウスカーソルの座標
    var x: Float = 0F,
    var y: Float = 0F,

    // 左右クリックを管理
    var clicked: Int = 0,
    val noClick: Int = 0,
    val leftClick: Int = 1,
    val rightClick: Int = 2,
    val middleClick: Int = 3,

    // ホイール距離
    var wheel: Float = 0F,

    // タッチした画面の座標
    var touchX: Float = 0F,
    var touchY: Float = 0F,

    // 端末のディスプレイサイズ
    var displayWidth: Int = 0,
    var displayHeight: Int = 0
) {
    fun setClick() {
        if (isOnLeftClickArea()) {
            clicked = leftClick
        }
        if (isOnRightClickArea()) {
            clicked = rightClick
        }
    }

    fun releaseClick() {
        clicked = noClick
        wheel = 0F
    }

    fun setWheelDistance(lastTouchY: Float) {
        if (touchX > displayWidth * 4 / 9 && touchX < displayWidth * 5 / 9) {
            wheel = touchY - lastTouchY
            return
        }
        wheel = 0F
    }

    private fun isOnLeftClickArea(): Boolean {
        val width = displayWidth as Float
        return touchX < width * 4 / 9
    }

    private fun isOnRightClickArea(): Boolean {
        val width = displayWidth as Float
        return touchX > width * 5 / 9
    }
}