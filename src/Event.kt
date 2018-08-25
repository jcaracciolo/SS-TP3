class Event(val tc: Double, val results: Array<CollisionResult>) : Comparable<Event> {
    override fun compareTo(other: Event): Int {
        return tc.compareTo(other.tc)
    }
}

class CollisionResult(val particle: Particle, val newVelocity: Vector, val collisionNumber: Int)