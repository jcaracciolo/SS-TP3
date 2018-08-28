object StatsPrinter {

    val collisionTimes = ArrayList<Double>()
    val velocities = ArrayList<ArrayList<DoubleArray>>()

    fun printStats(particles: ArrayList<Particle>, deltaTimeCollision: Double) {

    }

    fun saveCollisionTime(deltaTimeCollision: Double) {
        collisionTimes.add(deltaTimeCollision)

    }

    fun saveVelocities(particles: ArrayList<Particle>, deltaTimeCollision: Double) {
        val velocitiesCurrentEvent = ArrayList<DoubleArray>()
        particles.forEach{
            velocitiesCurrentEvent.add(doubleArrayOf(it.getSpeed(), deltaTimeCollision))
            velocities.add(velocitiesCurrentEvent)
        }
    }

}