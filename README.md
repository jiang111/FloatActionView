# FloatActionView

![](https://github.com/jiang111/FloatActionView/raw/master/art/art.gif)



### usage
``` 
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


