//
// Created by L&J on 2020/1/15.
//

#include <jni.h>
#include <string>
#include "Constant/cJSON.h"
#include "android/log.h"
#include "NetWork.c"


#define TAG "karorkefz_native"
#define LOGD(...)__android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define LOGW(...)__android_log_print(ANDROID_LOG_WARN,TAG,__VA_ARGS__)
#define LOGE(...)__android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

char *out;
cJSON *json;
const char *error_ptr;

extern "C" JNIEXPORT jstring JNICALL Java_com_ms_karorkefz_util_NDKTools_getStringFromNDK(
        JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++ 隔壁老头";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jstring JNICALL Java_com_ms_karorkefz_util_NDKTools_setStringFromNDK(
        JNIEnv *env, jobject /* this */, jstring string) {
    json = cJSON_Parse((char *) env->GetStringUTFChars(string, 0));
    LOGW("入值:%s", (char *) env->GetStringUTFChars(string, 0));
    if (NULL == json) {
        error_ptr = cJSON_GetErrorPtr();
        if (error_ptr != NULL) {
            LOGE("cJson Error before: %s", error_ptr);
        }
    }
    out = cJSON_Print(json);  //这个是可以输出的。为获取的整个json的值
    LOGW("cJSON 解析完成值:%s", out);
//    LOGW("code:%s", cJSON_Print(cJSON_GetObjectItem(json, "code")));
//    LOGW("msg:%s", cJSON_Print(cJSON_GetObjectItem(json, "msg")));
//    LOGW("data:%s", cJSON_Print(cJSON_GetObjectItem(json, "data")));
//    LOGW("adapter:%s", cJSON_Print(cJSON_GetObjectItem(json, "adapter")));
//    LOGW("gift:%s", cJSON_Print(cJSON_GetObjectItem(json, "gift")));
//    LOGW("db_user:%s",cJSON_GetObjectItem(json, "syslog_db"));
//    cJSON *arrayItem = cJSON_GetObjectItem(json, "syslog_db"); //获取这个对象成员
//    cJSON *object = cJSON_GetArrayItem(arrayItem, 0);   //因为这个对象是个数组获取，且只有一个元素所以写下标为0获取

    /*下面就是可以重复使用cJSON_GetObjectItem来获取每个成员的值了*/
//    cJSON *item = cJSON_GetObjectItem(object, "db_user");  //
//    printf("db_user:%s\n", item->valuestring);

//    item = cJSON_GetObjectItem(object, "db_password");
//    printf("db_password:%s\n", item->valuestring);
//    return env->NewStringUTF(item->valuestring);
    std::string hello = "Hello from C++   隔壁  老头";
    return env->NewStringUTF(out);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_ms_karorkefz_util_NDKTools_sumFromNDK(JNIEnv *env, jobject j, jint a, jint b) {
    int sum = a + b;
    return sum;
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ms_karorkefz_util_NDKTools_NetWorkFromNDK( JNIEnv *env, jobject /* this */,jstring send_data_path,jstring data) {
    LOGW("调用网络发送类。");
    LOGW("调用网络发送类，参数path:%s", env->GetStringUTFChars(send_data_path, 0));
    LOGW("调用网络发送类，data:%s", env->GetStringUTFChars(data, 0));

    char *result = network_main((char *) env->GetStringUTFChars(send_data_path, 0),(char *) env->GetStringUTFChars(data, 0));

    LOGW("调用网络发送类，结束:%s", result);
    const char *d = "\n";
    char *p;
    char *c;
    p = strtok(result, d);
    while (p) {
        LOGW("%s\n", p);
        p = strtok(NULL, d);
        if (p!=NULL) {
            c = p;
        }
    }
    json = cJSON_Parse(c);
    LOGW("入值:%s", c);
    if (NULL == json) {
        error_ptr = cJSON_GetErrorPtr();
        if (error_ptr != NULL) {
            LOGE("cJson Error before: %s", error_ptr);
        }
    }
    out = cJSON_Print(json);  //这个是可以输出的。为获取的整个json的值
    LOGW("cJSON 解析完成值:%s", out);


    return env->NewStringUTF(out);
}