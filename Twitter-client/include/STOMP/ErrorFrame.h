/*
 * ErrorFrame.h
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#ifndef ERRORFRAME_H_
#define ERRORFRAME_H_
#include "StompFrame.h"
#include <string>
#include <vector>

using namespace std;

class ErrorFrame: public StompFrame {
public:
	ErrorFrame(string& wholeMessage);
	virtual ~ErrorFrame();
	const string& getSTOMPType();

};

#endif /* ERRORFRAME_H_ */
