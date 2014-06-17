/*
 * BuildStompFrames.h
 *
 *  Created on: Jan 10, 2014
 *      Author: daniel
 */

#ifndef BUILDSTOMPFRAMES_H_
#define BUILDSTOMPFRAMES_H_
#include "StompFrame.h"
#include <sstream>
#include <string>
#include <vector>
#include <stdlib.h>     /* atoi */


using namespace std;


class BuildStompFrames {
public:
	BuildStompFrames();
	virtual ~BuildStompFrames();

	static string* buildConnectFrame(const string& hostPort,const string& hostIp, string& username, const string& password){
		stringstream connectFrame;
		connectFrame <<"CONNECT" << endl;
		connectFrame<< "accept-version:1.2" << endl;
		connectFrame<< "host:" << hostIp << endl;
		connectFrame<< "login:" << username << endl;
		connectFrame<< "passcode:" << password << endl << endl;
		connectFrame<< StompFrame::stompFrameDelimiter;
		string fullConnectFrame= connectFrame.str();
		fullConnectFrame= fullConnectFrame.substr(0, fullConnectFrame.length() -1);
		return new string(fullConnectFrame);
	}

	static string* buildSubscribeFrame(const string& followUserName, int id){
		stringstream subscribeFrame;
		subscribeFrame <<"SUBSCRIBE" << endl;
		subscribeFrame<< "destination:/topic/" << followUserName << endl;
		subscribeFrame<< "id:" << id << endl << endl ;
		subscribeFrame<< StompFrame::stompFrameDelimiter;;
		string fullSubscribeFrame= subscribeFrame.str();
		fullSubscribeFrame= fullSubscribeFrame.substr(0, fullSubscribeFrame.length() -1);
		return new string(fullSubscribeFrame);
	}

	static string* buildUnSubscribeFrame(const string& followusername, int id){/////see if can find id inside the function
		stringstream unSubscribeFrame;
		unSubscribeFrame <<"UNSUBSCRIBE" << endl;
		unSubscribeFrame<< "id:" << id << endl << endl ;
		unSubscribeFrame<< StompFrame::stompFrameDelimiter;;
		string fullUnSubscribeFrame= unSubscribeFrame.str();
		fullUnSubscribeFrame= fullUnSubscribeFrame.substr(0, fullUnSubscribeFrame.length() -1);
		return new string(fullUnSubscribeFrame);
	}

	static string* buildSendFrame(string &message, string &thisUser){
		stringstream sendFrame;
		sendFrame <<"SEND" << endl;
		sendFrame<< "destination:/topic/" << thisUser << endl<< endl;
		sendFrame<< message << endl<< endl;
		sendFrame<< StompFrame::stompFrameDelimiter;;
		string fullSendFrame= sendFrame.str();
		fullSendFrame= fullSendFrame.substr(0, fullSendFrame.length() -1);
		return new string(fullSendFrame);
	}

	static string* buildStopSendFrame(){
		stringstream sendFrame;
		sendFrame <<"SEND" << endl;
		sendFrame<< "destination:/topic/server"<< endl<< endl;
		sendFrame<< "stop";
		sendFrame<< StompFrame::stompFrameDelimiter;
		string fullSendFrame= sendFrame.str();
		fullSendFrame= fullSendFrame.substr(0, fullSendFrame.length() -1);
		return new string(fullSendFrame);
	}

	static string* buildClientsSendFrame(){
		stringstream sendFrame;
		sendFrame <<"SEND" << endl;
		sendFrame<< "destination:/topic/server" << endl<< endl;
		sendFrame<< "clients";
		sendFrame<< StompFrame::stompFrameDelimiter;;
		string fullSendFrame= sendFrame.str();
		fullSendFrame= fullSendFrame.substr(0, fullSendFrame.length() -1);
		return new string(fullSendFrame);
	}

	static string* buildClientsOnlineSendFrame(){
		stringstream sendFrame;
		sendFrame <<"SEND" << endl;
		sendFrame<< "destination:/topic/server" << endl<< endl;
		sendFrame<< "clients online";
		sendFrame<< StompFrame::stompFrameDelimiter;;
		string fullSendFrame= sendFrame.str();
		fullSendFrame= fullSendFrame.substr(0, fullSendFrame.length() -1);
		return new string(fullSendFrame);
	}


	static string* buildStatsSendFrame(){
		stringstream sendFrame;
		sendFrame <<"SEND" << endl;
		sendFrame<< "destination:/topic/server" << endl<< endl;
		sendFrame<< "stats";
		sendFrame<< StompFrame::stompFrameDelimiter;;
		string fullSendFrame= sendFrame.str();
		fullSendFrame= fullSendFrame.substr(0, fullSendFrame.length() -1);
		return new string(fullSendFrame);
	}

	static string* buildLogOutDisconnectFrame(int id){
		stringstream disconnectedFrame;
		disconnectedFrame <<"DISCONNECT" << endl;
		disconnectedFrame<< "receipt:" << id << endl<< endl;
		disconnectedFrame<< StompFrame::stompFrameDelimiter;;
		string fulldisconnectedFrame= disconnectedFrame.str();
		fulldisconnectedFrame= fulldisconnectedFrame.substr(0, fulldisconnectedFrame.length() -1);
		return new string(fulldisconnectedFrame);
	}

};

#endif /* BUILDSTOMPFRAMES_H_ */
