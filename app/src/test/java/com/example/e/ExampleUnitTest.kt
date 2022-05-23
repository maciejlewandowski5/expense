package com.example.e

import com.example.e.domain.*
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val expense = Expense(
            id = 1, title = "",
            participants =
            listOf(
                Participant(
                    id = 1,
                    user = User(id = "ola", name = "ola"),
                    amount = BigDecimal("-1")
                ),
                Participant(
                    id = 1,
                    user = User(id = "jarek", name = "jarek"),
                    amount = BigDecimal("-2")
                ),
                Participant(
                    id = 1,
                    user = User(id = "tomasz", name = "tomasz"),
                    amount = BigDecimal("-3")
                ),
                Participant(
                    id = 1,
                    user = User(id = "mikołaj", name = "mikołaj"),
                    amount = BigDecimal("6")
                ),
            ),
            date = sampleDateTime,
            isExternal = isExternal
        )

        //   val result = GroupDetails.splitExpense(expense = expense)
        //   result
    }

    @Test
    fun addition_isCorrect2() {
        val expense = Expense(
            id = 1, title = "",
            participants =
            listOf(
                Participant(
                    id = 1,
                    user = User(id = "ola", name = "ola"),
                    amount = BigDecimal("5")
                ),
                Participant(
                    id = 1,
                    user = User(id = "jarek", name = "jarek"),
                    amount = BigDecimal("4")
                ),
                Participant(
                    id = 1,
                    user = User(id = "tomasz", name = "tomasz"),
                    amount = BigDecimal("3")
                ),
                Participant(
                    id = 1,
                    user = User(id = "mikołaj", name = "mikołaj"),
                    amount = BigDecimal("-12")
                ),
            ),
            date = sampleDateTime,
            isExternal = isExternal
        )

        //   val result = GroupDetails.splitExpense(expense = expense)
        //   result
    }

    @Test
    fun addition_isCorrect3() {
        val expense = Expense(
            id = 1, title = "",
            participants =
            listOf(
                Participant(
                    id = 1,
                    user = User(id = "ola", name = "ola"),
                    amount = BigDecimal("5")
                ),
                Participant(
                    id = 1,
                    user = User(id = "jarek", name = "jarek"),
                    amount = BigDecimal("-5")
                ),
            ),
            date = sampleDateTime,
            isExternal = isExternal
        )

        //   val result = GroupDetails.splitExpense(expense = expense)
        //   result
    }

    @Test
    fun addition_isCorrect4() {
        val expense = Expense(
            id = 1, title = "",
            participants =
            listOf(
                Participant(
                    id = 1,
                    user = User(id = "ola", name = "A"),
                    amount = BigDecimal("-20")
                ),
                Participant(
                    id = 1,
                    user = User(id = "jarek", name = "B"),
                    amount = BigDecimal("10")
                ),
                Participant(
                    id = 1,
                    user = User(id = "tomasz", name = "C"),
                    amount = BigDecimal("-7")
                ),
                Participant(
                    id = 1,
                    user = User(id = "mikołaj", name = "D"),
                    amount = BigDecimal("14")
                ),
                Participant(
                    id = 1,
                    user = User(id = "jan", name = "E"),
                    amount = BigDecimal("17")
                ),
                Participant(
                    id = 1,
                    user = User(id = "marek", name = "F"),
                    amount = BigDecimal("-87")
                ),
                Participant(
                    id = 1,
                    user = User(id = "ala", name = "G"),
                    amount = BigDecimal("92")
                ),
                Participant(
                    id = 1,
                    user = User(id = "monika", name = "H"),
                    amount = BigDecimal("-19")
                ),
            ),
            date = sampleDateTime,
            isExternal = isExternal
        )

        //    val result = GroupDetails.splitExpense(expense = expense)
        //   result
    }

    @Test
    fun addition_isCorrect5() {
        val groupDetails = GroupDetails(
            AccountingGroup(1, ""),
            expenses = listOf(
                Expense(
                    id = 1, title = "",
                    participants =
                    listOf(
                        Participant(
                            id = 1,
                            user = User(id = "ola", name = "A"),
                            amount = BigDecimal("-20")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "jarek", name = "B"),
                            amount = BigDecimal("10")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "tomasz", name = "C"),
                            amount = BigDecimal("-7")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "mikołaj", name = "D"),
                            amount = BigDecimal("14")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "jan", name = "E"),
                            amount = BigDecimal("17")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "marek", name = "F"),
                            amount = BigDecimal("-87")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "ala", name = "G"),
                            amount = BigDecimal("92")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "monika", name = "H"),
                            amount = BigDecimal("-19")
                        ),
                    ),
                    date = sampleDateTime,
                    isExternal = isExternal
                ),
                Expense(
                    id = 1, title = "",
                    participants =
                    listOf(
                        Participant(
                            id = 1,
                            user = User(id = "ola", name = "A"),
                            amount = BigDecimal("-20")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "jarek", name = "B"),
                            amount = BigDecimal("10")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "tomasz", name = "C"),
                            amount = BigDecimal("-7")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "mikołaj", name = "D"),
                            amount = BigDecimal("14")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "jan", name = "E"),
                            amount = BigDecimal("17")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "marek", name = "F"),
                            amount = BigDecimal("-87")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "ala", name = "G"),
                            amount = BigDecimal("92")
                        ),
                        Participant(
                            id = 1,
                            user = User(id = "monika", name = "H"),
                            amount = BigDecimal("-19")
                        ),
                    ),
                    date = sampleDateTime,
                    isExternal = isExternal
                )
            ),
            members = listOf(
                User(id = "ola", name = "A"),
                User(id = "jarek", name = "B"),
                User(id = "tomasz", name = "C"),
                User(id = "mikołaj", name = "D"),
                User(id = "jan", name = "E"),
                User(id = "marek", name = "F"),
                User(id = "ala", name = "G"),
                User(id = "monika", name = "H")
            )
        )

        val result = groupDetails.getPayOffExpense("")
        result
    }
}
