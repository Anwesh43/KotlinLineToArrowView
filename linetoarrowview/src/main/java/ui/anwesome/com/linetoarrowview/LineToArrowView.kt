package ui.anwesome.com.linetoarrowview

/**
 * Created by anweshmishra on 26/04/18.
 */
import android.view.*
import android.content.*
import android.graphics.*

class LineToArrowView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        return true
    }

}