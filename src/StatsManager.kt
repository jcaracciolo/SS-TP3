import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class Stats {

    val collisionTimes = ArrayList<Double>()
    val velocities = ArrayList<ArrayList<Double>>()
    val trackedParticles = ArrayList<TrackableParticle>()


    fun saveCollisionTime(deltaTimeCollision: Double) {
        collisionTimes.add(deltaTimeCollision)

    }

    fun saveVelocities(particles: ArrayList<Particle>) {
        val speeds = ArrayList<Double>()
        particles.forEach{
            speeds.add(it.getSpeed())
        }
        velocities.add(speeds)
    }

    fun addTrackedParticle(trackableParticle: TrackableParticle) {
        trackedParticles.add(trackableParticle)
    }

}