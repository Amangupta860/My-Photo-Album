package com.photoalbum.myphotoalbum;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyImagesDao {
    @Insert
    void insert(MyImages myImage);
    @Delete
     void delete(MyImages myImage);
    @Update
    void update(MyImages myImage);
    @Query( "SELECT * FROM my_images ORDER BY image_id ASC")
    LiveData<List<MyImages>>getAllImages();



}