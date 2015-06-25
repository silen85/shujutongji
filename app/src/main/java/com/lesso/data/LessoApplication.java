package com.lesso.data;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by meisl on 2015/6/25.
 */
public class LessoApplication extends Application {

    private LoginUser loginUser;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());

        //捕获程序的崩溃信息
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(this);
    }

    /**
     * 配置使用universal-image-loader-1.9.3.jar显示图片的基本信息
     * 这个jar包可用来异步显示图片,同步下载图片,图片显示的时候自动缓存
     */
    public static void initImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.default_head)
                // .showImageForEmptyUri(R.drawable.default_head)
                // .showImageOnFail(R.drawable.default_head)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config;
        config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheExtraOptions(480, 800)
                .threadPriority(Thread.NORM_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .writeDebugLogs() // Remove for release app
                .threadPoolSize(4)
                .build();


        ImageLoader.getInstance().init(config);
    }

    public LoginUser getLoginUser() {
        return this.loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public class LoginUser {

        private String userName;
        private String userPassword;

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPassword() {
            return this.userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }


    }


}
