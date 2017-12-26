package models

import java.util.*

enum class Category(val rating: Int) {
    CRITICAL(5),
    IMPORTANT(4),
    ANNOYING(3),
    IRRITATING(2),
    PERSPECTIVE(1),
    FIXED(0),
    CANCELED(-1);
}

enum class BugType {
    BUG, IMPROVEMENT, IDEA
}

data class TheBug(val id: Int,
                  var name: String,
                  var description: String = "",
                  var date: Date = Date(System.currentTimeMillis()),
                  var category: Category = Category.ANNOYING,
                  var type: BugType = BugType.BUG)