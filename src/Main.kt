import java.util.*
import kotlin.collections.ArrayList

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            // STEP 1: Create borders and all particles

            val worldWidth = 0.5
            val worldHeight = 0.5
            var time = 0.0 // TODO should time start in 0 or something else?
            val pq = PriorityQueue<Event>()
            val maxTime = 50
            val EPSILON = 0.000001

            val borders = Borders(worldWidth, worldHeight)
            val pg = ParticleGenerator(
                    worldWidth = worldWidth,
                    worldHeight = worldHeight,
                    bigParticleRadius = 0.05, // TODO change to actual value
                    bigParticleMass = 100.0,
                    smallParticleRadius = 0.005,
                    smallParticleMass = 0.1,
                    maxVelocity = 0.1,
                    EPSILON = EPSILON)
            val particles = pg.generateParticles(200)
//            print(particles) // TODO delete this print

            val printer = ParticlePrinter(borders)

            // STEP 2: For all particles,
            // Calculate boards collision events
            // for each particle in all particles, calculate particles collision events
            val newEvents = EventCalculator.calculateNewEvents(
                    changedParticles = particles,
                    allParticles = particles,
                    borders = borders,
                    time = time)
            pq.addAll(newEvents)

            // Main loop
//            var currentIteration = 0
            while (time < maxTime) {
//                if (currentIteration % 10000 == 0) println("Iteration N°" + currentIteration * 10000 + "Tc: " + time)
//                currentIteration++
                //  Pop event
                if (pq.isEmpty()) break
                val currentEvent = pq.poll()
                // Check that collision time is always bigger if not skip event
                if (currentEvent.results.find { it.collisionNumber != it.particle.collisionCount + 1 } == null) {

                    // STEP 3: For all particle recalculate position
                    if (time > currentEvent.tc) { // TODO maybe add an epsilon here?
                        throw IllegalStateException("New time can't be smaller than before")
                    }

                    val oldTime = printer.printIfNecessary(particles, currentEvent.tc)
                    time = currentEvent.tc
                    val deltaTime = time - oldTime

                    particles.forEach {
                        it.calculateNewPosition(deltaTime)
                        if (it.position.x < -EPSILON || it.position.x + EPSILON > worldWidth || it.position.y < -EPSILON || it.position.y > worldHeight + EPSILON) {
                            throw IllegalStateException("Particles can't be outside of bounds")
                        }
                    }

                    // STEP 4: For particles affected by event recalculate velocity and collision number
                    currentEvent.results.forEach { it.particle.collisionResult(it.newVelocity) }

                    // STEP 2: Repeat step 2 for particles affected by event,
                    // TODO change all this to not have to create a new arrayList each time but trying to keep it DRY. Or not, maximum arraySize is 2 in our scenario
                    val changedParticles = ArrayList<Particle>()
                    currentEvent.results.forEach { changedParticles.add(it.particle) }
                    val newEvents = EventCalculator.calculateNewEvents(
                            changedParticles = changedParticles,
                            allParticles = particles,
                            borders = borders,
                            time = time)
                    pq.addAll(newEvents)

                    // TODO save current state for drawing latter
//                    print(particles)
                }
            }
        }
    }
}