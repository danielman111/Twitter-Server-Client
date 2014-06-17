/*
 * user.h
 *
 *  Created on: Jan 10, 2014
 *      Author: daniel
 */

#ifndef USER_H_
#define USER_H_

#include <string>
#include <vector>

using namespace std;

class user {
public:
	user(string& username);
	virtual ~user();
	string getUserName () const;
	int findFollowingUserLocation(const string& userIfollow);//returns the location of the "userIfollow" in the vectors
	static const int NOT_FOUND=-1;
	void addFollowUser(string &toFollowUsername, int id);//this user now following "toFollowUsername"
	void removeFollowUser(string &toRemoveFollowUsername);//this user will not follow "toFollowUsername" anymore
	vector<int> *getFollowingId();
	user& operator=(const user &User);
	user(const user &User);


private:
	string _username;
	vector<string> *_following;////every user has vector of all who he is following
	vector<int> *_followingId;/// every user has vector of all the id who he is following

};

#endif /* USER_H_ */
