/*
 * MessageFrame.cpp
 *
 *  Created on: Jan 11, 2014
 *      Author: daniel
 */

#include "../../include/STOMP/MessageFrame.h"
#include "../../include/STOMP/StompFrame.h"
#include <string>
#include <vector>

using std::string;
using std::vector;



MessageFrame::MessageFrame(string& wholeMessage) {
	this->_headers= StompFrame::parseHeaders(wholeMessage);
	this->_body= StompFrame::parseBody(wholeMessage);

}

MessageFrame::~MessageFrame() {
	// TODO Auto-generated destructor stub
}

const string& MessageFrame::getSTOMPType(){
	return "MESSAGE";
}

