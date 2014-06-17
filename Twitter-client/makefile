CC = g++
CFLAGS  = -g -Wall -Weffc++
LFLAGS = -L/usr/include -L/usr/local/include -lpthread -lboost_system -lboost_filesystem -lboost_thread
IFLAGS = -I/usr/local/include -I/usr/include
OFILES = bin/Client.o bin/ClientTheard.o bin/connectionHandler.o bin/Logging.o bin/ConnectedFrame.o bin/ErrorFrame.o bin/MessageFrame.o bin/ReceiptFrame.o bin/StompFrame.o bin/user.o bin/UserDatabase.o

# All Targets
all: runFile


runFile: $(OFILES)
	$(CC) -o bin/runFile $(OFILES) $(LFLAGS)

bin/Client.o: src/Client.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/Client.o src/Client.cpp

bin/ClientTheard.o: src/Communication/ClientTheard.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/ClientTheard.o src/Communication/ClientTheard.cpp

bin/connectionHandler.o: src/Communication/connectionHandler.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/connectionHandler.o src/Communication/connectionHandler.cpp    

bin/Logging.o: src/Communication/Logging.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/Logging.o src/Communication/Logging.cpp

bin/ConnectedFrame.o: src/STOMP/ConnectedFrame.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/ConnectedFrame.o src/STOMP/ConnectedFrame.cpp

bin/ErrorFrame.o: src/STOMP/ErrorFrame.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/ErrorFrame.o src/STOMP/ErrorFrame.cpp 

bin/MessageFrame.o: src/STOMP/MessageFrame.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/MessageFrame.o src/STOMP/MessageFrame.cpp

bin/ReceiptFrame.o: src/STOMP/ReceiptFrame.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/ReceiptFrame.o src/STOMP/ReceiptFrame.cpp 	

bin/StompFrame.o: src/STOMP/StompFrame.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/StompFrame.o src/STOMP/StompFrame.cpp
	
bin/StompTokenizer.o: src/STOMP/StompTokenizer.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/StompTokenizer.o src/STOMP/StompTokenizer.cpp
	
bin/UserTokenizer.o: src/STOMP/UserTokenizer.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/UserTokenizer.o src/STOMP/UserTokenizer.cpp
	
bin/user.o: src/UsersInformation/user.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/user.o src/UsersInformation/user.cpp
	
bin/UserDatabase.o: src/UsersInformation/UserDatabase.cpp
	$(CC) $(CFLAGS) $(IFLAGS) -c -Linclude -o bin/UserDatabase.o src/UsersInformation/UserDatabase.cpp

#Clean the build directory
clean:
	rm -f bin/*
