package models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class DesktopBugModel : Model {
    companion object {
        private val FILENAME = "foundbugs.json"
    }

    private val foundBugs = loadData().toMutableList()
    private fun saveData() {
        FileWriter(FILENAME).use { writer ->
            val type = object : TypeToken<List<TheBug>>() {}.type
            writer.write(Gson().toJson(foundBugs, type))
        }
    }

    private fun loadData() = try {
        FileReader(FILENAME).use { reader ->
            val type = object : TypeToken<List<TheBug>>() {}.type
            Gson().fromJson<List<TheBug>>(reader, type)
        }
    } catch (e: FileNotFoundException) {
        emptyList<TheBug>()
    }

    override fun getBugs(type: BugType) = foundBugs.filter { it.type == type }

    override fun getBugById(id: Int) = foundBugs.find { it.id == id }

    override fun addBug(bug: TheBug): Int {
        var maxId = foundBugs.maxBy(TheBug::id)?.id ?: 0
        foundBugs.add(bug.copy(id = ++maxId))
        saveData()
        return maxId
    }

    override fun updateBug(bug: TheBug) {
        val oldBug = getBugById(bug.id) ?: throw IllegalArgumentException("id illegaly modified or record deleted")
        val index = foundBugs.indexOf(oldBug)
        foundBugs[index] = bug
        saveData()
    }

    override fun deleteBug(id: Int) {
        val bug = getBugById(id) ?: throw IllegalArgumentException("id illegaly modified or record deleted")
        foundBugs.remove(bug)
        saveData()
    }
}