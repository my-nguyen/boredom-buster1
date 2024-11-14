package eu.maxkim.boredombuster1.activity.fake.usecase

import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.usecase.SaveActivity

class FakeSaveActivity : SaveActivity {

    override suspend fun invoke(activity: Activity) {
        TODO("Not yet implemented")
    }
}
