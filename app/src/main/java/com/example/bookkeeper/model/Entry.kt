package com.example.bookkeeper.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import com.example.bookkeeper.utils.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


const val tablename = "EntryBook"

@Entity(tableName = tablename)
data class Entry(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo val description: String?,
    @ColumnInfo val amount: Double?,
//    @ColumnInfo val date: Long?,
    @ColumnInfo val date: OffsetDateTime?
)

@Dao
interface EntryDao {
    @Query("Select * from `${tablename}` order by date desc, _id desc")
    fun readAllEntry(): LiveData<List<Entry>>

    @Query("Select * from `${tablename}` where amount > 0 order by date desc")
    fun readAllRevenue(): LiveData<List<Entry>>

    @Query("Select * from `${tablename}` where amount < 0 order by date desc")
    fun readAllExpense(): LiveData<List<Entry>>

    @Query("Select TOTAL(amount) from `${tablename}`")
    fun readBalance(): LiveData<Double>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntry(entry: Entry)

    @Delete
    suspend fun deleteEntry(entry: Entry)
}

@Database(entities = [Entry::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class EntryDB : RoomDatabase() {
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile
        private var INSTANCE: EntryDB? = null

        fun getDB(context: Context): EntryDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntryDB::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

class EntryRepository(private val accountDao: EntryDao) {

    val readAllEntry: LiveData<List<Entry>> = accountDao.readAllEntry()

    //    val readAllRevenue: LiveData<List<Entry>> = accountDao.readAllRevenue()
//    val readAllExpense: LiveData<List<Entry>> = accountDao.readAllExpense()
    val readBalance: LiveData<Double> = accountDao.readBalance()
//    fun readEntry(accountname: String, password: String): LiveData<List<Entry>> {
//        return accountDao.readEntry(accountname, password)
//    }

    suspend fun insertEntry(entry: Entry) {
        accountDao.insertEntry(entry)
    }

    suspend fun deleteEntry(entry: Entry) {
        accountDao.deleteEntry(entry)
    }

}

class EntryVM(application: Application) : AndroidViewModel(application) {

    val readAllEntry: LiveData<List<Entry>>
    val readBalance: LiveData<Double>
    private val repository: EntryRepository

    init {
        val userDao = EntryDB.getDB(application).entryDao()
        repository = EntryRepository(userDao)
        readAllEntry = repository.readAllEntry
        readBalance = repository.readBalance
    }

//    fun readEntry(accountname: String, password: String): LiveData<List<Entry>> {
////        viewModelScope.launch(Dispatchers.IO) {
//        Log.v("coroutine","run")
//        return repository.readEntry(accountname, password)
//    }

    fun insertEntry(entry: Entry): Boolean {

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertEntry(entry)

        }
        return true
    }

    fun deleteEntry(entry: Entry): Boolean {

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEntry(entry)

        }
        return true
    }

}
