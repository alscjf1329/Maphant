package com.tovelop.maphant.utils

import java.io.InputStream

class BadWordFiltering {
    private val badWords: Set<Regex>

    init {
        val badWordsStream: InputStream = javaClass.classLoader.getResourceAsStream("badwords.txt")!!
        val rawBadWords = badWordsStream.bufferedReader().lineSequence()
            .filter { it.isNotBlank() }
            .toList()

        badWords = rawBadWords.map { badWord ->
            val pattern = badWord
                .map { c ->
                    if (c.isLetter()) "(?i:[${c.uppercaseChar()}${c.lowercaseChar()}])"
                    else Regex.escape(c.toString())
                }
                .joinToString(separator = "(?:[\\p{Punct}\\d]*|\\s*)")
            pattern.toRegex()
        }.toSet()
    }

    fun hasBadWords(text: String): Boolean {
        return badWords.any { it.containsMatchIn(text) }
    }

    fun filterBadWords(text: String, replacement: String = "***"): String {
        var newText = text
        badWords.forEach { regex ->
            newText = regex.replace(newText, replacement)
        }

        return newText
    }
}