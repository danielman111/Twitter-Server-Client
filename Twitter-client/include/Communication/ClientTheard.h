/*
 * ClientTheard.h
 *
 *  Created on: Jan 12, 2014
 *      Author: daniel
 */

#ifndef CLIENTTHEARD_H_
#define CLIENTTHEARD_H_
#include "connectionHandler.h"
#include "../STOMP/BuildStompFrames.h"
#include "../STOMP/StompTokenizer.h"
#include "../STOMP/StompFrame.h"
#include "../STOMP/UserTokenizer.h"
#include "../UsersInformation/UserDatabase.h"
#include "../UsersInformation/user.h"
#include <string>
#include <vector>
#include <stdlib.h>
#include <boost/locale.hpp>
#include "Logging.h"

using namespace std;

class ClientTheard {
public:
	ClientTheard();
	virtual ~ClientTheard();
	ClientTheard& operator=(const ClientTheard &clientTheard);
	ClientTheard(const ClientTheard &clientTheard);
	void run();


private:
	bool loginSuccess;
	bool serverListener;
	bool exitClient;
	ConnectionHandler *connectionHandler;
	boost::thread *aServerListener;
	UserDatabase userDatabase;
	bool startNewSession;
	string thisUserName;
	int disconnectedID;
	void ServerListener();
	int id;
	int generateId();
	Logging *userLogger;

};

#endif /* CLIENTTHEARD_H_ */
