package org.chewing.v1.repository


interface ChatSequenceRepository {
    fun readCurrentSequence(roomId: String): Long
    fun updateSequenceIncrement(roomId: String): Long
}