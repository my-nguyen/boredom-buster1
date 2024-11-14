package eu.maxkim.boredombuster1.activity.fake.usecase

import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.usecase.DeleteActivity

class FakeDeleteActivity : DeleteActivity {

    var wasCalled = false
        private set

    override suspend fun invoke(activity: Activity) {
        wasCalled = true
    }
}
