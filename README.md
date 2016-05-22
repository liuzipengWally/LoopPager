#LoopPager
***
这是一个Android轮转图控件，可作为banner，也可作为引导页。图片能够从网络中获取，也能使用本地的资源文件。  
下面看截图：   
![looppager](https://github.com/550609334/LoopPager/blob/master/screenshots/looppager.gif?raw=true)
  
 截图掉帧，实际很流畅
##matters needing attention

该控件可最低支持到API14 

######该控件依赖了以下两个library，使用者无需在项目里再次添加 （Don't need to add）。

    compile 'com.android.support:appcompat-v7:23.4.0'  
    compile 'com.github.bumptech.glide:glide:3.7.0'    是的图片加载是用glide，所以可以加载gif哦~

##How to use（如何使用）
***
###Gradle
    compile 'com.lzp.looppager:looppager:1.0.0'
    
###Maven

    <dependency>
    <groupId>com.lzp.looppager</groupId>
    <artifactId>looppager</artifactId>
    <version>1.0.0</version>
    </dependency>
    
###example（示例）
####local（从本地加载）
首先需要一个String-Array

    <string-array name="imgs">
        <item>@mipmap/img1</item>
        <item>@mipmap/img2r</item>
        <item>@mipmap/img3</item>
        <item>@mipmap/img4</item>
    </string-array>

接着只要将这个String-Array引用即可

    <com.lzp.looppager.LoopPager
        android:id="@+id/loopPager"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:imagesForLocal="@array/imgs"
        >
        
或者在JAVA代码中设置

     List<Integer> images = new ArrayList<>();
     images.add(R.mipmap.header);
     images.add(R.mipmap.header);
     images.add(R.mipmap.header);
     mLoopPager.setLocalImages(images);
####remote（从网络获取）
     List<String> urls = new ArrayList<>();
     urls.add("http://i0.hdslb.com/video/e0/e0814751cf26c9b319ed974d7f3d5f67.jpg");
     urls.add("http://img4.imgtn.bdimg.com/it/u=3252822932,941775074&fm=21&gp=0.jpg");
     urls.add("http://img1.imgtn.bdimg.com/it/u=3547247058,1962251339&fm=21&gp=0.jpg");
     urls.add("http://static.vgtime.com/article/web/150515191332685.jpg");
     mLoopPager.setRemoteImageUrls(urls);

###Events（事件） 
给了三个事件，点击，长按和选中。
       
       mLoopPager.setOnClickListener(new LoopPager.OnClickListener() {
            @Override
            public void onClick(int position, ImageView imageView) {

            }

            @Override
            public void longClick(int position, ImageView imageView) {

            }
        });

        mLoopPager.setPagerChangeListener(new LoopPager.PagerChangeListener() {
            @Override
            public void pagerChange(int position) {

            }
        });

###More settings（更多设置）
***
####XML
      设置导航标识的颜色
      app:naviColor="#ff9800"  
      设置导航标示所处位置 bottom_center，bottom_left，bottom_right，top_center，top_left，top_right  六个可选      
      app:mPosition="bottom_right"
      设置图片切换等待时间，默认为2000毫秒           
      app:loopDuration="3000"
      设置是否开启导航标识
      app:enableNavi="true"
      设置是否自动切换
      app:loop="true"
      设置导航标识的图形，circle，rectangle，roundRectangle，三种可选，默认圆形
      app:naviShape="circle"
      设置导航标识在圆形状态下的半径
      app:naviRadius="4dp"
      设置导航标识在矩形或圆角矩形状态下的宽高
      app:rectangleWidth="8dp"
      app:rectangleHeight="4dp"
***

####Java Code
Java代码基本对应上面XML中设置的项
 
      mLoopPager.setEnableNavi(true);
      mLoopPager.setLoop(true);
      mLoopPager.setLoopDuration(3000);
      mLoopPager.setNaviPosition(LoopPager.BOTTOM_RIGHT)
      mLoopPager.setNaviRadius(4);
      mLoopPager.setNaviShape(LoopPager.CIRCLE);
      mLoopPager.setNaviWidthAndHeight(8,4); 

##Other
#####About bugs
如果出现Bug，或者你有什么建议或需求，可以Email连系我。

#####E-Mail
**tracy550609334@gmail.com or 550609334@qq.com**

##License
Copyright 2016 liu zi peng

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.


