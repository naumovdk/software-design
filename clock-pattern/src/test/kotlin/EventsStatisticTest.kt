import clock.ControllableClock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statistic.EventsStatistic
import statistic.EventsStatisticImpl
import java.time.Duration
import java.time.Instant
import kotlin.math.abs

class EventsStatisticTest {
    private lateinit var now: Instant
    private lateinit var clock: ControllableClock
    private lateinit var eventsStatistic: EventsStatistic

    private val events = listOf("event0", "event1", "event2")

    @BeforeEach
    fun initializeClock() {
        now = Instant.now()
        clock = ControllableClock(now)
        eventsStatistic = EventsStatisticImpl(clock)
    }

    private fun incClock(hoursAmount: Long) {
        now = now.plus(Duration.ofHours(hoursAmount))
        clock.setNow(now)
    }

    private fun assertDoubleEquals(a: Double, b: Double) {
        assertTrue(abs(a - b) <= 1e-9)
    }

    @Test
    fun deleteTest() {
        eventsStatistic.incEvent(events[0])
        eventsStatistic.incEvent(events[1])
        incClock(1)
        assertDoubleEquals(0.0, eventsStatistic.getEventStatisticByName(events[0]))
        assertDoubleEquals(0.0, eventsStatistic.getEventStatisticByName(events[1]))
        eventsStatistic.incEvent(events[0])
        eventsStatistic.incEvent(events[1])
        eventsStatistic.incEvent(events[1])
        eventsStatistic.incEvent(events[2])
        incClock(1)
        assertDoubleEquals(0.0, eventsStatistic.getEventStatisticByName(events[0]))
        assertDoubleEquals(0.0, eventsStatistic.getEventStatisticByName(events[1]))
        assertDoubleEquals(0.0, eventsStatistic.getEventStatisticByName(events[2]))
    }

    private fun assertMapsEqual(expected: Map<String, Double>, actual: Map<String, Double>) {
        assertEquals(expected.keys, actual.keys)
        for ((key, value) in expected) {
            assertDoubleEquals(value, actual.getValue(key))
        }
    }

    @Test
    fun getEventStatisticByNameTest() {
        eventsStatistic.incEvent(events[1])
        incClock(1)
        eventsStatistic.incEvent(events[0])
        eventsStatistic.incEvent(events[0])
        eventsStatistic.incEvent(events[2])
        assertDoubleEquals(2.0 / 60, eventsStatistic.getEventStatisticByName(events[0]))
        assertDoubleEquals(0.0, eventsStatistic.getEventStatisticByName(events[1]))
        assertDoubleEquals(1.0 / 60, eventsStatistic.getEventStatisticByName(events[2]))
    }

    @Test
    fun getAllEventsStatisticTest() {
        eventsStatistic.incEvent(events[0])
        eventsStatistic.incEvent(events[1])
        eventsStatistic.incEvent(events[1])
        eventsStatistic.incEvent(events[2])
        eventsStatistic.incEvent(events[2])
        eventsStatistic.incEvent(events[2])
        assertMapsEqual(
            eventsStatistic.getAllEventsStatistic(),
            mapOf(Pair(events[0], 1.0 / 60), Pair(events[1], 2.0 / 60), Pair(events[2], 3.0 / 60))
        )
    }

    @Test
    fun randomTest() {
        val passed = (0..1).random().toLong()
        val amount0 = (1..10).random()
        val amount1 = (1..10).random()
        (0 until amount0).forEach { _ -> eventsStatistic.incEvent(events[0]) }
        (0 until amount1).forEach { _ -> eventsStatistic.incEvent(events[1]) }
        incClock(passed)
        var expected = mapOf<String, Double>()
        if (passed < 1) {
            expected = mapOf(Pair(events[0], amount0 / 60.0), Pair(events[1], amount1 / 60.0))
        }
        assertMapsEqual(expected, eventsStatistic.getAllEventsStatistic())
    }
}