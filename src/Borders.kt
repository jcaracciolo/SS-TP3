class Borders(val worldWidth: Double, val worldHeight: Double) {

    fun nextCollision(particle: Particle, currentTime: Double): Event? {
        val v = particle.velocity
        val horizontalCollision = horizontalCollisionTime(particle) ?: Double.POSITIVE_INFINITY
        val verticalCollision = verticalCollisionTime(particle) ?: Double.POSITIVE_INFINITY

        if (horizontalCollision == Double.POSITIVE_INFINITY && verticalCollision == Double.POSITIVE_INFINITY) { // La particula esta quieta
            return null
        }

        val (newVelocity, collisionTime) = if (horizontalCollision < verticalCollision) {
            Vector(-particle.velocity.x, particle.velocity.y) to currentTime + horizontalCollision
        } else {
            Vector(particle.velocity.x, -particle.velocity.y) to currentTime + verticalCollision
        }

        val result = CollisionResult(particle, newVelocity, particle.collisionCount + 1)

        return Event(collisionTime, arrayOf(result))
    }

    private fun horizontalCollisionTime(particle: Particle): Double? = collisionTime(worldWidth, particle.position.x, particle.radius, particle.velocity.x)
    private fun verticalCollisionTime(particle: Particle): Double? = collisionTime(worldHeight, particle.position.y, particle.radius, particle.velocity.y)


    private fun collisionTime(positiveBorderLimit: Double, centerCoordinate: Double, radius: Double, velocityComponent: Double): Double? {
        if (velocityComponent == 0.0) return null

        val positive = velocityComponent > 0
        val borderLimit = if (positive) positiveBorderLimit else 0.0
        val particleEdge = centerCoordinate + (if (positive) radius else -radius)
        return (borderLimit - particleEdge) / velocityComponent
    }
}