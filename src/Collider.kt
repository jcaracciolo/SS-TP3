// Two particle collider / Singleton
object Collider {
    fun collide(first: Particle, second: Particle, time: Double): Event? {
        val sigma: Double = first.radius + second.radius
        val deltaR: Vector = second.position - first.position
        val deltaV: Vector = second.velocity - first.velocity
        val dVdR: Double = deltaV * deltaR
        val normDeltaV: Double = deltaV * deltaV
        val normDeltaR: Double = deltaR * deltaR
        val sigmaSquared = sigma * sigma

        // Calculate collision time
        val d = (dVdR * dVdR) - normDeltaV * (normDeltaR - sigmaSquared)

        val collisionTime =
                if (d >= 0 && dVdR < 0) {
                    -(dVdR + Math.sqrt(d)) / normDeltaV
                } else {
                    null
                }

        collisionTime ?: return null // No collision

        // Calculate new relative vectors on collision
        val firstNewPosition = advancePosition(first, collisionTime)
        val secondNewPosition = advancePosition(second, collisionTime)
        val collisionDeltaR = secondNewPosition - firstNewPosition
        val collisionDvDr = deltaV * collisionDeltaR

        // Calculate new velocities based on momentum conservation
        val impulse = collisionDeltaR.scaledBy((2 * first.mass * second.mass * collisionDvDr) / (sigmaSquared * (first.mass + second.mass)))
        val firstNewVelocity = newVelocity(first, impulse)
        val secondNewVelocity = newVelocity(second, impulse.scaledBy(-1.0))

        // Create collision results
        val firstCollisionResult = CollisionResult(first, firstNewVelocity, first.collisionCount + 1)
        val secondCollisionResult = CollisionResult(second, secondNewVelocity, second.collisionCount + 1)

        // Return event
        return Event(time + collisionTime, arrayOf(firstCollisionResult, secondCollisionResult), EventType.PARTICLE_COLLISION)
    }

    private fun advancePosition(p: Particle, time: Double): Vector = Vector(p.position.x + p.velocity.x * time, p.position.y + p.velocity.y * time)


    private fun newVelocity(particle: Particle, impulse: Vector): Vector {
        val v = particle.velocity
        val m = particle.mass
        return Vector(v.x + impulse.x / m, v.y + impulse.y / m)
    }


}