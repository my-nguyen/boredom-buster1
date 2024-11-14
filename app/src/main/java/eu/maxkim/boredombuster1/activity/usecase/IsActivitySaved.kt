package eu.maxkim.boredombuster1.activity.usecase

interface IsActivitySaved {
    suspend operator fun invoke(key: String): Boolean
}