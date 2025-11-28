package com.ios26keyboard.utils

class WordSuggestionHelper {
    
    private val vietnameseWords = listOf(
        "xin", "xin chào", "xin lỗi", "xin cảm ơn",
        "chào", "chào bạn", "chào anh", "chào chị", "chào em",
        "cảm ơn", "cám ơn",
        "tôi", "tốt", "tổng", "từ", "tại", "trong", "trên", "theo",
        "bạn", "bao", "bằng", "biết", "bây giờ",
        "anh", "ấy", "ai",
        "em", "ở",
        "và", "với", "về", "vì", "vậy",
        "là", "lại", "làm", "lên", "lúc",
        "có", "của", "cho", "chúng", "chúc", "cũng", "còn", "các",
        "được", "đã", "đi", "đây", "đó", "đến", "để", "đang",
        "không", "khi", "khác",
        "một", "mình", "muốn", "mới",
        "này", "như", "nhưng", "những", "nhiều", "nào", "nên", "năm",
        "rất", "ra", "rồi",
        "sẽ", "sau", "số",
        "thì", "thế", "thôi", "thêm", "thấy", "thật", "thuộc",
        "người", "ngày", "nữa",
        "hay", "hoặc", "hơn", "học",
        "gì", "giờ", "gửi",
        "phải", "phần",
        "quá", "qua",
        "yêu", "ý"
    )
    
    private val englishWords = listOf(
        "the", "be", "to", "of", "and", "a", "in", "that", "have", "I",
        "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
        "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
        "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
        "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
        "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
        "people", "into", "year", "your", "good", "some", "could", "them", "see", "other",
        "than", "then", "now", "look", "only", "come", "its", "over", "think", "also",
        "back", "after", "use", "two", "how", "our", "work", "first", "well", "way",
        "even", "new", "want", "because", "any", "these", "give", "day", "most", "us",
        "hello", "hi", "hey", "thanks", "thank you", "please", "sorry", "okay", "ok", "yes",
        "no", "maybe", "sure", "great", "good", "nice", "cool", "awesome", "amazing",
        "love", "like", "happy", "sad", "help", "need", "want", "think", "know", "feel"
    )
    
    fun getSuggestions(input: String, isVietnamese: Boolean, limit: Int = 3): List<String> {
        if (input.isEmpty()) {
            return getDefaultSuggestions(isVietnamese)
        }
        
        val lowerInput = input.lowercase()
        val wordList = if (isVietnamese) vietnameseWords else englishWords
        
        val exactMatches = mutableListOf<String>()
        val startsWithMatches = mutableListOf<String>()
        val containsMatches = mutableListOf<String>()
        
        for (word in wordList) {
            val lowerWord = word.lowercase()
            when {
                lowerWord == lowerInput -> exactMatches.add(word)
                lowerWord.startsWith(lowerInput) -> startsWithMatches.add(word)
                lowerWord.contains(lowerInput) -> containsMatches.add(word)
            }
        }
        
        startsWithMatches.sortBy { it.length }
        containsMatches.sortBy { it.length }
        
        val results = mutableListOf<String>()
        
        if (exactMatches.isNotEmpty()) {
            results.addAll(startsWithMatches.filter { it != exactMatches.first() }.take(limit))
        } else {
            results.addAll(startsWithMatches.take(limit))
        }
        
        if (results.size < limit) {
            results.addAll(containsMatches.take(limit - results.size))
        }
        
        if (results.isEmpty()) {
            results.add(input)
        }
        
        return results.take(limit).map { capitalizeIfNeeded(it, input) }
    }
    
    private fun capitalizeIfNeeded(suggestion: String, input: String): String {
        if (input.isEmpty()) return suggestion
        
        return if (input.first().isUpperCase()) {
            suggestion.replaceFirstChar { it.uppercaseChar() }
        } else {
            suggestion
        }
    }
    
    private fun getDefaultSuggestions(isVietnamese: Boolean): List<String> {
        return if (isVietnamese) {
            listOf("Xin chào", "Cảm ơn", "Tôi")
        } else {
            listOf("Hello", "Thanks", "I")
        }
    }
}
