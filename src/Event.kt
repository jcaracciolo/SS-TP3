class Event(val tc: Double, val results: Array<Result>)

class Result(val particle: Particle, val newVelocity: Velocity, val collisionNumber: Int)