package com.dac.tictactoe.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class UserData(
    @Id
    var id: Long = 0,
    var name: String = "",
    var isAudioPlay: Boolean = true,
    var isUserFirst: Boolean = false,
    var isUserRated: Boolean = false
)