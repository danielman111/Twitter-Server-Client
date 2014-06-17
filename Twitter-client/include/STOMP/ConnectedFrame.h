/*
 * MessageFrame.h
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#ifndef ConnectedFrame_H_
#define ConnectedFrame_H_
#include "StompFrame.h"
#include <string>
#include <vector>

using namespace std;

class ConnectedFrame: public StompFrame {

public:
	ConnectedFrame(string& wholeMessage);
	virtual ~ConnectedFrame();
	const string& getSTOMPType();



};

#endif /* ConnectedFrame_H_ */
