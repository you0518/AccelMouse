package e.lllll.accelmouse.data

data class Mouse(
    // マウスカーソルの座標
    var x: Int = 0,
    var y: Int = 0,

    var clicked: Int = 0,
    val leftClick: Int = 1,
    val rightClick: Int  = 2,
    val middleClick: Int  = 3,

    // タッチした画面の座標
    var touchX: Float = 0F,
    var touchY: Float = 0F,

    // 端末のディスプレイサイズ
    var displayWidth: Int = 0,
    var displayHeight: Int = 0
) {
    fun setClick(): Unit {
        if (isOnLeftClickArea()) {
            clicked = leftClick
        }
        if (isOnRightClickArea()) {
            clicked = rightClick
        }
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