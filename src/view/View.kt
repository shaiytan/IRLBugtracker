package view

import models.TheBug

interface View {
    fun setList(data: List<TheBug>)
    fun setBugData(bug: TheBug)
    fun showBugForm(bug: TheBug?)
}