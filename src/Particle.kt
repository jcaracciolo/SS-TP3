data class Particle(val id: Int, var x: Double, var y: Double, var velocity: Velocity, val mass: Double, val radius: Double){
    var collisionCount: Int = 0
}


data class Velocity(val vx: Double, val vy: Double)