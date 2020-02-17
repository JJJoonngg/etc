#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <fcntl.h>
#include <signal.h>

#define MAX_CMD_ARG 10
#define MAX_CMD_GRP 10

const char *prompt = "Command> ";

char* cmdgrps[MAX_CMD_GRP];
char* cmdvector[MAX_CMD_ARG];
char cmdline[BUFSIZ];
struct sigaction act;

void fatal(char* str);
void execute_cmdline(char *cmdline);
void execute_cmdgrp(char* cmdgrp);
int makelist(char *s,const char *delimiters, char** list, int MAX_LIST);
int makeargv(char *s,const char *delimiters, char** argvp, int MAX_LIST);
int cmd_cd(int argc, char** argv);
int cmd_background(int cnt);
void zombie_handler();
void setSignalIgnore();
void changeSignalIngore();

int main(int argc, char** argv){

	memset(&act, 0, sizeof(act));
	act.sa_handler = zombie_handler;
	act.sa_flags = SA_RESTART;
	sigaction(SIGCHLD, &act, NULL);

	setSignalIgnore();
	

	while(1){

		fputs(prompt, stdout);
		fgets(cmdline, BUFSIZ, stdin);
		cmdline[strlen(cmdline)-1] = '\0';
		execute_cmdline(cmdline);
		printf("\n");
	}
	return 0;
}

void fatal(char *str){
	perror(str);
	exit(1);
}

void zombie_handler(){
	pid_t pid;
	int stat;
	while((pid = waitpid(-1, &stat, WNOHANG)) > 0);
}

void setSignalIgnore(){
	signal(SIGINT, SIG_IGN);
	signal(SIGQUIT, SIG_IGN);
	signal(SIGTSTP, SIG_IGN);
	signal(SIGTTIN, SIG_IGN);
	signal(SIGTTOU, SIG_IGN);
}

void changeSignalIngore(){
	signal(SIGINT, SIG_DFL);
        signal(SIGQUIT, SIG_DFL);
        signal(SIGTSTP, SIG_DFL);
        signal(SIGTTIN, SIG_DFL);
        signal(SIGTTOU, SIG_DFL);
}
	
int cmd_background(int cnt){
	int i;
	for(i = 0; i < cnt; i++){
		if(!strcmp(cmdvector[i],"&")){
			cmdvector[i] = NULL;			
			return 1;
		}
	}
	return 0;
}

int cmd_cd(int argc, char** argv){
        if(argc == 1)
                chdir(getenv("Home"));
        else if(argc == 2){
                if(chdir(argv[1]))
                        printf("No directory\n");
        }
        else
                printf("Error\n");
        return 1;
}


void execute_cmdline(char* cmdline){
	int cnt = 0;
	int  i = 0, j = 0;
	int numtokens = 0;
	char cmdgrptmp[BUFSIZ];
	pid_t pid;

	cnt = makeargv(cmdline, ";", cmdgrps, MAX_CMD_GRP);
	

	for(i = 0; i < cnt; i++){
	memcpy(cmdgrptmp, cmdgrps[i], strlen(cmdgrps[i]) + 1);
	numtokens = makeargv(cmdgrps[i], " \t", cmdvector, MAX_CMD_GRP);
		if(strcmp(cmdvector[0], "exit") == 0){
			exit(-1);			
		}
		else if(strcmp("cd", cmdvector[0]) == 0){
			cmd_cd(numtokens, cmdvector);
			return;
		}
		switch(pid = fork()){
			case -1: fatal("fork error");
			case 0: execute_cmdgrp(cmdgrptmp);
			default:
				if(cmd_background(numtokens)) break;
				tcsetpgrp(STDIN_FILENO, pid);
				waitpid(pid, NULL, 0);
				tcsetpgrp(STDIN_FILENO, getpgid(0));
				fflush(stdout);
		}
	}
}

void execute_cmdgrp(char *cmdgrp){
	int  i = 0;
	int cnt = 0;
	cnt  = makeargv(cmdgrp, " \t", cmdvector, MAX_CMD_ARG);

	setpgid(0, 0);
	changeSignalIngore();
	if(cmd_background(cnt)){
		switch(fork()){
			case -1: fatal("fork error");
			case 0: 
				 usleep(1000);
				 setsid();
				 break;
			default:exit(0);
		}
	}
	execvp(cmdvector[0], cmdvector);
	fatal("exec error");
}

int makeargv(char *s,const char *delimiters, char** argvp, int MAX_LIST){
	int i = 0;
	int numtokens = 0;
	char *snew = NULL;
	if( (s==NULL) || (delimiters == NULL)) return -1;

	snew = s + strspn(s, delimiters);
	if((argvp[numtokens] = strtok(snew, delimiters)) == NULL)
		return numtokens;
	numtokens = 1;

	while(1){
		if ((argvp[numtokens] = strtok(NULL, delimiters)) == NULL)
			break;
		if( numtokens == (MAX_LIST -1)) return -1;

		numtokens++;
	}
	return numtokens;
}
