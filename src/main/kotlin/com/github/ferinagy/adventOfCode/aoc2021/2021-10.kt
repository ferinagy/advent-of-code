package com.github.ferinagy.adventOfCode.aoc2021

fun main(args: Array<String>) {
    println("Part1:")
    println(part1(testInput1))
    println(part1(input))

    println()
    println("Part2:")
    println(part2(testInput1))
    println(part2(input))
}

private fun part1(input: String): Int {
    return input.lines().map { it.status() }.filterIsInstance<Status.Corrupted>().sumOf { it.score }
}

private fun part2(input: String): Long {
    val sorted = input.lines().map { it.status() }.filterIsInstance<Status.Incomplete>().map { it.score }.sorted()

    return sorted[sorted.size / 2]
}

private sealed class Status {
    class Corrupted(val score: Int) : Status()
    class Incomplete(val score: Long) : Status()
}

private fun String.status(): Status {
    val corruptedTable = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val incompleteTable = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    val stack = ArrayDeque<Char>()
    var position = 0
    while (position < length) {
        when (val current = get(position)) {
            '(' -> {
                stack.addFirst(')')
            }
            '[' -> {
                stack.addFirst(']')
            }
            '<' -> {
                stack.addFirst('>')
            }
            '{' -> {
                stack.addFirst('}')
            }
            '>', ')', ']', '}' -> {
                val first = stack.removeFirst()
                if (first != current) return Status.Corrupted(corruptedTable[current]!!)
            }
            else -> error("Unexpected char $current")
        }

        position++
    }

    if (stack.isEmpty()) error("Complete line")

    val score = stack.fold(0L) { acc, char -> acc * 5 + incompleteTable[char]!! }
    return Status.Incomplete(score)
}

private const val testInput1 = """[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]"""

