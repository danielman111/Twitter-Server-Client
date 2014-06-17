/*
 * MessageFrame.h
 *
 *  Created on: Jan 11, 2014
 *      Author: daniel
 */

#ifndef MESSAGEFRAME_H_
#define MESSAGEFRAME_H_
#include "StompFrame.h"
#include <string>
#include <vector>

using std::string;
using std::vector;

class MessageFrame : public StompFrame  {
public:
	MessageFrame(string& wholeMessage);
	virtual ~MessageFrame();
	const string& getSTOMPType();

};

#endif /* MESSAGEFRAME_H_ */
