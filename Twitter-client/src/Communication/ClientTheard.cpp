/*
 * ClientTheard.cpp
 *
 *  Created on: Jan 12, 2014
 *      Author: daniel
 */

#include "../../include/Communication/ClientTheard.h"
#include <string>
#include <vector>
#include <stdlib.h>
#include <boost/locale.hpp>
#include <boost/thread.hpp>
using namespace std;

ClientTheard::ClientTheard(): connectionHandler(),aServerListener(), userDatabase(), thisUserName() {
	loginSuccess= false;
	startNewSession= true;
	exitClient= false;
	id=1;
}

ClientTheard::~ClientTheard() {
	delete connectionHandler;
}



void ClientTheard::run(){
	while(((this->loginSuccess == false) && (this->startNewSession == true) && (this->exitClient == false))){
			std::string line= UserTokenizer::getLineFromUser();
			std::string firstWordFromUser= UserTokenizer::getFirstWord(line);
			if((UserTokenizer::getNumberOfWords(line) == 4) && (firstWordFromUser == "login")){
				std::string hostIp;
				std::string portAsString;
				std::string username;
				std::string password;
				cout<<hostIp<<endl;
				cout<<portAsString<<endl;
				cout<<username<<endl;
				cout<<password<<endl;
				UserTokenizer::getUserLogin(line ,hostIp, portAsString, username, password);
				////////
				int hostPort =std::atoi(portAsString.c_str());//////////////////////do it as short
				connectionHandler= new ConnectionHandler(hostIp, hostPort);


			    if (!connectionHandler->connect()) {
			        std::cerr << "Cannot connect to " << hostIp << ":" << hostPort << std::endl;
			    }
			    else{//connected with connection handler
			    	string *connectFrame= BuildStompFrames::buildConnectFrame(portAsString, hostIp, username, password);
			    	if(!connectionHandler->sendFrameAscii(*connectFrame, StompFrame::stompFrameDelimiter)){
			             std::cerr << "connect frame was not sent successfully" << std::endl;
			            delete connectFrame;
			    	}
			    	else{//send connection frame was successfull, wait for connect frame from the server
			    		string serverFrameRecieved= "";
			    		//connectionHandler->getFrameAscii(serverFrameRecieved, StompFrame::stompFrameDelimiter);
			    		if(!(connectionHandler->getFrameAscii(serverFrameRecieved, StompFrame::stompFrameDelimiter))){
				            std::cerr << "error while receiving from server" << std::endl;
			    		}
			    		else{//Received a StopmFrame from the sever
			    			if((StompTokenizer::getStompFrame(serverFrameRecieved)) == "CONNECTED"){
			    				loginSuccess= true;
				    			cout<<serverFrameRecieved<<endl;
			    				startNewSession= false;
			    				this->thisUserName= username;
								this->userDatabase.addUser(username);
								this->userLogger= new Logging(this->thisUserName);
								aServerListener= new boost::thread(&ClientTheard::ServerListener, this);//after connected is true

			    			}
			    			else if(StompTokenizer::getStompFrame(serverFrameRecieved) == "ERROR"){//got error frame
					            StompTokenizer::printErrorMessage(serverFrameRecieved);
			    			}
			    		}

			    	}

			    }
			}
			else if(firstWordFromUser == "exit_client"){//works
				this->loginSuccess=false;
				this->startNewSession= true;
				this->exitClient= true;
			}
			else if(loginSuccess == false){
				std::cerr << "please use the proper login command"<<endl;
				delete this->connectionHandler;

			}
		// end login while
		while((loginSuccess) && (!startNewSession)){
			std::string line= UserTokenizer::getLineFromUser();
			std::string lineCopy= line;
			std::string firstWordFromUser= UserTokenizer::getFirstWord(line);
			if(firstWordFromUser == "follow"){
				if((UserTokenizer::getNumberOfWords(line) == 1)){
					std::string usernameToFollow;
					UserTokenizer::getUserFollowOrUnFollow(line, usernameToFollow);
					string *subscribeFrame= BuildStompFrames::buildSubscribeFrame(usernameToFollow, this->generateId());
					if(!connectionHandler->sendFrameAscii(*subscribeFrame, StompFrame::stompFrameDelimiter)){
						std::cerr << "subscribe frame was not sent"<<endl;
					}
					delete subscribeFrame;
				}
				else{
					std::cerr << "please use the proper follow command"<<endl;
				}

			}
			else if(firstWordFromUser == "unfollow"){
				if((UserTokenizer::getNumberOfWords(line) == 1)){
					std::string usernameToUnFollow;
					UserTokenizer::getUserFollowOrUnFollow(line, usernameToUnFollow);
					if(this->userDatabase.checkIfUserAllreadyFollowing(this->thisUserName, usernameToUnFollow)){
						int newId= this->userDatabase.getFollowId(this->thisUserName, usernameToUnFollow);
						this->userDatabase.removeUserIfollow(this->thisUserName, usernameToUnFollow);
						string *unsubscribeFrame= BuildStompFrames::buildUnSubscribeFrame(usernameToUnFollow, newId);
						if(!connectionHandler->sendFrameAscii(*unsubscribeFrame, StompFrame::stompFrameDelimiter)){
							std::cerr << "unsubscribe frame was not sent"<<endl;
						}
						delete unsubscribeFrame;
					}
					else{
						std::cerr << "you are not following this user!"<<endl;
					}
				}
				else{
					std::cerr << "please use the proper unfollow command"<<endl;
				}
			}
			else if(firstWordFromUser == "tweet"){//works
				string message= line;
				string tempUserName= thisUserName;
				string *tweetSendFrame= BuildStompFrames::buildSendFrame(message, tempUserName);
				if(!connectionHandler->sendFrameAscii(*tweetSendFrame, StompFrame::stompFrameDelimiter)){
					std::cerr << "tweet frame was not sent"<<endl;
				}
				this->userLogger->writeToFile(*tweetSendFrame);
				delete tweetSendFrame;
			}
			else if(firstWordFromUser == "clients"){//works
				if((UserTokenizer::getNumberOfWords(lineCopy) == 1)){
					string clients= "clients";
					string server= "server";
					string *clientsSendFrame= BuildStompFrames::buildSendFrame(clients, server);
					if(!connectionHandler->sendFrameAscii(*clientsSendFrame, StompFrame::stompFrameDelimiter)){
						std::cerr << "message clients frame was not sent"<<endl;
					}
					delete clientsSendFrame;
				}
				else if((UserTokenizer::getNumberOfWords(lineCopy) == 2)){
					string secondWordFromUser = UserTokenizer::getFirstWord(line);
					if(secondWordFromUser == "online"){//works
						string clients= "clients online";
						string server= "server";
						string *clientsSendFrame= BuildStompFrames::buildSendFrame(clients, server);
						if(!connectionHandler->sendFrameAscii(*clientsSendFrame, StompFrame::stompFrameDelimiter)){
							std::cerr << "message clients was not sent"<<endl;
						}
						std::cerr << "clients frame was sent" << *clientsSendFrame<<endl;
						delete clientsSendFrame;
					}
					else{
						std::cerr << "please use the proper clients command"<<endl;
					}
				}
				else{
					std::cerr << "please use the proper clients command"<<endl;
				}
			}
			else if((firstWordFromUser == "stats") && (UserTokenizer::getNumberOfWords(line) == 1)){//works
				string stats= "stats";
				string server= "server";
				string *statsSendFrame= BuildStompFrames::buildSendFrame(stats, server);
				if(!connectionHandler->sendFrameAscii(*statsSendFrame, StompFrame::stompFrameDelimiter)){
					std::cerr << "message stats was not sent"<<endl;
				}
				delete statsSendFrame;
			}
			else if(firstWordFromUser == "logout"){//works
				this->disconnectedID= this->generateId();
				string *disconnectedFrame= BuildStompFrames::buildLogOutDisconnectFrame(disconnectedID);
				if(!connectionHandler->sendFrameAscii(*disconnectedFrame, StompFrame::stompFrameDelimiter)){
					std::cerr << "message logout was not sent"<<endl;
				}
				delete disconnectedFrame;
				this->loginSuccess=false;
				this->startNewSession= true;
				serverListener= false;
				delete this->userLogger;
			}
			else if(firstWordFromUser == "exit_client"){//works
				this->disconnectedID= this->generateId();
				string *disconnectedFrame= BuildStompFrames::buildLogOutDisconnectFrame(disconnectedID);
				if(!connectionHandler->sendFrameAscii(*disconnectedFrame, StompFrame::stompFrameDelimiter)){
					std::cerr << "message logout was not sent"<<endl;
				}
				delete disconnectedFrame;
				this->loginSuccess=false;
				this->startNewSession= true;
				this->exitClient= true;
				serverListener= false;
				delete this->userLogger;

			}
			else if(firstWordFromUser == "stop"){
				string stop= "stop";
				string *stopSendFrame= BuildStompFrames::buildStopSendFrame();
				if(!connectionHandler->sendFrameAscii(*stopSendFrame, StompFrame::stompFrameDelimiter)){
					std::cerr << "message clients frame was not sent"<<endl;
				}
				delete stopSendFrame;
			}
			else{
				std::cerr <<line<<endl;
				std::cerr << "please use the proper a command"<<endl;
			}
		}
	}
}



