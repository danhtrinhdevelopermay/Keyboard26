package com.ios26keyboard.utils

class VietnameseInputProcessor {
    
    private val vowelMap = mapOf(
        'a' to mapOf(
            'a' to 'â',
            'w' to 'ă'
        ),
        'e' to mapOf(
            'e' to 'ê'
        ),
        'o' to mapOf(
            'o' to 'ô',
            'w' to 'ơ'
        ),
        'u' to mapOf(
            'w' to 'ư'
        ),
        'd' to mapOf(
            'd' to 'đ'
        )
    )
    
    private val toneMap = mapOf(
        'f' to ToneType.HUYEN,
        's' to ToneType.SAC,
        'r' to ToneType.HOI,
        'x' to ToneType.NGA,
        'j' to ToneType.NANG
    )
    
    private val baseVowels = setOf('a', 'ă', 'â', 'e', 'ê', 'i', 'o', 'ô', 'ơ', 'u', 'ư', 'y',
                                    'A', 'Ă', 'Â', 'E', 'Ê', 'I', 'O', 'Ô', 'Ơ', 'U', 'Ư', 'Y')
    
    private val toneChars = mapOf(
        'a' to mapOf(
            ToneType.HUYEN to 'à', ToneType.SAC to 'á', 
            ToneType.HOI to 'ả', ToneType.NGA to 'ã', ToneType.NANG to 'ạ'
        ),
        'ă' to mapOf(
            ToneType.HUYEN to 'ằ', ToneType.SAC to 'ắ',
            ToneType.HOI to 'ẳ', ToneType.NGA to 'ẵ', ToneType.NANG to 'ặ'
        ),
        'â' to mapOf(
            ToneType.HUYEN to 'ầ', ToneType.SAC to 'ấ',
            ToneType.HOI to 'ẩ', ToneType.NGA to 'ẫ', ToneType.NANG to 'ậ'
        ),
        'e' to mapOf(
            ToneType.HUYEN to 'è', ToneType.SAC to 'é',
            ToneType.HOI to 'ẻ', ToneType.NGA to 'ẽ', ToneType.NANG to 'ẹ'
        ),
        'ê' to mapOf(
            ToneType.HUYEN to 'ề', ToneType.SAC to 'ế',
            ToneType.HOI to 'ể', ToneType.NGA to 'ễ', ToneType.NANG to 'ệ'
        ),
        'i' to mapOf(
            ToneType.HUYEN to 'ì', ToneType.SAC to 'í',
            ToneType.HOI to 'ỉ', ToneType.NGA to 'ĩ', ToneType.NANG to 'ị'
        ),
        'o' to mapOf(
            ToneType.HUYEN to 'ò', ToneType.SAC to 'ó',
            ToneType.HOI to 'ỏ', ToneType.NGA to 'õ', ToneType.NANG to 'ọ'
        ),
        'ô' to mapOf(
            ToneType.HUYEN to 'ồ', ToneType.SAC to 'ố',
            ToneType.HOI to 'ổ', ToneType.NGA to 'ỗ', ToneType.NANG to 'ộ'
        ),
        'ơ' to mapOf(
            ToneType.HUYEN to 'ờ', ToneType.SAC to 'ớ',
            ToneType.HOI to 'ở', ToneType.NGA to 'ỡ', ToneType.NANG to 'ợ'
        ),
        'u' to mapOf(
            ToneType.HUYEN to 'ù', ToneType.SAC to 'ú',
            ToneType.HOI to 'ủ', ToneType.NGA to 'ũ', ToneType.NANG to 'ụ'
        ),
        'ư' to mapOf(
            ToneType.HUYEN to 'ừ', ToneType.SAC to 'ứ',
            ToneType.HOI to 'ử', ToneType.NGA to 'ữ', ToneType.NANG to 'ự'
        ),
        'y' to mapOf(
            ToneType.HUYEN to 'ỳ', ToneType.SAC to 'ý',
            ToneType.HOI to 'ỷ', ToneType.NGA to 'ỹ', ToneType.NANG to 'ỵ'
        )
    )
    
    private val tonedToBase = mutableMapOf<Char, Char>().apply {
        toneChars.forEach { (base, tones) ->
            tones.values.forEach { toned ->
                this[toned] = base
            }
        }
    }
    
    enum class ToneType {
        HUYEN, SAC, HOI, NGA, NANG
    }
    
    data class ProcessResult(
        val text: String,
        val deleteCount: Int,
        val isProcessed: Boolean
    )
    
