package eu.maxkim.boredombuster1.activity.fake.usecase

import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.usecase.DeleteActivity

class FakeDeleteActivity : DeleteActivity {

    override suspend fun invoke(activity: Activity) {
        TODO("Not yet implemented")
    }
}
