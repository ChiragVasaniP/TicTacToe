package com.dac.tictactoe.database

import com.dac.tictactoe.data.UserData
import io.objectbox.Box

object MyBoxes {

    private val homeDataBox: Box<UserData?> = ObjectBox.store.boxFor(UserData::class.java)

    /**
     * Deletes all data stored in the ObjectBox database.
     */
    fun deleteAll() {
        ObjectBox.store.removeAllObjects()
    }

    /**
     * Returns the Box instance for HomeData objects.
     */
    fun homeData(): Box<UserData?> = homeDataBox


}
