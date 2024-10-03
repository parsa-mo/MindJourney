import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "thought_form_table")
data class ThoughtFormEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userUid: String,
    val title: String,
    val date: String,
    val prompt1: String,
    val prompt2: String,
    val prompt3: String,
    val beliefRating: Int,
    val consequenceRating: Int
)



@Dao
interface ThoughtFormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(form: ThoughtFormEntity)

    @Delete
    suspend fun delete(form: ThoughtFormEntity)

    @Query("SELECT * FROM thought_form_table WHERE userUid = :userUid")
    fun getUserForms(userUid: String): Flow<List<ThoughtFormEntity>>
}
