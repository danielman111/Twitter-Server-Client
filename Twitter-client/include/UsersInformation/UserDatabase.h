/*
 * UserDatabase.h
 *
 *  Created on: Jan 10, 2014
 *      Author: daniel
 */

#ifndef USERDATABASE_H_
#define USERDATABASE_H_

#include "user.h"
#include <string>
#include <vector>

using namespace std;


class UserDatabase {
public:
	UserDatabase();
	virtual ~UserDatabase();
	void addUser(string& username);
	////////////////remove?
	user* getUser(string& username);
	bool checkIfUserExists(string &username);
	bool checkIfUserAllreadyFollowing(string& thisUsername, string& userToFollow);
	void AddNewUserToFollow(string& thisUsername, string& userToFollow, int subsribeId);
	int getFollowId(string& username, string& usernameIFollow);
	void removeUserIfollow(string& username, string& usernameToUnFollow);
	UserDatabase& operator=(const UserDatabase &userDatabase);
	UserDatabase(const UserDatabase &userDatabase);

private:
	vector<user *> *_users;


};

#endif /* USERDATABASE_H_ */
