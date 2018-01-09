package views

import models.*

interface View {
    fun setBugsList(data: List<String>)
    fun setImprovementsList(data: List<String>)
    fun setIdeasList(data: List<String>)
    fun setBugData(bug: TheBug)
    fun setIdeaData(bug: TheBug)
    fun setImprovementData(bug: TheBug)
    fun showBugForm(bug: TheBug)
    fun showBugForm(type: BugType)
}