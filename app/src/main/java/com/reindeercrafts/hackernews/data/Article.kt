package com.reindeercrafts.hackernews.data

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable

@Entity(indices = arrayOf(Index(value = "id", unique = true)))
data class Article(@PrimaryKey @ColumnInfo val id: String,
                   @ColumnInfo val deleted: Boolean,
                   @ColumnInfo val type: String,
                   @ColumnInfo val by: String?,
                   @ColumnInfo val time: Long,
                   @ColumnInfo val text: String?,
                   @ColumnInfo val dead: Boolean,
                   @ColumnInfo val parent: String?,
                   @ColumnInfo val kids: ArrayList<String>?,
                   @ColumnInfo val url: String?,
                   @ColumnInfo val score: Int,
                   @ColumnInfo val title: String?) : Parcelable {


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
    }

    class StringListTypeConverter private constructor() {
        companion object {
            @TypeConverter
            @JvmStatic
            fun fromList(kids: ArrayList<String>?): String? {
                if (kids == null) {
                    return null
                }
                return kids.joinToString()
            }

            @TypeConverter
            @JvmStatic
            fun toList(kidsString: String?): ArrayList<String>? {
                if (kidsString == null) {
                    return null
                }
                return ArrayList(kidsString.split(", "))
            }
        }
    }
}