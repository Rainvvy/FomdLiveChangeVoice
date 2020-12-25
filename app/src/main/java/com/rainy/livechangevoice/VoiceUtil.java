package com.rainy.livechangevoice;

/**
 * create by Rainy on 2020/12/23.
 * email: im.wyu@qq.com
 * github: Rainvy
 * describe:
 */
public class VoiceUtil {
    public static final int MODE_YUANSHENG = 0;
    public static final int MODE_LUOLI = 1;
    public static final int MODE_DASHU = 2;
    public static final int MODE_GAOGUAI = 3;
    public static final int MODE_KONGLING = 4;
    public static final int MODE_JINGSONG = 5;
    static {
        System.loadLibrary("change-voice");
    }

    //利用 javah -classpath . -jni com.rainy.livechangevoice.VoiceUtil  来生成头文件

    public static native void voiceChange(String path, int mode);



}
