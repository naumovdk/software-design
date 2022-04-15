import visitor.CalcVisitor
import visitor.PrintVisitor
import visitor.Token
import visitor.TokenVisitor
import visitor.parser.ParserVisitor
import visitor.parser.Tokenizer


fun TokenVisitor.acceptAll(tokens: List<Token>): TokenVisitor {
    tokens.forEach { it.accept(this) }
    return this
}

fun main() {
    val input = readLine()!!

    try {
        var tokens = Tokenizer(input).tokenize()
        println(tokens)

        val parserVisitor = ParserVisitor()
        parserVisitor.acceptAll(tokens)
        tokens = parserVisitor.finish()
        println(tokens)

        PrintVisitor().acceptAll(tokens)

        val calculationVisitor = CalcVisitor()
        calculationVisitor.acceptAll(tokens)

        println("\n = ${calculationVisitor.finish()}")
    } catch (e: Throwable) {
        println("${e.message} (${e::class})")
    }
}