package com.example.socialNetwork.repository;

import java.util.List;
import java.util.Map;

public interface SocialRepository {


    void insertInitialDataToUserTable();

    void insertInitialDataToLikeTable();

    void insertInitialDataToFriendshipTable();

    List<String> getNameOfUsersWithMore100LikesAndMore100Friends();

    void insertInitialDataToRandomTable(String tableName,
	   List<Map<String, Object>> listMapsToInsert);

}
