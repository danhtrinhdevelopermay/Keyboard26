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
    
    private val allVowels = setOf(
        'a', 'ă', 'â', 'e', 'ê', 'i', 'o', 'ô', 'ơ', 'u', 'ư', 'y',
        'A', 'Ă', 'Â', 'E', 'Ê', 'I', 'O', 'Ô', 'Ơ', 'U', 'Ư', 'Y',
        'à', 'ằ', 'ầ', 'è', 'ề', 'ì', 'ò', 'ồ', 'ờ', 'ù', 'ừ', 'ỳ',
        'á', 'ắ', 'ấ', 'é', 'ế', 'í', 'ó', 'ố', 'ớ', 'ú', 'ứ', 'ý',
        'ả', 'ẳ', 'ẩ', 'ẻ', 'ể', 'ỉ', 'ỏ', 'ổ', 'ở', 'ủ', 'ử', 'ỷ',
        'ã', 'ẵ', 'ẫ', 'ẽ', 'ễ', 'ĩ', 'õ', 'ỗ', 'ỡ', 'ũ', 'ữ', 'ỹ',
        'ạ', 'ặ', 'ậ', 'ẹ', 'ệ', 'ị', 'ọ', 'ộ', 'ợ', 'ụ', 'ự', 'ỵ'
    )
    
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
    
    private val priorityVowels = setOf('ơ', 'ê', 'ô', 'â', 'ă', 'ư')
    
    private val consonants = setOf(
        'b', 'c', 'd', 'đ', 'g', 'h', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'x',
        'B', 'C', 'D', 'Đ', 'G', 'H', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'X'
    )
    
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
        
        val vowelPositions = mutableListOf<Int>()
        for (i in word.indices) {
            val lowerChar = word[i].lowercaseChar()
            if (isVowel(lowerChar)) {
                vowelPositions.add(i)
            }
        }
        
        if (vowelPositions.isEmpty()) {
            return ProcessResult(toneKey.toString(), 0, false)
        }
        
        if (vowelPositions.size == 1) {
            return applyToneAtPosition(word, vowelPositions[0], tone)
        }
        
        val targetIndex = findTonePosition(word, vowelPositions)
        return applyToneAtPosition(word, targetIndex, tone)
    }
    
    private fun findTonePosition(word: String, vowelPositions: List<Int>): Int {
        for (pos in vowelPositions) {
            val baseVowel = getBaseVowel(word[pos].lowercaseChar())
            if (baseVowel != null && baseVowel in priorityVowels) {
                return pos
            }
        }
        
        val lastVowelPos = vowelPositions.last()
        val hasTrailingConsonant = (lastVowelPos < word.lastIndex) && 
            word.substring(lastVowelPos + 1).any { it.lowercaseChar() in consonants }
        
        if (hasTrailingConsonant) {
            return vowelPositions.last()
        }
        
        if (vowelPositions.size >= 2) {
            val lastVowel = getBaseVowel(word[vowelPositions.last()].lowercaseChar())
            val secondLastVowel = getBaseVowel(word[vowelPositions[vowelPositions.size - 2]].lowercaseChar())
            
            if (secondLastVowel == 'o' && lastVowel in setOf('a', 'e')) {
                return vowelPositions.last()
            }
            
            if (secondLastVowel == 'u' && lastVowel == 'y') {
                return vowelPositions.last()
            }
            
            if (secondLastVowel == 'i' && lastVowel == 'a') {
                return vowelPositions.last()
            }
            
            if (secondLastVowel == 'u' && lastVowel in setOf('a', 'e', 'i', 'o')) {
                return vowelPositions.last()
            }
            
            if (lastVowel in setOf('i', 'u', 'y') && secondLastVowel in setOf('a', 'e', 'o')) {
                return vowelPositions[vowelPositions.size - 2]
            }
            
            return vowelPositions[vowelPositions.size - 2]
        }
        
        return vowelPositions.last()
    }
    
    private fun applyToneAtPosition(word: String, position: Int, tone: ToneType): ProcessResult {
        val char = word[position]
        val lowerChar = char.lowercaseChar()
        val baseVowel = getBaseVowel(lowerChar) ?: return ProcessResult(
            toneMap.entries.find { it.value == tone }?.key?.toString() ?: "", 
            0, 
            false
        )
        
        val tonedChar = toneChars[baseVowel]?.get(tone) ?: return ProcessResult(
            toneMap.entries.find { it.value == tone }?.key?.toString() ?: "", 
            0, 
            false
        )
        
        val finalChar = if (char.isUpperCase()) tonedChar.uppercaseChar() else tonedChar
        val deleteCount = word.length - position
        val suffix = if (position < word.lastIndex) word.substring(position + 1) else ""
        
        return ProcessResult(finalChar.toString() + suffix, deleteCount, true)
    }
    
    private fun isVowel(char: Char): Boolean {
        val lower = char.lowercaseChar()
        return lower in allVowels || getBaseVowel(lower) != null
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
