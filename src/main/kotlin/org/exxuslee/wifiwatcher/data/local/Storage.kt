package org.exxuslee.wifiwatcher.data.local

import org.mapdb.*

class Storage {

    private val db: DB = DBMaker
        .fileDB("./data.db")
        .fileMmapEnableIfSupported()
        .transactionEnable()
        .make()

    private val set: MutableSet<String> = db.hashSet("ssidSet", Serializer.STRING)
        .createOrOpen()

    fun put(value: String) {
        set.add(value)
        db.commit()
    }

    fun putBatch(values: Set<String>) {
        val old = get()
        val toRemove = old - values
        val toAdd = values - old
        set.removeAll(toRemove)
        set.addAll(toAdd)
        db.commit()
    }

    fun get() = set.toSet()

    fun delete(value: String) {
        set.remove(value)
        db.commit()
    }

    fun close() {
        db.close()
    }
}