/*
 * StompFrame.cpp
 *
 *  Created on: Jan 3, 2014
 *      Author: daniel
 */

#include "../../include/STOMP/StompFrame.h"
#include <string>
#include <vector>
#include <iostream>

using std::string;
using std::vector;

StompFrame::StompFrame(): _headers(), _body() {
	// TODO Auto-generated constructor stub

}

StompFrame::~StompFrame() {
	delete _headers;
	delete _body;
}

vector<string>* StompFrame::getHeaders(){
	return new vector<string> (*_headers);

}

const string* StompFrame::getBody(){
	return new string(*_body);
}


string StompFrame::toString(){
	string ans;
	ans.append(getSTOMPType());
	ans.append("" + StompFrame::stompFrameNewlineDelimiter);
	for(int i=0; i < _headers->size(); i++){
		ans.append(this->_headers->at(i));
		ans.append("" + StompFrame::stompFrameNewlineDelimiter);
	}
	string *temp= this->_body;
	ans.append(*temp);
	delete temp;
	return ans;
}


/**
 * Assignment Operator
 */
StompFrame & StompFrame::operator=(const StompFrame &frame) //frame go into this
{
  // check for "self assignment" and do nothing in that case
  if (this == &frame) {
    return *this;
  }
  this->_headers->clear();
  int size= frame._headers->size();
  for(int i=0; i < size; i++){
	  this->_headers->push_back(frame._headers->at(i));
  }
  this->_body= frame._body;
  return *this;
}

/**
 * Copy Constructor:deep copy of frame
 */
StompFrame::StompFrame(const StompFrame &frame) : _headers(), _body()
{
	int size= frame._headers->size();
	for(int i=0; i < size; i++){
	  this->_headers->push_back(frame._headers->at(i));
	}
	this->_body= frame._body;
}

