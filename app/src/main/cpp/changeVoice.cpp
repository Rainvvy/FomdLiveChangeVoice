//
// Created by Administrator on 2020/12/23.
//



#include "com_rainy_livechangevoice_VoiceUtil.h"



extern "C"
JNIEXPORT void JNICALL
Java_com_rainy_livechangevoice_VoiceUtil_voiceChange(JNIEnv
* env,
jclass clazz, jstring
path,
jint mode
) {
    //JNI 的 jstring ----> C语言认识的指针  一级指针const char
    const char * voicePath = env->GetStringUTFChars(path,0);

    //播放声音
    FMOD::System *system;                    //Fmod 音效系统引擎库
    FMOD::Sound  *sound;                      //fmod 声音
    FMOD::Channel *channel;                        //fmod 声道
    FMOD::DSP *dsp ;                            //fmod dsp

    //TODO 第一步 系统初始化
   //创建一个系统对象  并且赋值给引用
    System_Create(&system);
    //系统初始化
    system->init(32,FMOD_INIT_NORMAL,0);
    //创建声音
    system->createSound(voicePath,FMOD_DEFAULT,0, &sound);
    //播放声音
    system->playSound(sound,0, false, &channel);

    switch (mode) {
        case com_rainy_livechangevoice_VoiceUtil_MODE_YUANSHENG:
            break;
        case com_rainy_livechangevoice_VoiceUtil_MODE_LUOLI:
            //萝莉声音特点  音调高
            system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT,&dsp);
            //设置dsp音调调节
            dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,2.0f);
            //添加音效到channel 通道
            channel->addDSP(0,dsp);
            break;
        case com_rainy_livechangevoice_VoiceUtil_MODE_DASHU:
            //大叔的声音 特点： 音调低 低沉
            system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT,&dsp);
            //设置dsp音调调节
            dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,0.7f);

            channel->addDSP(0,dsp);

            break;
        case com_rainy_livechangevoice_VoiceUtil_MODE_GAOGUAI:

            //搞怪 小黄人的声音 频率很快

            //获取当前的频率
            float  freq;
            channel->getFrequency(&freq);

            //在原来频率上在加
            channel->setFrequency(freq * 1.5f);



            break;
        case com_rainy_livechangevoice_VoiceUtil_MODE_KONGLING:
            //空灵的声音  echo 回声 的dsp 类型
            system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
            dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY , 200); // 回声延时
            dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK , 10); // 回声 衰减 反馈 默认50  0就是完全衰减
            channel->addDSP(0,dsp);

            break;
        case com_rainy_livechangevoice_VoiceUtil_MODE_JINGSONG:

            //惊悚的音效  特点： 很多声音的组合
            //特点： 音调低 低沉
            system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT,&dsp);
            //设置dsp音调调节
            dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,0.7f);
            channel->addDSP(0,dsp);
            //回声
            system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
            dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY , 400); // 回声延时
            dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK , 40); // 回声 衰减 反馈 默认50  0就是完全衰减
            channel->addDSP(1,dsp);

            //增加颤音
            system->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
            dsp->setParameterFloat(FMOD_DSP_TREMOLO_FREQUENCY,8.0f);
            dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW,0.8f);
            channel->addDSP(2,dsp);

            break;

    }


    bool isPlay = true;
    while (isPlay){

         channel->isPlaying(&isPlay);

         usleep(1000*1000);

    }


    //时时刻刻记得回收
    sound->release();
    system->close();
    system->release();
    env->ReleaseStringUTFChars(path,voicePath);

}