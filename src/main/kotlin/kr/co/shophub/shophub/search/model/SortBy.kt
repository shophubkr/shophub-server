package kr.co.shophub.shophub.search.model

enum class SortBy {
    DISTANCE,
    MIN_PRICE,;

    companion object {
        fun fromValue(value: String?): SortBy? = values().find { it.name.equals(value, ignoreCase = true) }
    }
}
