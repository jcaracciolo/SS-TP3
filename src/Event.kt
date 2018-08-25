class Event(val tc: Double, val results: Array<Result>)

class Result(val particle: Particle, val newVelocity: Vector, val collisionNumber: Int)