    fun processKey(currentWord: String, key: Char): ProcessResult {
        val lowerKey = key.lowercaseChar()
        val isUpperCase = key.isUpperCase()
        
        if (currentWord.isEmpty()) {
            return ProcessResult(key.toString(), 0, false)
        }
        
        if (lowerKey == 'w') {
            return processDoubleVowelW(currentWord, isUpperCase)
        }
        
        if (lowerKey in toneMap.keys) {
            return processTone(currentWord, lowerKey)
        }
        
        val lastChar = currentWord.lastOrNull()?.lowercaseChar()
        if (lastChar != null && vowelMap.containsKey(lastChar) && vowelMap[lastChar]?.containsKey(lowerKey) == true) {
            return processDoubleVowel(currentWord, lastChar, lowerKey, isUpperCase)
        }
        
        return ProcessResult(key.toString(), 0, false)
    }
    
    private fun processDoubleVowel(word: String, lastChar: Char, key: Char, isUpperCase: Boolean): ProcessResult {
        val newChar = vowelMap[lastChar]?.get(key)
        if (newChar != null) {
            val finalChar = if (word.last().isUpperCase()) newChar.uppercaseChar() else newChar
            return ProcessResult(finalChar.toString(), 1, true)
        }
        return ProcessResult(if (isUpperCase) key.uppercaseChar().toString() else key.toString(), 0, false)
    }
    
    private fun processDoubleVowelW(word: String, isUpperCase: Boolean): ProcessResult {
        val lastIndex = word.lastIndex
        for (i in lastIndex downTo maxOf(0, lastIndex - 2)) {
            val char = word[i].lowercaseChar()
            if (char == 'u' || char == 'o' || char == 'a') {
                val newChar = when (char) {
                    'u' -> 'ư'
                    'o' -> 'ơ'
                    'a' -> 'ă'
                    else -> continue
                }
                val finalChar = if (word[i].isUpperCase()) newChar.uppercaseChar() else newChar
                val deleteCount = lastIndex - i + 1
                val prefix = if (i < lastIndex) word.substring(i + 1) else ""
                return ProcessResult(finalChar.toString() + prefix, deleteCount, true)
            }
            val baseChar = tonedToBase[char]
            if (baseChar == 'u' || baseChar == 'o' || baseChar == 'a') {
                val newBaseChar = when (baseChar) {
                    'u' -> 'ư'
                    'o' -> 'ơ'
                    'a' -> 'ă'
                    else -> continue
                }
                val tone = getToneFromChar(char)
                val newTonedChar = if (tone != null) {
                    toneChars[newBaseChar]?.get(tone) ?: newBaseChar
                } else newBaseChar
                val finalChar = if (word[i].isUpperCase()) newTonedChar.uppercaseChar() else newTonedChar
                val deleteCount = lastIndex - i + 1
                val prefix = if (i < lastIndex) word.substring(i + 1) else ""
                return ProcessResult(finalChar.toString() + prefix, deleteCount, true)
            }
        }
        return ProcessResult(if (isUpperCase) "W" else "w", 0, false)
    }
    
    private fun processTone(word: String, toneKey: Char): ProcessResult {
        val tone = toneMap[toneKey] ?: return ProcessResult(toneKey.toString(), 0, false)
        
        val lastIndex = word.lastIndex
        for (i in lastIndex downTo maxOf(0, lastIndex - 4)) {
            val char = word[i]
            val lowerChar = char.lowercaseChar()
            
            val baseVowel = getBaseVowel(lowerChar)
            if (baseVowel != null) {
                val tonedChar = toneChars[baseVowel]?.get(tone)
                if (tonedChar != null) {
                    val finalChar = if (char.isUpperCase()) tonedChar.uppercaseChar() else tonedChar
                    val deleteCount = lastIndex - i + 1
                    val suffix = if (i < lastIndex) word.substring(i + 1) else ""
                    return ProcessResult(finalChar.toString() + suffix, deleteCount, true)
                }
            }
        }
        
        return ProcessResult(toneKey.toString(), 0, false)
    }
    
    private fun getBaseVowel(char: Char): Char? {
        if (char in setOf('a', 'ă', 'â', 'e', 'ê', 'i', 'o', 'ô', 'ơ', 'u', 'ư', 'y')) {
            return char
        }
        return tonedToBase[char]
    }
    
    private fun getToneFromChar(char: Char): ToneType? {
        toneChars.forEach { (_, tones) ->
            tones.forEach { (tone, toned) ->
                if (toned == char) return tone
            }
        }
        return null
    }
    
    fun isVietnameseModifierKey(key: Char): Boolean {
        val lowerKey = key.lowercaseChar()
        return lowerKey in toneMap.keys || lowerKey == 'w'
    }
}
