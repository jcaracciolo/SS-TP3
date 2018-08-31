open class Particle(val id: Int, var position: Vector, var velocity: Vector, val mass: Double, val radius: Double) {
    var collisionCount: Int = 0

    open fun calculateNewPosition(deltaTime: Double, eventType: EventType) {
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

class TrackableParticle(id: Int, position: Vector, velocity: Vector, mass: Double, radius: Double, val name: String)
    : Particle(id, position, velocity, mass, radius){

    val initialPosition = position
    val positions = ArrayList<Vector>()
    var hasHitWall = false

    init {
        positions.add(initialPosition)
        StatsPrinter.addTrackedParticle(this)
    }

    override fun calculateNewPosition(deltaTime: Double, eventType: EventType) {
        super.calculateNewPosition(deltaTime, eventType)
        when(eventType){
            EventType.MOVEMENT -> if(!hasHitWall) positions.add(position.copy())
            EventType.WALL_COLLISION -> hasHitWall = true
        }

    }








}