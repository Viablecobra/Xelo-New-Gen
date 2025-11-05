//
// Created by mrjar on 10/5/2025.
//
#include <jni.h>
#include <android/native_activity.h>
#include <dlfcn.h>
#include <android/log.h>

#define LOG_TAG "NativeLoader"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static void (*onCreate)(ANativeActivity*, void*, size_t) = nullptr;
static void (*onFinish)(ANativeActivity*) = nullptr;
static void (*androidMain)(struct android_app*) = nullptr;

extern "C" {

JNIEXPORT void ANativeActivity_onCreate(ANativeActivity* activity, void* savedState, size_t savedStateSize) {
    if (onCreate) {
        onCreate(activity, savedState, savedStateSize);
    } else {
        LOGE("onCreate function not loaded");
    }
}

JNIEXPORT void ANativeActivity_finish(ANativeActivity* activity) {
    if (onFinish) {
        onFinish(activity);
    }
}

JNIEXPORT void android_main(struct android_app* state) {
    if (androidMain) {
        androidMain(state);
    } else {
        LOGE("android_main function not loaded");
    }
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
Java_io_kitsuri_pelauncher_Launcher_MinecraftActivity_nativeOnLauncherLoaded(
        JNIEnv* env,
        jobject thiz,
        jstring libPath
) {
    const char* path = env->GetStringUTFChars(libPath, nullptr);

    LOGD("Loading Minecraft library from: %s", path);

    void* handle = dlopen(path, RTLD_NOW);
    if (!handle) {
        LOGE("Failed to load library: %s", dlerror());
        env->ReleaseStringUTFChars(libPath, path);
        return;
    }

    onCreate = reinterpret_cast<decltype(onCreate)>(
            dlsym(handle, "ANativeActivity_onCreate")
    );

    onFinish = reinterpret_cast<decltype(onFinish)>(
            dlsym(handle, "ANativeActivity_finish")
    );

    androidMain = reinterpret_cast<decltype(androidMain)>(
            dlsym(handle, "android_main")
    );

    if (!onCreate || !androidMain) {
        LOGE("Failed to resolve required symbols");
    } else {
        LOGD("Successfully loaded Minecraft native functions");
    }

    env->ReleaseStringUTFChars(libPath, path);
}

} // extern "C"
