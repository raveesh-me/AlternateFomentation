package `in`.cookytech.alternatefomentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : AppCompatActivity() {

    val times = arrayOf(180_000L, 60_000L,
            180_000L, 60_000L,
            180_000L, 60_000L,
            180_000L, 300_000L)//original


//    val times = arrayOf(10_000L, 20_000L,
//            10_000L, 20_000L,
//            10_000L, 20_000L,
//            10_000L, 30_000L)//testReduced

    lateinit var relativeLayout: RelativeLayout
    lateinit var timerView: TextView
    lateinit var instructionView: TextView
    lateinit var nextButton: Button
    var attempt = 0


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = this.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = colors[0]
        }


        relativeLayout = relativeLayout {
            keepScreenOn = true
            backgroundColor = colors[0]
            padding = dip(20)


            textView {
                text = "Alternate Fomentation"
                textSize = 30f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textAlignment = 4
                }
                textColor = Color.parseColor("#BBFFFFFF")

            }.lparams {
                centerHorizontally()
            }


            textView {
                text = "MM : SS"
                textSize = 20f
                textColor = Color.parseColor("#99FFFFFF")

            }.lparams {
                centerHorizontally()
                above(1)
                bottomMargin = dip(5)
            }


            timerView = textView {
                id = 1
                text = "03" + " : " + "00"
                textSize = 80f
                textColor = Color.WHITE

            }.lparams {
                centerInParent()
            }

            instructionView = textView {
                textResource = R.string.instruction_hot
                textColor = Color.WHITE
                textSize = 17f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textAlignment = 4
                }//CENTER

            }.lparams {
                centerHorizontally()
                above(2)
                bottomMargin = dip(20)
            }

            nextButton = button {
                id = 2
                backgroundColor = Color.WHITE
                text = "Start Timer"
                textSize = 20f
                textColor = colors[0]
                onClick {
                    if (attempt == 8) {
                        timerView.text = "Completed"
                        instructionView.text = "Restart app to begin again"
                        nextButton.text = "Reset"
                        attempt++

                    } else if (attempt > 8) {
                        recreate()
                    } else {
                        val evenOdd = attempt % 2
                        val time = times[attempt++]
                        clicked(time, evenOdd)
                    }

                }
            }.lparams {
                width = matchParent
                bottomMargin = dip(20)
                alignParentBottom()
                centerHorizontally()
                horizontalPadding = dip(30)
            }


        }


    }

    private fun clicked(time: Long, evenOdd: Int) {
        var remainingSeconds: Long
        instructionView.animate().scaleY(0f).scaleX(0f).start()
        nextButton.animate().scaleX(0f).scaleY(0f).start()



        object : CountDownTimer(time, 1_000) {
            override fun onTick(p0: Long) {
                remainingSeconds = p0 / 1_000
                timerView.text = String.format("%02d : %02d", remainingSeconds / 60, remainingSeconds % 60)

            }

            override fun onFinish() {
                if (evenOdd == 0) {
                    instructionView.textResource = R.string.instruction_cold
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.statusBarColor = colors[1]
                    }
                    nextButton.textColor = colors[1]
                    relativeLayout.backgroundColor = colors[1]

                } else {
                    instructionView.textResource = R.string.instruction_hot
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.statusBarColor = colors[0]
                    }
                    nextButton.textColor = colors[0]
                    relativeLayout.backgroundColor = colors[0]

                }

                try {
                    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val r = RingtoneManager.getRingtone(applicationContext, notification)
                    r.play()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                instructionView.animate().scaleX(1f).scaleY(1f).start()
                nextButton.animate().scaleX(1f).scaleY(1f).start()
            }

        }.start()
    }

    //TODO:Use android ViewModel to persist with rotation changes with Anko

}


