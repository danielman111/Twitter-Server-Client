/*
 * UserDatabase.cpp
 *
 *  Created on: Jan 10, 2014
 *      Author: daniel
 */

#include "../../include/UsersInformation/UserDatabase.h"
#include <string>
#include <vector>
#include <iostream>

using namespace std;

UserDatabase::UserDatabase(): _users(new vector<user *>)
{
}

UserDatabase::~UserDatabase() {
	int numOfUsers= this->_users->size();
	for(int i=0; i< numOfUsers; i++){
		delete this->_users->at(i);
	}
	delete this->_users;
}


void UserDatabase::addUser(string & username){
	if(!(checkIfUserExists(username))){
		user *newUser= new user(username);
		this->_users->push_back(newUser);
	}
	else
		cout<<"user " <<username<< " allready exits!";
}

user* UserDatabase::getUser(string &username){//need to check if user exits outside
	bool found= false;
	int userLocation=0;
	int numOfUsers= this->_users->size();
	for(int i=0; ((i < numOfUsers) && (!found)); i++){
		if((this->_users->at(i)->getUserName()) == username){
			found= true;
			userLocation= i;
		}
	}
	return this->_users->at(userLocation);
}



int UserDatabase::getFollowId(string& username, string& usernameIFollow){
	user *tempUser= getUser(username);
	int location= tempUser->findFollowingUserLocation(usernameIFollow);
	int id= tempUser->getFollowingId()->at(location);
	return id;
}




bool UserDatabase::checkIfUserExists(string &username){
	bool exists= false;
	int numOfUsers= this->_users->size();
	for(int i=0; ((i < numOfUsers) && (!exists)); i++){
		if((this->_users->at(i)->getUserName()) == username){

			exists= true;
		}
	}
	return exists;
}

bool UserDatabase::checkIfUserAllreadyFollowing(string& thisUsername, string& userToFollow){
	user *tempUser= getUser(thisUsername);
	bool ans= tempUser->findFollowingUserLocation(userToFollow) != tempUser->NOT_FOUND;
	return ans;
}

void UserDatabase::AddNewUserToFollow(string& thisUsername, string& userToFollow, int subsribeId){
	user *tempUser= getUser(thisUsername);
	tempUser->addFollowUser(userToFollow, subsribeId);
}



void UserDatabase::removeUserIfollow(string& username, string& usernameToUnFollow){
	user *tempUser= getUser(username);
	tempUser->removeFollowUser(usernameToUnFollow);
}

UserDatabase & UserDatabase::operator=(const UserDatabase &userDatabase) //frame go into this
{
  // check for "self assignment" and do nothing in that case
  if (this == &userDatabase) {
    return *this;
  }
  this->_users->clear();
  int size= userDatabase._users->size();

  for(int i=0; i < size; i++){
	  this->_users->push_back(userDatabase._users->at(i));
  }
  return *this;
}

/**
 * Copy Constructor:deep copy of frame
 */
UserDatabase::UserDatabase(const UserDatabase &userDatabase): _users(new vector<user *>)
{
	  int size= userDatabase._users->size();
	  for(int i=0; i < size; i++){
		  this->_users->push_back(userDatabase._users->at(i));
	  }
}




