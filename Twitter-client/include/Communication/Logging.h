/*
 * Logging.h
 *
 *  Created on: Jan 14, 2014
 *      Author: gal
 */

#ifndef LOGGING_H_
#define LOGGING_H_

#include <iostream>
#include <fstream>

using std::string;
using std::ofstream;

class Logging {
public:
	Logging(string& filename);
	virtual ~Logging();
	void writeToFile(string& message);
	Logging& operator=(const Logging &log);
	Logging(const Logging &log);


private:
	string* _filename;
	ofstream _file;
};

#endif /* LOGGING_H_ */
