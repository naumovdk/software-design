package visitor.parser

import visitor.*

class ParserException(message: String) : RuntimeException(message)

class ParserVisitor : TokenVisitor {

    private val tokens = mutableListOf<Token>()

    private val stack = ArrayDeque<Token>()

    private fun <T> ArrayDeque<T>.removeWhile(predicate: (T) -> Boolean): List<T> {
        val result = takeWhile(predicate)
        repeat(result.size) { removeFirst() }
        return result
    }

    override fun visit(token: NumberToken) {
        tokens.add(token)
    }

    override fun visit(token: Parenthesis) {
        if (token.type == ParenthesisType.OPEN) {
            stack.addFirst(token)
            return
        }

        tokens.addAll(stack.removeWhile { it !is Parenthesis })
        stack.removeFirstOrNull() ?: throw ParserException("Invalid expression")
    }

    override fun visit(token: Operation) {
        tokens.addAll(stack.removeWhile { it is Operation && it.priority >= token.priority })
        stack.addFirst(token)
    }

    fun finish(): List<Token> {
        tokens.addAll(stack)
        stack.clear()
        return tokens
    }
}