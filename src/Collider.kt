// Two particle collider / Singleton
object Collider {
    fun collide(first: Particle, second: Particle, time: Double): Event? {
        val sigma: Double = first.radius + second.radius
        val deltaR: Vector = second.position - second.position
        val deltaV: Vector = second.velocity - first.velocity
        val dVdR: Double = deltaV * deltaR
        val normDeltaV: Double = deltaV * deltaV
        val normDeltaR: Double = deltaR * deltaR
        val sigmaSquared = sigma * sigma

        // 1. Calculate collision time
        val d = (dVdR * dVdR) - normDeltaV * (normDeltaR - sigmaSquared)

        val collisionTime =
                if (d > 0 && dVdR >= 0) {
                    -(dVdR + Math.sqrt(d)) / normDeltaV
                } else {
                    null
                }

        collisionTime ?: return null // No collision

        // 2. Calculate new velocities
        val jConservation = deltaR.scaledBy((2 * first.mass * second.mass * dVdR) / (sigmaSquared * (first.mass + second.mass)))

        val firstNewVelocity = newVelocity(first, jConservation)
        val secondNewVelocity = newVelocity(first, jConservation.scaledBy(-1.0))


        // 3. Create collision results
        val firstCollisionResult = CollisionResult(first, firstNewVelocity, first.collisionCount + 1)
        val secondCollisionResult = CollisionResult(second, secondNewVelocity, second.collisionCount + 1)

        // 4. Return event
        return Event(time + collisionTime, arrayOf(firstCollisionResult, secondCollisionResult))
    }

    private fun newVelocity(particle: Particle, j: Vector): Vector {
        val v = particle.velocity
        val m = particle.mass
        return Vector(v.x + j.x / m, v.y + j.y / m)
    }


}