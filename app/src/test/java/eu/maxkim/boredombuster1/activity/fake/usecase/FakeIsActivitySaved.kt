package eu.maxkim.boredombuster1.activity.fake.usecase

import eu.maxkim.boredombuster1.activity.usecase.IsActivitySaved

class FakeIsActivitySaved(private val isActivitySaved: Boolean = false) : IsActivitySaved {

    override suspend fun invoke(key: String): Boolean {
        return isActivitySaved
    }
}
