package clock

import java.time.Instant

class ControllableClock(private var now: Instant) : Clock {
    override fun now() = now

    fun setNow(now: Instant) {
        this.now = now
    }
}