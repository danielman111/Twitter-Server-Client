/*
 * ReceiptFrame.h
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#ifndef RECEIPTFRAME_H_
#define RECEIPTFRAME_H_
#include "StompFrame.h"
#include <string>
#include <vector>

using std::string;
using std::vector;

class ReceiptFrame: public StompFrame {
public:
	ReceiptFrame(string& wholeMessage);
	virtual ~ReceiptFrame();
	const string& getSTOMPType();
	int getId();

private:
	int id;
	int parseIdFromFrame(string& wholeMessage);
};

#endif /* RECEIPTFRAME_H_ */
