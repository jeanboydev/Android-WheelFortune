# WheelFortune

幸运抽奖转盘，类似于StarMaker直播间抽奖。

## 效果图

![效果视频][https://raw.githubusercontent.com/jeanboydev/Android-WheelFortune/main/resources/00.mp4]
![效果图1][https://raw.githubusercontent.com/jeanboydev/Android-WheelFortune/main/resources/01.jpeg]
![效果图2][https://raw.githubusercontent.com/jeanboydev/Android-WheelFortune/main/resources/02.jpeg]
![效果图3][https://raw.githubusercontent.com/jeanboydev/Android-WheelFortune/main/resources/03.jpeg]
![效果图4][https://raw.githubusercontent.com/jeanboydev/Android-WheelFortune/main/resources/04.jpeg]

## 使用

- 设置布局

```xml
<com.jeanboy.component.wheelfortune.WheelFortuneView
	android:id="@+id/wheelFortuneView"
  android:layout_width="320dp"
  android:layout_height="320dp"
  android:layout_gravity="center" />
```

- 处理回调

```kotlin
val wheelFortuneView = findViewById(R.id.wheelFortuneView)
tv_user_count = findViewById(R.id.tv_user_count)

wheelFortuneView?.setListener(object : WheelFortuneView.WheelStateListener {
  override fun onJoinClick() {
    // join 按钮点击时回调，移除 item
    wheelFortuneView?.toRunning(0)
  }

  override fun onCountChange(count: Int) {
    // item 数量变化回调
    tv_user_count?.text = resources.getString(R.string.user_count, count, dataList.size)
  }

  override fun onRemove(removeData: ItemData) {
		// item 移除时回调
  }
})
```

- 处理数据

```kotlin
// 添加数据
wheelFortuneView?.addData(dataList[index])

// 清除数据
wheelFortuneView?.clearData()
```


