/*
 * Client.cpp
 *
 *  Created on: Jan 11, 2014
 *      Author: daniel
 */
#include "../include/Communication/connectionHandler.h"
#include "../include/STOMP/BuildStompFrames.h"
#include "../include/Communication/ClientTheard.h"
#include "../include/STOMP/StompTokenizer.h"
#include "../include/STOMP/StompFrame.h"
#include "../include/STOMP/UserTokenizer.h"

#include <string>
#include <vector>
#include <stdlib.h>
#include <boost/locale.hpp>
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

int main (int argc, char *argv[]) {
	ClientTheard* t= new ClientTheard();
	t->run();
	delete t;

}


