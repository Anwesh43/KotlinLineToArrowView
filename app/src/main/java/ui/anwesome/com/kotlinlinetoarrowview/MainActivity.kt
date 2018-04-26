package ui.anwesome.com.kotlinlinetoarrowview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.linetoarrowview.LineToArrowView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LineToArrowView.create(this)
    }
}
