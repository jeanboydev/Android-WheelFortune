# WheelFortune

幸运抽奖转盘，类似于StarMaker直播间抽奖。

## 效果图



## 使用

```xml
<com.jeanboy.component.wheelfortune.WheelFortuneView
	android:id="@+id/wheelFortuneView"
  android:layout_width="320dp"
  android:layout_height="320dp"
  android:layout_gravity="center" />
```



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

// 添加数据
wheelFortuneView?.addData(dataList[index])

// 清除数据
wheelFortuneView?.clearData()
```

