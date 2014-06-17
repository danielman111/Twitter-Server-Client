/*
 * StompTokenizer.h
 *
 *  Created on: Jan 11, 2014
 *      Author: daniel
 */

#ifndef STOMPTOKENIZER_H_
#define STOMPTOKENIZER_H_
#include "StompFrame.h"
#include "ReceiptFrame.h"
#include "ConnectedFrame.h"
#include "ErrorFrame.h"
#include "MessageFrame.h"
#include "ReceiptFrame.h"
#include <iostream>

#include <string>
#include <vector>

using namespace std;

class StompTokenizer {
public:
	StompTokenizer();
	virtual ~StompTokenizer();

	static void printErrorMessage(string &ErrorString){
		int endlineLocation= ErrorString.find(StompFrame::stompFrameNewlineDelimiter);
		string Workingstring(ErrorString);
		string StompType= Workingstring.substr(0, endlineLocation);
		Workingstring= Workingstring.substr(endlineLocation + 1);
		cout <<Workingstring<<endl;


	}
	static string getStompFrame(string &frameInString){
		int endlineLocation= frameInString.find(StompFrame::stompFrameNewlineDelimiter);
		string Workingstring(frameInString);
		string StompType= Workingstring.substr(0, endlineLocation);
		Workingstring= Workingstring.substr(endlineLocation + 1);
		if(StompType == "CONNECTED"){
			return "CONNECTED";
		}
		else if(StompType == "ERROR"){
			return "ERROR";

		}
		else if(StompType == "MESSAGE"){
			return "MESSAGE";

		}
		else if(StompType == "RECEIPT"){
			return "RECEIPT";

		}
		return "NOTHING";
	}


	static StompFrame* createStomp(string &frameInString){
		int endlineLocation= frameInString.find(StompFrame::stompFrameNewlineDelimiter);
		string Workingstring(frameInString);
		string StompType= Workingstring.substr(0, endlineLocation);
		Workingstring= Workingstring.substr(endlineLocation + 1);
		if(StompType == "CONNECTED"){
			StompFrame* connectFrame= new ConnectedFrame(Workingstring);


			return connectFrame;
		}
		else if(StompType == "ERROR"){
			StompFrame* errorFrame= new ErrorFrame(Workingstring);
			return errorFrame;
		}
		else if(StompType == "MESSAGE"){
			StompFrame* messageFrame= new MessageFrame(Workingstring);
			return messageFrame;
		}
		else if(StompType == "RECEIPT"){
			StompFrame* receiptFrame= new ReceiptFrame(Workingstring);
			return receiptFrame;
		}
		cout <<"could not receive properly"<<endl;
		return 0;
	}
};

#endif /* STOMPTOKENIZER_H_ */
