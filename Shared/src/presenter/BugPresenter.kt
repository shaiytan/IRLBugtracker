package presenter

import models.*
import views.View
import java.util.function.Predicate
import kotlin.Comparator

class BugPresenter(
        private val model: Model,
        private val view: View
) : Presenter {
    private val bugsList = model.getBugs(BugType.BUG).toMutableList()
    private val ideasList = model.getBugs(BugType.IDEA).toMutableList()
    private val improvementsList = model.getBugs(BugType.IMPROVEMENT).toMutableList()

    init {
        setLists()
    }

    private fun chooselist(type: BugType) = when (type) {
        BugType.BUG -> bugsList
        BugType.IMPROVEMENT -> improvementsList
        BugType.IDEA -> ideasList
    }

    private fun setLists() {
        val comp = Comparator<TheBug> { first, second ->
            val res = -first.category.rating.compareTo(second.category.rating)
            if (res == 0) first.date.compareTo(second.date) else res
        }
        bugsList.sortWith(comp)
        view.setBugsList(bugsList.map(TheBug::name))
        improvementsList.sortWith(comp)
        view.setImprovementsList(improvementsList.map(TheBug::name))
        ideasList.sortWith(comp)
        view.setIdeasList(ideasList.map(TheBug::name))
    }

    override fun onAdd(type: BugType) {
        view.showBugForm(type)
    }

    override fun addBug(bug: TheBug) {
        val bugId = model.addBug(bug)
        val currentData = chooselist(bug.type)
        currentData.add(bug.copy(id = bugId))
        setLists()
    }

    override fun onEdit(index: Int, type: BugType) {
        val currentData = chooselist(type)
        if (index in currentData.indices) view.showBugForm(currentData[index])
    }

    override fun editBug(bug: TheBug) {
        model.updateBug(bug)
        BugType.values()
                .map { chooselist(it) }
                .forEach { currentData ->
                    currentData
                            .filter { it.id == bug.id }
                            .forEach { currentData.remove(it) }
                }
        val currentData = chooselist(bug.type)
        currentData.add(bug)
        setLists()
    }

    override fun onDelete(index: Int, type: BugType) {
        val currentData = chooselist(type)
        model.deleteBug(currentData[index].id)
        currentData.removeAt(index)
        setLists()
    }

    override fun onFix(index: Int, type: BugType) {
        val currentData = chooselist(type)
        if (index in currentData.indices) {
            val bug = currentData[index]
            bug.category = Category.FIXED
            model.updateBug(bug)
            setLists()
        }
    }

    override fun onBugSelected(index: Int, type: BugType) {
        val currentData = chooselist(type)
        if (index in currentData.indices)
            when (type) {
                BugType.BUG -> view.setBugData(currentData[index])
                BugType.IMPROVEMENT -> view.setImprovementData(currentData[index])
                BugType.IDEA -> view.setIdeaData(currentData[index])
            }
    }
}