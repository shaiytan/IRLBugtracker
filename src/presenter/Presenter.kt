package presenter

import models.TheBug

interface Presenter {
    fun onAdd(index: Int)
    fun addBug(bug: TheBug)
    fun onEdit(index: Int)
    fun editBug(bug: TheBug)
    fun onDelete(index: Int)
    fun onSwitchList(index: Int)
    fun onBugSelected(index: Int)
}