package com.reindeercrafts.hackernews.data

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Article(val id: String, val deleted: Boolean, val type: String, val by: String?,
                   val time: Long, val text: String?, val dead: Boolean, val parent: String?,
                   val kids: ArrayList<String>?, val url: String?, val score: Int, val title: String) : Parcelable {


    protected constructor(source: Parcel) : this(source.readString(), source.readInt() == 1, source.readString(),
            source.readString(), source.readLong(), source.readString(), source.readInt() == 1, source.readString(),
            source.readArrayList(ArrayList<String>().javaClass.classLoader) as ArrayList<String>?, source.readString(),
            source.readInt(), source.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeInt(if (deleted) 1 else 0)
        dest.writeString(type)
        dest.writeString(by)
        dest.writeLong(time)
        dest.writeString(text)
        dest.writeInt(if (dead) 1 else 0)
        dest.writeString(parent)
        dest.writeList(kids)
        dest.writeString(url)
        dest.writeInt(score)
        dest.writeString(title)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Article> = object : Parcelable.Creator<Article> {
            override fun createFromParcel(`in`: Parcel): Article {
                return Article(`in`)
            }

            override fun newArray(size: Int): Array<Article?> {
                return arrayOfNulls(size)
            }
        }

        object Columns {
            val TABLE_NAME = "articles"
            val _ID = "_id"
            val COLUMN_ID = "id"
            val COLUMN_DELETED = "deleted"
            val COLUMN_TYPE = "type"
            val COLUMN_BY = "by"
            val COLUMN_TIME = "time"
            val COLUMN_TEXT = "text"
            val COLUMN_DEAD = "dead"
            val COLUMN_PARENT = "parent"
            val COLUMN_KIDS = "kids"
            val COLUMN_URL = "url"
            val COLUMN_SCORE = "score"
            val COLUMN_TITLE = "title"
        }
    }
}