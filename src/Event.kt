class Event(val tc: Double, val results: Array<CollisionResult>)

class CollisionResult(val particle: Particle, val newVelocity: Vector, val collisionNumber: Int)