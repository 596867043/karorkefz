#include <cstring>
#include <cctype>

//
// Created by L&J on 2020/1/26.
//
char * trim_string(char *str)
{
    char *start, *end;
    int len = strlen(str);

    //去掉最后的换行符
    if(str[len-1] == '\n')
    {
        len--;		//字符串长度减一
        str[len] = 0;	//给字符串最后一个字符赋0，即结束符
    }

    //去掉两端的空格
    start = str;		//指向首字符
    end = str + len -1;	//指向最后一个字符
    while(*start && isspace(*start))
        start++;	//如果是空格，首地址往前移一位，如果不是，则跳过该循环
    while(*end && isspace(*end))
        *end-- = 0;	//如果是空格，末地址往前移一位，并赋结束符
    strcpy(str, start);	//把首地址还给str
    return str;
}
