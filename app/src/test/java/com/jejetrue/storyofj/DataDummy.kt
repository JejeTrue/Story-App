package com.jejetrue.storyofj

import com.example.storysubmissionapp.data.model.Story

object DataDummy {

    fun generateDummyQuoteResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "photo  + $i",
                "created + $i",
                "name + $i",
                "desc + $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()




            )
            items.add(story)
        }
        return items
    }
}