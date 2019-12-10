import java.util.*
import kotlin.collections.ArrayList
import Player as Player
import kotlin.collections.MutableCollection as MutableCollection1

val cardsGlobal = mapOf(1 to "2 Diamond", 2 to "3 Diamond", 3 to "4 Diamond", 4 to "5 Diamond", 5 to "6 Diamond",
    6 to "7 Diamond", 8 to "8 Diamond", 8 to "9 Diamond", 9 to "10 Diamond", 10 to "Jack Diamond",
    11 to "Queen Diamond", 12 to "King Diamond", 13 to "Ace Diamond", 14 to "2 Heart", 15 to "3 Heart", 16 to "4 Heart",
    17 to "5 Heart", 18 to "6 Heart", 19 to "7 Heart", 20 to "8 Heart", 21 to "9 Heart", 22 to "10 Heart", 23 to "Jack Heart",
    24 to "Queen Heart", 25 to "King Heart", 26 to "Ace Heart", 27 to "2 Spade", 28 to "3 Spade", 29 to "4 Spade", 30 to "5 Spade",
    31 to "6 Spade", 32 to "7 Spade", 33 to "8 Spade", 34 to "9 Spade", 35 to "10 Spade", 36 to "Jack Spade", 37 to "Queen Spade",
    38 to "King Spade", 39 to "Ace Spade", 40 to "2 Club", 41 to "3 Club", 42 to "4 Club", 43 to "5 Club", 44 to "6 Club",
    45 to "7 Club", 46 to "8 Club", 47 to "9 Club", 48 to "10 Club", 49 to "Jack Club", 50 to "Queen Club", 51 to "King Club", 52 to "Ace Club")

class Player(p: Long) {
    val CD = generateCD(p)
    var cardEncrypt: Pair<Long, Long> = Pair(0, 0)
    var cardDecrypt: Pair<Long, Long> = Pair(0, 0)
}

fun generateCD(p: Long): Pair<Long,Long> {
    var c: Long
    var d: Long
    while(true) {
        c = generatePrivateKey(p)
        d = gcd(c, p - 1).second
        if (modularPow(c * d, 1, p - 1) == 1.toLong()) {
            break
        }
    }
    return Pair(c, d)
}

fun generatePlayers(P: Long): Array<Player> {
    val n = generatePrivateKey(26)
    return Array(n.toInt(), {i -> Player(P)})
}

fun encryptCards(cards: MutableList<Long>, C: Long, P: Long): MutableList<Long> {
    for(i in cards.indices) {
        cards[i] = modularPow(cards[i], C, P)
    }
    cards.shuffle()
    return cards
}

fun playerEncryptCard(player: Array<Player>, cards: MutableList<Long>, P: Long): MutableList<Long> {
    var tempCards = cards
    for(i in player.indices) {
        tempCards = encryptCards(cards, player[i].CD.first, P)
    }
    return tempCards
}

fun deckInit(player: Array<Player>, cards: MutableList<Long>): Array<Player> {
    var checkout = 0
    for(i in player.indices) {
        player[i].cardEncrypt = Pair(cards[checkout], cards[checkout + 1])
        checkout += 2
    }
    return player
}

fun decryptCards(player: Array<Player>, cards: Pair<Long, Long>, P: Long): Pair<Long, Long> {
    var cardOne = cards.first
    var cardSecond = cards.second
    for (i in player.indices) {
        cardOne = modularPow(cardOne, player[i].CD.second, P)
        cardSecond = modularPow(cardSecond, player[i].CD.second, P)
    }
    return Pair(cardOne, cardSecond)
}

fun playerDecryptCard(player: Array<Player>, cards: MutableList<Long>, P: Long): Array<Player> {
    for(i in player.indices) {
        player[i].cardDecrypt = decryptCards(player, player[i].cardEncrypt, P)
    }
    return player
}

fun main() {
    var P: Long
    while (true) {
        P = generatePQg(10000).first
        if (P > 52 ) break
    }

    var player = generatePlayers(P)

    var cards = MutableList(52, {i -> (i + 1).toLong()})

    cards = playerEncryptCard(player, cards, P)

    player = deckInit(player, cards)

    player = playerDecryptCard(player, cards, P)

    for(i in player.indices) {
        println("------------------")
        println("Player: ${i}")
        println("Card first: ${cardsGlobal[player[i].cardDecrypt.first.toInt()]}")
        println("Card second: ${cardsGlobal[player[i].cardDecrypt.second.toInt()]}")
        println("------------------")
    }
}