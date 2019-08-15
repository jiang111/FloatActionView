# FloatActionView

![](https://github.com/jiang111/FloatActionView/raw/master/art/art.gif)

### 集成 [![](https://jitpack.io/v/jiang111/FloatActionView.svg)](https://jitpack.io/#jiang111/FloatActionView)

```
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.jiang111:FloatActionView:1.0.0'
	}


```


### 示例
```
//请注意,布局文件中必须有个同级别的ViewGroup,并且它的LayoutParam为match_parent 才能达到最佳效果
FloatActionView actionView = findViewById(R.id.actionview);
        actionView.setMainButtonIcon(R.drawable.ic_add_black_24dp);
        actionView.setOnClick(new FloatActionView.OnClick() {
            @Override
            public void positionClicked(int position) {
                Log.i(TAG, "positionClicked: " + position);
            }

            @Override
            public void mainClicked() {
                Log.i(TAG, "mainClicked: ");
            }

            @Override
            public void dismissed() {
                Log.i(TAG, "dismissed: ");
            }
        });
        List<String> tips = new ArrayList<>();
        List<Integer> images = new ArrayList<>();
        actionView.setData(tips, images);

actionView.dismiss();  //dismiss the view

//可通过new FloatActionView.Builder()来对整个样式进行全局配置


menuTextSize  //菜单中的文字大小
menuTextColor //菜单中的文字颜色

menuImageSize //菜单中的图片大小
mainImageSize //加号图片大小

animatDelay  //菜单动画延迟的时间
animatDuration //动画时长
mainRotateAble //加号按钮是否更随动画旋转
blurRadius //背景模糊
blurFactor //背景模糊
```


