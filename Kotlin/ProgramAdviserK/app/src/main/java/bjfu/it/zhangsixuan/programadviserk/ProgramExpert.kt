package bjfu.it.zhangsixuan.programadviserk

class ProgramExpert {
    fun getLanguage(feature: String): String =
        when (feature) {
            "fast" -> "C/C++"
            "easy" -> "Python"
            "new" -> "Kotlin"
            "OO" -> "Java"
            else -> "You got me."
        }
}