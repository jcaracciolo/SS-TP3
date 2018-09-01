import java.util.*
import kotlin.collections.ArrayList

class ParticleGenerator(
        val worldWidth: Double,
        val worldHeight: Double,
        val bigParticleRadius: Double,
        val bigParticleMass: Double,
        val smallParticleRadius: Double,
        val smallParticleMass: Double,
        val maxVelocity: Double,
        seed: Long = 0,
        val EPSILON: Double,
        val hardCodedSeparation: Double) {


    var idCount: Int = 0 // TODO check if random generation is not creating too many particles

    val rand = Random(seed)

    private fun generateSmallParticle(): Particle {
        val speed = rand.nextDouble() * maxVelocity;
        val angle = 2 * Math.PI * rand.nextDouble();

        return Particle(idCount++,
                Vector(rand.nextDouble() * (worldWidth - 2 * smallParticleRadius - 2 * EPSILON) + smallParticleRadius + EPSILON,
                        rand.nextDouble() * (worldHeight - 2 * smallParticleRadius - 2 * EPSILON) + smallParticleRadius + EPSILON),
                Vector(speed * Math.cos(angle), speed * Math.sin(angle)),
                smallParticleMass,
                smallParticleRadius)
    }

    private fun generateSmallTrackableParticle(name: String): TrackableParticle {
        val speed = rand.nextDouble() * maxVelocity;
        val angle = 2 * Math.PI * rand.nextDouble();

        return TrackableParticle(idCount++,
                Vector(rand.nextDouble() * (worldWidth - 2 * smallParticleRadius - 2 * EPSILON) + smallParticleRadius + EPSILON,
                        rand.nextDouble() * (worldHeight - 2 * smallParticleRadius - 2 * EPSILON) + smallParticleRadius + EPSILON),
                Vector(speed * Math.cos(angle), speed * Math.sin(angle)),
                smallParticleMass,
                smallParticleRadius,
                name)
    }

    private fun generateBigParticle(): TrackableParticle {
        return TrackableParticle(idCount++,
                Vector(worldWidth / 2, worldHeight / 2),
                Vector(0.0, 0.0),
                bigParticleMass,
                bigParticleRadius, "BigParticle")
    }

    fun generateParticles(trackedSmallParticlesNum: Int, nonTrackedSmallParticlesNum: Int, stats: Stats): ArrayList<Particle> {
        val particles = ArrayList<Particle>()

        val bigParticle = generateBigParticle();
        stats.addTrackedParticle(bigParticle);
        particles.add(bigParticle)

        val hardCodedParticle = generateSmallTrackableParticle("SmallParticle_0")
        stats.addTrackedParticle(hardCodedParticle);

        hardCodedParticle.position = Vector(
                x = worldWidth/2 + bigParticleRadius + smallParticleRadius + hardCodedSeparation,
                y = worldHeight/2 + bigParticleRadius + smallParticleRadius + hardCodedSeparation)
        particles.add(hardCodedParticle)

        while (particles.size < trackedSmallParticlesNum + 1){
            val particle = generateSmallTrackableParticle("SmallParticle-" + particles.size)
            if (particles.find { overlaps(particle, it) } == null) {
                particles.add(particle)
                stats.addTrackedParticle(particle)
            }
        }
        while (particles.size < nonTrackedSmallParticlesNum + trackedSmallParticlesNum + 1) {
            val particle = generateSmallParticle()
            if (particles.find { overlaps(particle, it) } == null) {
                particles.add(particle)
            }
        }
        return particles
    }

    private fun overlaps(particle1: Particle, particle2: Particle): Boolean {
        val deltaR = Vector.delta(particle1.position, particle2.position)
        return Vector.norm(deltaR) - (particle1.radius + particle2.radius) <= EPSILON
    }


}