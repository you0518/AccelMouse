package e.lllll.accelmouse.mouse

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Mouse(
    /**
     * ジャイロセンサで取得するマウスカーソルの座標
     */
    var x: Float = 0F,
    var y: Float = 0F,
    /**
     * クリックポジション
     */
    var clicked: Int = 0,

    /**
     * マウスホイール距離
     */
    var wheel: Float = 0F
)