package com.photoalbum.myphotoalbum;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImageRepository  {
    private MyImagesDao myImageDao;
    private LiveData<List<MyImages>> imageList;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyImageRepository(Application application){
        MyImageDatabase database = MyImageDatabase.getInstance(application);
        myImageDao= database.myImageDao();
        imageList= myImageDao.getAllImages();
    }
    public void insert(MyImages myImage){
        // new  InsertNoteAsyncTask(noteDao).execute(note);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImageDao.insert(myImage);
            }
        });
    }
    public void update(MyImages myImage){
        // new  UpdateNoteAsyncTask(noteDao).execute(note);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImageDao.update(myImage);
            }
        });
    }
    public void delete(MyImages myImage){
        //    new  DeleteNoteAsyncTask(noteDao).execute(note);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
             myImageDao.delete(myImage);
            }
        });
    }
    public LiveData<List<MyImages>>getAllNotes() {
        return imageList;
    }

    }

