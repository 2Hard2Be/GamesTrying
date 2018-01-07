#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_a2hard2be_petoSpiel_BlankGame_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
