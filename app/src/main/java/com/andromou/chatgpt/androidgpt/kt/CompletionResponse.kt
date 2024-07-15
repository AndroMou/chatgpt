package com.andromou.chatgpt.androidgpt.kt

data class CompletionResponse(val choices: List<Choice>)

data class Choice(val text: String)