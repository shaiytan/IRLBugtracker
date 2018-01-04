package presenter

import models.*

interface Presenter {
    fun onAdd(type: BugType)
    fun addBug(bug: TheBug)
    fun onEdit(index: Int, type: BugType)
    fun editBug(bug: TheBug)
    fun onDelete(index: Int, type: BugType)
    fun onBugSelected(index: Int, type: BugType)
}