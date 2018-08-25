data class Particle(val id: Int, var position: Vector, var velocity: Vector, val mass: Double, val radius: Double){
    var collisionCount: Int = 0


}