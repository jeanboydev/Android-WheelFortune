package com.jeanboy.app.wheelfortune

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jeanboy.component.wheelfortune.ItemData
import com.jeanboy.component.wheelfortune.WheelFortuneView

class MainActivity : AppCompatActivity() {

    private val dataList = mutableListOf(
        ItemData(
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3510986481,3852924315&fm=11&gp=0.jpg",
            false
        ),
        ItemData(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic4.zhimg.com%2F50%2Fv2-095b2db945e2d3e0644ccbb26eab8ed8_hd.jpg&refer=http%3A%2F%2Fpic4.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614157465&t=050b31134d572e38ca02ecb23ac33870",
            false
        ),
        ItemData(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F17%2F20190117092809_ffwKZ.thumb.700_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614157465&t=d59a5f1124cd37e84645f54dc08219d1",
            true
        ),
        ItemData(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202007%2F01%2F20200701063944_5VaBk.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614157465&t=8715e2f970e934447e6d158a6254b159",
            false
        ),
        ItemData(
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3155998395,3600507640&fm=26&gp=0.jpg",
            false
        ),
        ItemData(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201510%2F01%2F20151001174653_L3wEF.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614157465&t=40b8181ae924bb82396318b301803e0a",
            false
        ),
        ItemData(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201510%2F18%2F20151018172940_5etXi.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614157465&t=a162ce2a6f394e4825e5ba788047507a",
            false
        ),
        ItemData(
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=387639067,1599589691&fm=26&gp=0.jpg",
            false
        ),
        ItemData(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201812%2F7%2F2018127203650_KvXLM.thumb.700_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614157465&t=4dc2acf58b9b3a1c44db1fef14fb36a4",
            false
        ),
    )

    var wheelFortuneView: WheelFortuneView? = null
    var tv_user_count: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wheelFortuneView = findViewById(R.id.wheelFortuneView)
        tv_user_count = findViewById(R.id.tv_user_count)

        wheelFortuneView?.setListener(object : WheelFortuneView.WheelStateListener {
            override fun onJoinClick() {
                wheelFortuneView?.toRunning(0)
            }

            override fun onCountChange(count: Int) {
                tv_user_count?.text = resources.getString(R.string.user_count, count, dataList.size)
            }

            override fun onRemove(removeData: ItemData) {

            }
        })

    }

    private var index = 0
    fun onAdd(view: View) {
        if (index >= dataList.size) {
            return
        }
        if (index >= 0 && index < dataList.size) {
            wheelFortuneView?.addData(dataList[index])
        }
        index++
    }

    fun onClear(view: View) {
        index = 0
        wheelFortuneView?.clearData()
    }
}