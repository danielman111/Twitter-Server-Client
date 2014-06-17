/*
 * UserTokenizer.h
 *
 *  Created on: Jan 11, 2014
 *      Author: daniel
 */

#ifndef USERTOKENIZER_H_
#define USERTOKENIZER_H_

#include "StompTokenizer.h"
#include "ConnectedFrame.h"
#include "ErrorFrame.h"
#include "MessageFrame.h"
#include "ReceiptFrame.h"
#include <iostream>
#include <boost/thread.hpp>
#include <string>
#include <vector>

using namespace std;


class UserTokenizer {
public:
	UserTokenizer();
	virtual ~UserTokenizer();
	static string getLineFromUser(){
	        const short bufsize = 1024;
	        char buf[bufsize];
	        cin.getline(buf, bufsize);
	        string line(buf);
	        return line;
	}

	static string getFirstWord(string& line){
		int delimiter= line.find(" ");
		string firstWord= line.substr(0, delimiter);
		line= line.substr(delimiter + 1);
		return firstWord;
	}


	static bool checkDoubleSpace(string& line){
		int lineSize= line.size();
		bool doubleSpace= false;
		for(int i=1; ((i < lineSize) && (!doubleSpace)); i++){
			if((line.at(i)) && (line.at(i-1))){
				doubleSpace= true;
			}
		}
		if((line.at(0)) || (line.at(line.at(line.size()-1)))){
			doubleSpace= true;
		}
		return doubleSpace;
	}

	static int getNumberOfWords(string& line){
		int numOfWords= 1;
		int lineSize= line.size();
		for(int i=0; i < lineSize; i++){
			if(line.at(i) == ' '){
				numOfWords++;
			}
		}
		if(line == "" || line == " "){
			numOfWords=0;
		}
		return numOfWords;
	}

	static int getLogoutId(string& line){
		int firstWordDelimiter= line.find(":");
		string message= line.substr(firstWordDelimiter + 1);
		int recieptId= std::atoi(message.c_str());
		return recieptId;
	}


	static void getUserLogin(string& line, string& hostIp, string& hostPort, string& username, string& password){
		int firstWordDelimiter= line.find(" ");
		hostIp= line.substr(0, firstWordDelimiter);
		line= line.substr(firstWordDelimiter + 1);
		int secondWordDelimiter= line.find(" ");
		hostPort= line.substr(0, secondWordDelimiter);
		line= line.substr(secondWordDelimiter + 1);
		int thirdWordDelimiter= line.find(" ");
		username= line.substr(0, thirdWordDelimiter);
		password= line.substr(thirdWordDelimiter + 1);
	}

	static void getUserFollowOrUnFollow(string &line, string& username){
		int firstWordDelimiter= line.find(" ");
		username= line.substr(firstWordDelimiter + 1);
	}

	static string getUserMessage(string &line){
		int firstWordDelimiter= line.find(":");
		string message= line.substr(firstWordDelimiter + 1);
		int secondDelimiter= line.find("\n");
		string answer= line.substr(0,secondDelimiter);
		return answer;
	}


	static string getUserToFollow(string &line){
		string *templine= new string(line);
		string userToFollow="";
		if (line.find("following") != std::string::npos) {
			int firstWordDelimiter= templine->find("following");
			userToFollow= templine->substr(firstWordDelimiter + 10);
			delete templine;
		}
		return userToFollow;
	}

};


#endif /* USERTOKENIZER_H_ */
