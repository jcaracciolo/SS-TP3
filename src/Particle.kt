open class Particle(val id: Int, var position: Vector, var velocity: Vector, val mass: Double, val radius: Double) {
    var collisionCount: Int = 0

    open fun calculateNewPosition(deltaTime: Double) {
        position += velocity.scaledBy(deltaTime)
    }

    open fun collisionResult(newVelocity: Vector, eventType: EventType) {
        velocity = newVelocity
        collisionCount++
    }

    fun getSpeed(): Double {
        return Vector.norm(velocity)
    }

}

class TrackableParticle(id: Int, position: Vector, velocity: Vector, mass: Double, radius: Double, val name: String)
    : Particle(id, position, velocity, mass, radius){

    val initialPosition = position
    val positions = ArrayList<Vector>()
    var hasHitWall = false

    init {
        positions.add(initialPosition)
    }

    override fun calculateNewPosition(deltaTime: Double) {
        super.calculateNewPosition(deltaTime)
        if(!hasHitWall){
            positions.add(position.copy())
        }
    }

    override fun collisionResult(newVelocity: Vector, eventType: EventType) {
        super.collisionResult(newVelocity, eventType)
        hasHitWall = eventType == EventType.WALL_COLLISION;
    }

}