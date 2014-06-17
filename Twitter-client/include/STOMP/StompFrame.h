/*
 * StompFrame.h
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#ifndef STOMPFRAME_H_
#define STOMPFRAME_H_
#include <string>
#include <vector>
#include <iostream>


using namespace std;

class StompFrame {
public:
	StompFrame();
	virtual ~StompFrame();
	vector<string>* getHeaders();
	const string* getBody();
	virtual const string& getSTOMPType()=0;
	static const char stompFrameDelimiter='\0';
	static const char stompFrameNewlineDelimiter='\n';
	string toString();
	StompFrame& operator=(const StompFrame &frame);
	StompFrame(const StompFrame &frame);

protected:

	vector<string>* _headers;
	string* _body;

	static vector<string>* parseHeaders(string &wholeMessage) {
		string stompMessage= wholeMessage;
		vector<string>* headers= new vector<string>();
		int endlineLocation= stompMessage.find('\n');
		string tempmessage= stompMessage.substr(0, endlineLocation);
		stompMessage= stompMessage.substr(endlineLocation + 1);

		while (tempmessage != "") {
			headers->push_back(tempmessage);
			endlineLocation= stompMessage.find('\n');
			tempmessage= stompMessage.substr(0, endlineLocation);
			stompMessage= stompMessage.substr(endlineLocation + 1);

		}

		return headers;
	}

	static string* parseBody(string &wholeMessage) {
		string* body= new string();
		string stompMessage= wholeMessage;
		string oneLineOfMessage;
		while (stompMessage != "") {
			int endlineLocation= stompMessage.find('\n');
			oneLineOfMessage= stompMessage.substr(0, endlineLocation);
			stompMessage= stompMessage.substr(endlineLocation + 1);
			if (oneLineOfMessage == "")
				break;
		}
		*body= stompMessage;
		return body;
	}


};

#endif /* STOMPFRAME_H_ */
