/*
 * ReceiptFrame.cpp
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#include "../../include/STOMP/ReceiptFrame.h"
#include <string>
#include <vector>
#include <stdlib.h>     /* atoi */

using std::string;
using std::vector;

ReceiptFrame::ReceiptFrame(string& wholeMessage) {
	this->_headers= StompFrame::parseHeaders(wholeMessage);
	this->_body= StompFrame::parseBody(wholeMessage);
	this->id= parseIdFromFrame(wholeMessage);
}

ReceiptFrame::~ReceiptFrame() {
	// TODO Auto-generated destructor stub
}

const string& ReceiptFrame::getSTOMPType(){
	return "RECEIPT";
}

int ReceiptFrame::parseIdFromFrame(string& wholeMessage){
	string stompMessage= wholeMessage;
	int idLocation= stompMessage.find("id:");
	stompMessage= stompMessage.substr(idLocation+3);
	int endOfLineLocation= stompMessage.find("\n");
	stompMessage= stompMessage.substr(0, endOfLineLocation);
	int id= atoi(stompMessage.c_str());
	return id;
}

int ReceiptFrame::getId(){
	return this->id;
}




