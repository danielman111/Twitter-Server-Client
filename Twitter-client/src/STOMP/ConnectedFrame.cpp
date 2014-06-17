/*
 * MessageFrame.cpp
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#include "../../include/STOMP/ConnectedFrame.h"
#include <string>
#include <vector>

using std::string;
using std::vector;



ConnectedFrame::ConnectedFrame(string& wholeMessage) {
	this->_headers= StompFrame::parseHeaders(wholeMessage);
}

ConnectedFrame::~ConnectedFrame() {
	// TODO Auto-generated destructor stub
}


const string& ConnectedFrame::getSTOMPType(){
	return "CONNECTED";
}