private const val input = """<[<<({{<{<[[([()][()()])<{[]<>}{<>}>><<<[]{}>>{<<><>>({}{})}>][<<<{}<>>([])>{<{}><()>}>]><(<{(()[]){[](
<{([({{[(<[({({}{})[()<>]}{{<>{}}[(){}]})]>({(<<<><>>{{}()}>)<<<[]><{}<>>><[{}[]]<<>[]>>>}))[{(<{([]
([<([({(<([<({()()}(<>[]))[<[]()>{<>{}}]>]<{{{{}{}}{()()})({[]}[<>{}])}>)>)}){<<{{(<<<()<>><<><>>>
[{{[<<[{([[{{[<>{}]<()<>>}[(()<>)[{}<>]]}<<[{}[]]([]<>)>{{[]{}}{()()}}>]][[{<({}{})<[]{}>>
<<{{<([(((([{<[]<>>{()<>}}(<<>[]>{(){}})]([{{}<>}<[]>](<[][]><{}()>)))))[({{{<[]{}>[()]}<<<><>>
<(<[{(<[{{{<{[[]<>]{<>()}}><[[{}()][(){}]]>}}<[<[<()()>({}<>)]><[{[]()}([][])](<(){}>[<><>])>]{
{[[<{{<<[[<<(({}<>){<><>})[[<>()][[]{})]>[[[{}[]]<<>>]{(<>[])}]>{{<<()[]>{<>{}}>[{[]<>}<[]{}>]
({[((((([{[({[{}[]]<[]()>}[({}{})<<>[]>])<[(()[])<{}()>]{[[]<>]{[]()})>](([({}<>)(()[])][<[]{}>])
{[[{[({(<<{<([{}<>])<({}<>)({}<>}>>[<[{}()]([]<>)>((<>())<[]{}>)]}<([(<>())[(){}]]{[{}<>]<[][]>}){<{<><
(<{<<[<<([[([<<><>>]<<{}()><[]<>>>)]][{(({<>{}}(()())))(<[()()]<[]()>>)}[[<<<>()><<>{}>>([{}<>][{}[]])]{<{(
<<[({{{{{[<([((){})([]())]<((){})({}[])>)[{[{}()]{[]<>}}]>([<{{}<>}><({}<>)({}[])>]<[{<>{}}
(([[{{({{{[<<{[]()}({})>{{{}()}[[]<>]}>{<([])>[[[]{}]]}](([<[]{}>]<<[]<>><{}[]>>))}}}(([[[{[<><>
<<[(<[{{<((<[<()>[<><>]][[<><>]([]())]>[({<>})<<()<>><{}[]>>])[[(<[]<>>([]())){{<>[]}(()())}]]){(<(<[]<>>
({[<<[[{<[[{{[()()]{()[]}}}]<{([{}{}]{[]()})[[()[]]<<>>]}>]>}[({((<{<><>)>{{[]()}{{}[]}}){([{}[]]<{}{}>)<
([{{<{[(<[<<[({}())({}())]>>[{[<<>[]>[<>()]]<(<>[]){()[]}>}(<<<>[]>[<>[]]>{<<>[]>{{}()}})]]{
<[{{<[((<([[{<<><>>{()}}{<{}[]><[]()]}]])<{({<()>{()[]}}(<(){}><[]<>>)){[({}[]){<>[]}]}}<<([()()]<()(
<{<{[(({(({({<[]>([])}<<()<>>{<>()}>){[[()[]]<{}()>]{({}[])[()<>]}}}<[<<{}()><[]{}>>({[][]}[()<>])}{<<[]
<[{{[<({(<[<[[()<>]({}[])]([{}[]]([]))>]{[{{<>[]}[[]{}]}(({}<>)<()[]>)]<<[()[]]>({(){}}<{}()>)>}>)}<<[[[[<<>(
{{<(<[((({(<[<[]{}>[(){}]](<{}<>>((){}))>){(({[]{}}<{}[]>){{<><>}[[]{}]}))}<([<(<>)>[<<>{}>[{}
<([({[<[(({[(({}))(<{}()><<>{}>)]<[{{}[]}{{}<>}]{[{}()]{{}()}}>})([[<(<>())({}{})>[<[]<>>[
{((<<<{<(<[[[<{}<>}]<(<><>)>]]<(([(){}]{[]()}))<({{}<>}<()<>>){<()<>>{(){}}}>>>{({({()[]}[<>
([<[[((<(<[<([{}[]])>{<(()<>)((){})>{<<>>(()<>)}}][{[(<>[])(<>[])]}[[{{}[]}[()()]]<<[]<>>{(){}}>]]><({(
[{<([[[{([{<<([]<>){<><>}>[<(){}>]><{[<>[]](()<>)}[{()()}]>}<({{{}{}}}{[{}{}]<<>()>})<(<[]<
(<({[{{[[(<[<(<>[])[[][]]>]{((<>[])({}<>))({[]<>})}>({<[()[]]]<{()[]}>})){(<(<<>[]>[<>{}])>{[({}<
(<{<(({[(({[[<<>{}>({}<>)]{({}{}){(){}}}]{(<(){}>[[][]])<{()<>}>}}[[[{[]{}}[()]]](([[]()][(){}]))])
([({<{[{<[{<({{}()}{{}()})>}]>}<<[[{{((){})<<>{}>}(({}[]){<>[]})}]]><[{{<({}<>)(()())>[{<>()}{{}()}]}{<{
<[{[({[<<[[<<{{}{}}<{}{}>>([<>][(){}])><<<{}{}>>>]]{([{[[]()][[][]]}<{[]<>}>]<<[<>[]][[]()]><<()[
[{({{{<<{[{([[()]({}[])]{[()()]})(((<>{})[[][]])<{{}{}}>)}]}>>}[((({{<([[]()][[]])}}{{<({}[])[{}
{[({<{<<{[(([<<>[]>({}{})>))]{(<{{{}<>}<<>{}>}>)}}><(<(({([]())(<>[])}{{[]()}{[][]}})<{(()[])[<>{}]}>){({
[<((<[<<(<<{<(<>)<[]{}>>[<[]{}><[]()>]}<((()[])((){}))[({}[]){[]{}}]>>([<[{}()][[]{}]>{{()[]}}][{[{}<>]<[]<
[<[({({[([{<(([]<>)({}[]))<{{}[]}[<>[]]>><[<{}[]>({}<>)]<(())>>]]([[[{[]<>}({}[])](<[]{}>{<>()
{[<<{(<[{(<[({[]{}}{[]{}}}]({<()[]><<>[]>}{<[]()>[{}{}]})>)}{[{(((<><>))(<[]<>>(()[])))[[<[]()>[{}()]](((
({[<<([[{(<<<{<>()}<{}<>>>[[[]{}){()}]><[[(){}]<[]<>>]({(){}}[[]{}])>>{[{[()<>][{}()]}][[[{}{}]<<>()
([(<((<{[[[[[{{}()}{()[]}]]<([[]<>])<[()<>][[][]]>>]][{([{()[]}(()[])]({<>{}}{[]()}))}({([[]
(({{[<<{(([{<{()()}[[]()]>{<()<>>[[]{}]}}{[<()()>[[]{}]]{([]())({}[])}}][(<{<>[]}[()()]><<()>(()())>)
[<(<{{{<{(<{(<<>[]><<>()>){<{}()><()<>>}}(<[<>{}]{<>{}}><[{}{}][<>()]>)>)}{{{(<(<><>)(<>())><{[]}([][
<<<{([[<[[[(([[]{}])<({}())(<>())>)({[<>{}]{[]()}}<<(){}>{(){}}>)]{([(()()>({}[])]((<><>)<
{{({<<<[[<(<{{{}<>}({}[])}{<{}<>><<>()>}>{({{}<>}({}()}){[[]<>]([]{})}}){{[[[][]](()())]}([<<>{}
<<[[<{{({<{[{<<>()><()()>}<[()<>]<()()>>]{(([]<>)[{}[]]){{{}<>}{[]<>})}}>{{<[<{}<>>({}())]
({[<[[{<<{{[{(<>{})}]{<[{}{}][[]()]>[[[]{}][()()]]}}[(<<{}[]>{<>[]}>{{{}<>}[(){}]}){{[<>{}]<{}{}>}{
(([<{<[[([[(((()<>))({{}[]}<()<>>))[[({}[])(())]<{(){})>]][{((()){[]()}){{()<>}{<><>}}}[{{()}{()[]}}]]]){{(({
{<[{(<[{[{{[<([]<>)<{}<>>>]}}]}]><{{<[[([{<>()}[[]{}]]({{}()}{<>{}}))]{([<[]{}>(()<>)]<[[]<>]([])
{[<<[<(<[{<<[{{}()}{<>{}}](<{}<>>({}<>))>><{<({}[])<()<>>>[[<>[]][<><>]]}[<(<><>){{}<>}>[<[]()>{[]}]]>}]{[<{<
(<({{[{<(<<<{[{}<>]({}[])}{[[]{}]<()[]>}>(<[[]()][()()]><[()[]][<>()]>)>>[(<<<{}()>{<><>}>>){<<{<><>}(
(<{[[{{<(<(<({{}<>}{{}()}){[()<>]<{}[]>}>[(<{}{}><[]()>)<({}<>){()()}>]){[[([]{}){{}[]}]<[{}{}]<[][]>>][{<()
[(<([({[[[<[[[[][]][{}{}]]([{}()]{<>{}})]{<{()()}<()<>>><{{}<>}<(){}>>}>]{{[([[][]]<()()>)
(<(<<({{(<[{{[()<>][<>{}]}[<[]<>>]}<<{[]{}}>[[{}()]{()()}]>]{[([()()]<<>>)({()<>}[<>[]])][{{[][]}(<>())}
[[((({(({{{[{{<>[]}[<>{}]}[[{}<>]([]())]][{(<><>)}]}][(<{{[][]}(<><>)}<[[]()]>>([[{}()]<[]<>>][<[][]>[()()]])
<{({(([((<[<{(()[])<[][]>}{<<><>><[]<>>}><<([]<>)([]<>)>>]{[<([]{}]{<>{}}>]}>[{[(([]{})[{}{}])[<<>{}
{(([<{(({<({{{{}{}}}[[[]]{[]()}]}{<[[]]<<>()>>(<()>[()<>])})>{{[(([]<>){{}()}){[{}]{{}<>}}]}([[([]{})]<[[](
<{[({<{[[[{{{[{}{}]}}([<[][]>[<>[]}][<<>()>{()}])}((<({}[])[<><>]>(((){})[{}()]))[[<{}[]>[(
{<[([(<((<<<<[{}[]]({}{})>((()())[(){}])><([[]{}][()<>])<<<>[]>([]<>)>>><[[((){}){[][]}}{[[]]
[{(<{<{<<({({(()<>){{}[]}}([[]()]{()<>}))(([[]<>](())))}<{({()[]}{[][]})}])>>}>(({[{[{{([]())(<><>)
<[[<([<<({[[(<[]()><<><>>)({<>})]{{<<><>>[()[]]}([()<>](()[]))}]((<{<>{}}<(){}>>))}<<[(<(){}><<>{}>)[[()[
<[{<<<{[{<{(<[<>{}][{}()]>((<>[])[<><>]))<[({}<>)<<>[]>]<(<><>)(<>{})>>}({<<[][]>([]<>)>([<>{}><<>[]>
[<<(<{((<([<({{}{}](<>[]))[[()<>]([][])]>((([])<(){}>))]){<{{<()[]>(()<>)}(<[]<>>([][]))}><[([(
{[[[{{<[[{<[<<<>[]>{<><>}>{<<>{}><[]<>>}]((<{}()>(<>[])))><<{<<>{}>{(){}}}([{}{}]<<>()>)><[{<>{}}]>>}]
((((([[(([{<({()[]}<()<>>)><<<<>()>({}{})>{{(){}}(<>[])}>}<<<{<><>}>><(((){})<[]()>){{()()}({}<>)}>
<<[<[{<[<[<{[[()[]]({}[])]{<{}<>>[{}{}]}}[([[][]])[([]())[[]<>]]]>[(<<{}()>(()<>)>{{[]{}}<{}{}>})({([
[{<(<{(<[<[[[{()<>}[[][]]][(()<>){{}[]}]](<{<>{}}[()[]]>(<[]>[{}()]))]([([<>{}]<()[]>){{<>{}}{()
<[<({<({[{({{(<>{}){[]()}}<{[]<>}[(){}]>}({[<>[]]{[]<>}}))<{([{}[]])(({}[]){(){}})}[{[[]<>]}[({}())[(){}]]]
[[[{<[((<[<{([[]<>]{<><>})<<{}[]>[[][]]>}{[[<>{}]]<<{}>([]<>)>}>{<{{<>[]}}[(<><>)[[]<>]])<{[(){}]<()()>}>}]{{
([(<({[[{<([{{<>{}}([]<>)}[[{}[]]{<>{}}]](<[{}<>]{[]()}><((){})[[][])>)){({{{}{}}{{}{}}}[<{}{}>({}<
<[{(((({[[{{({{}[]]<<>>)<{[]()}<()<>>>}((<(){}>([]{})))}{({[()()]([]<>)}<([]<>)[()()]>)<[{{}[
<[({[<<<<{[({{(){}}[{}<>}}(<<>[]>[[]()])){(<()[]><<>()>)}]}><({<[{()()}]({[]{}}<{}>)>[({[]
<<([[<{{([{<{([]{})[(){}]}{([]{})<{}[]>}><<(<>{})[<>()]>({{}<>})>}<[(<<>()><{}()>)<{()<>}<<>>
<[[[({[({<[{((<>[]]<{}<>>){<<>()>{[]()}}}<{<{}{}>[<>{}]}{{{}[]}{{}()}}>]{<{{()()}<[]<>>}((
[[({(([[(([<[([]<>){{}()}]>(<{{}{}}(()<>)>[{<><>}{{}<>}])]{{<[[]<>]>[[{}()]]}([<{}()>][([]{})<{}
<(([<{([[<[((([]<>)<<><>>){[[]()]({}{})})({{(){}}((){}]}({{}()}))]([<{<>()}<[]{}>><{[]{}}<(){}>>])>
<<[[{<<{[(<<{{[][]}[[]<>]}<<[][]><<>[]>]>([{<>}[<>[]]]{{[]()}<{}()>})>{({({}<>){[]<>}}[{<>{}}[<>[]]
((<<<<[[(({[[<()<>>[(){}]]{(<>()){<>{}}}]}{(([<>[]><<>()>)<<(){}><()()>>)[(<{}{}>((){}))[{[][]}(()<>)]]})){
[({(<{{<<<{<[{[]()}<<><>>][[()[]][<>[]]]>}>([([<[]<>>]{<<>()><()<>>})({<{}[]><()()>}[[[]()]])]<{<(()
([(<({[([{{{((()())[<>[]]){{[]<>}[{}{}]}}([{()<>}<{}()>])}}<{[({(){}}(<><>)}<(<>())>][<[(){}]{(){}}
{[[{((((([[{{[[]{}]({}{})}{[[]<>](()[])}}[{[()()]}((<>{})<(){}>)]]{(<{<>()}<(){}>>){(<{}()>{{}<>})({<>(
([[{<[<[[<<{<{()<>}[<>()]>[[[]{}]{{}{}}]}<[{{}[]}]>>{<{{<>{}}([]())}>[(<<>()>)(<{}()>[<>()])]}>{({<[()<>]
{(([{[{{(({(<[()[]][<>[]]>)<[(()<>){{}{}}]>}[(<<{}{}><{}<>>>){<[()()]<()()>>({[][]}<<><>>)}
<[{<(<<[{[({[({}<>)<<>[]>](<<>()>(<><>))})[([{<><>}<()()>])]]}]><<[<({{{<>}}<([]][<>{}]>}[<(())<<><>>>])
(<(({<[[{{[<[{<>[]}[()<>])((()()){{}[]})>]}}]]((<<<((({}{})<{}>){{{}<>}{<>}})[({{}()}{<>()}){{{}()}}]
{[<[{(([<[{{[([]{})<[][]>]{([][])[()<>]}}<<(()<>)>>}[[(({}{}){{}<>})]]]{{[(<{}{}>[{}()])({()()}
[{{((<<[[((<<{[]<>}<{}>>(<{}<>>{[]()})>({<()>([][])><[{}{}]{{}()}>))(({([]<>)([]())}<{[][]}<()>>)
({([{({[({[<({{}{}}([]()))(((){}){{}{}}>>({(()()){{}()}})]<[[{{}{}}<{}[]>]{<[][]><<>[]>}]({<<>[]>
{[({[{{[<[{([<{}{}>[(){}]](<{}<>>{{}()}))([([]()){()[]}]([{}<>]{<>{}}))}]>[{{[[{[][]}]]<((()[])([]{}))<[()
[[(({{{(([[[{[()()]([]())}((<>){{}{}])]]({<[<>]({}{})>([<>{}]{{}{}})})]))}[(<[[(([[][]]{<>{}}){{(){}}})
<(<({<{<[<(<{<()><<><>>}<<{}[]>([]<>)>>)<{<{[]{}}[{}()]>}([(()[]){[]<>}]{{<>()}{{}<>}})>}{[[{(()[]){(
[(<(<([{<[{<(<()>([]<>))[{()()}<{}<>>]><{([]())<<>()>}(<{}{}>[()[]])>}]<{<({[][]}<[]()>){{{}[
[<({<{(<<[[[<{()[]}{{}[]}>{[<><>]}]{[<()<>>({}<>)]}]][[{<[<>[]>([]())><{()()}{[]{}}>}([({}{})[[](
<<[[{{<<([[{[(()())[()]][[{}[]]{{}}]}[[({}<>){[]{}}]{{<><>}[()[]]}]]{<[(<>())]>{({{}()}(()<>))}}])
{<[{({[({{<({[{}()]([][])}([{}<>]{[]()}))[<(<><>)([]{})><{()()}(<>[])>]>{{(<[]<>>[{}])}[<{(){}}(<>
<{{(({([[<[{<(<>)([]{})>{<<>{}>{(){}}}}]{[<([]{})<{}{}>>[[<>{}]{()<>}]][{{<><>}([]())}({()<>}<()>)]}>]
{<{[{[({[(({<[{}()]({}[]}>[<()>]}{(<<>>)}))]})]{<{<{[[{(<>())[()[]]}[[{}{}][[]]]][([[]()]<{}<>>)[<[]{}>([]<>
((<{{<[[<{{([([]{})<<>()>]<(<>{})(()<>)))([{()()}(<><>)]<(<>)(<>{})>)}([<{(){}}<<>{}>>]<[([]<
[<<<{(((<<{([<()[]>({})]({<>()}(()())))({([])<<>>}((()[])[()[]]))}{<[<{}()>]<[{}{}]({}<>)>>{<<
[[[<<<<[{(<<[{{}[]}]<(<><>)(<>[])>><<<<><>][[][]]>(([]{}))>>([<[[]<>]<[]<>>><{()<>}([][])>]))}<(
<<{[[[{{{{[[(([]{}))([<>[]]<()>)][{(<>()){[]()}})]{(({<><>}{<>[]})[<<>[]>[()()]])}}}<{(<((<><>)(<>{}))<{"""