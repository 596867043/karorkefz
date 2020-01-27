//
// Created by L&J on 2020/1/24.
//
#include <arpa/inet.h>
#include <assert.h>
#include <errno.h>
#include <netinet/in.h>
#include <signal.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <netdb.h>
#include <unistd.h>
#include <strings.h>
#include "android/log.h"

#define SA      struct sockaddr
#define MAXLINE 8192 //网络数据返回缓存区大小
#define MAXSUB  2000
#define MAXPARAM 2048

#define LISTENQ 1024

int sockfd;
char *hname = "149.129.60.44";
//char *send_data_path = "/tp5.1/public/karorkefz/gift/get_Gift_main";
char *result;
/**
 * 发http post请求
 */
ssize_t http_post(char *page, char *poststr)
{
    char sendline[MAXLINE + 1], recvline[MAXLINE + 1];
    ssize_t n;
    snprintf(sendline, MAXSUB,
             "POST %s HTTP/1.0\r\n"
             "Host: %s\r\n"
             "Content-type: application/x-www-form-urlencoded\r\n"
             "Content-length: %zu\r\n\r\n"
             "%s", page, hname, strlen(poststr), poststr);

    write(sockfd, sendline, strlen(sendline));
//    n = read(sockfd, recvline, MAXLINE);
//    while ((n = read(sockfd, recvline, MAXLINE)) > 0) {
//        recvline[n] = '\0';
//        printf("%s", recvline);
//    }
    n =recv(sockfd, recvline, MAXLINE, MSG_WAITALL),
    recvline[n] = '\0';
    result=recvline;
    return n;
}

/**
 * 使用通用接口发数据
 */
ssize_t send_data(char *send_data_path ,char *data)
{
    char params[MAXPARAM + 1];
    char *cp = params;
    sprintf(cp,"data=%s", data);
    return http_post(send_data_path, cp);
}

char * network_main(char *send_data_path ,char *data)
{
    struct sockaddr_in servaddr;
    char **pptr;
    char str[50];
    struct hostent *hptr;
    if ((hptr = gethostbyname(hname)) == NULL) {
        fprintf(stderr, "通过域名获取IP失败: %s: %s",hname, hstrerror(h_errno));
        printf( "通过域名获取IP失败: %s: %s",hname, hstrerror(h_errno));
    }
    printf("域名: %s\n", hptr->h_name);
    if (hptr->h_addrtype == AF_INET && (pptr = hptr->h_addr_list) != NULL) {
        printf("IP: %s\n",inet_ntop(hptr->h_addrtype, *pptr, str,sizeof(str)));
    } else {
        fprintf(stderr, "Error call inet_ntop \n");
        printf("Error call inet_ntop \n");
    }
    //建立socket连接
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(80);
    inet_pton(AF_INET, str, &servaddr.sin_addr);

    connect(sockfd, (SA *) & servaddr, sizeof(servaddr));

    send_data(send_data_path ,data);

    close(sockfd);
    return result;
}