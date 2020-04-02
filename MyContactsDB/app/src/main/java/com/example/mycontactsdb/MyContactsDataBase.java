package com.example.mycontactsdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class MyContactsDataBase extends RoomDatabase {

    public abstract ContactDao getContactDao();
}
