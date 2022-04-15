package visitor

interface TokenVisitor {

    fun visit(token: NumberToken)

    fun visit(token: Parenthesis)

    fun visit(token: Operation)
}