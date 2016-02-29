# RatingView 

用来显示评分、评价的自定义视图控件。灵感来源于360手机安全卫士。此控件已经应用在我个人开发的[户外探子](http://android.myapp.com/myapp/detail.htm?apkName=com.dean.travltotibet)，有兴趣的可以看看。

This custom view is using for rating, scoring, marking. Inspired by 360 mobile security guards.

## 预览图 Screenshots
## 集成 Integrate
*  添加一个dependency到你的`build.gradle`   // Add a dependency to your `build.gradle`:
```
dependencies {
    compile 'com.deanguo.ratingview:library:1.1.0'
}
```
*  或者将[library](/RatingView/library)导入到工程中  // Or import [library](/RatingView/library) as a model to your project
  
*  或者将[library](/RatingView/library)中的`RatingBar`，`RatingView`，和`attrs.xml`复制到你的项目中去 // Or copy `RatingBar`,`RatingView`,`attrs.xml` from [library](/RatingView/library) to your project

## 使用 Usage
####初始化 Initialization
添加`com.deanguo.ratingview.RatingView` 到你的布局文件中  
Add `com.deanguo.ratingview.RatingView` to your layout XML file.
```XML
<com.deanguo.ratingview.RatingView
        android:id="@+id/rating_view"
        android:layout_width="200dp"
        android:layout_height="200dp">
    </com.deanguo.ratingview.RatingView>
```
之后在代码中调用  
Then using in java code
```Java
RatingView view = (RatingView) this.findViewById(R.id.rating_view);
        view.addRatingBar(new RatingBar(8, "HARD"));
        view.addRatingBar(new RatingBar(8, "VIEW"));
        view.addRatingBar(new RatingBar(8, "WEATHER"));
        view.addRatingBar(new RatingBar(8, "TRAFFIC"));
        view.show();
```

####设置颜色 Seting Color
#####RatingView
```xml
<com.deanguo.ratingview.RatingView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/rating_view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:rating_default_color="@color/white" />
```
```java
    RatingView view = (RatingView) this.findViewById(R.id.rating_view);
    view.setDefaultColor(getColor(R.color.white));
```

#####RatingBar
```java
RatingBar hard = new RatingBar(8, "HARD");
hard.setRatingBarColor(getColor(R.color.white));
```
也可以使用下面方法深度定义Ratingbar样式  
Using below method to create your own Ratingbar.   
`setRatedColor(int color)`  
`setUnRatedColor(int color)`  
`setTitleColor(int color)`  
`setOutlineColor(int color)`  
`setShadowColor(int color)`  

####自定义视图 Custom View
可以再RatingView中间放置任意视图，比如文字，图片，按钮，布局，控件等  
You can add any view,layout in RatingView
```xml
<com.deanguo.ratingview.RatingView
        android:id="@+id/rating_view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_centerInParent="true">
        <RelativeLayout
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_gravity="center"
            >
            <TextView
                android:visibility="invisible"
                android:id="@+id/center_btn"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_centerInParent="true"
                android:text="update"
                android:gravity="center"
                android:textColor="#ffffff"
                android:textSize="20dp" />

        </RelativeLayout>
    </com.deanguo.ratingview.RatingView>
```
####动画监听 Animation Listener
```java
RatingView view = (RatingView) this.findViewById(R.id.rating_view);
        view.setAnimatorListener(new RatingView.AnimatorListener() {
            @Override
            public void onRotateStart() {
            }

            @Override
            public void onRotateEnd() {
            }

            @Override
            public void onRatingStart() {
            }

            @Override
            public void onRatingEnd() {
            }
        });
```

更多使用方法请参考项目中的[sample](/RatingView/sample)  
please reference the [sample](/RatingView/sample) code for more information.  

## License
```
Copyright 2016 Dean Guo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
