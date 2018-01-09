package models

interface Model {
    fun getBugs(type: BugType): List<TheBug>
    fun getBugById(id: Int): TheBug?
    fun addBug(bug: TheBug): Int
    fun updateBug(bug: TheBug)
    fun deleteBug(id: Int)
}