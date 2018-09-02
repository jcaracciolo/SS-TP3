import com.sun.org.apache.xpath.internal.operations.Bool

open class Particle(val id: Int, var position: Vector, var velocity: Vector, val mass: Double, val radius: Double) {
    var collisionCount: Int = 0

    open fun calculateNewPosition(deltaTime: Double, timestamp: Double, track: Boolean = false) {
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
    val positions = ArrayList<Pair<Double, Vector>>()
    var hasHitWall = false

    init {
        positions.add(0.0 to initialPosition)
    }

    override fun calculateNewPosition(deltaTime: Double, timestamp: Double, track: Boolean) {
        super.calculateNewPosition(deltaTime, timestamp, track)
        if(track && !hasHitWall){
            println("ts $timestamp")
            positions.add(timestamp to position.copy())
        }
    }

    override fun collisionResult(newVelocity: Vector, eventType: EventType) {
        super.collisionResult(newVelocity, eventType)
        if(!hasHitWall && eventType == EventType.WALL_COLLISION){
            hasHitWall = true
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrackableParticle

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}