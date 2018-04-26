package ui.anwesome.com.linetoarrowview

/**
 * Created by anweshmishra on 26/04/18.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*

class LineToArrowView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * this.dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false){

        fun animate (updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class LineToArrow (var i : Int, val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val size : Float = Math.min(w, h) / 15
            paint.color = Color.parseColor("#2ecc71")
            paint.strokeWidth = Math.min(w, h)/60
            paint.strokeCap = Paint.Cap.ROUND
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.rotate(90f * state.scales[1])
            for (i in 0..1) {
                canvas.save()
                canvas.scale(1f - 2 * i, 1f)
                canvas.translate(h/2 * state.scales[2], 0f)
                val sy : Float = (size/2)
                for (j in 0..1) {
                    canvas.drawLine(0f, sy * (1 - 2 * j), size * state.scales[0], 0f, paint)
                }
                canvas.restore()
            }
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class Renderer (var view : LineToArrowView) {

        private val animator : Animator = Animator(view)

        private val lineToArrow : LineToArrow = LineToArrow(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            lineToArrow.draw(canvas, paint)
            animator.animate {
                lineToArrow.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            lineToArrow.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : LineToArrowView  {
            val view : LineToArrowView = LineToArrowView(activity)
            activity.setContentView(view)
            return view
        }
    }
}