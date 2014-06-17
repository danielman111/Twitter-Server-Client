/*
 * Logging.cpp
 *
 *  Created on: Jan 14, 2014
 *      Author: gal
 */

#include "../../include/Communication/Logging.h"
#include <sstream>

using std::stringstream;

Logging::Logging(string& filename){
	_filename= new string(filename);
	stringstream filePath;
	filePath << *_filename << ".html";
	_file.open(filePath.str().c_str());
	stringstream text;
	text << "<!DOCTYPE html>" << std::endl;
	text << "<html>" << std::endl;
	text << "<head>" << std::endl;
	text << "<title>" << filename << "</title>" << std::endl;
	text << "</head>" << std::endl;
	text << "<body>" << std::endl;
	text << "<table>" << std::endl;
	_file << text.str();
}

Logging::~Logging() {
	stringstream filePath;
	filePath << *_filename << ".html";
	stringstream text;
	text << "</table>" << std::endl;
	text << "</body>" << std::endl;
	text << "</html>" << std::endl;
	_file << text.str();
	_file.close();
}

void Logging::writeToFile(string& message){
	string newmessage= message.substr(0, message.size()-2);
	stringstream filePath;
	filePath << *_filename << ".html";
	_file << "<tr> <td>\n"  + newmessage + "\n</td> </tr>" +"\n";

}


Logging & Logging::operator=(const Logging &log) //frame go into this
{
  // check for "self assignment" and do nothing in that case
  if (this == &log) {
    return *this;
  }
  return *this;
}

/**
 * Copy Constructor:deep copy of frame
 */
Logging::Logging(const Logging &log)
{
}


