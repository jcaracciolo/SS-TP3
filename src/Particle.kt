import java.lang.IllegalStateException

data class Particle(val id: Int, var position: Vector, var velocity: Vector, val mass: Double, val radius: Double){
    var collisionCount: Int = 0

    fun calculateNewPosition(deltaTime: Double) {
        val deltaDistance =  velocity.scaledBy(deltaTime)
        velocity += deltaDistance
    }

    fun collisionResult(newVelocity: Vector) {
        velocity = newVelocity
        collisionCount++
    }


}