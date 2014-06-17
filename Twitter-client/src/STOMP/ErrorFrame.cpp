/*
 * ErrorFrame.cpp
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#include "../../include/STOMP/ErrorFrame.h"
#include <string>
#include <vector>

using namespace std;

ErrorFrame::ErrorFrame(string& wholeMessage) {
	this->_headers= StompFrame::parseHeaders(wholeMessage);
	this->_body= StompFrame::parseBody(wholeMessage);

}

ErrorFrame::~ErrorFrame() {
	// TODO Auto-generated destructor stub
}

const string& ErrorFrame::getSTOMPType(){
	return "ERROR";

}
