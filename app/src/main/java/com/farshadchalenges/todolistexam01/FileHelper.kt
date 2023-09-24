package com.farshadchalenges.todolistexam01

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val File_Name = "todolist.dat"

fun writeData(items : SnapshotStateList<String>,context: Context){

    val fos = context.openFileOutput(File_Name,Context.MODE_PRIVATE)
    val oas = ObjectOutputStream(fos)

    val itemList = ArrayList<String>()
    itemList.addAll(items)

    oas.writeObject(itemList)
    oas.close()
}

fun readData(context: Context):SnapshotStateList<String>{

    var itemList : ArrayList<String>

    try{
        val fis = context.openFileInput(File_Name)
        val ois = ObjectInputStream(fis)
        itemList = ois.readObject() as ArrayList<String>
    }catch (e : FileNotFoundException){
        itemList = ArrayList()
    }

    val items = SnapshotStateList<String>()
    items.addAll(itemList)

    return items
}