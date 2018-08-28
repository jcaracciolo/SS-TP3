data class Particle(val id: Int, var position: Vector, var velocity: Vector, val mass: Double, val radius: Double) {
    var collisionCount: Int = 0

    fun calculateNewPosition(deltaTime: Double) {
        position += velocity.scaledBy(deltaTime)
    }

    fun collisionResult(newVelocity: Vector) {
        velocity = newVelocity
        collisionCount++
    }

    fun getSpeed(): Double {
        return Vector.norm(velocity)
    }


}