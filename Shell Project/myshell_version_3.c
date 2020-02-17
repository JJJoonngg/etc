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
#define MAX_CMD_LIST 10

const char *prompt = "Command> ";

char* cmdgrps[MAX_CMD_GRP];
char* cmdvector[MAX_CMD_ARG];
char* cmdlist[MAX_CMD_LIST];
char cmdline[BUFSIZ];
int judge_background = 0;
struct sigaction act;

void fatal(char* str);
void execute_cmdline(char *cmdline);
void execute_cmdgrp(char* cmdgrp);
void execute_cmd(char* cmdgrp);
int makeargv(char *s, const char *delimiters, char** argvp, int MAX_LIST);
int cmd_cd(int argc, char** argv);
int cmd_background(char* cmd);
void cmd_redirect(char* cmd);

void zombie_handler();
void setSignalIgnore();
void changeSignalIngore();

int main(int argc, char** argv) {

	memset(&act, 0, sizeof(act));
	act.sa_handler = zombie_handler;
	act.sa_flags = SA_RESTART;
	sigaction(SIGCHLD, &act, NULL);

	setSignalIgnore();


	while (1) {

		fputs(prompt, stdout);
		fgets(cmdline, BUFSIZ, stdin);
		cmdline[strlen(cmdline) - 1] = '\0';
		execute_cmdline(cmdline);
		printf("\n");
	}
	return 0;
}

void fatal(char *str) {
	perror(str);
	exit(1);
}

void zombie_handler() {
	pid_t pid;
	int stat;
	while ((pid = waitpid(-1, &stat, WNOHANG)) > 0);
}

void setSignalIgnore() {
	signal(SIGINT, SIG_IGN);
	signal(SIGQUIT, SIG_IGN);
	signal(SIGTSTP, SIG_IGN);
	signal(SIGTTIN, SIG_IGN);
	signal(SIGTTOU, SIG_IGN);
}

void changeSignalIngore() {
	signal(SIGINT, SIG_DFL);
	signal(SIGQUIT, SIG_DFL);
	signal(SIGTSTP, SIG_DFL);
	signal(SIGTTIN, SIG_DFL);
	signal(SIGTTOU, SIG_DFL);
}

int cmd_background(char* cmd) {
	int i;
	for (i = 0; i < strlen(cmd); i++) {
		if (cmd[i] == '&') {
			cmd[i] = ' ';
			return 1;
		}
	}
	return 0;
}

int cmd_cd(int argc, char** argv) {
	if (argc == 1)
		chdir(getenv("Home"));
	else if (argc == 2) {
		if (chdir(argv[1]))
			printf("No directory\n");
	}
	else
		printf("Error\n");
	return 1;
}

void cmd_redirect(char* cmd) {
	char* tmp;
	int fd;
	int cmdlen = strlen(cmd);
	cmdlen--;

	for (; cmdlen >= 0; cmdlen--) {
		switch (cmd[cmdlen]) {
		case '<':
			tmp = strtok(&cmd[cmdlen + 1], " \t");
			fd = open(tmp, O_RDONLY | O_CREAT, 0644);
			dup2(fd, 0);
			close(fd);
			cmd[cmdlen] = '\0';
			break;
		case '>':
			tmp = strtok(&cmd[cmdlen + 1], " \t");
			fd = open(tmp, O_WRONLY | O_CREAT | O_TRUNC, 0644);
			dup2(fd, 1);
			close(fd);
			cmd[cmdlen] = '\0';
			break;
		}
	}
}

void execute_cmdline(char* cmdline) {
	int cnt = 0;
	int  i = 0, j = 0;
	int numtokens = 0;
	char* cmdtmp[MAX_CMD_GRP];
	pid_t pid;

	cnt = makeargv(cmdline, ";", cmdgrps, MAX_CMD_GRP);

	for (i = 0; i < cnt; i++) {
		if (cmdgrps[i][0] == 'e' && cmdgrps[i][1] == 'x'
			&& cmdgrps[i][2] == 'i' && cmdgrps[i][3] == 't')
			exit(-1);
		else if (cmdgrps[i][0] == 'c' && cmdgrps[i][1] == 'd') {
			numtokens = makeargv(cmdgrps[i], " \t", cmdtmp, MAX_CMD_GRP);
			cmd_cd(numtokens, cmdtmp);
			return;
		}
	}

	for (i = 0; i < cnt; i++) {

		judge_background = cmd_background(cmdgrps[i]);
		switch (pid = fork()) {
		case -1: fatal("fork error");
		case 0: execute_cmdgrp(cmdgrps[i]);
		default:
			if (judge_background) break;
			tcsetpgrp(STDIN_FILENO, pid);
			waitpid(pid, NULL, 0);
			tcsetpgrp(STDIN_FILENO, getpgid(0));
			fflush(stdout);
		}
	}
}

void execute_cmdgrp(char *cmdgrp) {
	int  i = 0;
	int cnt = 0;
	int pfd[2];

	setpgid(0, 0);
	if (!judge_background) tcsetpgrp(STDIN_FILENO, getpid());

	changeSignalIngore();

	cnt = makeargv(cmdgrp, "|", cmdlist, MAX_CMD_LIST);
	if (cnt <= 0) fatal("makargv error");

	for (i = 0; i < cnt - 1; i++) {
		pipe(pfd);
		switch (fork()) {
		case -1: fatal("fork error");
		case 0:
			close(pfd[0]);
			dup2(pfd[1], STDOUT_FILENO);
			execute_cmd(cmdlist[i]);
		default:
			close(pfd[1]);
			dup2(pfd[0], STDIN_FILENO);
		}
	}
	execute_cmd(cmdlist[i]);
}

void execute_cmd(char* cmd) {

	cmd_redirect(cmd);

	if (makeargv(cmd, " \t", cmdvector, MAX_CMD_ARG) <= 0)
		fatal("makeargv error");

	execvp(cmdvector[0], cmdvector);
	fatal("exec error");
}


int makeargv(char *s, const char *delimiters, char** argvp, int MAX_LIST) {
	int i = 0;
	int numtokens = 0;
	char *snew = NULL;
	if ((s == NULL) || (delimiters == NULL)) return -1;

	snew = s + strspn(s, delimiters);
	if ((argvp[numtokens] = strtok(snew, delimiters)) == NULL)
		return numtokens;
	numtokens = 1;

	while (1) {
		if ((argvp[numtokens] = strtok(NULL, delimiters)) == NULL)
			break;
		if (numtokens == (MAX_LIST - 1)) return -1;

		numtokens++;
	}
	return numtokens;
}
