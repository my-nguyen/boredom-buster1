package eu.maxkim.boredombuster1.activity.framework.datasource

import eu.maxkim.boredombuster1.activity.model.Activity

val successfulResponse = """
    {
      "activity": "Go to a music festival with some friends",
      "type": "social",
      "participants": 4,
      "price": 0.4,
      "link": "",
      "key": "6482790",
      "accessibility": 0.2
    }
""".trimIndent()

val errorResponse = "I am not a json :o"

val responseActivity = Activity(
    name = "Go to a music festival with some friends",
    type = Activity.Type.Social,
    participantCount = 4,
    price = 0.4f,
    accessibility = 0.2f,
    key = "6482790",
    link = ""
)
