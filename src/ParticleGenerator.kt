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
        val EPSILON: Double) {


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

    private fun generateBigParticle(): Particle {
        return Particle(idCount++,
                Vector(worldWidth / 2, worldHeight / 2),
                Vector(0.0, 0.0),
                bigParticleMass,
                bigParticleRadius)
    }

    fun generateParticles(smallParticlesNum: Int): ArrayList<Particle> {
        val particles = ArrayList<Particle>()
        particles.add(generateBigParticle())
        while (particles.size < smallParticlesNum + 1) {
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