void ClientTheard::ServerListener(){
	while(!(boost::this_thread::interruption_requested())){
		string serverFrameRecieved;
		if(!connectionHandler->getFrameAscii(serverFrameRecieved, StompFrame::stompFrameDelimiter)){
            std::cout << "error while receiving from server" << std::endl;
            aServerListener->interrupt();
		}
		else{
			serverFrameRecieved=serverFrameRecieved.substr(1, serverFrameRecieved.size());
            std::cout << serverFrameRecieved << std::endl;
			if((StompTokenizer::getStompFrame(serverFrameRecieved)) == "RECEIPT"){
				int recieptId=UserTokenizer::getLogoutId(serverFrameRecieved);
				if(this->disconnectedID == recieptId){
					//reciept id was verified
					this->connectionHandler->close();
					this->aServerListener->interrupt();
				}
			}
			else if((StompTokenizer::getStompFrame(serverFrameRecieved)) == "MESSAGE"){
				if (serverFrameRecieved.find("following") != std::string::npos) {
				    string userToFollow= UserTokenizer::getUserToFollow(serverFrameRecieved);
				    this->userDatabase.AddNewUserToFollow(this->thisUserName, userToFollow, this->id-1);
				}
				else if(serverFrameRecieved.find("subscription") != std::string::npos){
					//got message
					this->userLogger->writeToFile(serverFrameRecieved);
				}
			}
		}
	}
}

