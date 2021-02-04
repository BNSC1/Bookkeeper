package com.example.bookkeeper.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.bookkeeper.utils.BooleanConverter
import com.example.bookkeeper.utils.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


const val tableName = "EntryBook"

@Entity(tableName = tableName)
data class Entry(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo val description: String?,
    @ColumnInfo val amount: Double?,
    @ColumnInfo val date: OffsetDateTime?,
)

@Dao
interface EntryDao {
    @Query("Select * from `${tableName}` order by date desc, _id desc")
    fun readAllEntry(): LiveData<List<Entry>>

    @Query("Select TOTAL(amount) from `${tableName}` where amount > 0")
    fun readRevenue(): LiveData<Double>

    @Query("Select ABS(TOTAL(amount)) from `${tableName}` where amount < 0")
    fun readExpense(): LiveData<Double>

    @Query("Select TOTAL(amount) from `${tableName}`")
    fun readBalance(): LiveData<Double>

    @Query("Select COUNT(*) from `${tableName}` where date between :startDate and :endDate limit 1")
    fun hasDataYear(startDate: String, endDate: String): LiveData<Boolean>

    @Query("Select SUM(amount) from `${tableName}` where date between :startDate and :endDate")
    fun readMonthlyBalance(startDate: String, endDate: String): LiveData<Double?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntry(entry: Entry)

    @Delete
    suspend fun deleteEntry(entry: Entry)

    @RawQuery
    fun checkpoint(supportSQLiteQuery: SupportSQLiteQuery?): Boolean
}

@Database(entities = [Entry::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class, BooleanConverter::class)
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
                ).setJournalMode(JournalMode.TRUNCATE)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

class EntryRepository(private val entryDao: EntryDao) {

    val readAllEntry: LiveData<List<Entry>> = entryDao.readAllEntry()

    val readRevenue: LiveData<Double> = entryDao.readRevenue()
    val readExpense: LiveData<Double> = entryDao.readExpense()
    val readBalance: LiveData<Double> = entryDao.readBalance()

    suspend fun insertEntry(entry: Entry) {
        entryDao.insertEntry(entry)
    }

    suspend fun deleteEntry(entry: Entry) {
        entryDao.deleteEntry(entry)
    }

}

class EntryVM(application: Application) : AndroidViewModel(application) {

    val readAllEntry: LiveData<List<Entry>>
    val readBalance: LiveData<Double>
    val readRevenue: LiveData<Double>
    val readExpense: LiveData<Double>
    private val repository: EntryRepository

    init {
        val userDao = EntryDB.getDB(application).entryDao()
        repository = EntryRepository(userDao)
        readAllEntry = repository.readAllEntry
        readBalance = repository.readBalance
        readRevenue = repository.readRevenue
        readExpense = repository.readExpense
    }

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
