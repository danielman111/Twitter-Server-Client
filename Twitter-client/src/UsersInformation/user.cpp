/*
 * user.cpp
 *
 *  Created on: Jan 10, 2014
 *      Author: daniel
 */

#include "../../include/UsersInformation/user.h"
#include <string>
#include <vector>
#include <iostream>
#include <boost/thread.hpp>
using namespace std;


user::user(string& username): _username(username),_following(new vector<string>),_followingId(new vector<int>)
{
}


user::~user() {
	delete _following;
	delete _followingId;
}

string user::getUserName () const{
	return (this->_username);
}


int user::findFollowingUserLocation(const string& userIfollow){
	bool found= false;
	int loc=-1;
	int numOfFollowUsers= this->_following->size();
	for (int i= 0; ((i < numOfFollowUsers) && (!found)); i++) {
		string tempFollowUser= this->_following->at(i);
		tempFollowUser=tempFollowUser.substr(0, tempFollowUser.size()-2);
		if(tempFollowUser == userIfollow){
			loc= i;
			found= true;
		}
	}
	if(found)
		return loc;
	else
		return NOT_FOUND;
}

void user::addFollowUser(string &toFollowUsername, int id){
	this->_following->push_back(toFollowUsername);
	this->_followingId->push_back(id);
}

void user::removeFollowUser(string &toRemoveFollowUsername){
	int removeLocation= this->findFollowingUserLocation(toRemoveFollowUsername);
	this->_following->erase(this->_following->begin() + removeLocation);
	this->_followingId->erase(this->_followingId->begin() + removeLocation);
}

vector<int> * user::getFollowingId(){
	return this->_followingId;
}

user & user::operator=(const user &User) //frame go into this
{
  // check for "self assignment" and do nothing in that case
  if (this == &User) {
    return *this;
  }
  this->_following->clear();
  this->_followingId->clear();
  int sizeFollowing= User._following->size();
  int sizeFollowingId= User._followingId->size();

  for(int i=0; i < sizeFollowing; i++){
	  this->_following->push_back(User._following->at(i));
  }
  for(int i=0; i < sizeFollowingId; i++){
	  this->_followingId->push_back(User._followingId->at(i));
  }
  this->_username= User._username;

  return *this;
}

/**
 * Copy Constructor:deep copy of frame
 */
user::user(const user &User) : _username(), _following(new vector<string>),_followingId(new vector<int>)
{
	  int sizeFollowing= User._following->size();
	  int sizeFollowingId= User._followingId->size();

	  for(int i=0; i < sizeFollowing; i++){
		  this->_following->push_back(User._following->at(i));
	  }
	  for(int i=0; i < sizeFollowingId; i++){
		  this->_followingId->push_back(User._followingId->at(i));
	  }
	  this->_username= User._username;
}





