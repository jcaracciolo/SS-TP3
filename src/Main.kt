import java.util.*
import kotlin.collections.ArrayList

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            // STEP 0: Set output folder
            StatsPrinter.changeDir("chelo")
            // STEP 1: Create borders and all particles

            // Simulation parameters
            val worldWidth = 0.5
            val worldHeight = 0.5
            val pq = PriorityQueue<Event>()
            val maxTime = 60.0
            val EPSILON = 0.00001
            val borders = Borders(worldWidth, worldHeight)


            val pg = ParticleGenerator(
                    worldWidth = worldWidth,
                    worldHeight = worldHeight,
                    bigParticleRadius = 0.05,
                    bigParticleMass = 10.0,
                    smallParticleRadius = 0.005,
                    smallParticleMass = 0.1,
                    maxVelocity = 0.2,
                    EPSILON = EPSILON,
                    hardCodedSeparation = 0.05)


            val SIMULATIONS = 20
            val statsList = mutableListOf<Stats>()

            // Start simulation
            // Create Stats tracking
            for(i in 1..SIMULATIONS) {
                val simStats = Stats();
                statsList.add(simStats);

                // Generate particles
                val particles = pg.generateParticles(
                        trackedSmallParticlesNum = 1,
                        nonTrackedSmallParticlesNum = 199,
                        stats = simStats)

                val printer = ParticlePrinter(borders, true)

                // Time starts at zero
                var time = 0.0

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
                while (time < maxTime) {
                    //  Pop event
                    if (pq.isEmpty()) break
                    val currentEvent = pq.poll()
                    // Check that collision time is always bigger if not skip event
                    if (currentEvent.results.find { it.collisionNumber != it.particle.collisionCount + 1 } == null) {

                        // STEP 3: For all particle recalculate position
                        if (time > currentEvent.tc) {
                            throw IllegalStateException("New time can't be smaller than before")
                        }
                        val oldEventTime = time
                        val oldTime = printer.printIfNecessary(particles, currentEvent.tc)
                        time = currentEvent.tc
                        val deltaTime = time - oldTime
                        val deltaTimeCollision = time - oldEventTime

                        particles.forEach {
                            it.calculateNewPosition(deltaTime, time)
                            if (it.position.x < -EPSILON || it.position.x + EPSILON > worldWidth || it.position.y < -EPSILON || it.position.y > worldHeight + EPSILON) {
                                throw IllegalStateException("Particles can't be outside of bounds")
                            }
                        }

                        simStats.saveCollisionTime(deltaTimeCollision)
                        simStats.saveVelocities(particles)


                        // STEP 4: For particles affected by event recalculate velocity and collision number
                        currentEvent.results.forEach {
                            it.particle.collisionResult(it.newVelocity, currentEvent.eventType)
                        }

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
                    }
                }
            }

            StatsPrinter.printDCM(statsList)
            StatsPrinter.printCollisionTimes(statsList[0])
            StatsPrinter.printVelocitiesSegment(statsList[0], maxTime * (2.0/3.0))
        }
    }
}