int ClientTheard::generateId(){
	int idToReturn= this->id;
	id++;
	return idToReturn;
}



/**
 * Assignment Operator
 */
ClientTheard & ClientTheard::operator=(const ClientTheard &clientTheard) //frame go into this
{
  // check for "self assignment" and do nothing in that case
  if (this == &clientTheard) {
    return *this;
  }
	this->connectionHandler= clientTheard.connectionHandler;
	this->aServerListener= clientTheard.aServerListener;
	this->thisUserName= clientTheard.thisUserName;
	this->disconnectedID= clientTheard.disconnectedID;
	this->userDatabase= clientTheard.userDatabase;
	this->loginSuccess= clientTheard.loginSuccess;
	this->exitClient= clientTheard.exitClient;
	this->serverListener=clientTheard.serverListener;
	this->userLogger= clientTheard.userLogger;
	this->id=clientTheard.id;
	this->startNewSession= clientTheard.startNewSession;
  return *this;
}

/**
 * Copy Constructor:deep copy of frame
 */
ClientTheard::ClientTheard(const ClientTheard &clientTheard) : connectionHandler(),aServerListener(), thisUserName(), userDatabase()
{
	this->connectionHandler= clientTheard.connectionHandler;
	this->aServerListener= clientTheard.aServerListener;
	this->thisUserName= clientTheard.thisUserName;
	this->disconnectedID= clientTheard.disconnectedID;
	this->userDatabase= clientTheard.userDatabase;
	this->loginSuccess= clientTheard.loginSuccess;
	this->exitClient= clientTheard.exitClient;
	this->serverListener=clientTheard.serverListener;
	this->userLogger= clientTheard.userLogger;
	this->id=clientTheard.id;
	this->startNewSession= clientTheard.startNewSession;
}


